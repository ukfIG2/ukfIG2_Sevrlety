

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
 * Servlet implementation class Hlavna_obrazovka
 */
public class Hlavna_obrazovka extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_02_Objednavky";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	
	public final static String tO = "Objednavky";
	public final static String tOid = "idObjednavky";
	public final static String tOdatum = "Datum_objednavky";
	public final static String tOidZ = "id_Zakaznikov";
	public final static String tOidT = "idTovar";
	
	
	public final static String tZ = "Zakaznici";
	public final static String tZid = "id_Zakaznikov";
	public final static String tZmeno = "Meno_zakaznika";
	public final static String tZpriezvisko = "Priezvisko_zakaznika";
	public final static String tZico = "ICO";
	public final static String tZadresa = "Adresa";
	
	public final static String tT = "Tovar";
	public final static String tTid = "idTovar";
	public final static String tTnazov = "Nazov_tovaru";
	public final static String tTcena = "Cena_tovaru";
	public final static String tThodnotenie = "Hodnotenie";
	
	private Connection con;
	
    public Hlavna_obrazovka() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, username, password);
		} catch (Exception e) {
			System.out.println("V init: " + e);
		}
	}

	public void destroy() {
		try {con.close();} catch(Exception e) {System.out.println("V destroy: " + e);	
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		try {
			Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tZ;
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        out.print("<body>");
	        out.println("<style>table, th, td{");
	        out.println("border: 1px solid;");
	        out.println("}\n</style>");
	        out.println("<h2>Tabulka zakaznikov</h2>");
	        out.println("<table>");
	        out.println("<tr>");
	        out.println("<th>" + tZid + "</th>");
	        out.println("<th>" + tZmeno + "</th>");
	        out.println("<th>" + tZpriezvisko + "</th>");
	        out.println("<th>" + tZico + "</th>");
	        out.println("<th>" + tZadresa + "</th>");
	        out.println("</tr>");
	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td>" + rs.getString(tZid) + "</td>");
	            out.println("<td>" + rs.getString(tZmeno) + "</td>");
	            out.println("<td>" + rs.getString(tZpriezvisko) + "</td>");
	            out.println("<td>" + rs.getString(tZico) + "</td>");
	            out.println("<td>" + rs.getString(tZadresa) + "</td>");
	            out.println("<td><a href='CRUD_DELETE?id=" + rs.getString(tZid) + "'><button>Vymazat zaznam</button></a></td>");
	            out.println("<td><a href='CRUD_EDIT?id=" + rs.getString(tZid) + "'><button>Upravit zaznam</button></a></td>");
	            out.println("</tr>");
	        }
	        out.println("</table>");	        
	        out.println("<br>");
	        
	        stmt.close();
	        //out.flush(); //??
				
		} catch (Exception e) {
			System.out.println("V doGet 01: " + e);
			}
		////////////////////////////////////////////tabulka 2
		try {
			Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tT;
	        ResultSet rs = stmt.executeQuery(sql);

	        out.println("<h2>Tabulka tovarov</h2>");
	        out.println("<table>");
	        out.println("<tr>");
	        out.println("<th>" + tTid + "</th>");
	        out.println("<th>" + tTnazov + "</th>");
	        out.println("<th>" + tTcena + "</th>");
	        out.println("<th>" + tThodnotenie + "</th>");
	        out.println("</tr>");
	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td>" + rs.getString(tTid) + "</td>");
	            out.println("<td>" + rs.getString(tTnazov) + "</td>");
	            out.println("<td>" + rs.getString(tTcena) + "</td>");
	            out.println("<td>" + rs.getString(tThodnotenie) + "</td>");
	            out.println("<td><a href='CRUD_DELETE?id=" + rs.getString(tTid) + "'><button>Vymazat zaznam</button></a></td>");
	            out.println("<td><a href='CRUD_EDIT?id=" + rs.getString(tTid) + "'><button>Upravit zaznam</button></a></td>");
	            out.println("</tr>");
	        }
	        out.println("</table>");
	        
	        out.println("<br>");
	        
	        stmt.close();
				
		} catch (Exception e) {
			System.out.println("V doGet 02: " + e);
			}
		
		/////////////////////////////////Taulka 03
		try {
			Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tO;
	        ResultSet rs = stmt.executeQuery(sql);

	        out.println("<h2>Tabulka objednavok</h2>");
	        out.println("<table>");
	        out.println("<tr>");
	        out.println("<th>" + tOid + "</th>");
	        out.println("<th>" + tOdatum + "</th>");
	        out.println("<th>" + tOidZ + "</th>");
	        out.println("<th>" + tOidT + "</th>");
	        out.println("</tr>");
	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td>" + rs.getString(tOid) + "</td>");
	            out.println("<td>" + rs.getString(tOdatum) + "</td>");
	            out.println("<td>" + rs.getString(tOidZ) + "</td>");
	            out.println("<td>" + rs.getString(tOidT) + "</td>");
	            out.println("<td><a href='CRUD_DELETE?id=" + rs.getString(tOid) + "'><button>Vymazat zaznam</button></a></td>");
	            out.println("<td><a href='CRUD_EDIT?id=" + rs.getString(tOid) + "'><button>Upravit zaznam</button></a></td>");
	            out.println("</tr>");
	        }
	        out.println("</table>");
	        
	        out.println("<br>");
	        
	        stmt.close();
				
		} catch (Exception e) {
			System.out.println("V doGet 03: " + e);
			}
		
	/////////////////////hokus pokus
	try {
		Statement stmt = con.createStatement();
		String sql = "SELECT " + 
				tO + "." + tOid + " AS " + tOid + ", " + 
                tO + "." + tOdatum + " AS " + tOdatum + ", " + 
                tZ + "." + tZmeno + " AS " + tZmeno + ", " +
                tZ + "." + tZpriezvisko + " AS " + tZpriezvisko + ", " +
                tT + "." + tTnazov + " AS " + tTnazov + 
     " FROM " + tO + " INNER JOIN " + tZ + " ON " + tO + "." + tOidZ + " = " + tZ + "." + tZid +
    " INNER JOIN " + tT + " ON " + tO + "." + tOidT + " = " + tT + "." + tTid;

        ResultSet rs = stmt.executeQuery(sql);

        out.println("<h2>Tabulka objednavok</h2>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>" + tOid + "</th>");
        out.println("<th>" + tOdatum + "</th>");
        out.println("<th>" + tZmeno + "</th>");
        out.println("<th>" + tZpriezvisko + "</th>");
        out.println("<th>" + tOidT + "</th>");
        out.println("</tr>");
        while (rs.next()) {
            out.println("<tr>");
            out.println("<td>" + rs.getString(tOid) + "</td>");
            out.println("<td>" + rs.getString(tOdatum) + "</td>");
            out.println("<td>" + rs.getString(tZmeno) + "</td>");
            out.println("<td>" + rs.getString(tZpriezvisko) + "</td>");
            out.println("<td>" + rs.getString(tTnazov) + "</td>");
            out.println("<td><a href='CRUD_DELETE?id=" + rs.getString(tOid) + "'><button>Vymazat zaznam</button></a></td>");
            out.println("<td><a href='CRUD_EDIT?id=" + rs.getString(tOid) + "'><button>Upravit zaznam</button></a></td>");
            out.println("</tr>");
        }
        out.println("</table>");
        
        out.println("<br>");
        
        stmt.close();
			
	} catch (Exception e) {
		System.out.println("V doGet 04: " + e);
		}
	
	out.close();
}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
