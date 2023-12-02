

import jakarta.servlet.ServletConfig;
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
 * Servlet implementation class SELECT_Tovar
 */
public class SELECT_Tovar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;
   
    public SELECT_Tovar() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(Hlavna_obrazovka.URL, Hlavna_obrazovka.username, Hlavna_obrazovka.password);
		} catch (Exception e) {
			System.out.println("V init: " + e);
		}
	}

	public void destroy() {
		try {con.close();} catch(Exception e) {System.out.println("V destroy: " + e);	
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + Hlavna_obrazovka.tT;
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        out.println("<body>");
	        out.println("<style>table, th, td{");
	        out.println("border: 1px solid;");
	        out.println("}\n</style>");

	        out.println("<h2>Tabulka tovarov</h2>");
	        out.println("<table>");
	        out.println("<tr>");
	        out.println("<th>" + Hlavna_obrazovka.tTid + "</th>");
	        out.println("<th>" + Hlavna_obrazovka.tTnazov + "</th>");
	        out.println("<th>" + Hlavna_obrazovka.tTcena + "</th>");
	        out.println("<th>" + Hlavna_obrazovka.tThodnotenie + "</th>");
	        out.println("</tr>");
	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td>" + rs.getString(Hlavna_obrazovka.tTid) + "</td>");
	            out.println("<td>" + rs.getString(Hlavna_obrazovka.tTnazov) + "</td>");
	            out.println("<td>" + rs.getString(Hlavna_obrazovka.tTcena) + "</td>");
	            out.println("<td>" + rs.getString(Hlavna_obrazovka.tThodnotenie) + "</td>");
	            out.println("<td><a href='CRUD_DELETE?id=" + rs.getString(Hlavna_obrazovka.tTid) + "'><button>Vymazat zaznam</button></a></td>");
	            out.println("<td><a href='CRUD_EDIT?id=" + rs.getString(Hlavna_obrazovka.tTid) + "'><button>Upravit zaznam</button></a></td>");
	            out.println("</tr>");
	        }
	        out.println("</table>");
	        
	        out.println("<br>");
	        
	        out.println("<a href='Hlavna_obrazovka'><button>Zobrazit tabulku OBJEDNAVKY</button></a>");
	        
	        stmt.close();
				
		} catch (Exception e) {
			System.out.println("V doGet 02: " + e);
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
