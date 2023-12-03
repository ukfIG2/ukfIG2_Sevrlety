

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

public class Objednavky extends HttpServlet {
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

	
    public Objednavky() {
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
		else {System.out.println("Pripojenie je");}
		
		String operacia = request.getParameter("operacia");
		if(operacia ==null) {													//asi neni najstasnejsie riesenie
			operacia=""; System.out.println("Prazdna operacia");
			//return;
		}
		else {System.out.println("Operacia "+operacia);}

		//switch(operacia) {
		/*case "Vymazat":
			VymazPolozku(request, response, out, request.getParameter(tZid));
			break;*/
		/*case "Pridat":
			PridatPolozku(out, request.getParameter(tZmeno), request.getParameter(tZpriezvisko), request.getParameter(tZico), request.getParameter(tZadresa));
			System.out.println("Operacia pridat prebehla");
			break;*/
		/*case "Upravit":
			System.out.println("case upravit");
			UpravitPolozku(out, request.getParameter(tZid));
			break;
		case "UlozitUpravu":
            String id = request.getParameter(tZid);
            String meno = request.getParameter(tZmeno);
            String priezvisko = request.getParameter(tZpriezvisko);
            String ico = request.getParameter(tZico);
            String adresa = request.getParameter(tZadresa);

            UlozitUpravu(out, id, meno, priezvisko, ico, adresa);
            break;*/
		//}

		VypisDatabazu(out);
		if(operacia.equals("addForm")) {ZobrazFormularPrePridanie(out); System.out.println(true);}
			
		out.close();
	}

	private void VypisDatabazu(PrintWriter out)		{
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
	        
	        out.println("<body>");
	        out.println("<style>table, th, td{");
	        out.println("border: 1px solid;");
	        out.println("}\n</style>");
	        
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
	        out.println("<form action='Objednavky' method='post'>");
	        out.println("<input type='hidden' name='operacia' value='addForm'>");
	        out.println("<input type='submit' value='Pridat zaznam'>");
	        out.println("</form>");
			out.println("<br>");
	        out.println("<a href='Zakaznik'><button>Zobrazit tabulku zakaznikov</button></a>\t<a href='Tovar'><button>Zobrazit tabulku tovarov</button></a>");
	        
	       // out.println("</body>");

	        
	        stmt.close();
				
		} catch (Exception e) {
			System.out.println("V doGet spojovacia tabulka: " + e);
			}
	}
	
	private void ZobrazFormularPrePridanie(PrintWriter out) {
		out.println("<br>");
		out.println("<br>");
		out.println("<form action='Objednavky' method='post'>");
		out.println("<table>");
		
        out.println("<tr>");
        out.println("<td>"+tOdatum+"</td>");
		out.println("<td><input type='text' name="+tOdatum+"></td>");
		out.println("</tr>");
		
        out.println("<tr>");
        out.println("<td>"+tOidZ+" v eurach</td>");
		out.println("<td><input type='number' name="+tOidZ+"></td>");
		out.println("</tr>");
		
        out.println("<tr>");
        out.println("<td>"+tOidT+" od 0 do 5</td>");
		out.println("<td><input type='number' name="+tOidT+"></td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("<input type='hidden' name='operacia' value='Pridat'>");
		out.println("<button type='submit'>Pridaj zaznam</button>");
		out.println("</form>");
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
