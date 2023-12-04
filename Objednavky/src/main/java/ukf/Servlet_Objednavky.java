package ukf;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Servlet_Objednavky extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String URL = "jdbc:mysql://localhost/objednavky";
    private String LOGIN = "root";
    private String PWD = ""; 
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DisplayData(response);
	}
	void DisplayData(HttpServletResponse response) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
            Statement stmt = con.createStatement();
            String sql = ""
            		+ "SELECT * FROM objednavka o "
            		+ "LEFT JOIN tovar t ON o.id_tovaru = t.id_tovaru "
            		+ "LEFT JOIN zakaznici z ON o.id_zakaznika = z.id_zakaznika;";
            ResultSet rs = stmt.executeQuery(sql);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<table border='1'>");
            out.println("<tr>"
            		+ "<th>ID objednavky</th>"
            		+ "<th>Datum</th>"
            		+ "<th>ID zakaznika</th>"
            		+ "<th>ID tovaru</th>"
            		+ "</tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("id_objednavky") + "</td>");
                out.println("<td>" + rs.getString("datum") + "</td>");
                out.println("<td>" + rs.getString("id_zakaznika") + "</td>");
                out.println("<td>" + rs.getString("id_tovaru") + "</td>");
                out.println("</tr>");
            }
            out.println("</table><br>");
            out.println("</body></html>");

            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
