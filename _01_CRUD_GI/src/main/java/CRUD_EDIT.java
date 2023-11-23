

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
import java.sql.Statement;

/**
 * Servlet implementation class CRUD_EDIT
 */
public class CRUD_EDIT extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CRUD_EDIT() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

        String id = request.getParameter("id");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(CRUD.URL, CRUD.username, CRUD.password);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM " + CRUD.tabulka + " WHERE " + CRUD.r1 + "='" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);

            out.println("<form action='CRUD_EDIT' method='post'>");
            out.println("<table>");
            if (rs.next()) {
                out.println("<tr>");
                out.println("<td>Datum:</td>");
                out.println("<td><input type='text' name='Datum' value='" + rs.getString(CRUD.r2) + "'></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>Prijem:</td>");
                out.println("<td><input type='text' name='Prijem' value='" + rs.getString(CRUD.r3) + "'></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>Vydaj:</td>");
                out.println("<td><input type='text' name='Vydaj' value='" + rs.getString(CRUD.r4) + "'></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>Poznamka:</td>");
                out.println("<td><input type='text' name='Poznamka' value='" + rs.getString(CRUD.r5) + "'></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td><input type='hidden' name='id' value='" + id + "'></td>");
                out.println("<td><button type='submit'>Upravit</button></td>");
                out.println("</tr>");
                //response.sendRedirect(request.getContextPath() + "/CRUD");
            }
            out.println("</table>");
            out.println("</form>");

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Pokazilo sa v EDIT_GET: " + e);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String id = request.getParameter("id");
	        String datum = request.getParameter("Datum");
	        String prijem = request.getParameter("Prijem");
	        String vydaj = request.getParameter("Vydaj");
	        String poznamka = request.getParameter("Poznamka");

	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection con = DriverManager.getConnection(CRUD.URL, CRUD.username, CRUD.password);
	            String sql = "UPDATE " + CRUD.tabulka + " SET " + CRUD.r2 + "=?, " + CRUD.r3 + "=?, " + CRUD.r4 + "=?, " + CRUD.r5 + "=? WHERE " + CRUD.r1 + "=?";
	            PreparedStatement pstmt = con.prepareStatement(sql);
	            pstmt.setString(1, datum);
	            pstmt.setString(2, prijem);
	            pstmt.setString(3, vydaj);
	            pstmt.setString(4, poznamka);
	            pstmt.setString(5, id);

	            pstmt.executeUpdate();

	            response.sendRedirect(request.getContextPath() + "/CRUD");

	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	            System.out.println("Pokazilo sa v EDIT_POST: " + e);
	        }
	    
	}

}
