package jv;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/main")
public class main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL="jdbc:mysql://localhost/";
	private String userName ="root";
	private String dpassword="";
	private String database ="chatPV";
	private Connection con;
	private Statement stmt;


    public main() {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			checkId(request,response,out);
			out.close();
	    }
	 protected void checkId(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws IOException, ServletException{
	 try {
	        String login = request.getParameter("login");
	        String pwd = request.getParameter("pwd");
	        HttpSession session = request.getSession();

	            Class.forName("com.mysql.jdbc.Driver");
	             con = DriverManager.getConnection(URL+database, userName, dpassword);
	             stmt = con.createStatement();
	             
				 ResultSet rs = stmt.executeQuery("SELECT *,COUNT(id) as count FROM user WHERE login=\""+login+"\" AND pwd="+pwd); 
				 		//System.out.print("SELECT * FROM user WHERE login=\""+login+"\" AND pwd="+pwd);
				 		rs.next();
	            	 if (rs.getInt("count") == 1) { 
	            		  session = request.getSession(true);
	            		   session.setAttribute("id", rs.getInt("id")); 
	            		   response.sendRedirect("session_Serv");
	            	 } else { 
	            		   System.out.println("Account was not found");
	            		   rs.close();
	   		            stmt.close();
	   		            con.close();
	   	            	session.invalidate(); 
	   	                response.sendRedirect("indexerr.html");
	            	   }
	            rs.close();
	            stmt.close();
	            con.close();
	 }
		 catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	 }

}
