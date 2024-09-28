package services.payment;

import com.google.gson.JsonObject;
import data.user.Customer;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import data.cart.CartItem;
import data.payment.CreditCard;
import mappers.CartItemMapper;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbMaster");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Customer customer = (Customer) request.getSession().getAttribute("user");

        String firstNameParam = request.getParameter("firstname");
        String lastNameParam = request.getParameter("lastname");
        String creditCardIdParam = request.getParameter("creditCardId");
        String expirationDateParam = request.getParameter("expDate");

        // Convert string date to Date object
        Date expirationDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            // Parse the date string into a Date object
            expirationDate = (Date) dateFormat.parse(expirationDateParam);
            System.out.println(expirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (customer == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", "Unauthorized request");
            response.setStatus(403);
            response.getWriter().write(jsonObject.toString());
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            // Add movie to card (items_in_cart)
            String getCreditCardQuery = "SELECT cu.id as customerId, cc.* FROM creditcards as cc  " +
                    "JOIN customers as cu ON cu.ccId = cc.id " +
                    "WHERE cc.id = ? ";

            PreparedStatement statement = connection.prepareStatement(getCreditCardQuery);

            statement.setString(1, creditCardIdParam);
            ResultSet rs = statement.executeQuery();
            String userId = "";
            CreditCard creditCard = null;
            if (rs.next()) {
                userId = rs.getString("customerId");
                creditCard = new CreditCard(
                        rs.getString("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getDate("expirationDate")
                );
            }
            rs.close();

            CreditCard creditCardInput = new CreditCard(
                    creditCardIdParam,
                    firstNameParam,
                    lastNameParam,
                    expirationDate
            );

            if (!creditCardInput.equals(creditCard)) {
                JsonObject jsonInvalidResult = new JsonObject();
                jsonInvalidResult.addProperty("status", "success");
                jsonInvalidResult.addProperty("message", "Invalid credit card");
                response.getWriter().write(jsonInvalidResult.toString());
                response.setStatus(200);
                return;
            }

            // Add movies to sales
            addMoviesToSales(userId, connection);

            statement.close();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "A movie was updated successfully");
            response.getWriter().write(jsonObject.toString());
            response.setStatus(200);
        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", e.getMessage());
            response.getWriter().write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error PaymentServlet:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }

    private void addMoviesToSales(String customerId, Connection connection) throws SQLException {

        ArrayList<CartItem> cartItems = getCartItems(customerId, connection);

        if (!transferItemsInCartToSales(customerId, cartItems, connection)) {
            throw new SQLException("Fail to insert items to sales");
        }
    }

    private ArrayList<CartItem> getCartItems(String userId, Connection connection) throws SQLException {
        // Get all items in the user's cart
        String getMovieListQuery = "SELECT items.* FROM items_in_cart as ic " +
                " INNER JOIN items ON items.id = ic.itemId WHERE ic.customerId = ? ";
        PreparedStatement itemQueryStatemenet = connection.prepareStatement(getMovieListQuery);
        itemQueryStatemenet.setString(1, userId);
        ResultSet rs = itemQueryStatemenet.executeQuery();
        ArrayList<CartItem> itemList = CartItemMapper.mapToArray(rs);
        itemQueryStatemenet.close();
        return itemList;
    }


    private boolean transferItemsInCartToSales (String customerId, ArrayList<CartItem> cartItems, Connection connection) throws SQLException {
        String addToSalesQuery = "INSERT INTO sales (customerId, movieId, saleDate) VALUES (?, ?, ?) ";
        String addToSalesInfoQuery = "INSERT INTO sales_info (salesId, quantity, unitPrice) VALUES (?, ? , ?) ";
        String removeItemsQuery = "DELETE FROM items WHERE id = ? ";
        boolean isSuccess = false;

        // Perform insert items to sales transaction
        connection.setAutoCommit(false);
        try {
            // Create prepared statement for adding sales
            try (PreparedStatement addToSalesStatement = connection.prepareStatement(addToSalesQuery,
                    Statement.RETURN_GENERATED_KEYS)) {
                for (CartItem item : cartItems) {
                    // Bind values to SQL statement
                    addToSalesStatement.setString(1, customerId);
                    addToSalesStatement.setString(2, item.getMovieId());
                    addToSalesStatement.setDate(3, new java.sql.Date(new Date().getTime()));
                    // Execute the SQL statement
                    addToSalesStatement.executeUpdate();

                    // Retrieve the auto-generated key (salesId) for each sale
                    ResultSet generatedKeysSet = addToSalesStatement.getGeneratedKeys();
                    if (generatedKeysSet.next()) {
                        // Insert item info to sales_info
                        try (PreparedStatement addToSalesInfoStatement = connection.prepareStatement(addToSalesInfoQuery)) {
                            int salesId = generatedKeysSet.getInt(1);
                            addToSalesInfoStatement.setInt(1, salesId);
                            addToSalesInfoStatement.setInt(2, item.getQuantity());
                            addToSalesInfoStatement.setDouble(3, item.getUnitPrice());
                            addToSalesInfoStatement.executeUpdate();
                        }

                        // Remove items from cart
                        try (PreparedStatement removeItemsFromCartStatement = connection.prepareStatement(removeItemsQuery)) {
                            removeItemsFromCartStatement.setInt(1, item.getId());
                            removeItemsFromCartStatement.executeUpdate();
                        }
                    }
                }
            }

            // Commit the transaction if all statements execute successfully
            connection.commit();
            isSuccess = true;
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
        return isSuccess;
    }
}
