package sk.marek;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class editTovar
 */
public class editTovar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public editTovar() {
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

	       
	        String query = "SELECT * FROM tovar WHERE id = ?";
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setInt(1, id);
	            ResultSet rs = preparedStatement.executeQuery();

	            if (rs.next()) {
	                
	                String nazov = rs.getString("nazov");
	                double cena = rs.getDouble("cena");
	                int hodnotenie = rs.getInt("hodnotenie");
	                

	               
	                response.setContentType("text/html");
	                PrintWriter out = response.getWriter();
	                out.println("<html><body>");
	                out.println("<form action='/Objednavky/editTovar' method='post'>");
	                out.println("<input type='hidden' name='id' value='" + id + "'>");



	                out.println("Nazov: <input type='text' name='nazov' value='" + nazov+ "' required><br>");
	                out.println("Cena: <input type='text' name='cena' value='" + cena + "'><br>");
	                out.println("Hodnotenie: <input type='number' name='hodnotenie' value='" + hodnotenie+ "' required min='0' max='10'><br>");

	                out.println("<input type='submit' value='Upraviť'>");
	                out.println("</form>");
	                
	                out.println("<form action='Servlet_main' method='post'>");
	                out.println("<input type='hidden' name='operacia' value='vypisTovaru'>");
	                out.println("<input type='submit' value='Spat na Tovary'>");
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
	        String nazov = request.getParameter("nazov");
	        double cena = Double.parseDouble(request.getParameter("cena"));
	        String hodnotenie = request.getParameter("hodnotenie");


	        
	        String updateQuery = "UPDATE tovar SET nazov = ?, cena = ?, hodnotenie = ? WHERE id = ?";
	        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
	            preparedStatement.setString(1, nazov);
	            preparedStatement.setDouble(2, cena);
	            preparedStatement.setString(3, hodnotenie);

	            preparedStatement.setInt(4, id);

	            preparedStatement.executeUpdate();
	        }

	        response.sendRedirect(request.getContextPath() + "/Servlet_main?operacia=vypisTovaru");
	    } catch (Exception e) {
	    	PrintWriter out = response.getWriter();
	    	out.print( e.toString() );
	        System.out.println("Ovládač nenájdený: " + e.toString());
	    }
	}

}
