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
import jakarta.servlet.http.HttpSession;





/**
 * Servlet implementation class viewServlet
 */
@WebServlet("/viewServlet")
public class viewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String URL = "jdbc:mysql://localhost/SKchat";
	String login = "root";
	String pwd = "";
	Connection con = null;
	String my_error = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public viewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init();
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
	
	protected void zapisPotvrdenie(PrintWriter out, HttpServletRequest request) {
	    try {
	      Statement stmt = con.createStatement();
	      String sql = "UPDATE znamky SET videne = NOW() WHERE id = " + 
	                  request.getParameter("id");
	      stmt.executeUpdate(sql);
	      stmt.close();
	   } catch (Exception e) {
	            out.println(e.getMessage());
	   }
	 }

	protected void urobLogout(PrintWriter out, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        out.println("Dovidenia");
        out.println("<a href='index.html'>Domov</a>");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void zobrazNeopravnenyPristup(PrintWriter out) {
        out.println("Neoprávnený prístup");
    }
	
	protected int getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer id = (Integer)(session.getAttribute("ID"));
        if (id == null) id = 0;
        return id;
    }
	
	
	
	  protected void vypisHlavicka(PrintWriter out, HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        out.println("<h2>" 
	                  + "Chatroom" + "</h2>");
	        out.println("<hr>");
	 }
	  
	  protected void vypisData(PrintWriter out, HttpServletRequest request) {
		  try {
		   Statement stmt = con.createStatement();
		   String sql = "SELECT user.id,user.ban, prispevok.meno,prispevok.content FROM prispevok INNER JOIN user ON user.id = prispevok.id_user";
		   ResultSet rs = stmt.executeQuery(sql);
		   while (rs.next()) { 
		     out.println("<form method='post' action='viewServlet'>");
	
		     out.println(rs.getString("meno") + "</b>");
		     if(rs.getString("ban").equals("0")) {
		    	 out.println(": "+rs.getString("content"));
		     }
		     else out.println(": removed");
		     out.println("</form>"); 
		   }
		   rs.close();
           stmt.close();
           out.println("<br /><br />");
           
           out.println("<form method='post' action='viewServlet'>");
           out.println("<input type='hidden' name='operacia' value='pridanie'>");
           out.println("<label for=Prispevok>Prispevok:</label>");
           out.println("<input type='text' name='Prispevok'>");
           out.println("<input type='submit' value='Pridaj'>");
           out.println("</form>");
           
           out.println("<form method='post' action='banServlet'>");
           out.println("<input type='hidden' name='operacia' value='banZobraz'>");
           out.println("<input type='hidden' name='id' value="+getUserID(request)+">");
           out.println("<input type='submit' value='Sprava banov'>");
           out.println("</form>");
           
           out.println("<form method='post' action='viewServlet'>");
           out.println("<input type='hidden' name='operacia' value='logout'>");
           out.println("<input type='submit' value='logout'>");
           out.println("</form>");
           

       } catch (Exception ex) {
           out.println(ex.getMessage());
       }
   }
	  
	
	protected void OverUsera(PrintWriter out, HttpServletRequest request) {
		 try {
	            String meno = request.getParameter("login");
	            String heslo = request.getParameter("pwd");
	            Statement stmt = con.createStatement();
	            String sql = "SELECT MAX(id) AS iid, COUNT(id) AS pocet FROM user "+
	                    " WHERE meno = '"+meno+"' AND heslo = '"+heslo+"'";
	            ResultSet rs = stmt.executeQuery(sql);
	            rs.next();
	            HttpSession session = request.getSession();
	            if (rs.getInt("pocet") == 1) {
	              sql = "SELECT id, meno, ban FROM user WHERE meno = '"+meno+"'"; 
	              rs = stmt.executeQuery(sql); 
	              rs.next();
	              session.setAttribute("ID", rs.getInt("id")); 
	              session.setAttribute("meno", rs.getString("meno"));
	              session.setAttribute("ban", rs.getString("ban"));
	            } else {
	              out.println("Prihlasovacie údaje nie sú v poriadku.");
	              session.invalidate(); 
	            }
	            rs.close();
	            stmt.close();
	           } catch (Exception ex) { out.println(ex.getMessage()); }
           }
	
	private void pridajPolozku(PrintWriter out,  HttpServletRequest request, String Prispevok) {
        try {
            Statement stmt = con.createStatement();
           HttpSession session = request.getSession();
            if(session.getAttribute("ban").equals("0")) {
            	 String sql = "INSERT INTO prispevok (id_user, meno, content) VALUES (";
                 sql += "'" + getUserID(request) + "', ";
                 sql += "'" + session.getAttribute("meno") + "', "; 
                 sql += "'" + Prispevok + "') "; 
                 int pocet = stmt.executeUpdate(sql);
                 out.print("Bolo pridanych " + pocet + " zaznamov.<br/>");
     
            }
            else out.print("Si zabanovany, nemozes postovat");
        } catch (Exception e) { out.println(e);}
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 response.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = response.getWriter();

	        try {
	            if (con == null) { out.println(my_error);  return; }
	            String operacia = request.getParameter("operacia");
	            if (operacia == null) { zobrazNeopravnenyPristup(out); return; }
	            if (operacia.equals("login")) { OverUsera(out, request); }
	            int user_id = getUserID(request);
	            if (user_id == 0) { zobrazNeopravnenyPristup(out); return; }
	            vypisHlavicka(out, request);
	            if (operacia.equals("pridanie")) {  
	 		       pridajPolozku(out, request, request.getParameter("Prispevok"));
	 		  }
	            if (operacia.equals("logout")) { urobLogout(out, request); return; }
	            vypisData(out, request);
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
