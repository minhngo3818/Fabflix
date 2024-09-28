package services._dashboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import data.metadata.Column;
import data.metadata.Table;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MetadataDbServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        try (Connection connection = dataSource.getConnection()) {

            DatabaseMetaData metaData = connection.getMetaData();

            String catalog = connection.getCatalog();
            ResultSet tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});

            ArrayList<Table> tableList = new ArrayList<Table>();

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                ResultSet columns = metaData.getColumns(null, null, tableName, null);
                ArrayList<Column> columnList = new ArrayList<Column>();

                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    Column column = new Column(columnName, columnType);
                    columnList.add(column);
                }

                Table tableInfo = new Table(tableName, columnList);
                tableList.add(tableInfo);
            }

            Gson gson = new Gson();
            out.write(gson.toJsonTree(tableList).getAsJsonArray().toString());
            response.setStatus(200);
        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "fail");
            jsonObject.addProperty("message", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error MetadataDbServlet:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
