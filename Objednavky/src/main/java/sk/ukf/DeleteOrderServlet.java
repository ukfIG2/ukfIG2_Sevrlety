package sk.ukf;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("id");

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/objednavkyMR", "root", "")) {
            try (PreparedStatement pst = con.prepareStatement("DELETE FROM objednavky WHERE id = ?")) {
                pst.setString(1, orderId);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("Servlet_main");
    }
}