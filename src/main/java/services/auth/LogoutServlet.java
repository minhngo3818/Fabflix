package services.auth;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();

        if (request.getSession().getAttribute("user") != null) {
            request.getSession().removeAttribute("user");
            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "success");
        } else {
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", "Failed to logout");
        }

        response.getWriter().write(jsonObject.toString());
    }
}
