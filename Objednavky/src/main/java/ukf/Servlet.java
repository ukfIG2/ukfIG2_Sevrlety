package ukf;

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
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String URL = "jdbc:mysql://localhost/objednavky";
    private String LOGIN = "root";
    private String PWD = ""; 
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if (action == null) {
            DisplayData(response);
        } else {
			switch (action) {
	        case "displayToadd":
	            DisplayDataToAdd(response);
	            break;
            case "updateForm":
            	DisplayDataToUpdate(request, response);
            	break;
	        case "add":
	            AddData(request, response);
	            break;
            case "update":
                UpdateData(request, response);
                break;
	        case "delete":
	            DeleteData(request, response);
	            break;
	        default:
	            DisplayData(response);
	            break;
			}
        }
	}
	void DisplayData(HttpServletResponse response) throws IOException {
		ArrayList<String> ids = new ArrayList<String>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
            Statement stmt = con.createStatement();
            String sql = ""
            		+ "SELECT * FROM objednavka o "
            		+ "LEFT JOIN tovar t ON o.id_tovaru = t.id_tovaru "
            		+ "LEFT JOIN zakaznici z ON o.id_zakaznika = z.id_zakaznika;";
            ResultSet rs = stmt.executeQuery(sql);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<table border='1'>");
            out.println("<tr>"
            		+ "<th>ID objednavky</th>"
            		+ "<th>Meno</th>"
            		+ "<th>Datum</th>"
            		+ "<th>Adresa</th>"
            		+ "<th>ICO</th>"
            		+ "<th>Tovar</th>"
            		+ "<th>Cena</th>"
            		+ "<th>Hodnotenie</th>"
            		+ "</tr>");

            while (rs.next()) {
            	ids.add(rs.getString("id_objednavky"));
                out.println("<tr>");
                out.println("<td>" + rs.getString("id_objednavky") + "</td>");
                out.println("<td>" + rs.getString("meno") + "</td>");
                out.println("<td>" + rs.getString("datum") + "</td>");
                out.println("<td>" + rs.getString("adresa") + "</td>");
                out.println("<td>" + rs.getString("ico") + "</td>");
                out.println("<td>" + rs.getString("nazov") + "</td>");
                out.println("<td>" + rs.getString("cena") + "</td>");
                out.println("<td>" + rs.getString("hodnotenie") + "</td>");
                out.print("<td>");
                Upravit_Button(out, rs.getString("id_objednavky"));
                out.print("</td>");
                out.print("<td>");
                Odstranit_Button(out, rs.getString("id_objednavky"));
                out.print("</td>");
                out.println("</tr>");
            }
            out.println("</table><br>");
            DisplayDataToAdd_Button(out);
            
            ServletChange_Button(out, "Servlet_Zakaznik", "Zakaznici", ids);
            ServletChange_Button(out, "Servlet_Tovar", "Tovar", ids);
            ServletChange_Button(out, "Servlet_Objednavky", "Spojovacia_tabulka", ids);
            
            out.println("</body></html>");

            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	void DisplayDataToUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<String> zakazniciIDs = new ArrayList<String>();
		ArrayList<String> zakazniciMeno = new ArrayList<String>();
		ArrayList<String> zakazniciAdresa = new ArrayList<String>();
		ArrayList<String> zakazniciICO = new ArrayList<String>();

		ArrayList<String> tovarIDs = new ArrayList<String>();
		ArrayList<String> tovarNazov = new ArrayList<String>();
		ArrayList<String> tovarCena = new ArrayList<String>();
		ArrayList<String> tovarHodnotenie = new ArrayList<String>();
		String id = request.getParameter("idToUpdate");
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM objednavka o "
                		+ "LEFT JOIN tovar t ON o.id_tovaru = t.id_tovaru "
                		+ "LEFT JOIN zakaznici z ON o.id_zakaznika = z.id_zakaznika "
                		+ "WHERE o.id_objednavky = ?;"); 
                Statement stmtZakaznik = con.createStatement();
                Statement stmtTovar = con.createStatement();) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            String sqlZakaznici = "SELECT * FROM zakaznici";
            String sqlTovar = "SELECT * FROM tovar";
            ResultSet rsZakaznici = stmtZakaznik.executeQuery(sqlZakaznici);
            ResultSet rsTovar = stmtTovar.executeQuery(sqlTovar);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<table border='1'>");
            out.println("<tr>"
            		+ "<th>ID objednavky</th>"
            		+ "<th>Meno</th>"
            		+ "<th>Datum</th>"
            		+ "<th>Adresa</th>"
            		+ "<th>ICO</th>"
            		+ "<th>Tovar</th>"
            		+ "<th>Cena</th>"
            		+ "<th>Hodnotenie</th>"
            		+ "</tr>");

            while (rs.next()) {
	            out.println("<tr>");
	            out.println("<td>" + rs.getString("id_objednavky") + "</td>");
	            out.println("<td>" + rs.getString("meno") + "</td>");
	            out.println("<td>" + rs.getString("datum") + "</td>");
	            out.println("<td>" + rs.getString("adresa") + "</td>");
	            out.println("<td>" + rs.getString("ico") + "</td>");
	            out.println("<td>" + rs.getString("nazov") + "</td>");
	            out.println("<td>" + rs.getString("cena") + "</td>");
	            out.println("<td>" + rs.getString("hodnotenie") + "</td>");
	            out.println("</tr>");
            }
            
            out.println("</table><br>");
            while (rsZakaznici.next()) {
            	zakazniciIDs.add(rsZakaznici.getString("id_zakaznika"));
            	zakazniciMeno.add(rsZakaznici.getString("meno"));
            	zakazniciAdresa.add(rsZakaznici.getString("adresa"));
            	zakazniciICO.add(rsZakaznici.getString("ico"));
            }
            while (rsTovar.next()) {
            	tovarIDs.add(rsTovar.getString("id_tovaru"));
            	tovarNazov.add(rsTovar.getString("nazov"));
            	tovarCena.add(rsTovar.getString("cena"));
            	tovarHodnotenie.add(rsTovar.getString("hodnotenie"));
            }
            
            out.println("<form action='Servlet' method='get'>");
            out.println("<input type='hidden' name='action' value='update'>");
            out.println("<input type='hidden' name='idObjednavky' value='" + id + "'>");

            ZakazniciSelection(out, zakazniciIDs, zakazniciMeno, zakazniciAdresa, zakazniciICO);
            TovarSelection(out, tovarIDs, tovarNazov, tovarCena, tovarHodnotenie);
            
            out.println("<input type='submit' value='Upravit_Objednavku'>");
            out.println("</form><br>");
            out.println("</body></html>");

            pstmt.close();
            stmtZakaznik.close();
            stmtTovar.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	void DisplayDataToAdd(HttpServletResponse response) throws IOException {
		ArrayList<String> zakazniciIDs = new ArrayList<String>();
		ArrayList<String> zakazniciMeno = new ArrayList<String>();
		ArrayList<String> zakazniciAdresa = new ArrayList<String>();
		ArrayList<String> zakazniciICO = new ArrayList<String>();

		ArrayList<String> tovarIDs = new ArrayList<String>();
		ArrayList<String> tovarNazov = new ArrayList<String>();
		ArrayList<String> tovarCena = new ArrayList<String>();
		ArrayList<String> tovarHodnotenie = new ArrayList<String>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
            Statement stmt = con.createStatement();
            Statement stmtZakaznik = con.createStatement();
            Statement stmtTovar = con.createStatement();
            String sql = ""
            		+ "SELECT * FROM objednavka o "
            		+ "LEFT JOIN tovar t ON o.id_tovaru = t.id_tovaru "
            		+ "LEFT JOIN zakaznici z ON o.id_zakaznika = z.id_zakaznika;";
            String sqlZakaznici = "SELECT * FROM zakaznici";
            String sqlTovar = "SELECT * FROM tovar";
            ResultSet rs = stmt.executeQuery(sql);
            ResultSet rsZakaznici = stmtZakaznik.executeQuery(sqlZakaznici);
            ResultSet rsTovar = stmtTovar.executeQuery(sqlTovar);
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<table border='1'>");
            out.println("<tr>"
            		+ "<th>ID objednavky</th>"
            		+ "<th>ID zakaznika</th>"
            		+ "<th>Meno</th>"
            		+ "<th>Datum</th>"
            		+ "<th>Adresa</th>"
            		+ "<th>ICO</th>"
            		+ "<th>Tovar</th>"
            		+ "<th>Cena</th>"
            		+ "<th>Hodnotenie</th>"
            		+ "</tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("id_objednavky") + "</td>");
                out.println("<td>" + rs.getString("id_zakaznika") + "</td>");
                out.println("<td>" + rs.getString("meno") + "</td>");
                out.println("<td>" + rs.getString("datum") + "</td>");
                out.println("<td>" + rs.getString("adresa") + "</td>");
                out.println("<td>" + rs.getString("ico") + "</td>");
                out.println("<td>" + rs.getString("nazov") + "</td>");
                out.println("<td>" + rs.getString("cena") + "</td>");
                out.println("<td>" + rs.getString("hodnotenie") + "</td>");
                out.println("</tr>");
            }
            out.println("</table><br>");
            
            while (rsZakaznici.next()) {
            	zakazniciIDs.add(rsZakaznici.getString("id_zakaznika"));
            	zakazniciMeno.add(rsZakaznici.getString("meno"));
            	zakazniciAdresa.add(rsZakaznici.getString("adresa"));
            	zakazniciICO.add(rsZakaznici.getString("ico"));
            }
            while (rsTovar.next()) {
            	tovarIDs.add(rsTovar.getString("id_tovaru"));
            	tovarNazov.add(rsTovar.getString("nazov"));
            	tovarCena.add(rsTovar.getString("cena"));
            	tovarHodnotenie.add(rsTovar.getString("hodnotenie"));
            }
            
            out.println("<form action='Servlet' method='get'>");
            out.println("<input type='hidden' name='action' value='add'>");

            ZakazniciSelection(out, zakazniciIDs, zakazniciMeno, zakazniciAdresa, zakazniciICO);
            TovarSelection(out, tovarIDs, tovarNazov, tovarCena, tovarHodnotenie);
            
            out.println("<input type='submit' value='Pridat_Objednavku'>");
            out.println("</form><br>");
            out.println("</body></html>");

            stmt.close();
            stmtZakaznik.close();
            stmtTovar.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	void AddData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idZakaznika = request.getParameter("id_zakaznik");
        String idTovaru = request.getParameter("id_tovar");
        LocalDate localDate = java.time.LocalDate.now();
        String datum = localDate.toString();
        
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO objednavka (id_zakaznika, id_tovaru, datum) VALUES (?, ?, ?)")) {
        	pstmt.setString(1, idZakaznika);
            pstmt.setString(2, idTovaru);
            pstmt.setString(3, datum);
            
            int rowsAffected = pstmt.executeUpdate();

            PrintWriter out = response.getWriter();
            if (rowsAffected > 0) {
                out.print("Data added successfully.");
            } else {
                out.print("Failed to add data.");
            }
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	void UpdateData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idObjednavky = request.getParameter("idObjednavky");
		String idZakaznika = request.getParameter("id_zakaznik");
        String idTovaru = request.getParameter("id_tovar");
        
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
                PreparedStatement pstmt = con.prepareStatement("UPDATE objednavka SET id_zakaznika=?, id_tovaru=? WHERE id_objednavky=?")) {
            pstmt.setString(1, idZakaznika);
            pstmt.setString(2, idTovaru);
            pstmt.setString(3, idObjednavky);
            pstmt.executeUpdate();
            
            response.setContentType("text/html");
        	PrintWriter out = response.getWriter();
                       
            out.print("Data successfully updated.");
            out.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	void DeleteData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idToDelete = request.getParameter("id");
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
             PreparedStatement pstmt = con.prepareStatement("DELETE FROM objednavka WHERE id_objednavky=?")) {
            pstmt.setString(1, idToDelete);
            pstmt.executeUpdate();
            
            PrintWriter out = response.getWriter();
            out.print("Data deleted successfully. " + idToDelete);
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	void Odstranit_Button(PrintWriter out, String id) {
		out.println("<form action='Servlet' method='get'>");
        out.println("<input type='hidden' name='action' value='delete'>");
        out.println("<input type='hidden' name='id' value='" + id + "'>");
        out.println("<input type='submit' value='Odstranit'>");
        out.println("</form>");
    }
	void ServletChange_Button(PrintWriter out, String ServletName, String text, ArrayList<String> ids)
	{
		String idsString = String.join(",", ids);
		out.println("<form action='" + ServletName + "' method='get'>");
        out.println("<input type='hidden' name='idsString' value='" + idsString + "'>");
        out.println("<input type='submit' value='" + text + "'>");
        out.println("</form><br>");
	}
	void DisplayDataToAdd_Button(PrintWriter out)
	{
		out.println("<form action='Servlet' method='get'>");
        out.println("<input type='hidden' name='action' value='displayToadd'>");
        out.println("<input type='submit' value='Pridat_zoznam'>");
        out.println("</form><br>");
	}
	void Upravit_Button(PrintWriter out, String id)
	{
		out.println("<form action='Servlet' method='get'>");
        out.println("<input type='hidden' name='action' value='updateForm'>");
        out.println("<input type='hidden' name='idToUpdate' value='" + id + "'>"); 
        out.println("<input type='submit' value='Upravit'>");
        out.println("</form>");
	}
	void ZakazniciSelection(PrintWriter out, ArrayList<String> id, ArrayList<String> meno, ArrayList<String> adresa, ArrayList<String> ico)
	{
		out.println("<label for='zakaznik'>Vyber zakaznika </label>");
		out.println("<select name='id_zakaznik' id='zakaznik'>");
		
		for (int i = 0; i < id.size(); i++)
		{
			out.println("<option value='" + id.get(i) + "'>" + id.get(i) + " | " + meno.get(i) + " | " + adresa.get(i) + " | " + ico.get(i) +"</option>");
		}
		
		out.println("</select>");
	}
	void TovarSelection(PrintWriter out, ArrayList<String> id, ArrayList<String> nazov, ArrayList<String> cena, ArrayList<String> hodnotenie)
	{
		out.println("<label for='tovar'>Vyber tovar </label>");
		out.println("<select name='id_tovar' id='tovar'>");
		
		for (int i = 0; i < id.size(); i++)
		{
			out.println("<option value='" + id.get(i) + "'>" + id.get(i) + " | " + nazov.get(i) + " | " + cena.get(i) + " | " + hodnotenie.get(i) +"</option>");
		}
		
		out.println("</select>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
