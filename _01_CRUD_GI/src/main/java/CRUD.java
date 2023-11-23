

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
import java.sql.Statement;

/**
 * Servlet implementation class CRUD
 */
public class CRUD extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 static final String r1 = "id";
	private static final String r2 = "Datum";
	private static final String r3 = "Prijem";
	private static final String r4 = "Vydaj";
	private static final String r5 = "Poznamka";
	
	public static final String databaza = "_01_CRUD";
	public static final String tabulka = "Prijmy_Vydaje";
	
	public static final String URL = "jdbc:mysql://localhost/" + databaza;
	public static final String username = "root";
	public static final String password = "";
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CRUD() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(URL, username, password);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tabulka;
	        ResultSet rs = stmt.executeQuery(sql);

	        out.print("<body>");
	        out.println("<style>table, th, td{");
	        out.println("border: 1px solid;");
	        out.println("}\n</style>");
	        out.println("<table>");
	        out.println("<tr>");
	        out.println("<th>" + r1 + "</th>");
	        out.println("<th>" + r2 + "</th>");
	        out.println("<th>" + r3 + "</th>");
	        out.println("<th>" + r4 + "</th>");
	        out.println("<th>" + r5 + "</th>");
	        out.println("</tr>");

	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td>" + rs.getString(r1) + "</td>");
	            out.println("<td>" + rs.getString(r2) + "</td>");
	            out.println("<td>" + rs.getString(r3) + "</td>");
	            out.println("<td>" + rs.getString(r4) + "</td>");
	            out.println("<td>" + rs.getString(r5) + "</td>");
	           // out.println("<td><button><a href='DeleteServlet?id=" + rs.getString(r1) + "'>Delete</a></button></td>");
	            out.println("<td><a href='CRUD_DELETE?id=" + rs.getString(r1) + "'><button>Vymazat zaznam</button></a></td>");
	            
	            out.println("</tr>");
	        }

	        out.println("</table>");
	        out.println("</body>");

	        out.close();
	        stmt.close();
	        con.close();
	    } catch (Exception e) {
	        System.out.println("Dodrbalo sa: " + e);
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
