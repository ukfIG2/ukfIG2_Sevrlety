

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
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class SELECT_Zakaznici
 */
public class Zakaznik extends HttpServlet {
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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Zakaznik() {
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		if(con==null) {
			out.println("Neni pripojena databaza");
			System.out.println("Pripojenie neni");
			return;
		}
		//else {System.out.println("Pripojenie je");}
		
		String operacia = request.getParameter("operacia");
		if(operacia ==null) {													//odkomentuj
			operacia=""; out.print("operacia: neni " + operacia);
			return;
		}
		else {System.out.println("Operacia "+operacia);}
		
		switch(operacia) {
		case "Vymazat":
			VymazPolozku(out, request.getParameter(tZid));
			break;
		case "Pridat":
			PridatPolozku(out, request.getParameter(tZmeno), request.getParameter(tZpriezvisko), request.getParameter(tZico), request.getParameter(tZadresa));
			System.out.println("Operacia pridat prebehla");
			break;
		/*case "Upravit":
			UpravitPolozku();
			break;*/
		}
		
		VypisDatabazu(out);
		if(operacia.equals("addForm")) {ZobrazFormularPrePridanie(out);}
		
		
		
		out.close();
	}
	
	private void VypisDatabazu(PrintWriter out)		{
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
	            
	            out.println("<form action='Zakaznik' method='post'>");
	 	        out.println("<input type=hidden name ="+ tZid +" value='"+rs.getString(tZid)+"'>");
	 	        
	            out.println("<td>" + rs.getString(tZid) + "</td>");
	            out.println("<td>" + rs.getString(tZmeno) + "</td>");
	            out.println("<td>" + rs.getString(tZpriezvisko) + "</td>");
	            out.println("<td>" + rs.getString(tZico) + "</td>");
	            out.println("<td>" + rs.getString(tZadresa) + "</td>");
	            
	            out.println("<input type=hidden name='operacia' value='mazanie'>");
	            out.println("<td><input type=submit value='Vymaz zaznam'></td>");
	            out.println("</form>");
	            out.println("</tr>");
	            
	        }
	        out.println("</table>");	        
	        out.println("<br>");
	        
	        out.println("<form action='Zakaznik' method='post'>");
	        out.println("<input type='hidden' name='operacia' value='addForm'>");
	        out.println("<input type='submit' value='pridat'>");
	        out.println("</form>");
	        
	        //out.println("<a href='Hlavna_obrazovka'><button>Zobrazit tabulku OBJEDNAVKY</button></a>");
	        
	        stmt.close();
	        //out.flush(); //??
				
		} catch (Exception e) {
			System.out.println("V select_zakaznici: " + e);
			}
	}//Vypis databazu
	
	private void ZobrazFormularPrePridanie(PrintWriter out) {
		out.println("<form action='Zakaznik' method='post'>");
		out.println("<table>");
		
        out.println("<tr>");
        out.println("<td>"+tZmeno+"</td>");
		out.println("<td><input type='text' name="+tZmeno+"></td>");
		out.println("</tr>");
		
        out.println("<tr>");
        out.println("<td>"+tZpriezvisko+"</td>");
		out.println("<td><input type='text' name="+tZpriezvisko+"></td>");
		out.println("</tr>");
		
        out.println("<tr>");
        out.println("<td>"+tZico+"</td>");
		out.println("<td><input type='text' name="+tZico+"></td>");
		out.println("</tr>");
		
        out.println("<tr>");
        out.println("<td>"+tZadresa+"</td>");
		out.println("<td><input type='text' name="+tZadresa+"></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<input type='hidden' name='operacia' value='Pridat'>");
		out.println("<button type='submit'>Pridaj zaznam</button>");
		out.println("</form>");
	}
				
	private void PridatPolozku(PrintWriter out, String meno, String priezvisko,
			String ico, String Adresa) {
		try {
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO Zakaznici(Meno_zakaznika, Priezvisko_zakaznika,"
					+ "ICO, Adresa) VALUES(";
			sql += "'" + meno + "', ";
            sql += "'" + priezvisko + "', ";
            sql += "'" + ico + "', ";
            sql += "'" +  Adresa + "') "; 
            int pocet = stmt.executeUpdate(sql);
           // out.print("Bolo pridanych " + pocet + " zaznamov.<br/>");
            System.out.println("Prida polozku sql");
		} catch (Exception e) {
			System.out.println("Zakaznik pridaj zlozku nejde: " + e);
		}
		
		
	}
	
	private void VymazPolozku(PrintWriter out, String id) {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
