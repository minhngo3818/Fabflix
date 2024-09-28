package mappers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import data.cart.CartItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartItemMapper {

    public static JsonArray mapToList(ResultSet rs) throws SQLException {
        Gson gson = new Gson();

        ArrayList<CartItem> itemList = mapping(rs);

        return gson.toJsonTree(itemList).getAsJsonArray();
    }

    public static ArrayList<CartItem> mapToArray(ResultSet rs) throws SQLException {
        return mapping(rs);
    }

    private static ArrayList<CartItem> mapping(ResultSet rs) throws SQLException {
        ArrayList<CartItem> itemList = new ArrayList<CartItem>();

        while (rs.next()) {
            CartItem cartItem = new CartItem(
                    rs.getInt("id"),
                    rs.getString("movieId"),
                    rs.getString("movieTitle"),
                    rs.getInt("quantity"),
                    rs.getDouble("unitPrice")
            );

            itemList.add(cartItem);
        }

        return itemList;
    }
}
