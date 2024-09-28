package services.cart;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import data.user.Customer;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.CartItemMapper;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartServlet extends HttpServlet {

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbMaster");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Customer customer = (Customer) request.getSession().getAttribute("user");

        PrintWriter out = response.getWriter();

        if (customer == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "Failed");
            jsonObject.addProperty("message", "Unauthorized request");
            out.write(jsonObject.toString());
            response.setStatus(403);
        } else {
            try (Connection connection = dataSource.getConnection()) {
                String query = "SELECT c.id as customerId, items.* FROM ( " +
                        "        SELECT customers.id " +
                        "        FROM customers WHERE customers.email = ? ) as c " +
                        "INNER JOIN items_in_cart as ic ON ic.customerId = c.id " +
                        "INNER JOIN items ON items.id = ic.itemId ";


                PreparedStatement statement = connection.prepareStatement(query);

                statement.setString(1, customer.getEmail());

                ResultSet rs = statement.executeQuery();

                JsonArray jsonArray = CartItemMapper.mapToList(rs);

                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("count", jsonArray.size());
                resultJson.add("data", jsonArray);

                rs.close();
                statement.close();
                out.write(resultJson.toString());
                response.setStatus(200);
            } catch (Exception e) {
                // Write error message JSON object to output
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("errorMessage", e.getMessage());
                out.write(jsonObject.toString());

                // Log error to localhost log
                request.getServletContext().log("Error MovieListServlet:", e);
                // Set response status to 500 (Internal Server Error)
                response.setStatus(500);
            } finally {
                out.close();
            }
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Customer customer = (Customer) request.getSession().getAttribute("user");

        String movieIdParam = request.getParameter("movieId");
        String movieTitleParam = request.getParameter("movieTitle");
        String unitPriceParam = request.getParameter("unitPrice");
        String quantityParam = request.getParameter("quantity");

        int quantity = 1;
        if (quantityParam != null && !quantityParam.isEmpty() && !quantityParam.equals("null")) {
            quantity = Integer.parseInt(quantityParam);
        }

        double unitPrice = 1.99;
        if (unitPriceParam != null && !unitPriceParam.isEmpty() && !unitPriceParam.equals("null")) {
            unitPrice = Double.parseDouble(unitPriceParam);
        }

        PrintWriter out = response.getWriter();

        if (customer == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", "Unauthorized request");
            out.write(jsonObject.toString());
            response.setStatus(403);
        } else {
            try (Connection connection = dataSource.getConnection()) {

                // Get user ID
                String getUserIdQuery = "SELECT customers.id FROM customers WHERE customers.email = ?";

                PreparedStatement statement = connection.prepareStatement(getUserIdQuery);

                statement.setString(1, customer.getEmail());

                ResultSet rs = statement.executeQuery();

                String userId = "";
                while (rs.next()) {
                    userId = rs.getString("id");
                }
                rs.close();

                if (userId.isEmpty()) throw new Exception("Invalid user id");

                if (checkItemIfExists(connection, movieIdParam, userId)) {
                    updateItemQuantity(connection, movieIdParam, userId);
                } else {
                    saveItem(connection, userId, movieIdParam, movieTitleParam, quantity, unitPrice);
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "A movie was added successfully");
                out.write(jsonObject.toString());
                response.setStatus(200);
            } catch (Exception e) {
                // Write error message JSON object to output
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", "failed");
                jsonObject.addProperty("errorMessage", e.getMessage());
                out.write(jsonObject.toString());

                // Log error to localhost log
                request.getServletContext().log("Error CarServlet:", e);
                // Set response status to 500 (Internal Server Error)
                response.setStatus(500);
            } finally {
                out.close();
            }
        }
    }


    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Customer customer = (Customer) request.getSession().getAttribute("user");

        String itemIdPram = request.getParameter("itemId");
        String quantityParam = request.getParameter("quantity");

        int itemId = -1;
        if (itemIdPram != null && !itemIdPram.isEmpty() && !itemIdPram.equals("null")) {
            itemId = Integer.parseInt(itemIdPram);
        }

        int quantity = 1;
        if (quantityParam != null && !quantityParam.isEmpty() && !quantityParam.equals("null")) {
            quantity = Integer.parseInt(quantityParam);
        }

        PrintWriter out = response.getWriter();

        if (customer == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", "Unauthorized request");
            out.write(jsonObject.toString());
            response.setStatus(403);
        } else {
            try (Connection connection = dataSource.getConnection()) {

                String updateQuantityQuery = "UPDATE items SET quantity = ? WHERE items.id = ? ";

                PreparedStatement statement = connection.prepareStatement(updateQuantityQuery);

                statement.setInt(1, quantity);
                statement.setInt(2, itemId);
                statement.executeUpdate();

                statement.close();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "A movie item was updated successfully");
                out.write(jsonObject.toString());
                response.setStatus(200);
            } catch (Exception e) {
                // Write error message JSON object to output
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", "failed");
                jsonObject.addProperty("errorMessage", e.getMessage());
                out.write(jsonObject.toString());

                // Log error to localhost log
                request.getServletContext().log("Error CartServlet:", e);
                // Set response status to 500 (Internal Server Error)
                response.setStatus(500);
            } finally {
                out.close();
            }
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Customer customer = (Customer) request.getSession().getAttribute("user");

        String itemIdPram = request.getParameter("id");
        int itemId = -1;
        if (itemIdPram != null && !itemIdPram.isEmpty() && !itemIdPram.equals("null")) {
            itemId = Integer.parseInt(itemIdPram);
        }

        PrintWriter out = response.getWriter();

        if (customer == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", "Unauthorized request");
            out.write(jsonObject.toString());
            response.setStatus(403);
        } else {
            try (Connection connection = dataSource.getConnection()) {
                // Remove a movie from cart
                String deleteItemQuery = "DELETE FROM items WHERE items.id = ? ";
                PreparedStatement statement = connection.prepareStatement(deleteItemQuery);
                statement.setInt(1, itemId);
                statement.executeUpdate();

                statement.close();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "A movie item was updated successfully");
                out.write(jsonObject.toString());
                response.setStatus(200);
            } catch (Exception e) {
                // Write error message JSON object to output
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", "failed");
                jsonObject.addProperty("message", e.getMessage());
                out.write(jsonObject.toString());

                // Log error to localhost log
                request.getServletContext().log("Error CarServlet:", e);
                // Set response status to 500 (Internal Server Error)
                response.setStatus(500);
            } finally {
                out.close();
            }
        }
    }

    private boolean checkItemIfExists(Connection connection, String movieId, String userId) throws SQLException {

        String query = "SELECT COUNT(*) AS count FROM items_in_cart as ic " +
                "JOIN items ON items.id = ic.itemId WHERE items.movieId = ? AND ic.customerId = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, movieId);
        statement.setString(2, userId);
        ResultSet rs = statement.executeQuery();

        int itemsInCartCount = 0;
        if (rs.next()) {
            itemsInCartCount = rs.getInt("count");
        }

        rs.close();
        statement.close();

        return itemsInCartCount == 1;
    }


    private void saveItem(Connection connection,
                          String userId,
                          String movieId,
                          String movieTitle,
                          int quantity,
                          double unitPrice) throws SQLException {

        // Add movie to items table
        String addItemInfoQuery = "INSERT INTO items (movieId, movieTitle, quantity, unitPrice) " +
                "VALUES (?, ?, ?, ?) ";
        PreparedStatement statement = connection.prepareStatement(addItemInfoQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, movieId);
        statement.setString(2, movieTitle);
        statement.setInt(3, quantity);
        statement.setDouble(4, unitPrice);
        int rowsAffected = statement.executeUpdate();

        // Check if any rows were inserted
        int generatedId = -1;
        if (rowsAffected > 0) {
            // Retrieve the generated keys
            ResultSet rs = statement.getGeneratedKeys();

            // Check if the ResultSet contains any data
            while (rs.next()) {
                // Retrieve the generated ID from the ResultSet
                generatedId = rs.getInt(1);
            }

            if (generatedId == -1) throw new SQLException("no row was added");
        } else {
            System.out.println("No rows inserted.");
        }

        String addToCartQuery = "INSERT INTO items_in_cart (customerId, itemId) VALUES (?, ?) ";
        statement = connection.prepareStatement(addToCartQuery);
        statement.setString(1, userId);
        statement.setInt(2, generatedId);
        statement.executeUpdate();
        statement.close();
    }

    private void updateItemQuantity(Connection connection, String movieId, String userId ) throws SQLException {
        String addToCartQuery = "UPDATE items " +
                "INNER JOIN items_in_cart as ic ON items.id = ic.itemId " +
                "SET items.quantity = items.quantity + 1 WHERE items.movieId = ? AND ic.customerId = ?";
        PreparedStatement statement = connection.prepareStatement(addToCartQuery);
        statement.setString(1, movieId);
        statement.setString(2, userId);
        statement.executeUpdate();
        statement.close();
    }
}
