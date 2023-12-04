package sk.marek;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class editObjednavku
 */
public class editObjednavku extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public editObjednavku() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, login, pwd);

            String idParam = request.getParameter("id");
            int id = Integer.parseInt(idParam);

            String query = "SELECT * FROM objednany_tovar WHERE id = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    int id_tovar = rs.getInt("id_tovar");
                    int id_zakaznik = rs.getInt("id_zakaznik");
                    Date datum = rs.getDate("datum");

                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<form action='/Objednavky/editObjednavku' method='post'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'>");

                    // Dropdown pre výber tovaru
                    out.println("<label for='id_tovar'>Tovar:</label>");
                    out.println("<select name='id_tovar'>");

                    Statement stmtTovar = con.createStatement();
                    String sqlTovar = "SELECT * FROM tovar";
                    ResultSet rsTovar = stmtTovar.executeQuery(sqlTovar);

                    while (rsTovar.next()) {
                        int tovarId = rsTovar.getInt("id");
                        String nazovTovaru = rsTovar.getString("nazov");

                        out.println("<option value='" + tovarId + "' " + (id_tovar == tovarId ? "selected" : "") + ">" + nazovTovaru + "</option>");
                    }

                    out.println("</select><br>");

                    // Dropdown pre výber zakaznika
                    out.println("<label for='id_zakaznik'>Zakaznik:</label>");
                    out.println("<select name='id_zakaznik'>");

                    Statement stmtZakaznik = con.createStatement();
                    String sqlZakaznik = "SELECT * FROM zakaznici";
                    ResultSet rsZakaznik = stmtZakaznik.executeQuery(sqlZakaznik);

                    while (rsZakaznik.next()) {
                        int zakaznikId = rsZakaznik.getInt("id");
                        String priezvisko = rsZakaznik.getString("priezvisko");

                        out.println("<option value='" + zakaznikId + "' " + (id_zakaznik == zakaznikId ? "selected" : "") + ">" + priezvisko + "</option>");
                    }

                    out.println("</select><br>");

                    
                    out.println("<input type='date' name='datum' value='" + datum + "' required><br>");
                    out.println("<input type='submit' value='Upraviť objednávku'>");
                    out.println("</form>");

                    out.println("<form action='Servlet_main' method='post'>");
                    out.println("<input type='hidden' name='operacia' value='vypisSpojovacej'>");
                    out.println("<input type='submit' value='Späť na spojovaciu tabulku(3tabulka,objednavky)'>");
                    out.println("</form>");

                    out.println("</body></html>");
                }
            }
        } catch (Exception e) {
            System.out.println("Ovládač nenájdený: " + e.toString());
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 try {
		        Class.forName("com.mysql.cj.jdbc.Driver");
		        Connection con = DriverManager.getConnection(URL, login, pwd);

		        int id = Integer.parseInt(request.getParameter("id"));
		        int id_tovar = Integer.parseInt(request.getParameter("id_tovar"));
		        int id_zakaznik = Integer.parseInt(request.getParameter("id_zakaznik"));
		        Date datum = Date.valueOf(request.getParameter("datum"));

		        String updateQuery = "UPDATE objednany_tovar SET id_tovar = ?, id_zakaznik = ?, datum = ? WHERE id = ?";
		        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
		            preparedStatement.setInt(1, id_tovar);
		            preparedStatement.setInt(2, id_zakaznik);
		            preparedStatement.setDate(3, datum);
		            preparedStatement.setInt(4, id);

		            preparedStatement.executeUpdate();
		        }

		        response.sendRedirect(request.getContextPath() + "/Servlet_main?operacia=vypisSpojovacej");
		    } catch (Exception e) {
		        PrintWriter out = response.getWriter();
		        out.print(e.toString());
		        System.out.println("Ovládač nenájdený: " + e.toString());
		    }
	}

}
