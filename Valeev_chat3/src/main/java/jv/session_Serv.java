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

@WebServlet("/session_Serv")
public class session_Serv extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL="jdbc:mysql://localhost/";
	private String userName ="root";
	private String dpassword="";
	private String database ="chatPV";
	private Connection con;
	private Statement stmt;

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    }
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			System.out.println("sessionserv opened");
			chat(request,response,out);
			out.close();
		}
	 protected void chat(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws IOException{
	 try {
		 
	        HttpSession session = request.getSession(false);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL+database, userName, dpassword);
            stmt = con.createStatement();
            
	        if (session != null) {
	        	int id = (int) session.getAttribute("id");
	            ResultSet rs = stmt.executeQuery("SELECT m.id_sender AS sender, CONCAT(u.Name, ' ', u.Surname) AS nickname, m.msg, m.date\r\n"
	            		+ "FROM message m\r\n"
	            		+ "JOIN user u ON m.id_sender = u.id\r\n"
	            		+ "WHERE m.id_sender \r\n"
	            		+ "NOT IN (SELECT blocked_id FROM blocks WHERE user_id = "+id+")\r\n"
	            		+ "ORDER BY m.date"); 
	            session.setAttribute("id", id);
	   		 out.print(" <style>\r\n"
	 		 		+ " #cell{\r\n"
	 		 		+ "resize:vertical;\r\n"
	 		 		+ "}\r\n"
	 		 		+ "  </style>");
	            
	            out.print("<table border='2' style=\"margin: 0 auto; width:50%\">");
				 
			 	out.print("<tr>"+
			 	"<th>Nickname</th>"+
			 	"<th>Message</th>"+
			 	"<th>Date</th>"+
			 	"<th>Options</th>"+
			 	"</tr>");
			 	
			 	while(rs.next()) { 
			 		System.out.println("rs next");
				    out.print("<tr>");
				    	out.print("<td id=\"cell\">" + rs.getString("nickname") + "</td>");
				    	out.print("<td id=\"cell\">" + rs.getString("msg") + "</td>");
				    	out.print("<td id=\"cell\">" + rs.getString("date") + "</td>");
				    	if(!rs.getString("sender").equals(""+id))
				    		out.print("<td> <button style=\"width:100%\" id=\"Button1\"method=\"post\"onclick=\"window.location.href = 'block_id"+"?id="+id+"&bid="+rs.getString("sender")+"'\">Block user</button>");
				    	else
				    		out.print("<td></td>");
				    out.print("</tr>");
			 	}

				 out.print("<tr>");
					out.print("<td colspan=\"10\" style=\"text-align: center;\"> ");
					out.print("<button style=\"width:100%\" id=\"Button3\"onclick=\"window.location.href = 'unblock_id'\">Blocked users</button>");
					out.print("</td>");
				out.print("</tr>");
			 out.print("</table>");
			 textwindow(out,id);

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
	 protected void textwindow(PrintWriter out,Integer id) throws IOException{

		 out.print("<table border='2' style=\"margin: 0 auto;width:50%\">");

		 	out.print("<tr>"+
		 	"<th style=\"width:100%\">Write your message</th>"+
		 	"</tr>");
		 			out.print("<tr>"); 
		 			out.print("<form action=\"new_message\" method=\"post\">");
	 				out.print("<input type=\"hidden\" name=\"id\" value=\"" + id+ "\">");
		 				out.print("<td> <textarea style=\"min-height:100px;width:100%;resize:vertical\" placeholder=\"Your message\" id=\"\"  name=\"msgbox\"></textarea></td>");
		 				out.print("<td> <input style=\"width:100%\" type=\"submit\" name=\"send\" value=\"send\"> </td>");
		 			out.print("<tr>");
		 	out.print("</form>");
				out.print("<tr>");
					out.print("<td colspan=\"10\" style=\"text-align: center;\"> ");
					out.print("<button style=\"width:100%\" id=\"Button3\"onclick=\"window.location.href = 'index.html'\">Log out</button>");
					out.print("</td>");
					
			    out.print("</tr>");
			     
		 out.println("</table>");
		 }
}
