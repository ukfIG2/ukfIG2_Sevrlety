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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/new_message")
public class new_message extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL="jdbc:mysql://localhost/";
	private String userName ="root";
	private String dpassword="";
	private String database ="chatPV";
	private Connection con;
	private Statement stmt;


    public new_message() {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			writeMessage(request,response,out);
			out.close();
	    }
	 protected void writeMessage(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws IOException, ServletException{
	 try {
		 	HttpSession session = request.getSession(false);
		 	
		 	String id = request.getParameter("id");
	        String msg = request.getParameter("msgbox");
		 	if ((session != null)&&!msg.equals(null)&&!id.equals(null)) {

	        LocalDateTime currentDateTime = LocalDateTime.now();
	        
	            Class.forName("com.mysql.jdbc.Driver");
	             con = DriverManager.getConnection(URL+database, userName, dpassword);
	             stmt = con.createStatement();
	             stmt.executeUpdate("INSERT INTO message (id_sender, msg, date) VALUES (" + id + ", '" + safeString(msg) + "', '" + 
	             Timestamp.valueOf(currentDateTime) + "')");
				 	
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
	  public static String safeString(String input) {
	        Pattern Chars = Pattern.compile("[{}()\\[\\].'\"+*?^$\\\\|]");
	        Matcher matcher = Chars.matcher(input);
	        
	        StringBuffer stringBuffer = new StringBuffer();
	        while (matcher.find()) {
	            matcher.appendReplacement(stringBuffer, "\\\\" + matcher.group());
	        }
	        matcher.appendTail(stringBuffer);
	        
	        return stringBuffer.toString();
	    }

}
