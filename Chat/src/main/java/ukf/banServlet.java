package ukf;

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


/**
 * Servlet implementation class banServlet
 */
@WebServlet("/banServlet")
public class banServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String URL = "jdbc:mysql://localhost/SKchat";
	String login = "root";
	String pwd = "";
	Connection con = null;
	String my_error = "";
	String idM ="";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public banServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		 try {
		      Class.forName("com.mysql.cj.jdbc.Driver");
		      con = DriverManager.getConnection(URL, login, pwd);
		  } catch (Exception e) {  my_error = e.getMessage();   }
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		 try {
		      con.close();
		  } catch (Exception e) {  my_error = e.getMessage();   }
	}
	
	protected void zobrazNeopravnenyPristup(PrintWriter out) {
        out.println("Neoprávnený prístup");
    }
	
	protected void setBanId(PrintWriter out, String id) {
        idM= id;
    }
	
	
	
	protected void vypisPouzivatelov(PrintWriter out) {
		   try {
		     Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery("SELECT * FROM user");
		     while (rs.next()) {
		    	if(!idM.equals(rs.getString("id"))) {
		    		 out.println("<div style=display:flex;flex-direction:row;>");
			    	 
				      out.print("<form action='banServlet' method='post'>");
				       out.print("<input type=hidden name ='id' value='"+rs.getString("id")+"'>");
				       out.println("<input type=hidden name='operacia' value='ban'>");
				       out.println("<button type=submit>Ban</button>");
				       out.println("</form>");    
				       
				       out.print("<form action='banServlet' method='post'>");
				       out.print("<input type=hidden name ='id' value='"+rs.getString("id")+"'>");
				       out.println("<input type=hidden name='operacia' value='unban'>");
				       out.println("<button type=submit>Unban</button>");
				       out.println("</form>");      
			       out.print( rs.getString("meno"));
			   
			     
			       out.println("</div>");
		    		 
		    	}
		    	
		       
		      
			     
			     
		    }	
			     out.println("<form action='viewServlet' method='post'>");
			     out.println("<input type='hidden' name='operacia' value='zobrForm'>");
			     out.println("<button type=submit>Naspäť</button>");
			     out.println("</form>");
		   

		     stmt.close();

		  } catch (Exception e) { out.println(e);}
		}
	
	protected void banovat(PrintWriter out, String id) {
	    try {
	      Statement stmt = con.createStatement();
	      String sql = "UPDATE user SET ban = 1 WHERE id = " + 
	                  id;
	      stmt.executeUpdate(sql);
	      stmt.close();
	   } catch (Exception e) {
	            out.println(e.getMessage());
	   }
	 }
	
	protected void odbanovat(PrintWriter out, String id) {
	    try {
	      Statement stmt = con.createStatement();
	      String sql = "UPDATE user SET ban = 0 WHERE id = " + 
	                  id;
	      stmt.executeUpdate(sql);
	      stmt.close();
	   } catch (Exception e) {
	            out.println(e.getMessage());
	   }
	 }
	
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 response.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = response.getWriter();

	        try {
	            if (con == null) { out.println(my_error);  return; }
	            String operacia = request.getParameter("operacia");
	      
	            if (operacia == null) { zobrazNeopravnenyPristup(out); return; }
	            if (operacia.equals("banZobraz")) {  
		 		       setBanId(out, request.getParameter("id"));
		 		  }
	    
	            
	            if (operacia.equals("ban")) {  
		 		       banovat(out, request.getParameter("id"));
		 		  }
	            
	            if (operacia.equals("unban")) {  
		 		       odbanovat(out, request.getParameter("id"));
		 		  }
	         
	            vypisPouzivatelov(out);
	        } catch (Exception e) {  out.println(e); }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
