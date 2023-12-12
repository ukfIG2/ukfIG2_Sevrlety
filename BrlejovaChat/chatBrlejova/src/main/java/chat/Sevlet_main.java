package chat;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class Sevlet_main
 */
public class Sevlet_main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con;
	private String url = "jdbc:mysql://localhost/";
	private String login = "root";
	private String password = "";
	private String databaseName = "chatSB";
	private String my_error = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Sevlet_main() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url + databaseName, login, password);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			con.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
            if (operacia.equals("pridaj")) {
                pridaj(out, request);
            }
            if (operacia.equals("ban")) {
                ban(request.getParameter("userId"));
            }
            if (operacia.equals("odban")) {
                odban(request.getParameter("userId"));
            }
            if (operacia.equals("logout")) { urobLogout(out, request); return; }
            
            vypisData(out, request);
        } catch (Exception e) {  out.println(e); }

	}
	
	private void ban(String userId) {
	    try {
	        Statement stmt = con.createStatement();
	        String updateSql = "UPDATE users SET ban = '1' WHERE id = " + userId;
	        stmt.executeUpdate(updateSql);
	        stmt.close();
	    } catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void odban(String userId) {
	    try {
	        Statement stmt = con.createStatement();
	        String updateSql = "UPDATE users SET ban = '0' WHERE id = " + userId;
	        stmt.executeUpdate(updateSql);
	        stmt.close();
	    } catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	protected void zobrazNeopravnenyPristup(PrintWriter out) {
        out.println("Nemáš právo tu byť...");
        out.println("<a href=\"index.html\"> Prihlasenie</a>");
    }
	protected void OverUsera(PrintWriter out, HttpServletRequest request) {
        try {
            String meno = request.getParameter("login");
            String heslo = request.getParameter("pwd");
            Statement stmt = con.createStatement();
            String sql = "SELECT MAX(id) AS iid, COUNT(id) AS pocet FROM users "+
                    " WHERE mail = '"+meno+"' AND heslo = '"+heslo+"'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            HttpSession session = request.getSession();
            if (rs.getInt("pocet") == 1) { 
              sql = "SELECT id, mail, heslo FROM users WHERE mail = '"+meno+"'"; 
              rs = stmt.executeQuery(sql); 
              rs.next(); 
              session.setAttribute("ID", rs.getInt("id")); 
              session.setAttribute("mail", rs.getString("mail"));
              session.setAttribute("heslo", rs.getString("heslo"));
            } else { 
              out.println("Prihlasovacie údaje nie sú v poriadku.");
              session.invalidate(); 
            }
            rs.close();
            stmt.close();
           } catch (Exception ex) { out.println(ex.getMessage()); }
           }
	
	protected int getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer id = (Integer)(session.getAttribute("ID"));
        if (id == null) id = 0;
        return id;
    }
	
	protected void vypisHlavicka(PrintWriter out, HttpServletRequest request) {
        HttpSession session = request.getSession();
        out.println("<h2>" + session.getAttribute("mail") + "</h2>");
        out.println("<hr>");
 }

	protected void vypisData(PrintWriter out, HttpServletRequest request) {
		try {
		 Statement stmt = con.createStatement();
		 String sql = "SELECT posts.id, posts.meno, obsah FROM posts " +
                 "JOIN users ON posts.user_id = users.id " +
                 "WHERE users.ban = 0";
         ResultSet rs = stmt.executeQuery(sql);
		 while (rs.next()) { 
			 out.println("<b>" + rs.getString("meno") + "</b>");
             out.println(rs.getString("obsah"));
             out.println("<br>");
             out.println("<br>");
		 }
         rs.close();
         stmt.close();
         HttpSession session = request.getSession();
         out.println("<br /><br />");

         out.println("<form method='post' action='Sevlet_main'>");
         out.println("<input type='hidden' name='operacia' value='pridaj'>");
         out.println("<input type='hidden' name='mail' value='" + session.getAttribute("mail") + "'>");
         out.println("<textarea name='text'></textarea><br>");
         out.println("<input type='submit' value='Pridať príspevok'>");
         out.println("</form>");
         out.println("<br /><br />");
         out.println("<form method='post' action='Sevlet_main'>");
         out.println("<input type='hidden' name='operacia' value='logout'>");
         out.println("<input type='submit' value='logout'>");
         out.println("</form>");
         
         if (session.getAttribute("mail").equals("admin")) {
         	out.println("<h1> Editacia </h1>");
         	Statement banstmt = con.createStatement();
         	String bansql = "SELECT id, mail, ban FROM users";
            ResultSet banrs = banstmt.executeQuery(bansql);
            out.println("<table>");
			out.println("<tr>");
			out.println("<th>LOGIN</th>");
			out.println("<th></th>");
			out.println("<th></th>");
			out.println("</tr>");            
            while (banrs.next()) {
            	out.println("<tr>");
                int userId = banrs.getInt("id");
                String meno = banrs.getString("mail");
                out.print("<td>" + meno + "</td>");
                out.println("<td>");
                out.println("<form method='post' action='Sevlet_main'>");
                out.println("<input type='hidden' name='operacia' value='ban'>");
                out.println("<input type='hidden' name='userId' value='" + userId + "'>");
                out.println("<input type='submit' value='Banuj'>");
                out.println("</form>");
                out.println("</td>");
                out.println("<td>");
                out.println("<form method='post' action='Sevlet_main'>");
                out.println("<input type='hidden' name='operacia' value='odban'>");
                out.println("<input type='hidden' name='userId' value='" + userId + "'>");
                out.println("<input type='submit' value='Odbanuj'>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            banrs.close();
            banstmt.close();
         }
     } catch (Exception ex) {
         out.println(ex.getMessage());
     }
 }
	
	protected void urobLogout(PrintWriter out, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        out.println("bol si odhlaseny");
        out.println("<a href=\"index.html\"> Prihlasenie</a>");
    }
	
	protected void pridaj(PrintWriter out, HttpServletRequest request) {
	    try {
	        HttpSession session = request.getSession();
	        String mail = (String) session.getAttribute("mail");
	        String text = request.getParameter("text");
	        int userID = getUserID(request); 
	        Statement stmt = con.createStatement();
	        String sql = "INSERT INTO posts (user_id, meno, obsah) VALUES (" + userID + ", '" + mail + "', '" + text + "')";
	        int pocet = stmt.executeUpdate(sql);
	        stmt.close();
	    } catch (Exception e) {
	        out.println(e.getMessage());
	    }
	}

}
