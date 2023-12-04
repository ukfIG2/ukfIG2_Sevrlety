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

public class EditOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String orderId = request.getParameter("id");

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/objednavkyMR", "root", "")) {
  
            PreparedStatement pst = con.prepareStatement("SELECT * FROM objednavky WHERE id = ?");
            pst.setString(1, orderId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                out.println("<form action='EditOrderServlet' method='post'>");
                out.println("<input type='hidden' name='id' value='" + orderId + "'>");

                out.println("<label for='tovar'>Tovar:</label>");
                out.println("<select name='tovar_id'>");
                PreparedStatement pstTovar = con.prepareStatement("SELECT id, nazov FROM tovar");
                ResultSet rsTovar = pstTovar.executeQuery();
                while (rsTovar.next()) {
                    String selected = rsTovar.getString("id").equals(rs.getString("tovar_id")) ? " selected" : "";
                    out.println("<option value='" + rsTovar.getString("id") + "'" + selected + ">" + rsTovar.getString("nazov") + "</option>");
                }
                out.println("</select><br>");

                out.println("<label for='zakaznici'>Zákazník:</label>");
                out.println("<select name='zakaznici_id'>");
                PreparedStatement pstZakaznici = con.prepareStatement("SELECT id, meno FROM zakaznici");
                ResultSet rsZakaznici = pstZakaznici.executeQuery();
                while (rsZakaznici.next()) {
                    String selected = rsZakaznici.getString("id").equals(rs.getString("zakaznici_id")) ? " selected" : "";
                    out.println("<option value='" + rsZakaznici.getString("id") + "'" + selected + ">" + rsZakaznici.getString("meno") + "</option>");
                }
                out.println("</select><br>");

                out.println("<label for='datum'>Dátum:</label>");
                out.println("<input type='text' name='datum' value='" + rs.getString("datum") + "'><br>");

                out.println("<input type='submit' value='Aktualizovať objednávku'>");
                out.println("</form>");
            }
        } catch (Exception e) {
            out.println("Database error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("id");
        String tovarId = request.getParameter("tovar_id");
        String zakazniciId = request.getParameter("zakaznici_id");
        String datum = request.getParameter("datum");

        if (tovarId == null || zakazniciId == null || datum == null || tovarId.isEmpty() || zakazniciId.isEmpty() || datum.isEmpty()) {
            System.out.println("One or more form fields are empty or null.");
            response.sendRedirect("EditOrderServlet?id=" + orderId); // Redirect back to the edit page
            return; 
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/objednavky", "root", "")) {
            try (PreparedStatement pst = con.prepareStatement("UPDATE objednavky SET zakaznici_id = ?, tovar_id = ?, datum = ? WHERE id = ?")) {
                pst.setString(1, zakazniciId);
                pst.setString(2, tovarId);
                pst.setString(3, datum);
                pst.setString(4, orderId);
                int updateCount = pst.executeUpdate();
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("Servlet_main");
   
    }
}