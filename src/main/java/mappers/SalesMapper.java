package mappers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import data.sales.Sales;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalesMapper {
    public static JsonArray mapToList(ResultSet rs) throws SQLException {
        Gson gson = new Gson();

        ArrayList<Sales> salesList = new ArrayList<Sales>();

        while (rs.next()) {
            Sales cartItem = new Sales(
                    rs.getInt("id"),
                    rs.getString("movieId"),
                    rs.getString("movieTitle"),
                    rs.getInt("quantity"),
                    rs.getDouble("unitPrice"),
                    rs.getDate("saleDate")
            );

            salesList.add(cartItem);
        }

        return gson.toJsonTree(salesList).getAsJsonArray();
    }
}
