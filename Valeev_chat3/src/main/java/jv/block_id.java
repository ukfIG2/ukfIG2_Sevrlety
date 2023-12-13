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

@WebServlet("/block_id")
public class block_id extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL="jdbc:mysql://localhost/";
	private String userName ="root";
	private String dpassword="";
	private String database ="chatPV";
	private Connection con;
	private Statement stmt;


    public block_id() {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		unblockId(request,response,out);
		out.close();
	}

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    }
	 protected void unblockId(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws IOException, ServletException{
	 try {
		 	HttpSession session = request.getSession(false);
		 	
		 	String id = request.getParameter("id");
	        String bid = request.getParameter("bid");
		 	if ((session != null)&&!id.equals(null)&&!bid.equals(null)) {

	        
	            Class.forName("com.mysql.jdbc.Driver");
	             con = DriverManager.getConnection(URL+database, userName, dpassword);
	             stmt = con.createStatement();
	             stmt.executeUpdate("INSERT INTO blocks (user_id, blocked_id) "
	            		    + "VALUES (" + id + "," + bid + ")");
				 	
	            stmt.close();
	            con.close();
	           session.setAttribute("id",Integer.parseInt(id)); 
	           response.sendRedirect("session_Serv");
		 	}
		 	else {
	        	System.out.println("Something isnt right");
	        	stmt.close();
	            con.close();
	            session.invalidate(); 
                response.sendRedirect("index.html");
		 	}
	 }
		 catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	 }

}
