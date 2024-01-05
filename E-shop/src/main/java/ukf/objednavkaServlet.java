package ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class objednavkaServlet extends HttpServlet {
    Connection con;
    String errorMessage = "";

    public objednavkaServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/ E_SHOP_MK ", "root", "");
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    private boolean badConnection(PrintWriter out) {
        if (errorMessage.length() > 0) {
            out.println(errorMessage);
            return true;
        }
        return false;
    }

    private void createHeader(PrintWriter out, HttpServletRequest request) {
        out.println("<head>");
        out.println("<title>Tesco</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; }");
        out.println("header { background-color: #333; color: #fff; padding: 20px; text-align: center; }");
        out.println("nav { background-color: #444; padding: 10px; }");
        out.println("nav a { color: #fff; text-decoration: none; margin-right: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head>");

        out.println("<body>");

        out.println("<header>");
        out.println("<h1>Tesco</h1>");
        out.println("<nav>");

        HttpSession ses = request.getSession();
        String vypis = (String) ses.getAttribute("meno") + " " + (String) ses.getAttribute("priezvisko");
        out.println("<span>");
        out.println("<a href='objednavkaServlet'>" + vypis + "</a>");

        Boolean isAdmin = (Boolean) ses.getAttribute("admin");
        if (isAdmin != null && isAdmin) {
            out.println("<a href='adminServlet'>Admin</a>");
        }

        out.println("<a href='kosikServlet'>Košík</a>");
        out.println("<a href='mainServlet?operacia=logout'>Odhlásiť</a>");
        out.println("</span></nav>");
        out.println("</header>");
    }

    private void vypisObjednavky(PrintWriter out, int idPouzivatela) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM obj_zoznam WHERE id_pouzivatela = ?");
            pstmt.setInt(1, idPouzivatela);
            ResultSet rs = pstmt.executeQuery();

            out.println("<h2>Objednávky:</h2>");

            while (rs.next()) {
                out.println("<p><b>Objednávka číslo: " + rs.getString("obj_cislo") + " | Datum: " + rs.getString("datum_objednavky") + "</b></p>");

                PreparedStatement pstmtPolozky = con.prepareStatement(
                        "SELECT s.nazov, op.cena, op.ks FROM obj_polozky op INNER JOIN sklad s ON op.id_tovaru = s.ID WHERE id_objednavky = ?");
                pstmtPolozky.setInt(1, rs.getInt("id"));
                ResultSet rsPolozky = pstmtPolozky.executeQuery();

                out.println(
                        "<table border='1'><tr><th>Názov</th><th>Cena</th><th>Kusy</th><th>Celková suma za kusy</th></tr>");

                double celkovaSuma = 0;

                while (rsPolozky.next()) {
                    double cena = rsPolozky.getDouble("cena");
                    double ks = rsPolozky.getDouble("ks");
                    double cenaSpolu = cena * ks;

                    celkovaSuma += cenaSpolu;

                    out.println("<tr>");
                    out.println("<td>" + rsPolozky.getString("nazov") + "</td>");
                    out.println("<td>" + cena + "</td>");
                    out.println("<td>" + ks + "</td>");
                    out.println("<td>" + cenaSpolu + "</td>");
                    out.println("</tr>");
                }

                out.println("<tr><td colspan='3'>Celková suma:</td><td>" + celkovaSuma
                        + "</td></tr>");

                out.println("</table>");
                out.println("<br><br>");
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        int idPouzivatela = (Integer) session.getAttribute("id");

        if (badConnection(out)) {
            return;
        }

        createHeader(out, request);
        vypisObjednavky(out, idPouzivatela);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
