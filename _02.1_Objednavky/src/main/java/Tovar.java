

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class Tovar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_02_Objednavky";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	
	public final static String tT = "Tovar";
	public final static String tTid = "idTovar";
	public final static String tTnazov = "Nazov_tovaru";
	public final static String tTcena = "Cena_tovaru";
	public final static String tThodnotenie = "Hodnotenie";
	
	private Connection con;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tovar() {
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

		switch(operacia) {
		case "Vymazat":
			VymazPolozku(out, request.getParameter(tTid));
			break;
		case "Pridat":
			PridatPolozku(out, request.getParameter(tTnazov), request.getParameter(tTcena), request.getParameter(tThodnotenie));
			System.out.println("Operacia pridat prebehla");
			break;
		case "Upravit":
			System.out.println("case upravit");
			UpravitPolozku(out, request.getParameter(tTid));
			break;
		case "UlozitUpravu":
		    String id = request.getParameter(tTid);
		    String nazov = request.getParameter(tTnazov);
		    String cena = request.getParameter(tTcena);
		    String hodnotenie = request.getParameter(tThodnotenie);

		    UlozitUpravu(out, id, nazov, cena, hodnotenie);
		    break;

		}

		VypisDatabazu(out);
		if(operacia.equals("addForm")) {ZobrazFormularPrePridanie(out);}
			
		out.close();
	}
	
	private void VypisDatabazu(PrintWriter out) {
		try {
			Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tT;
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        out.print("<body>");
	        out.println("<style>table, th, td{");
	        out.println("border: 1px solid;");
	        out.println("}\n</style>");
	        
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
	            
	            out.println("<form action='Tovar' method='post'>");
	 	        out.println("<input type=hidden name ="+ tTid +" value='"+rs.getString(tTid)+"'>");
	 	        
	            out.println("<td>" + rs.getString(tTid) + "</td>");
	            out.println("<td>" + rs.getString(tTnazov) + "</td>");
	            out.println("<td>" + rs.getString(tTcena) + "</td>");
	            out.println("<td>" + rs.getString(tThodnotenie) + "</td>");
	            
	            out.println("<input type=hidden name='operacia' value='Vymazat'>");
	            out.println("<td><input type=submit value='Vymaz zaznam'></td>");
	            out.println("</form>");
	            
	            out.println("<form action='Tovar' method='post'>");
	 	        out.println("<input type=hidden name ="+ tTid +" value='"+rs.getString(tTid)+"'>");
	            out.println("<input type=hidden name='operacia' value='Upravit'>");
	            out.println("<td><input type=submit value='Uprav zaznam'></td>");
	            out.println("</form>");
	            out.println("</tr>");
	            
	        }
	        out.println("</table>");	        
	        out.println("<br>");
	        
	        out.println("<form action='Tovar' method='post'>");
	        out.println("<input type='hidden' name='operacia' value='addForm'>");
	        out.println("<input type='submit' value='pridat'>");
	        out.println("</form>");
	        	        
	        stmt.close();
	        //out.flush(); //?? treba?
				
		} catch (Exception e) {
			System.out.println("V Zakaznici: Vypis databazu " + e);
			}
	}//Vypis databazu
		
	private void ZobrazFormularPrePridanie(PrintWriter out) {
		
		out.println("<form action='Tovar' method='post'>");
		out.println("<table>");
		
        out.println("<tr>");
        out.println("<td>"+tTnazov+"</td>");
		out.println("<td><input type='text' name="+tTnazov+"></td>");
		out.println("</tr>");
		
        out.println("<tr>");
        out.println("<td>"+tTcena+" v eurach</td>");
		out.println("<td><input type='number' name="+tTcena+"></td>");
		out.println("</tr>");
		
        out.println("<tr>");
        out.println("<td>"+tThodnotenie+" od 0 do 5</td>");
		out.println("<td><input type='number' name="+tThodnotenie+"></td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("<input type='hidden' name='operacia' value='Pridat'>");
		out.println("<button type='submit'>Pridaj zaznam</button>");
		out.println("</form>");
	}
		
	private void PridatPolozku(PrintWriter out, String Nazov, String Cena, String hodnotenie) {
	    try {
	        String sql = "INSERT INTO " + tT + "(" + tTnazov + ", " + tTcena + ", " + tThodnotenie + ") VALUES (?, ?, ?)";
	        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
	            pstmt.setString(1, Nazov);
	            pstmt.setDouble(2, Double.parseDouble(Cena));
	            
	            int Hodnotenie = Integer.parseInt(hodnotenie);
	            if(Hodnotenie<0) {Hodnotenie=0;}
	            else if(Hodnotenie>5) {Hodnotenie=5;}
	            pstmt.setInt(3, Hodnotenie);

	            int pocet = pstmt.executeUpdate();
	            System.out.println("Pridaj polozku SQL");
	        }
	    } catch (Exception e) {
	        System.out.println("Tovar pridaj polozku nejde: " + e);
	    }
	}

	private void VymazPolozku(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "DELETE FROM Tovar WHERE " + tTid + " = " + id;
	        int pocet = stmt.executeUpdate(sql);
	        //out.print("Bolo odstranenych " + pocet + " zaznamov.<br/>");
	    } /*catch (Exception e) {
	        System.out.println("Tovar vymaz polocku: " + e);
	    }*/
	    catch (SQLIntegrityConstraintViolationException f) {
	    	System.out.println("Nemozes vymazat, lebo ju uz pouzivas");
	    	///////////////////////////////////////////////////////////////////////////Daj tam potom este aj out.print
	    } catch (SQLException e) {
	    	System.out.println("Tovar vymaz polocku: " + e);	
	    }
	}

	private void UpravitPolozku(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tT + " WHERE " + tTid + " = " + id;
	        ResultSet rs = stmt.executeQuery(sql);

	        out.println("<h2>Upravit zaznam</h2>");
	        out.println("<h3>Co chces zmenit, ostatne nechaj napokoj</h3>");
	        out.println("<form action='Tovar' method='post'>");
	        out.println("<table>");

	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td><label for='" + tTnazov + "'>" + tTnazov + ":</label></td>");
	            out.println("<td><input type='text' name='" + tTnazov + "' value='" + rs.getString(tTnazov) + "'></td>");
	            out.println("</tr>");

	            out.println("<tr>");
	            out.println("<td><label for='" + tTcena + "'>" + tTcena + ":</label></td>");
	            out.println("<td><input type='number' name='" + tTcena + "' value='" + rs.getString(tTcena) + "'></td>");
	            out.println("</tr>");

	            out.println("<tr>");
	            out.println("<td><label for='" + tThodnotenie + "'>" + tThodnotenie + " od 0 do 5:</label></td>");
	            out.println("<td><input type='number' name='" + tThodnotenie + "' value='" + rs.getString(tThodnotenie) + "'></td>");
	            out.println("</tr>");

	            out.println("<input type='hidden' name='" + tTid + "' value='" + rs.getString(tTid) + "'>");

	            out.println("</table>");
	            out.println("<input type='hidden' name='operacia' value='UlozitUpravu'>");
	            out.println("<input type='submit' value='Ulozit upravu'>");
	            out.println("</form>");
	        }
	    } catch (Exception e) {
	        System.out.println("Tovar uprav polozku: " + e);
	    }
	}

	private void UlozitUpravu(PrintWriter out, String id, String nazov, String cena, String hodnotenie) {
	    try {
	    	String sql = "UPDATE " + tT + " SET " +
	                tTnazov + "='" + nazov + "', " +
	                tTcena + "=" + Double.parseDouble(cena) + ", " +
	                tThodnotenie + "=" + Integer.parseInt(hodnotenie) + " " +
	                "WHERE " + tTid + "=" + id;

	        try (Statement stmt = con.createStatement()) {
	            int pocet = stmt.executeUpdate(sql);
	            System.out.println("Uprava polozky uspesna, zmenenych riadkov: " + pocet);
	        }
	    } catch (Exception e) {
	        System.out.println("Tovar uprav polozku: " + e);
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
