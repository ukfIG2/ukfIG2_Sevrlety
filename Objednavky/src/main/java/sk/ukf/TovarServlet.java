package sk.ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TovarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection con = null;

    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/objednavkyMR", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.destroy();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        if ("add".equals(action)) {
            out.println("<form action='TovarServlet' method='POST'>");
            out.println("<input type='hidden' name='action' value='insert'/>");
            out.println("Nazov: <input type='text' name='nazov'/><br/>");
            out.println("Cena: <input type='text' name='cena'/><br/>");
            out.println("Hodnotenie: <input type='text' name='hodnotenie'/><br/>");
            out.println("<input type='submit' value='Pridať'/>");
            out.println("</form>");
        } else if ("edit".equals(action)) {
            String itemId = request.getParameter("id");
            try {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM tovar WHERE id = ?");
                stmt.setString(1, itemId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String nazov = rs.getString("nazov");
                    String cena = rs.getString("cena");
                    String hodnotenie = rs.getString("hodnotenie");

                    out.println("<form action='TovarServlet' method='POST'>");
                    out.println("<input type='hidden' name='action' value='update'/>");
                    out.println("<input type='hidden' name='id' value='" + itemId + "'/>");
                    out.println("Nazov: <input type='text' name='nazov' value='" + nazov + "'/><br/>");
                    out.println("Cena: <input type='text' name='cena' value='" + cena + "'/><br/>");
                    out.println("Hodnotenie: <input type='text' name='hodnotenie' value='" + hodnotenie + "'/><br/>");
                    out.println("<input type='submit' value='Upraviť'/>");
                    out.println("</form>");
                }
                stmt.close();
            } catch (Exception e) {
                out.println("Chyba pri načítaní údajov: " + e.getMessage());
            }
        }
        
        if ("delete".equals(action)) {
            String itemId = request.getParameter("id");

            try {
           
                PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM objednavky WHERE tovar_id = ?");
                checkStmt.setString(1, itemId);
                ResultSet rsCheck = checkStmt.executeQuery();
                if (rsCheck.next() && rsCheck.getInt(1) == 0) {
                    PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM tovar WHERE id = ?");
                    deleteStmt.setString(1, itemId);
                    int deleted = deleteStmt.executeUpdate();
                    if (deleted > 0) {
                        out.println("Položka bola úspešne vymazaná.");
                    } else {
                        out.println("Položka sa nepodarilo vymazať.");
                    }
                    deleteStmt.close();
                 out.println("<form action='TovarServlet' method='post' target='_self'>");
           	     out.println("<input type='submit' value='Tovar'>");
           	     out.println("</form>");
                } else {
                    out.println("Tento tovar sa nedá vymazať, pretože sa nachádza v objednávkach.");
                    out.println("<form action='TovarServlet' method='post' target='_self'>");
           	     out.println("<input type='submit' value='Tovar'>");
           	     out.println("</form>");
                }
                checkStmt.close();
            } catch (Exception e) {
                out.println("Chyba pri vymazávaní položky: " + e.getMessage());
            }
        }
        
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            String nazov = request.getParameter("nazov");
            String cena = request.getParameter("cena");
            String hodnotenie = request.getParameter("hodnotenie");

            try {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO tovar (nazov, cena, hodnotenie) VALUES (?, ?, ?)");
                stmt.setString(1, nazov);
                stmt.setString(2, cena);
                stmt.setString(3, hodnotenie);

                int inserted = stmt.executeUpdate();
                if (inserted > 0) {
                    out.println("Nový tovar bol úspešne pridaný.");
                } else {
                    out.println("Nepodarilo sa pridať nový tovar.");
                }
                stmt.close();
            } catch (Exception e) {
                out.println("Chyba pri pridávaní zákazníka: " + e.getMessage());
            }
        }
       
        
        if ("update".equals(action)) {
            String itemId = request.getParameter("id");
            String nazov = request.getParameter("nazov");
            String cena = request.getParameter("cena");
            String hodnotenie = request.getParameter("hodnotenie");

            try {
                PreparedStatement stmt = con.prepareStatement("UPDATE tovar SET nazov = ?, cena = ?, hodnotenie = ? WHERE id = ?");
                stmt.setString(1, nazov);
                stmt.setString(2, cena);
                stmt.setString(3, hodnotenie);
                stmt.setString(4, itemId);

                int updated = stmt.executeUpdate();
                if (updated > 0) {
                    out.println("Údaje boli úspešne aktualizované.");
                } else {
                    out.println("Neboli vykonané žiadne zmeny.");
                }
                stmt.close();
                out.println("<form action='TovarServlet' method='post' target='_self'>");
          	     out.println("<input type='submit' value='Tovar'>");
          	     out.println("</form>");
                
            } catch (Exception e) {
                out.println("Chyba pri aktualizácii údajov: " + e.getMessage());
            }
        } else {
            // Display items with edit links
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM tovar");
                while (rs.next()) {
                    out.println("<p>");
                    out.println("ID: " + rs.getString("id") + ", ");
                    out.println("Nazov: " + rs.getString("nazov") + ", ");
                    out.println("Cena: " + rs.getString("cena") + ", ");
                    out.println("Hodnotenie: " + rs.getString("hodnotenie") + " ");
                    out.println("<a href='TovarServlet?action=edit&id=" + rs.getString("id") + "'>Uprav</a>");
                    out.println("<a href='TovarServlet?action=delete&id=" + rs.getString("id") + "'>Vymaž</a>");
                    out.println("</p>");
                }
                rs.close();
                stmt.close();
                out.println("<a href='TovarServlet?action=add'>Pridať nový tovar</a>");
                out.println("<br><br>");
                out.println("<a href='javascript:void(0)' onclick='window.close();'>Zavrieť</a>");
            } catch (Exception e) {
                out.println("Chyba pri zobrazovaní tovaru: " + e.getMessage());
            }
        }
    }
}