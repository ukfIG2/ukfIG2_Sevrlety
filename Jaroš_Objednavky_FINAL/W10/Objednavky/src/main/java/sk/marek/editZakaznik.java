package sk.marek;

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

/**
 * Servlet implementation class editZakaznik
 */
public class editZakaznik extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public editZakaznik() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(URL, login, pwd);

	        
	        String idParam = request.getParameter("id");
	        int id = Integer.parseInt(idParam);

	       
	        String query = "SELECT * FROM zakaznici WHERE id = ?";
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setInt(1, id);
	            ResultSet rs = preparedStatement.executeQuery();

	            if (rs.next()) {
	                
	                String meno = rs.getString("meno");
	                String priezvisko = rs.getString("priezvisko");
	                String ico = rs.getString("ico");
	                String adresa = rs.getString("adresa");
	                

	               
	                response.setContentType("text/html");
	                PrintWriter out = response.getWriter();
	                out.println("<html><body>");
	                out.println("<form action='/Objednavky/editZakaznik' method='post'>");
	                out.println("<input type='hidden' name='id' value='" + id + "'>");



	                out.println("Meno: <input type='text' name='meno' value='" + meno+ "' required><br>");
	                out.println("Priezvisko: <input type='text' name='priezvisko' value='" + priezvisko + "'><br>");
	                out.println("ICO: <input type='text' name='ico' value='" + ico+ "' required maxlength='8' ><br>");
	                out.println("Adresa: <input type='text' name='adresa' value='" + adresa + "'><br>");

	                out.println("<input type='submit' value='Upraviť zakaznika'>");
	                out.println("</form>");
	                
	                out.println("<form action='Servlet_main' method='post'>");
	                out.println("<input type='hidden' name='operacia' value='vypisZakaznikov'>");
	                out.println("<input type='submit' value='Spat na Zakaznikov'>");
	                out.println("</form>");
	                out.println("</body></html>");
	                
	            }
	            
	        }
	    } catch (Exception e) {
	        System.out.println("Ovládač nenájdený: " + e.toString());
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(URL, login, pwd);

	        
	        int id = Integer.parseInt(request.getParameter("id"));
	        String meno = request.getParameter("meno");
	        String priezvisko = request.getParameter("priezvisko");
	        String ico = request.getParameter("ico");
	        String adresa = request.getParameter("adresa");


	        
	        String updateQuery = "UPDATE zakaznici SET meno = ?, priezvisko = ?, ico = ?, adresa = ? WHERE id = ?";
	        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
	            preparedStatement.setString(1, meno);
	            preparedStatement.setString(2, priezvisko);
	            preparedStatement.setString(3, ico);
	            preparedStatement.setString(4, adresa);

	            preparedStatement.setInt(5, id);

	            preparedStatement.executeUpdate();
	        }

	        response.sendRedirect(request.getContextPath() + "/Servlet_main?operacia=vypisZakaznikov");
	    } catch (Exception e) {
	    	PrintWriter out = response.getWriter();
	    	out.print( e.toString() );
	        System.out.println("Ovládač nenájdený: " + e.toString());
	    }
	}

}
