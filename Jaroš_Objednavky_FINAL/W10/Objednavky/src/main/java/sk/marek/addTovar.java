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
import java.sql.Statement;

/**
 * Servlet implementation class addTovar
 */
public class addTovar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addTovar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	        try  {
	        		response.setContentType("text/html");
	                PrintWriter out = response.getWriter();
	                out.println("<html><body>");
	                out.println("<form action='/Objednavky/addTovar' method='post'>");
	                



	                out.println("Nazov: <input type='text' name='nazov'  required><br>");
	                out.println("Cena: <input type='text' name='cena' ><br>");
	                out.println("Hodnotenie: <input type='number' name='hodnotenie' max='10' min='0' required><br>");

	                out.println("<input type='submit' value='Pridaj novy tovar'>");
	                out.println("</form>");
	                out.println("</body></html>");
	            
	            
	        
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
	        Statement stmt = con.createStatement();

	        String nazov = request.getParameter("nazov");
	        String cena = request.getParameter("cena");
	        String hodnotenie= request.getParameter("hodnotenie");


	      
	        String dotaz = "INSERT INTO tovar(nazov, cena, hodnotenie) VALUES (?, ?, ?)";
	        try (PreparedStatement preparedStatement = con.prepareStatement(dotaz)) {
	            preparedStatement.setString(1, nazov);
	            preparedStatement.setDouble(2, Double.parseDouble(cena));
	            preparedStatement.setString(3, hodnotenie);

	            preparedStatement.executeUpdate();
	        }

	        response.sendRedirect(request.getContextPath() + "/Servlet_main?operacia=vypisTovaru");
	    } catch (Exception cE) {
	    	PrintWriter out = response.getWriter();
	    	out.print( cE.toString() );
	        System.out.println("Ovladac nenajdeny: " + cE.toString());
	    }
	}

}
