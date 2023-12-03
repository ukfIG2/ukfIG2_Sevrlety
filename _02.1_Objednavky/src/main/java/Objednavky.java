

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
		else {/*System.out.println("Pripojenie je");*/}
		
		String operacia = request.getParameter("operacia");
		if(operacia ==null) {													//asi neni najstasnejsie riesenie
			operacia=""; System.out.println("Prazdna operacia");
			//return;
		}
		else {System.out.println("Operacia "+operacia);}

		switch(operacia) {
		case "Vymazat":
			VymazPolozku(out, request.getParameter(tOid));
			break;
		case "Pridat":
			PridatPolozku(out, request.getParameter(tOdatum), request.getParameter(tOidZ), request.getParameter(tOidT));
			break;
		case "Upravit":
			UpravitPolozku(out, request.getParameter(tOid));
			break;
		case "UlozitUpravu":
		    String id = request.getParameter(tOid);
		    String datum = request.getParameter(tOdatum);
		    String idZakaznika = request.getParameter(tOidZ);
		    String idTovar = request.getParameter(tOidT);

		    UlozitUpravu(out, id, datum, idZakaznika, idTovar);
		    break;

		}

		VypisDatabazu(out);
		if(operacia.equals("addForm")) {ZobrazFormularPrePridanie(out); System.out.println(true);}
			
		out.close();
	}

	private void VypisDatabazu(PrintWriter out) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT " +
	                tO + "." + tOid + " AS " + tOid + ", " +
	                tO + "." + tOdatum + " AS " + tOdatum + ", " +
	                tZ + "." + tZmeno + " AS " + tZmeno + ", " +
	                tZ + "." + tZpriezvisko + " AS " + tZpriezvisko + ", " +
	                tZ + "." + tZico + " AS " + tZico + ", " +
	                tZ + "." + tZadresa + " AS " + tZadresa + ", " +
	                tT + "." + tTnazov + " AS " + tTnazov + ", " +
	                tT + "." + tTcena + " AS " + tTcena + ", " +
	                tT + "." + tThodnotenie + " AS " + tThodnotenie +
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
	        
	        out.println("<th>XXX</th>"); // Gap added
	        
	        out.println("<th>" + tZmeno + "</th>");
	        out.println("<th>" + tZpriezvisko + "</th>");
	        out.println("<th>" + tZico + "</th>");
	        out.println("<th>" + tZadresa + "</th>");
	        
	        out.println("<th>XXX</th>"); // Gap added
	        
	        out.println("<th>" + tTnazov + "</th>");
	        out.println("<th>" + tTcena + "</th>");
	        out.println("<th>" + tThodnotenie + "</th>");
	        out.println("</tr>");
	        while (rs.next()) {
	            out.println("<tr>");

	            out.println("<form action='Objednavky' method='post'>");
	            out.println("<input type=hidden name =" + tOid + " value='" + rs.getString(tOid) + "'>");

	            out.println("<td>" + rs.getString(tOid) + "</td>");
	            out.println("<td>" + rs.getString(tOdatum) + "</td>");
	            
	            out.println("<th>XXX</th>"); // Gap added
	            
	            out.println("<td>" + rs.getString(tZmeno) + "</td>");
	            out.println("<td>" + rs.getString(tZpriezvisko) + "</td>");
	            out.println("<td>" + rs.getString(tZico) + "</td>");
	            out.println("<td>" + rs.getString(tZadresa) + "</td>");
	            
	            out.println("<th>XXX</th>"); // Gap added
	            
	            out.println("<td>" + rs.getString(tTnazov) + "</td>");
	            out.println("<td>" + rs.getString(tTcena) + "</td>");
	            out.println("<td>" + rs.getString(tThodnotenie) + "</td>");

	            out.println("<input type=hidden name='operacia' value='Vymazat'>");
	            out.println("<td><input type=submit value='Vymaz zaznam'></td>");
	            out.println("</form>");

	            out.println("<form action='Objednavky' method='post'>");
	            out.println("<input type=hidden name =" + tOid + " value='" + rs.getString(tOid) + "'>");
	            out.println("<input type=hidden name='operacia' value='Upravit'>");
	            out.println("<td><input type=submit value='Uprav zaznam'></td>");
	            out.println("</form>");

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
	    out.println("<td>" + tOdatum + "</td>");
	    out.println("<td><input type='date' name=" + tOdatum + "></td>");
	    out.println("</tr>");

	    out.println("<tr>");
	    out.println("<td>" + tOidZ + "</td>");
	    out.println("<td><select name='" + tOidZ + "'>");

	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tZ;
	        ResultSet rs = stmt.executeQuery(sql);

	        while (rs.next()) {
	            String customerId = rs.getString(tZid);
	            String customerName = rs.getString(tZmeno) + " " + rs.getString(tZpriezvisko);
	            out.println("<option value='" + customerId + "'>" + customerName + "</option>");
	        }

	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        System.out.println("Error in ZobrazFormularPrePridanie: " + e);
	    }

	    out.println("</select></td>");
	    out.println("</tr>");

	    
	    out.println("<tr>");
	    out.println("<td>" + tOidT + "</td>");
	    out.println("<td><select name='" + tOidT + "'>");

	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tT;
	        ResultSet rs = stmt.executeQuery(sql);

	        while (rs.next()) {
	            String productId = rs.getString(tTid);
	            String productName = rs.getString(tTnazov);
	            out.println("<option value='" + productId + "'>" + productName + "</option>");
	        }

	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        System.out.println("Error in ZobrazFormularPrePridanie: " + e);
	    }

	    out.println("</select></td>");
	    out.println("</tr>");

	    out.println("</table>");

	    out.println("<input type='hidden' name='operacia' value='Pridat'>");
	    out.println("<button type='submit'>Uloz novy zaznam</button>");
	    out.println("</form>");
	}
	
	private void PridatPolozku(PrintWriter out, String datum, String idZakaznika, String idTovar) {
	    try {
	        Statement stmt = con.createStatement();
	        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO " + tO + "(");

	        sqlBuilder.append(tOidZ).append(", ").append(tOidT);
	        
	        if (!datum.isEmpty()) {
	            sqlBuilder.append(", ").append(tOdatum);
	        }

	        sqlBuilder.append(") VALUES ('").append(idZakaznika).append("', '").append(idTovar);

	        if (!datum.isEmpty()) {
	            sqlBuilder.append("', '").append(datum);
	        }

	        sqlBuilder.append("')");

	        String sql = sqlBuilder.toString();
	        int pocet = stmt.executeUpdate(sql);
	        //System.out.println("Pridat polozku SQL: " + sql);
	       // System.out.println("Bolo pridanych " + pocet + " zaznamov.<br/>");
	    } catch (Exception e) {
	        System.out.println("Objednavky pridaj polozku nejde: " + e);
	    }
	}

	private void VymazPolozku(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "DELETE FROM " + tO + " WHERE " + tOid + " = " + id;
	        int pocet = stmt.executeUpdate(sql);
	        System.out.println("VymazPolozku SQL: " + sql);
	        System.out.println("Bolo odstranenych " + pocet + " zaznamov.<br/>");
	    } catch (SQLIntegrityConstraintViolationException f) {
	        
	    } catch (SQLException e) {
	        System.out.println("Objednavky vymaz polozku: " + e);
	    }
	}

	private void UpravitPolozku(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM " + tO + " WHERE " + tOid + " = " + id;
	        ResultSet rs = stmt.executeQuery(sql);

	        out.println("<h2>Upravit zaznam</h2>");
	        out.println("<h3>Co chces zmenit, ostatne nechaj napokoj</h3>");
	        out.println("<form action='Objednavky' method='post'>");
	        out.println("<table>");

	        while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td><label for='" + tOdatum + "'>" + tOdatum + ":</label></td>");
	            out.println("<td><input type='date' name='" + tOdatum + "' value='" + rs.getString(tOdatum) + "'></td>");
	            out.println("</tr>");

	            out.println("<tr>");
	            out.println("<td><label for='" + tOidZ + "'>" + tOidZ + ":</label></td>");
	            out.println("<td><select name='" + tOidZ + "'>");

	            try {
	                Statement zakazniciStmt = con.createStatement();
	                String zakazniciSql = "SELECT * FROM " + tZ;
	                ResultSet zakazniciRs = zakazniciStmt.executeQuery(zakazniciSql);

	                while (zakazniciRs.next()) {
	                    String customerId = zakazniciRs.getString(tZid);
	                    String customerName = zakazniciRs.getString(tZmeno) + " " + zakazniciRs.getString(tZpriezvisko);

	                    if (customerId.equals(rs.getString(tOidZ))) {
	                        out.println("<option value='" + customerId + "' selected>" + customerName + "</option>");
	                    } else {
	                        out.println("<option value='" + customerId + "'>" + customerName + "</option>");
	                    }
	                }

	                zakazniciRs.close();
	                zakazniciStmt.close();
	            } catch (Exception e) {
	                System.out.println("Error in UpravitPolozku (Zakaznici): " + e);
	            }

	            out.println("</select></td>");
	            out.println("</tr>");

	            out.println("<tr>");
	            out.println("<td><label for='" + tOidT + "'>" + tOidT + ":</label></td>");
	            out.println("<td><select name='" + tOidT + "'>");

	            try {
	                Statement tovarStmt = con.createStatement();
	                String tovarSql = "SELECT * FROM " + tT;
	                ResultSet tovarRs = tovarStmt.executeQuery(tovarSql);

	                while (tovarRs.next()) {
	                    String productId = tovarRs.getString(tTid);
	                    String productName = tovarRs.getString(tTnazov);

	                    if (productId.equals(rs.getString(tOidT))) {
	                        out.println("<option value='" + productId + "' selected>" + productName + "</option>");
	                    } else {
	                        out.println("<option value='" + productId + "'>" + productName + "</option>");
	                    }
	                }

	                tovarRs.close();
	                tovarStmt.close();
	            } catch (Exception e) {
	                System.out.println("Error in UpravitPolozku (Tovar): " + e);
	            }

	            out.println("</select></td>");
	            out.println("</tr>");

	            out.println("<input type='hidden' name='" + tOid + "' value='" + rs.getString(tOid) + "'>");
	        }

	        out.println("</table>");
	        out.println("<input type='hidden' name='operacia' value='UlozitUpravu'>");
	        out.println("<input type='submit' value='Ulozit upravu'>");
	        out.println("</form>");
	    } catch (Exception e) {
	        System.out.println("Objednavky uprav polozku: " + e);
	    }
	}


	private void UlozitUpravu(PrintWriter out, String id, String datum, String idZakaznika, String idTovar) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "UPDATE " + tO + " SET " +
	                     tOdatum + "='" + datum + "', " +
	                     tOidZ + "='" + idZakaznika + "', " +
	                     tOidT + "='" + idTovar + "' " +
	                     "WHERE " + tOid + "=" + id;
	        int pocet = stmt.executeUpdate(sql);
	        System.out.println("Uprava polozky uspesna, zmenenych riadkov: " + pocet);
	    } catch (Exception e) {
	        System.out.println("Objednavky uprav polozku: " + e);
	    }
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
