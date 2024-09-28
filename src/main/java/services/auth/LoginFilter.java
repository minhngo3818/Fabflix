package services.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

public class LoginFilter implements Filter {
    private final ArrayList<String> allowedURIs = new ArrayList<>();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        System.out.println("LoginFilter: " + url);

        // Check if this URL is allowed to access without logging in
        if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
            // Keep default action: pass along the filter chain
            chain.doFilter(request, response);
            return;
        }

        String role = (String) httpRequest.getSession().getAttribute("role");

        boolean isLoggedIn = httpRequest.getSession().getAttribute("user") != null;
        boolean isCustomer = role != null && role.equals("customer");

        if (isLoggedIn && isCustomer && url.contains("_dashboard")) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/not-allowed.jsp");
        }

        // Redirect to login page if the "user" attribute doesn't exist in session
        if (httpRequest.getSession().getAttribute("user") == null) {
            httpResponse.sendRedirect("login.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isUrlAllowedWithoutLogin(String requestURI) {
        /*
         Setup your own rules here to allow accessing some resources without logging in
         Always allow your own login related requests(html, js, servlet, etc..)
         You might also want to allow some CSS files, etc..
         */
        return allowedURIs.stream().anyMatch(requestURI.toLowerCase()::endsWith);
    }

    public void init(FilterConfig fConfig) {
        allowedURIs.add("login.jsp");
        allowedURIs.add("styles/styles.css");
        allowedURIs.add("styles/login.css");
        allowedURIs.add("scripts/login.js");
        allowedURIs.add("api/login");
        allowedURIs.add("login.jsp");
        allowedURIs.add("_dashboard/api/login-employee");
    }

    public void destroy() {
        // ignored.
    }
}
