

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
	
	private static final String r1 = "id";
	private static final String r2 = "Datum";
	private static final String r3 = "Prijem";
	private static final String r4 = "Vydaj";
	private static final String r5 = "Poznamka";
	
	private static final String databaza = "_01_CRUD";
	private static final String tabulka = "Prijmy_Vydaje";
	
	private static final String URL = "jdbc:mysql://localhost/" + databaza;
	private static final String username = "root";
	private static final String password = "";
	
	
       
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: Tuna to funguje.");
		PrintWriter out = response.getWriter();
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");	
		Connection con = DriverManager.getConnection(URL, username, password);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM " + tabulka;
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			out.print(r1 + " " +rs.getString(r1)+" ");
			out.print(r2 + " " +rs.getString(r2)+" ");
			out.print(r3 + " " +rs.getString(r3)+" ");
			out.print(r4 + " " +rs.getString(r4)+" ");
			out.print(r5 + " " +rs.getString(r5)+" ");
			out.print("\n");
			
			
		}
		out.close(); stmt.close(); con.close();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
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
