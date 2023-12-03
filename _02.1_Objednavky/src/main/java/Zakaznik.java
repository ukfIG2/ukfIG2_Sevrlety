

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
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class Zakaznik extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_02_Objednavky";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
		
	public final static String tZ = "Zakaznici";
	public final static String tZid = "id_Zakaznikov";
	public final static String tZmeno = "Meno_zakaznika";
	public final static String tZpriezvisko = "Priezvisko_zakaznika";
	public final static String tZico = "ICO";
	public final static String tZadresa = "Adresa";
	
	private Connection con;
       

    public Zakaznik() {
        super();
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

		switch(operacia) {
		case "Vymazat":
			VymazPolozku(request, response, out, request.getParameter(tZid));
			break;
		case "Pridat":
			PridatPolozku(out, request.getParameter(tZmeno), request.getParameter(tZpriezvisko), request.getParameter(tZico), request.getParameter(tZadresa));
			break;
		case "Upravit":
			UpravitPolozku(out, request.getParameter(tZid));
			break;
		case "UlozitUpravu":
            String id = request.getParameter(tZid);
            String meno = request.getParameter(tZmeno);
            String priezvisko = request.getParameter(tZpriezvisko);
            String ico = request.getParameter(tZico);
            String adresa = request.getParameter(tZadresa);

            UlozitUpravu(out, id, meno, priezvisko, ico, adresa);
            break;
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
	            
	            out.println("<input type=hidden name='operacia' value='Vymazat'>");
	            out.println("<td><input type=submit value='Vymaz zaznam'></td>");
	            out.println("</form>");
	            
	            out.println("<form action='Zakaznik' method='post'>");
	 	        out.println("<input type=hidden name ="+ tZid +" value='"+rs.getString(tZid)+"'>");
	            out.println("<input type=hidden name='operacia' value='Upravit'>");
	            out.println("<td><input type=submit value='Uprav zaznam'></td>");
	            out.println("</form>");
	            out.println("</tr>");
	            
	        }
	        out.println("</table>");	        
	        out.println("<br>");
	        
	        out.println("<form action='Zakaznik' method='post'>");
	        out.println("<input type='hidden' name='operacia' value='addForm'>");
	        out.println("<input type='submit' value='Pridat zaznam'>");
	        out.println("</form>");
	        
	        out.println("<br>");
	        out.println("<a href='Objednavky'><button>Zobrazit tabulku objednavky</button></a>\t<a href='Tovar'><button>Zobrazit tabulku tovarov</button></a>");
	        	        
	        stmt.close();
	        //out.flush(); //?? treba?
				
		} catch (Exception e) {
			System.out.println("V Zakaznici: Vypis databazu " + e);
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
		out.println("<button type='submit'>Uloz novy zaznam</button>");
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
	
	private void VymazPolozku(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String id) {
		    try {
		        Statement stmt = con.createStatement();
		        String sql = "DELETE FROM Zakaznici WHERE " + tZid + " = " + id;
		        int pocet = stmt.executeUpdate(sql);
		        //out.print("Bolo odstranenych " + pocet + " zaznamov.<br/>");
		    } catch (SQLIntegrityConstraintViolationException f) {
		        try {
		            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/DELETE.html"));
		        } catch (IOException e) {
		            System.out.println("Error redirecting: " + e);
		        }
		    } catch (SQLException e) {
		        System.out.println("Zakaznik vymaz polocku: " + e);
		    }
		}

	private void UpravitPolozku(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tZ + " WHERE " + tZid + " = " + id;
	        ResultSet rs = stmt.executeQuery(sql);

	        out.println("<h2>Upravit zaznam</h2>");
	        out.println("<h3>Co chces zmen, ostatne nehaj napokoj</h3>");
	        out.println("<form action='Zakaznik' method='post'>");
	        out.println("<table>");

	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td><label for='" + tZmeno + "'>" + tZmeno + ":</label></td>");
	            out.println("<td><input type='text' name='" + tZmeno + "' value='" + rs.getString(tZmeno) + "'></td>");
	            out.println("</tr>");

	            out.println("<tr>");
	            out.println("<td><label for='" + tZpriezvisko + "'>" + tZpriezvisko + ":</label></td>");
	            out.println("<td><input type='text' name='" + tZpriezvisko + "' value='" + rs.getString(tZpriezvisko) + "'></td>");
	            out.println("</tr>");

	            out.println("<tr>");
	            out.println("<td><label for='" + tZico + "'>" + tZico + ":</label></td>");
	            out.println("<td><input type='text' name='" + tZico + "' value='" + rs.getString(tZico) + "'></td>");
	            out.println("</tr>");

	            out.println("<tr>");
	            out.println("<td><label for='" + tZadresa + "'>" + tZadresa + ":</label></td>");
	            out.println("<td><input type='text' name='" + tZadresa + "' value='" + rs.getString(tZadresa) + "'></td>");
	            out.println("</tr>");

	            out.println("<input type='hidden' name='" + tZid + "' value='" + rs.getString(tZid) + "'>");
	        }

	        out.println("</table>");
	        out.println("<input type='hidden' name='operacia' value='UlozitUpravu'>");
	        out.println("<input type='submit' value='Ulozit upravu'>");
	        out.println("</form>");
	    } catch (Exception e) {
	        System.out.println("Zakaznik uprav polozku: " + e);
	    }
	}

	private void UlozitUpravu(PrintWriter out, String id, String meno, String priezvisko, String ico, String adresa) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "UPDATE " + tZ + " SET " +
	                     tZmeno + "='" + meno + "', " +
	                     tZpriezvisko + "='" + priezvisko + "', " +
	                     tZico + "='" + ico + "', " +
	                     tZadresa + "='" + adresa + "' " +
	                     "WHERE " + tZid + "=" + id;
	        int pocet = stmt.executeUpdate(sql);
	        System.out.println("Uprava polozky uspesna, zmenenych riadkov: " + pocet);
	    } catch (Exception e) {
	        System.out.println("Zakaznik uprav polozku: " + e);
	    }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
