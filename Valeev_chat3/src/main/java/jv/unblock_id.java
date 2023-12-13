package jv;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@WebServlet("/unblock_id")
public class unblock_id extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL="jdbc:mysql://localhost/";
	private String userName ="root";
	private String dpassword="";
	private String database ="chatPV";
	private Connection con;
	private Statement stmt;


    public unblock_id() {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		String uid = request.getParameter("uid");
		if(uid!=null) {
			writeUnblock(request,response,out);
		}
		else
			unblockId(request,response,out);
		out.close();
	}

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    }
	 protected void unblockId(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws IOException, ServletException{
	 try {
		    HttpSession session = request.getSession(false);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL+database, userName, dpassword);
            stmt = con.createStatement();
            
	        if (session != null) {
	        	int id = (int) session.getAttribute("id");
	        	session.setAttribute("id", id);
	            ResultSet rs = stmt.executeQuery("SELECT blocks.blocked_id as bid, CONCAT(u.Name, ' ', u.Surname) as blocked FROM blocks \r\n"
	            		+ "right join user u on blocks.blocked_id = u.id\r\n"
	            		+ "WHERE blocks.user_id = "+id); 
	            
	   		 out.print(" <style>\r\n"
	 		 		+ " #cell{\r\n"
	 		 		+ "resize:vertical;\r\n"
	 		 		+ "}\r\n"
	 		 		+ "  </style>");
	            
	            out.print("<table border='2' style=\"margin: 0 auto; width:50%\">");
				 
			 	out.print("<tr>"+
			 	"<th>Nickname</th>"+
			 	"<th>Options</th>"+
			 	"</tr>");
			 	
			 	while(rs.next()) { 
			 		System.out.println("rs next");
				    out.print("<tr>");
				    	out.print("<td id=\"cell\">" + rs.getString("blocked") + "</td>");
				    	out.print("<td> <button style=\"width:100%\" id=\"Button1\"method=\"post\"onclick=\"window.location.href = 'unblock_id"+"?id="+id+"&uid="+rs.getString("bid")+"'\">Unblock user</button>");
				    	System.out.println("yee");
				    	out.print("</tr>");
			 	}
				 out.print("<tr>");
					out.print("<td colspan=\"10\" style=\"text-align: center;\"> ");
					out.print("<button style=\"width:100%\" id=\"Button3\"onclick=\"window.location.href = 'session_Serv'\">Go back</button>");
					out.print("</td>");
				out.print("</tr>");

			 out.print("</table>");
			 rs.close();
	            
	        } else {
	        	System.out.println("Something isnt right");
     		   
	            stmt.close();
	            con.close();
                response.sendRedirect("index.html");
	        }
	            stmt.close();
	            con.close();
	 }
		 catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	 }
	 protected void writeUnblock(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws IOException{
		 try { 
			 Class.forName("com.mysql.cj.jdbc.Driver");
	   	 	 con = DriverManager.getConnection(URL + database, userName, dpassword);
			 stmt = con.createStatement();
			 String id = request.getParameter("id");
			 String uid = request.getParameter("uid");

			 
			 
			 if(uid!=null&&id!=null) {
	         stmt.executeUpdate("DELETE FROM blocks WHERE blocked_id = "+uid); 
	         con.close();
	         stmt.close();
	         response.sendRedirect("session_Serv");
			 }
			 else {
		        	System.out.println("Something isnt right");
		        	stmt.close();
		            con.close();
	                response.sendRedirect("index.html");
			 }
			 
			}
			 catch (SQLException | ClassNotFoundException e) {
		            e.printStackTrace();
		        }
	 }

}
