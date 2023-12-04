package sk.ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/objednavkyMR", "root", "")) {
            out.println("<form action='AddOrderServlet' method='post'>");

            out.println("<label for='tovar'>Tovar:</label>");
            out.println("<select name='tovar_id'>");
            try (PreparedStatement pst = con.prepareStatement("SELECT id, nazov FROM tovar")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    out.println("<option value='" + rs.getString("id") + "'>" + rs.getString("nazov") + "</option>");
                }
            }
            out.println("</select><br>");

            out.println("<label for='zakaznici'>Zákazník:</label>");
            out.println("<select name='zakaznici_id'>");
            try (PreparedStatement pst = con.prepareStatement("SELECT id, meno FROM zakaznici")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    out.println("<option value='" + rs.getString("id") + "'>" + rs.getString("meno") + "</option>");
                }
            }
            out.println("</select><br>");

            out.println("<label for='datum'>Dátum:</label>");
            out.println("<input type='text' name='datum'><br>");

            out.println("<input type='submit' value='Pridať objednávku'>");
            out.println("</form>");
        } catch (Exception e) {
            out.println("Database error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tovarId = request.getParameter("tovar_id");
        String zakazniciId = request.getParameter("zakaznici_id");
        String datum = request.getParameter("datum");

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/objednavky", "root", "")) {
            try (PreparedStatement pst = con.prepareStatement("INSERT INTO objednavky (zakaznici_id, tovar_id, datum) VALUES (?, ?, ?)")) {
                pst.setString(1, zakazniciId);
                pst.setString(2, tovarId);
                pst.setString(3, datum);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("Servlet_main");
      
    }
}