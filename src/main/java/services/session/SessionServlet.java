package services.session;

import com.google.gson.JsonObject;
import data.user.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SessionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        // Get a instance of current session on the request
        HttpSession session = request.getSession(true);

        String greetings;

        Customer customer = (Customer) session.getAttribute("user");

        JsonObject jsonRes = new JsonObject();

        if (customer != null) {
            // Retrieve data named "accessCount" from session, which count how many times the user requested before
            Integer accessCount = (Integer) session.getAttribute("accessCount");

            if (accessCount == null) {
                // First timer
                accessCount = 0;
                greetings = "Welcome, first time!";
            } else {
                // Regular user
                accessCount++;
                greetings = "Welcome back!";
            }

            // Update the new accessCount to session, replacing the old value if existed
            session.setAttribute("accessCount", accessCount);
            jsonRes.addProperty("username", customer.getUsername());
            jsonRes.addProperty("greetings", greetings);
            jsonRes.addProperty("accessCount", accessCount);
            jsonRes.addProperty("creationTime", session.getCreationTime());
            jsonRes.addProperty("lastAccessedTime", session.getLastAccessedTime());
            response.setStatus(200);
            response.getWriter().write(jsonRes.toString());
        } else {
            jsonRes.addProperty("status", "fail");
            jsonRes.addProperty("message", "user does not login ");
            response.setStatus(500);
            response.getWriter().write(jsonRes.toString());
        }
    }
}
