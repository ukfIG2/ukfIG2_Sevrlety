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

//SPÚŠŤAJ CEZ HLAVNÝ Servlet.java

public class Servlet_Tovar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL = "jdbc:mysql://localhost/objednavky";
    private String LOGIN = "root";
    private String PWD = ""; 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

        if (action == null) {
            DisplayData(request, response);
        } else {
            switch (action) {
                case "add":
                    AddData(request, response);
                    break;
                case "update":
                    UpdateData(request, response);
                    break;
                case "updateForm":
                	DisplayDataToUpdate(request, response);
                	break;
                case "check":
                	CheckIfInConTable(request, response);
                	break;
                default:
                    DisplayData(request, response);
                    break;
            }
        }
	}
	void DisplayData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idsString = request.getParameter("idsString");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM tovar";
            ResultSet rs = stmt.executeQuery(sql);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<table border='1'>");
            out.println("<tr>"
            		+ "<th>ID</th>"
            		+ "<th>Nazov</th>"
            		+ "<th>Cena</th>"
            		+ "<th>Hodnotenie</th>"
            		+ "</tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("id_tovaru") + "</td>");
                out.println("<td>" + rs.getString("nazov") + "</td>");
                out.println("<td>" + rs.getString("cena") + "</td>");
                out.println("<td>" + rs.getString("hodnotenie") + "</td>");
                out.print("<td>");
                Upravit_Button(out, rs.getString("id_tovaru"));
                out.print("</td>");
                out.print("<td>");
                Odstranit_Button(out, rs.getString("id_tovaru"), idsString);
                out.print("</td>");
                out.println("</tr>");
            }
            out.println("</table><br>");
            Pridat_Button(out);
            out.println("</body></html>");

            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	void DisplayDataToUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM tovar";
            ResultSet rs = stmt.executeQuery(sql);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<table border='1'>");
            out.println("<tr>"
            		+ "<th>ID</th>"
            		+ "<th>Nazov</th>"
            		+ "<th>Cena</th>"
            		+ "<th>Hodnotenie</th>"
            		+ "</tr>");

            String idToUpdate = request.getParameter("idToUpdate");
            
            while (rs.next()) {
            	out.println("<tr>");
                if (idToUpdate.equals(rs.getString("id_tovaru")))
                {
                	out.println("<form action='Servlet_Tovar' method='get'>");
                    out.println("<input type='hidden' name='action' value='update'>");
                    out.println("<td><input type='text' name='id_tovaru' value='" + rs.getString("id_tovaru") + "'></td>");
                    out.println("<td><input type='text' name='nazov' value='" + rs.getString("nazov") + "'></td>");
                    out.println("<td><input type='text' name='cena' value='" + rs.getString("cena") + "'></td>");
                    out.println("<td><input type='text' name='hodnotenie' value='" + rs.getString("hodnotenie") + "'></td>");
                    out.println("<td><input type='submit' value='Upravit'></td>");
                    out.println("</form>");
                }
                else
                {
                    out.println("<td>" + rs.getString("id_tovaru") + "</td>");
                    out.println("<td>" + rs.getString("nazov") + "</td>");
                    out.println("<td>" + rs.getString("cena") + "</td>");
                    out.println("<td>" + rs.getString("hodnotenie") + "</td>");
                }
                out.println("</tr>");
        	}
            
            out.println("</table><br>");
            
            out.println("</body></html>");

            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	void AddData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nazov = request.getParameter("nazov");
        String cena = request.getParameter("cena");
        String hodnotenie = request.getParameter("hodnotenie");
        
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO tovar (nazov, cena, hodnotenie) VALUES (?, ?, ?)")) {
        	pstmt.setString(1, nazov);
            pstmt.setString(2, cena);
            pstmt.setString(3, hodnotenie);
            
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
        String id = request.getParameter("id_tovaru");
        String nazov = request.getParameter("nazov");
        String cena = request.getParameter("cena");
        String hodnotenie = request.getParameter("hodnotenie");
        
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
                PreparedStatement pstmt = con.prepareStatement("UPDATE tovar SET nazov=?, cena=?, hodnotenie=? WHERE id_tovaru=?")) {
            pstmt.setString(1, nazov);
            pstmt.setString(2, cena);
            pstmt.setString(3, hodnotenie);
            pstmt.setString(4, id);
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
	void DeleteData(HttpServletRequest request, HttpServletResponse response, String idToDelete) throws IOException {
        try (Connection con = DriverManager.getConnection(URL, LOGIN, PWD);
             PreparedStatement pstmt = con.prepareStatement("DELETE FROM tovar WHERE id_tovaru=?")) {
            pstmt.setString(1, idToDelete);
            pstmt.executeUpdate();
            
            PrintWriter out = response.getWriter();
            out.print("Data deleted successfully. " + idToDelete);
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	void Pridat_Button(PrintWriter out)
    {
        out.println("<form action='Servlet_Tovar' method='get'>");
        out.println("<input type='hidden' name='action' value='add'>");
        out.println("<h2>Pridat Tovar</h2>");
        out.println("Nazov");
        out.println("<input type='text' name='nazov'>");
        out.println("Cena");
        out.println("<input type='text' name='cena'>");
        out.println("Hodnotenie");
        out.println("<input type='text' name='hodnotenie'>");
        out.println("<input type='submit' value='Pridat'>");
        out.println("</form><br>");
    }
	void Upravit_Button(PrintWriter out, String id)
	{
		out.println("<form action='Servlet_Tovar' method='get'>");
        out.println("<input type='hidden' name='action' value='updateForm'>");
        out.println("<input type='hidden' name='idToUpdate' value='" + id + "'>"); 
        out.println("<input type='submit' value='Upravit'>");
        out.println("</form>");
	}
	void Odstranit_Button(PrintWriter out, String id, String idsString) {
		out.println("<form action='Servlet_Tovar' method='get'>");
        out.println("<input type='hidden' name='action' value='check'>");
        out.println("<input type='hidden' name='id' value='" + id + "'>");
        out.println("<input type='hidden' name='idsString' value='" + idsString + "'>");
        out.println("<input type='submit' value='Odstranit'>");
        out.println("</form>");
    }
	void CheckIfInConTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idsString = request.getParameter("idsString");
		String id = request.getParameter("id");
		String ids[] = idsString.split(",");

        response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		boolean canDelete = true;
		
		for (String idToCheck : ids)
		{
			if(idToCheck == null) { break; }
			else if (idToCheck.equals(id)) { canDelete = false; }
		}
		if (canDelete)
		{
			DeleteData(request, response, id);
		}
		else {
			out.println("Nie je mozne vymazat prvok, pretoze je v spojovacej tabulke");
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
