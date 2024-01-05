package ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class kosikServlet extends HttpServlet {
	Connection con;
	String errorMessage = "";

	public kosikServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/ E_SHOP_MK ", "root", "");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
	}

	private boolean badConnection(PrintWriter out) {
		if (errorMessage.length() > 0) {
			out.println(errorMessage);
			return true;
		}
		return false;
	}

	private void vypisObsahKosika(PrintWriter out, int idPouzivatela) {
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT k.ID, k.id_tovaru, k.cena, k.ks, s.nazov, (k.cena * k.ks) AS suma_kusov FROM kosik k JOIN sklad s ON k.id_tovaru = s.ID WHERE k.ID_pouzivatela = '"
					+ idPouzivatela + "'";
			ResultSet rs = stmt.executeQuery(sql);

			out.println("<h2>Obsah košíka:</h2>");
			out.println(
					"<table border='1'><tr><th>ID</th><th>Názov</th><th>Cena</th><th>Kusy</th><th>Suma za kusy</th></tr>");
			
			double celkovaSuma = 0;

			while (rs.next()) {
				double cena = rs.getDouble("cena");
				double ks = rs.getDouble("ks");
				double cenaSpolu = cena * ks;
				
				celkovaSuma += cenaSpolu;

				out.println("<tr>");
				out.println("<td>" + rs.getInt("ID") + "</td>");
				out.println("<td>" + rs.getString("nazov") + "</td>");
				out.println("<td>" + cena + "</td>");
				out.println("<td>" + ks + "</td>");
				out.println("<td>" + cenaSpolu + "</td>");
				out.println("<td>");
	            out.println("<form action='kosikServlet' method='post'>");
	            out.println("<input type='hidden' name='operacia' value='odstranit'>");
	            out.println("<input type='hidden' name='idTovaru' value='" + rs.getInt("id_tovaru") + "'>");
	            out.println("<input type='submit' name='odstranit_polozku' value='Odstrániť'>");
	            out.println("</form>");
	            out.println("</td>");
				out.println("</tr>");
			}
			
			out.println("<tr><td colspan='4'><b>Celková suma za počet kusov:</b></td><td>" + celkovaSuma
					+ "</td></tr>");

			out.println("</table>");

			rs.close();
			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	private void createHeader(PrintWriter out, HttpServletRequest request) {
	    out.println("<head>");
	    out.println("<title>Tesco</title>");
	    out.println("<style>");
	    out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; }");
	    out.println("header { background-color: #333; color: #fff; padding: 20px; text-align: center; }");
	    out.println("nav { background-color: #444; padding: 10px; }");
	    out.println("nav a { color: #fff; text-decoration: none; margin-right: 20px; }");
	    out.println("</style>");
	    out.println("</head>");
	    
	    out.println("<body>");

	    out.println("<header>");
	    out.println("<h1>Tesco</h1>");
	    out.println("<nav>");
	    
	    HttpSession ses = request.getSession();
	    String vypis = (String) ses.getAttribute("meno") + " " + (String) ses.getAttribute("priezvisko");
	    out.println("<span>");
	    out.println("<a href='objednavkaServlet'>" + vypis + "</a>");

	    Boolean isAdmin = (Boolean) ses.getAttribute("admin");
	    if (isAdmin != null && isAdmin) {
	        out.println("<a href='adminServlet'>Admin</a>");
	    }

	    out.println("<a href='kosikServlet'>Košík</a>");
	    out.println("<a href='mainServlet?operacia=logout'>Odhlásiť</a>");
	    out.println("</span></nav>");
	    out.println("</header>");
	}
	
	private void createFooter(PrintWriter out, HttpServletRequest request) {
		out.println("<a href='objednavkaServlet'>Zoznam objednávok</a>");
	}

	private String generateUniqueCode() {
		String uniqueCode = "";
		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT obj_cislo FROM obj_zoznam ORDER BY obj_cislo DESC LIMIT 1");
			if (rs.next()) {
				String lastCode = rs.getString("obj_cislo");

				int incremented = Integer.parseInt(lastCode) + 1;
				uniqueCode = String.format("%08d", incremented);
			} else {
				uniqueCode = "00000001";
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uniqueCode;
	}

	private void vytvorObjednavku(int idPouzivatela, PrintWriter out) {
		try {
			Statement stmt = con.createStatement();

			String uniqueCode = generateUniqueCode();

			String insertObjZoznam = "INSERT INTO obj_zoznam (obj_cislo, datum_objednavky, id_pouzivatela, suma, stav) VALUES ('"
					+ uniqueCode + "', NOW(), '" + idPouzivatela
					+ "', (SELECT SUM(cena * ks) FROM kosik WHERE ID_pouzivatela = '" + idPouzivatela + "'), 'nová')";
			stmt.executeUpdate(insertObjZoznam);

			String insertObjPolozky = "INSERT INTO obj_polozky (id_objednavky, id_tovaru, cena, ks) SELECT o.id, k.id_tovaru, k.cena, k.ks FROM kosik k JOIN obj_zoznam o ON k.ID_pouzivatela = o.id_pouzivatela WHERE k.ID_pouzivatela = '"
					+ idPouzivatela + "'";

			stmt.executeUpdate(insertObjPolozky);

			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	private void vyprazdniKosik(int idPouzivatela, PrintWriter out) {
		try {
			Statement stmt = con.createStatement();
			String deleteKosik = "DELETE FROM kosik WHERE ID_pouzivatela = '" + idPouzivatela + "'";
			stmt.executeUpdate(deleteKosik);

			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}
	
	private void odstranPolozkuZKosika(int idPouzivatela, int idTovaru, PrintWriter out) {
	    try {
	        Statement stmt = con.createStatement();

	        ResultSet rs = stmt.executeQuery("SELECT ks FROM kosik WHERE ID_pouzivatela = '" + idPouzivatela
	                + "' AND id_tovaru = '" + idTovaru + "'");
	        if (rs.next()) {
	            int removedQuantity = rs.getInt("ks");

	            if (removedQuantity > 1) {
	                String updateCart = "UPDATE kosik SET ks = ks - 1 WHERE ID_pouzivatela = '" + idPouzivatela
	                        + "' AND id_tovaru = '" + idTovaru + "'";
	                stmt.executeUpdate(updateCart);
	            } else {
	                String deleteItemFromCart = "DELETE FROM kosik WHERE ID_pouzivatela = '" + idPouzivatela
	                        + "' AND id_tovaru = '" + idTovaru + "'";
	                stmt.executeUpdate(deleteItemFromCart);
	            }

	            String incrementStock = "UPDATE sklad SET ks = ks + 1 WHERE ID = '" + idTovaru + "'";
	            stmt.executeUpdate(incrementStock);

	            out.println("Položka bola odstránená z košíka.");
	        } else {
	            out.println("Položka sa nenašla v košíku.");
	        }

	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        out.println(e.getMessage());
	    }
	}


	private void includeStyles(PrintWriter out) {
	    out.println("<style>");
	    out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; }");
	    out.println("header { background-color: #333; color: #fff; padding: 20px; text-align: center; }");
	    out.println("nav { background-color: #444; padding: 10px; }");
	    out.println("nav a { color: #fff; text-decoration: none; margin-right: 20px; }");
	    out.println("section { padding: 20px; }");
	    out.println("table { border-collapse: collapse; width: 100%; }");
	    out.println("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
	    out.println("th { background-color: #f2f2f2; }");
	    out.println("form { margin-bottom: 10px; }");
	    out.println("img { display: block; margin-bottom: 10px; }");
	    out.println("</style>");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	    doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();

	    HttpSession session = request.getSession();
	    int idPouzivatela = (Integer) session.getAttribute("id");

	    try {
	        String operacia = request.getParameter("operacia");
	        if (operacia == null) operacia = "";

	        if (badConnection(out)) {
	            return;
	        }

	        if (operacia.equals("objednat")) {
	            vytvorObjednavku(idPouzivatela, out);
	            vyprazdniKosik(idPouzivatela, out);
	        } else if (operacia.equals("odstranit")) {
	            int idTovaru = Integer.parseInt(request.getParameter("idTovaru"));
	            odstranPolozkuZKosika(idPouzivatela, idTovaru, out);
	        }
	    } catch (Exception e) {
	        out.println(e);
	    }

	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Tesco</title>");
	    includeStyles(out);
	    out.println("</head>");
	    out.println("<body>");

	    createHeader(out, request);
	    vypisObsahKosika(out, idPouzivatela);

	    out.println("<form action='kosikServlet' method='post'>");
	    out.println("<input type='hidden' name='operacia' value='objednat'>");
	    out.println("<input type='submit' name='vytvorit_objednavku' value='Vytvoriť objednávku'>");
	    out.println("</form>");

	    createFooter(out, request);

	    out.println("</body>");
	    out.println("</html>");
	}
}
