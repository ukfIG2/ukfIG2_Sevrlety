

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

/**
 * Servlet implementation class nieco2
 */
public class nieco2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String c1 = "Id";
	private static final String c2="Meno";
	private static final String c3="Priezvisko";
	private static final String c4="Popis_priestupku";
	private static final String c5="Datum";
	private static final String c6="Suma";
	
	private static final String databaza = "Java_01";
	private static final String tabulka = "Tabulka_01";
	
	private static final String URL = "jdbc:mysql://localhost/"+databaza;
	private static final String username = "root";
	private static final String password = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public nieco2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(URL, username, password);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM " + tabulka;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				out.print(c1 + " " +rs.getString(c1)+" ");
				out.print(c2 + " " +rs.getString(c2)+" ");
				out.print(c3 + " " +rs.getString(c3)+" ");
				out.print(c4 + " " +rs.getString(c4)+" ");
				out.print(c5 + " " +rs.getString(c5)+" ");
				out.print(c6 + " " +rs.getString(c6)+" ");
				out.print("\n");
				
				
			}
			out.close(); stmt.close(); con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
