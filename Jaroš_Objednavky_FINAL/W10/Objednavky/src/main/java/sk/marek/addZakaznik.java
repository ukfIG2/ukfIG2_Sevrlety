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
import java.sql.Statement;

/**
 * Servlet implementation class addZakaznik
 */
public class addZakaznik extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addZakaznik() {
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
            out.println("<form action='/Objednavky/addZakaznik' method='post'>");
            



            out.println("Meno: <input type='text' name='meno'  required><br>");
            out.println("Priezvisko: <input type='text' name='priezvisko' ><br>");
            out.println("ICO: <input type='text' name='ico' required maxlength='8'><br>");
            out.println("Adresa: <input type='text' name='adresa' required><br>");

            out.println("<input type='submit' value='Pridaj noveho zakaznika'>");
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

	        String meno = request.getParameter("meno");
	        String priezvisko = request.getParameter("priezvisko");
	        String ico = request.getParameter("ico");
	        String adresa = request.getParameter("adresa");



	      
	        String dotaz = "INSERT INTO zakaznici(meno, priezvisko, ico, adresa) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement preparedStatement = con.prepareStatement(dotaz)) {
	            preparedStatement.setString(1, meno);
	            preparedStatement.setString(2, priezvisko);
	            preparedStatement.setString(3, ico);
	            preparedStatement.setString(4, adresa);
	            


	            preparedStatement.executeUpdate();
	        }

	        response.sendRedirect(request.getContextPath() + "/Servlet_main?operacia=vypisZakaznikov");
	    } catch (Exception cE) {
	    	PrintWriter out = response.getWriter();
	    	out.print( cE.toString() );
	        System.out.println("Ovladac nenajdeny: " + cE.toString());
	    }
	}

}
