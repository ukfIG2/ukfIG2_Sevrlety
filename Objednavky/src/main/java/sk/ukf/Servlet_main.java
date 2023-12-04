package sk.ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




public class Servlet_main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;

	public void init(ServletConfig config) throws ServletException {
	    try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    con = DriverManager.getConnection("jdbc:mysql://localhost/objednavkyMR", "root", "");
	    } catch (Exception e) {}
	}

	
	public void destroy() {
	  try {
	      con.close();
	  } catch (Exception ex) {}
	  super.destroy();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("text/html;charset=UTF-8");
	  PrintWriter out = response.getWriter();
	  if (con == null) {
	      out.println("niet spojenia na datab�zu");
	       return;
	  }
	  String operacia = request.getParameter("operacia");
	  if (operacia == null) operacia=""; out.println("Objednavky: " + operacia);

	   vypisDatabazy(out);
	   
	}
	
	
	protected void vypisDatabazy(PrintWriter out) {
	    try {
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM objednavky");
	        while (rs.next()) {
	            out.println("<div style='margin-bottom: 10px;'>"); 
	            out.print(rs.getString("id") + " ");
	            out.print(rs.getString("zakaznici_id") + " ");
	            out.print(rs.getString("tovar_id") + " ");
	            out.print(rs.getString("datum") + " ");

	            out.println("<form action='EditOrderServlet' method='post' style='display: inline;'>");
	            out.println("<input type='hidden' name='id' value='" + rs.getString("id") + "'>");
	            out.println("<input type='submit' value='Editovať'>");
	            out.println("</form>");

	            out.println("<form action='DeleteOrderServlet' method='post' style='display: inline;'>");
	            out.println("<input type='hidden' name='id' value='" + rs.getString("id") + "'>");
	            out.println("<input type='submit' value='Vymazať'>");
	            out.println("</form>");
	            out.println("</div>"); 
	        }

	     out.println("<form action='TovarServlet' method='post' target='_blank'>");
	     out.println("<input type='submit' value='Tovar'>");
	     out.println("</form>");
	     
	     out.println("<form action='ZakazniciServlet' method='post' target='_blank'>");
	     out.println("<input type='submit' value='Zakaznici'>");
	     out.println("</form>");
	     
	     out.println("<form action='AddOrderServlet' method='get'>");
	     out.println("<input type='submit' value='Pridať novú objednávku'>");
	     out.println("</form>");
	     
	     stmt.close();
	     
	     

	  } catch (Exception e) { out.println(e);}
	}

	 


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}