package ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class adminServlet extends HttpServlet {
	Connection con;
	String errorMessage = "";

	public adminServlet() {
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
	private void zobrazTovar(PrintWriter out) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM sklad");

			out.println("<h2>Tovar</h2>");
			out.println("<table border='1'>");
			out.println("<tr><th>ID</th><th>Nazov</th><th>Ks</th><th>Cena</th><th>Image</th><th>Action</th></tr>");

			while (rs.next()) {
				int itemId = rs.getInt("ID");
				String itemName = rs.getString("nazov");
				int itemQuantity = rs.getInt("ks");
				double itemPrice = rs.getDouble("cena");
				String imageURL = rs.getString("img");

				out.println("<tr>");
				out.println("<td>" + itemId + "</td>");
				out.println("<td>" + itemName + "</td>");
				out.println("<td>" + itemQuantity + "</td>");
				out.println("<td>" + itemPrice + "</td>");
				out.println("<td><img src='" + imageURL + "' width='50' height='50'></td>");

				out.println("<td>");
				out.println("<form action='adminServlet' method='post'>");
				out.println("<input type='hidden' name='operacia' value='showEditForms'>");
				out.println("<input type='hidden' name='itemId' value='" + itemId + "'>");
				out.println("<input type='submit' value='Edit'>");
				out.println("</form>");

				out.println("<form action='adminServlet' method='post'>");
				out.println("<input type='hidden' name='operacia' value='deleteTovarItem'>");
				out.println("<input type='hidden' name='itemId' value='" + itemId + "'>");
				out.println("<input type='submit' value='Delete'>");
				out.println("</form>");
				out.println("</td>");

				out.println("</tr>");
			}

			out.println("</table>");

			out.println("Pridat novy tovar<br>");
			out.println("<form action='adminServlet' method='post'>");
			out.println("<input type='hidden' name='operacia' value='addTovarItem'>");
			out.println("Nazov: <input type='text' name='newItemName'>");
			out.println("Mnozstvo: <input type='text' name='newItemQuantity'>");
			out.println("Cena: <input type='text' name='newItemPrice'>");
			out.println("Img URL: <input type='text' name='newItemImage'><br>");
			out.println("<input type='submit' value='Add Item'>");
			out.println("</form>");
			out.println("<br><br><br>");

			rs.close();
			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	private void addTovarItem(HttpServletRequest request, PrintWriter out) {
		String newItemName = request.getParameter("newItemName");
		int newItemQuantity = Integer.parseInt(request.getParameter("newItemQuantity"));
		double newItemPrice = Double.parseDouble(request.getParameter("newItemPrice"));
		String newItemImage = request.getParameter("newItemImage");

		try {
			Statement stmt = con.createStatement();
			String insertQuery = "INSERT INTO sklad (nazov, ks, cena, img) VALUES ('" + newItemName + "', "
					+ newItemQuantity + ", " + newItemPrice + ", '" + newItemImage + "')";

			int rowsAffected = stmt.executeUpdate(insertQuery);

			if (rowsAffected > 0) {
				out.println("<h3>New item added successfully.</h3>");
			} else {
				out.println("<h3>Failed to add a new item.</h3>");
			}

			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	private void showEditForm(HttpServletRequest request, PrintWriter out) {
		int itemId = Integer.parseInt(request.getParameter("itemId"));

		try {
			Statement stmt = con.createStatement();
			String selectQuery = "SELECT * FROM sklad WHERE ID = " + itemId;
			ResultSet rs = stmt.executeQuery(selectQuery);

			if (rs.next()) {
				String itemName = rs.getString("nazov");
				int itemQuantity = rs.getInt("ks");
				double itemPrice = rs.getDouble("cena");
				String imageURL = rs.getString("img");

				out.println("<h2>Edit Item ID: " + itemId + "</h2>");
				out.println("<form action='adminServlet' method='post'>");
				out.println("<input type='hidden' name='operacia' value='updateTovarItem'>");
				out.println("<input type='hidden' name='itemId' value='" + itemId + "'>");
				out.println("Name: <input type='text' name='itemName' value='" + itemName + "'><br>");
				out.println("Quantity: <input type='text' name='itemQuantity' value='" + itemQuantity + "'><br>");
				out.println("Price: <input type='text' name='itemPrice' value='" + itemPrice + "'><br>");
				out.println("Image URL: <input type='text' name='itemImage' value='" + imageURL + "'><br>");
				out.println("<input type='submit' value='Update'>");
				out.println("</form>");
			} else {
				out.println("<h3>Item ID: " + itemId + " not found.</h3>");
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}
	
	private void editTovarItem(HttpServletRequest request, PrintWriter out) {
	    int itemId = Integer.parseInt(request.getParameter("itemId"));
	    String itemName = request.getParameter("itemName");
	    int itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
	    double itemPrice = Double.parseDouble(request.getParameter("itemPrice"));
	    String itemImage = request.getParameter("itemImage");

	    try {
	        PreparedStatement pstmt = con.prepareStatement("UPDATE sklad SET nazov = ?, ks = ?, cena = ?, img = ? WHERE ID = ?");
	        pstmt.setString(1, itemName);
	        pstmt.setInt(2, itemQuantity);
	        pstmt.setDouble(3, itemPrice);
	        pstmt.setString(4, itemImage);
	        pstmt.setInt(5, itemId);

	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            out.println("<h3>Item ID: " + itemId + " updated successfully.</h3>");
	        } else {
	            out.println("<h3>Failed to update Item ID: " + itemId + ".</h3>");
	        }

	        pstmt.close();
	    } catch (Exception e) {
	        out.println(e.getMessage());
	    }
	}


	private void deleteTovarItem(HttpServletRequest request, PrintWriter out) {
		int itemId = Integer.parseInt(request.getParameter("itemId"));

		try {
			Statement stmt = con.createStatement();
			String deleteQuery = "DELETE FROM sklad WHERE ID = " + itemId;
			int rowsAffected = stmt.executeUpdate(deleteQuery);

			if (rowsAffected > 0) {
				out.println("<h3>Item ID: " + itemId + " deleted successfully.</h3>");
			} else {
				out.println("<h3>Failed to delete Item ID: " + itemId + ".</h3>");
			}

			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	private void zobrazObjednavky(PrintWriter out) {
	    try {
	        PreparedStatement pstmt = con.prepareStatement(
	                "SELECT * FROM obj_zoznam INNER JOIN users ON obj_zoznam.id_pouzivatela = users.id");
	        ResultSet rs = pstmt.executeQuery();

	        out.println("<h2>Objednávky:</h2>");

	        while (rs.next()) {
	            out.println("<p>Objednávka číslo: " + rs.getString("obj_cislo") + " | Meno a priezvisko: "
	                    + rs.getString("meno") + " " + rs.getString("priezvisko") + " | Adresa: "
	                    + rs.getString("adresa") + " | Datum: " + rs.getString("datum_objednavky") + " | Stav: " + rs.getString("stav") + "</p>");

	            out.println("<form action='adminServlet' method='post'>");
				out.println("<input type='hidden' name='operacia' value='updateStav'>");
				out.println("<input type='hidden' name='objednavkaId' value='" + rs.getInt("id") + "'>");
				out.println("<select name='stav'>");
				out.println("<option value='nova'>Nová</option>");
				out.println("<option value='expedicia'>Expedícia</option>");
				out.println("<option value='odoslana'>Odoslaná</option>");
				out.println("</select>");
				out.println("<input type='submit' value='Uložiť stav'>");
				out.println("</form>");
	            
	            out.println("<form action='adminServlet' method='post'>");
	            out.println("<input type='hidden' name='operacia' value='vymazObjednavku'>");
	            out.println("<input type='hidden' name='objednavkaId' value='" + rs.getInt("id") + "'>");
	            out.println("<input type='submit' value='Vymazať objednávku'>");
	            out.println("</form>");

	            PreparedStatement pstmtPolozky = con.prepareStatement(
	                    "SELECT s.nazov, op.cena, op.ks FROM obj_polozky op INNER JOIN sklad s ON op.id_tovaru = s.ID WHERE id_objednavky = ?");
	            pstmtPolozky.setInt(1, rs.getInt("id"));
	            ResultSet rsPolozky = pstmtPolozky.executeQuery();

	            out.println(
	                    "<table border='1'><tr><th>Názov</th><th>Cena</th><th>Kusy</th><th>Celková suma za kusy</th></tr>");

	            double celkovaSuma = 0;

	            while (rsPolozky.next()) {
	                double cena = rsPolozky.getDouble("cena");
	                double ks = rsPolozky.getDouble("ks");
	                double cenaSpolu = cena * ks;

	                celkovaSuma += cenaSpolu;

	                out.println("<tr>");
	                out.println("<td>" + rsPolozky.getString("nazov") + "</td>");
	                out.println("<td>" + cena + "</td>");
	                out.println("<td>" + ks + "</td>");
	                out.println("<td>" + cenaSpolu + "</td>");
	                out.println("</tr>");
	            }

	            out.println("<tr><td colspan='3'><b>Celková suma:</b></td><td>" + celkovaSuma
	                    + "</td></tr>");

	            out.println("</table>");
	            out.println("<hr>");
	        }

	        rs.close();
	        pstmt.close();
	    } catch (Exception e) {
	        out.println(e.getMessage());
	    }
	}

	private void vymazObjednavku(PrintWriter out, String objednavkaId) {
	    try {
	        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM obj_zoznam WHERE id = ?");
	        deleteStatement.setString(1, objednavkaId);

	        int rowsAffected = deleteStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            out.println("Objednávka bola uspesne vymazana.");
	        } else {
	            out.println("Vymazanie objednavky zlyhalo");
	        }

	        deleteStatement.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	private void updateStav(PrintWriter out, String objednavkaId, String stav) {
		try {
			PreparedStatement updateStatement = con.prepareStatement("UPDATE obj_zoznam SET stav = ? WHERE id = ?");
			updateStatement.setString(1, stav);
			updateStatement.setString(2, objednavkaId);

			int rowsAffected = updateStatement.executeUpdate();

			if (rowsAffected > 0) {
				out.println("Stav objednávky bol úspešne aktualizovaný.");
			} else {
				out.println("Aktualizácia stavu objednávky zlyhala.");
			}

			updateStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void zobrazPouzivatelov(PrintWriter out) {
		try {
			Statement stmt = con.createStatement();
			String selectQuery = "SELECT id, login, meno, priezvisko, adresa, admin FROM users";
			ResultSet rs = stmt.executeQuery(selectQuery);

			out.println("<h2>Pouzivatelia</h2>");

			while (rs.next()) {
				int userId = rs.getInt("id");
				String login = rs.getString("login");
				String meno = rs.getString("meno") + " " + rs.getString("priezvisko");
				String adresa = rs.getString("adresa");
				boolean jeAdmin = rs.getBoolean("admin");

				out.println("ID: " + userId + " | ");
				out.println("Login: " + login + " | ");
				out.println("Meno: " + meno + " | ");
				out.println("Adresa: " + adresa + " | ");
				out.println("Admin prava: " + (jeAdmin ? "Ano" : "Nie"));

				out.println("<form action='adminServlet' method='post'>");
				out.println("<input type='hidden' name='operacia' value='toggleAdmin'>");
				out.println("<input type='hidden' name='userId' value='" + userId + "'>");
				out.println("<input type='submit' value='" + (jeAdmin ? "Odobrat Admin Prava" : "Pridelit Admin Prava")
						+ "'>");
				out.println("</form>");

				out.println("<hr>");
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	private void toggleAdmin(int userId) {
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT admin FROM users WHERE id = ?");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				boolean isAdmin = rs.getBoolean("admin");
				isAdmin = !isAdmin;

				PreparedStatement updateStatement = con.prepareStatement("UPDATE users SET admin = ? WHERE id = ?");
				updateStatement.setBoolean(1, isAdmin);
				updateStatement.setInt(2, userId);

				int rowsAffected = updateStatement.executeUpdate();

				if (rowsAffected > 0) {
				} else {
				}

				updateStatement.close();
			}

			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		try {
			String operacia = request.getParameter("operacia");
			if (operacia == null)
				operacia = "";

			if (badConnection(out)) {
				return;
			}

			if (operacia.equals("zobrazTovar")) {
				zobrazTovar(out);
			}

			if (operacia.equals("addTovarItem")) {
				addTovarItem(request, out);
			}

			if (operacia.equals("showEditForm")) {
				showEditForm(request, out);
			}
			
			if (operacia.equals("editTovarItem")) {
		        editTovarItem(request, out);
		    }

			if (operacia.equals("deleteTovarItem")) {
				deleteTovarItem(request, out);
			}

			if (operacia.equals("zobrazObjenavky")) {
				zobrazObjednavky(out);
			}

			if (operacia.equals("vymazObjednavku")) {
				String objednavkaId = request.getParameter("objednavkaId");
				vymazObjednavku(out, objednavkaId);
			}

			if (operacia.equals("updateStav")) {
				String objednavkaId = request.getParameter("objednavkaId");
				String stav = request.getParameter("stav");
				updateStav(out, objednavkaId, stav);
			}

			if (operacia.equals("zobrazPouzivatelov")) {
				zobrazPouzivatelov(out);
			}

			if (operacia.equals("toggleAdmin")) {
				int userId = Integer.parseInt(request.getParameter("userId"));
				toggleAdmin(userId);
			}

			createHeader(out, request);
			zobrazTovar(out);
			zobrazObjednavky(out);
			zobrazPouzivatelov(out);

		} catch (Exception e) {
			out.println(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
