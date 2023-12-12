package sk.marek;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Servlet implementation class viewServlet
 */
public class viewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con = null;
	String my_error = "";
	
	private String URL = "jdbc:mysql://localhost/chatMJ";
    private String login = "root";
    private String pwd = "";  

       
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
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL,login,pwd);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
            if (con == null) {
            	out.println(my_error);
            	return; 
            	}
            String operacia = request.getParameter("operacia");
            if (operacia == null) { 
            	zobrazNeopravnenyPristup(out);
            	return; 
            	}
            if (operacia.equals("login")) { 
            	OverUsera(out, request); 
            	}
            int user_id = getUserID(request);
            if (user_id == 0) {
            	zobrazNeopravnenyPristup(out);
            	return; 
            	}
            vypisHlavicka(out, request);
            if (operacia.equals("potvrdit")) {
            	zapisPotvrdenie(out, request);
            	}
            if (operacia.equals("logout")) {
            	urobLogout(out, request); 
            	return; 
            	}
            if(operacia.equals("pridajSpravu")) {
            	pridajSpravu(out,request);
            }
            if(operacia.equals("zobrazBany")) {
            	zobrazBany(out, request);
            }
            if(operacia.equals("zmenitBan")) {
            	 String akcia = request.getParameter("akcia");
            	System.out.println(akcia);
            	zmenitBan(out, request, response);
            }
            if(operacia.equals("pridajPouzivatela")) {
            	pridajPouzivatela(out, request, response);
            }
            vypisData(out, request);
        } catch (Exception e) {  out.println(e); }
	}

	protected void pridajPouzivatela(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
	        String meno = request.getParameter("meno");
	        String priezvisko = request.getParameter("priezvisko");
	        String email = request.getParameter("email");
	        String passwd = request.getParameter("passwd");

	        String query = "INSERT INTO user (meno, priezvisko, email, passwd) VALUES (?, ?, ?, ?)";

	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, meno);
	            preparedStatement.setString(2, priezvisko);
	            preparedStatement.setString(3, email);
	            preparedStatement.setString(4, passwd);

	            preparedStatement.executeUpdate();
	            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	private void pridajSpravu(PrintWriter out, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			
			String content = request.getParameter("content");
			Statement stmt = con.createStatement();
			HttpSession session = request.getSession();
			Integer user_ID = (Integer)session.getAttribute("ID");
			
			// Získanie aktuálneho dátumu a času
	        LocalDateTime dateTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String formattedDateTime = dateTime.format(formatter);
//			
	        String insertQuery = "INSERT INTO post (id_user, content, date) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, user_ID);
                preparedStatement.setString(2, content);
                preparedStatement.setString(3, formattedDateTime);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Dáta boli úspešne vložené do tabuľky post.");
                } else {
                    System.out.println("Chyba pri vkladaní dát do tabuľky post.");
                }
            }
            
            stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		PrintWriter out = response.getWriter();
		out.println("<a href='index.html'> prihlasenie</a>");

	}
	
	protected void zobrazNeopravnenyPristup(PrintWriter out) {
        out.println("Nemáš právo tu byť...");
    }
	
	 protected void OverUsera(PrintWriter out, HttpServletRequest request) {
	        try {
	            String meno = request.getParameter("login");
	            String heslo = request.getParameter("pwd");
	            Statement stmt = con.createStatement();
	            String sql = "SELECT MAX(id) AS iid, COUNT(id) AS pocet FROM user "+
	                    " WHERE email = '"+meno+"' AND passwd = '"+heslo+"'";
	            ResultSet rs = stmt.executeQuery(sql);
	            rs.next();
	            HttpSession session = request.getSession();
	            if (rs.getInt("pocet") == 1) { // ak je user s menom a heslo jeden - OK
	              sql = "SELECT id, meno, priezvisko FROM user WHERE email = '"+meno+"'"; 
	              rs = stmt.executeQuery(sql); // prečítam jeho údaje z db
	              rs.next(); // nastavím sa na prvý načítaný záznam
	              session.setAttribute("ID", rs.getInt("id")); // a do session povkladám data
	              session.setAttribute("meno", rs.getString("meno"));
	              session.setAttribute("priezvisko", rs.getString("priezvisko"));
	            } else { // ak je userov 0 alebo viac – autorizacia sa nepodarila
	              out.println("Prihlasovacie údaje nie sú v poriadku.");
	              session.invalidate(); // vymazem session
	            }
	            rs.close();
	            stmt.close();
	           } catch (Exception ex) { out.println(ex.getMessage()); }
	           
//			  if (operacia.equals("login")) { OverUsera(out, request); }
//			  int user_id = getUserID(request);
//			  if (user_id == 0) { zobrazNeopravnenyPristup(out); return; }
			  
	}

	protected int getUserID(HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    Integer id = (Integer)(session.getAttribute("ID"));
	    if (id == null) id = 0;//asi radsej -1
	    return id;
	}
	
	
	protected void vypisHlavicka(PrintWriter out, HttpServletRequest request) {
        HttpSession session = request.getSession();
        out.println("<h2>" 
                  + session.getAttribute("meno") + " " 
                  + session.getAttribute("priezvisko")+"</h2>");
        out.println("<hr>");
 }

	
	protected void vypisData(PrintWriter out, HttpServletRequest request) {
		try {
			
		 Statement stmt = con.createStatement();
//		 String sql = "SELECT znamky.id, znamky.datum,predmet, znamka, videne FROM znamky " 
//		              +" INNER JOIN predmety ON (znamky.predmet_id = predmety.id) "
//		              +" WHERE user_id = " + getUserID(request);
		 String sql = "SELECT * FROM chatMJ.user INNER JOIN post ON user.id = post.id_user;";
		 ResultSet rs = stmt.executeQuery(sql);
		 while (rs.next()) { // samostatný formulár pre každý riadok/známku
//		   out.println("<form method='post' action='viewServlet'>");
//		   out.println(rs.getString("datum"));
//		   out.println("<b>" + rs.getString("predmet") + "</b>");
//		   out.println(rs.getString("znamka"));
//		   if (rs.getString("videne") == null) { // tlačidlo, ak to nevidel aj s hidden
//		     out.println("<input type='hidden' name='operacia' value='potvrdit'>");
//		     out.println("<input type='hidden' name='id' value='"+rs.getString("id")+"'>");
//		     out.println("<input type='submit' value='potvrd videnie'>");
//		   } else { // výpis dátumu, ak známku potvrdil
//		      out.println("videl: " + rs.getString("videne")); }
//		   out.println("</form>"); // ukončenie formu 
			 int id_aktualneho_vypisu = rs.getInt("id");
			 if(overUserBan(id_aktualneho_vypisu, request)) {
				 continue;
			 }
			 out.println("<p>"+ rs.getObject("date", LocalDateTime.class) + "</p>");
			 out.println("<p>" + rs.getString("priezvisko") +"</p>");
			 out.println("<p>"+ rs.getString("content") +"</p>");
			 out.println("<hr>");

		 }
         rs.close();
         stmt.close();
         
         
         out.println("<br /><br />");
         out.println("<form method='post' action='viewServlet'>");
         out.println("<input type='hidden' name='operacia' value='pridajSpravu'>");
         out.println("<input type=text name='content' >");
         out.println("<input type='submit' value='Odoslat spravu'>");
         out.println("</form>");
         
         out.println("<br /><br />");
         out.println("<form method='post' action='viewServlet'>");
         out.println("<input type='hidden' name='operacia' value='zobrazBany'>");
         out.println("<input type='submit' value='Zobrazit zabanovanych/bloknutych'>");
         out.println("</form>");
         
         
         out.println("<br /><br />");
         out.println("<form method='post' action='viewServlet'>");
         out.println("<input type='hidden' name='operacia' value='pridajPouzivatela'>");
         out.println("Meno: <input type='text' name='meno'>");
         out.println("Priezvisko: <input type='text' name='priezvisko'>");
         out.println("email: <input type='text' name='email'>");
         out.println("Heslo: <input type='text' name='passwd'>");
         out.println("<input type='submit' value='Pridaj'>");
         out.println("</form>");
         
         
         out.println("<br /><br />");
         out.println("<form method='post' action='viewServlet'>");
         out.println("<input type='hidden' name='operacia' value='logout'>");
         out.println("<input type='submit' value='logout'>");
         out.println("</form>");

     } catch (Exception ex) {
         out.println(ex.getMessage());
     }
 }
	

	protected void zobrazBany(PrintWriter out, HttpServletRequest request) {
	    try {
	        HttpSession session = request.getSession();
	        Statement stmt = con.createStatement();
	        
	        String query = "SELECT u.id AS user_id, u.meno, u.priezvisko, u.email, " +
	                       "CASE WHEN b.id_user IS NOT NULL THEN 'banned' ELSE 'not banned' END AS ban_status " +
	                       "FROM user AS u LEFT JOIN ban AS b ON u.id = b.id_user AND b.blocked_by =" +
	                       (Integer) session.getAttribute("ID") +
	                       " WHERE u.id <> " + (Integer) session.getAttribute("ID");

	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            ResultSet resultSet = preparedStatement.executeQuery();

	            out.println("<html><body><table border=\"1\">");
	            out.println("<tr><th>User ID</th><th>Meno</th><th>Priezvisko</th><th>Email</th><th>Ban Status</th><th>Zmenit ban</th></tr>");

	            while (resultSet.next()) {
	                String banStatus = resultSet.getString("ban_status");
	                String buttonText = banStatus.equals("banned") ? "Odbanovat" : "Zabanovat";

	                out.println("<tr><td>" + resultSet.getInt("user_id") + "</td>"
	                        + "<td>" + resultSet.getString("meno") + "</td>"
	                        + "<td>" + resultSet.getString("priezvisko") + "</td>"
	                        + "<td>" + resultSet.getString("email") + "</td>"
	                        + "<td>" + banStatus + "</td>"
	                        + "<td>" +
	                            "<form method='post' action='viewServlet'>" +
	                                "<input type='hidden' name='operacia' value='zmenitBan'>" +
	                                "<input type='hidden' name='akcia' value='" + buttonText+ "'>" +
	                                "<input type='hidden' name='user_id' value='" + resultSet.getInt("user_id") + "'>" +
	                                "<input type='submit' value='" + buttonText + "'>" +
	                            "</form>" +
	                        "</td></tr>");
	            }

	            out.println("</table></body></html>");
	        }

	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	protected void zmenitBan(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		try {
	        HttpSession session = request.getSession();
	        int userId = (Integer) session.getAttribute("ID");
	        int targetUserId = Integer.parseInt(request.getParameter("user_id"));

	        String akcia = request.getParameter("akcia");
	        String query = "";jmak@gmail.com

	        if ("Odbanovat".equals(akcia)) {
	            query = "DELETE FROM ban WHERE (id_user = ? AND blocked_by = ?) OR (id_user = ? AND blocked_by = ?)";
	        } else if ("Zabanovat".equals(akcia)) {
	            query = "INSERT INTO ban (id_user, blocked_by) VALUES (?, ?), (?, ?)";
	        }

	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            if ("Odbanovat".equals(akcia)) {
	                preparedStatement.setInt(1, userId);
	                preparedStatement.setInt(2, targetUserId);
	                preparedStatement.setInt(3, targetUserId);
	                preparedStatement.setInt(4, userId);
	            } else if ("Zabanovat".equals(akcia)) {
	                preparedStatement.setInt(1, userId);
	                preparedStatement.setInt(2, targetUserId);
	                preparedStatement.setInt(3, targetUserId);
	                preparedStatement.setInt(4, userId);
	            }

	            preparedStatement.executeUpdate();
//	            response.sendRedirect("Servlet_main?operacia=zobrazBany");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	

	protected boolean overUserBan(int id_poslaneho, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			HttpSession session = request.getSession();
			Statement stmt = con.createStatement();
			String sqlQuery = "SELECT user.id AS user_id, user.meno, user.priezvisko, user.email, user.passwd, ban.blocked_by "
	                + "FROM user INNER JOIN ban ON user.id = ban.id_user WHERE ban.blocked_by =" + (Integer)session.getAttribute("ID");
			 try (PreparedStatement preparedStatement = con.prepareStatement(sqlQuery)) {
	                
	                ResultSet resultSet = preparedStatement.executeQuery();

	                
	                while (resultSet.next()) {
	                    int userId = resultSet.getInt("user_id");
	                    if(userId == id_poslaneho) {
	                    	return true;
	                    }

	                }
	            }
			 
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return false;
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
	        out.println("bye bye");
	    }




}
