package servlety;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
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

/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con = null;
	String my_error = "";
	
	@Override
	 public void init() throws ServletException {
	   super.init();
	   try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection("jdbc:mysql://localhost/chatJA", "root", "");
	  } catch (Exception e) {  my_error = e.getMessage();   }
	 }

    /**
     * Default constructor. 
     */
    public Servlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
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
            
            
            
            if (operacia.equals("logout")) { 
            	urobLogout(out, request,response); 
            	return; 
            	}
            
            if (operacia.equals("pridajText")) {
            	pridajText(out, 
            			request.getParameter("id_user"),
            			request.getParameter("text"),
            			request
            			);
            	
            	
            }
            
            if(operacia.equals("spravovatPristup")) {
            	spravovatPristup(out);
            }
            
            if(operacia.equals("pridajBan")) {
            	pridajBan(out);
            }
            if(operacia.equals("ulozPridanieBanu")) {
            	ulozPridanieBanu(out,request.getParameter("meno"));
            }
            
            if(operacia.equals("odstranBan")) {
            	odstranBan(out,request.getParameter("id"));
            }
            if(operacia.equals("zakrySpravcu")) {
            	OverUsera(out,request);
            }
            
            if(!operacia.equals("spravovatPristup") && !operacia.equals("pridajBan") && !operacia.equals("odstranBan")&& !operacia.equals("ulozPridanieBanu") ) {
                vypisHlavicka(out, request);
                vypisData(out, request);
                }
        } catch (Exception e) {  out.println(e); }

	}
	
	protected void zobrazNeopravnenyPristup(PrintWriter out) {
        out.println("Nie si prihlaseny.");
    }
	//////Prihlasuje sa pod menom
	protected void OverUsera(PrintWriter out, HttpServletRequest request) {
        try {
            String meno = request.getParameter("meno");
            String heslo = request.getParameter("pwd");
            Statement stmt = con.createStatement();
            String sql = "SELECT MAX(id) AS iid, COUNT(id) AS pocet FROM users "+
                    " WHERE meno = '"+meno+"' AND passwd = '"+heslo+"'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            
            HttpSession session = request.getSession();
            if (rs.getInt("pocet") == 1) { 
              sql = "SELECT id, meno FROM users WHERE meno = '"+meno+"'"; 
              rs = stmt.executeQuery(sql); 
              rs.next(); 
              session.setAttribute("ID", rs.getInt("id")); 
              session.setAttribute("meno", rs.getString("meno"));
              System.out.println("Prihlaseny");
            } else { 
              out.println("Prihlasovacie údaje nie sú v poriadku.");
              System.out.println("Neprihlaseny");
              session.invalidate(); 
            }
            rs.close();
            stmt.close();
        }catch (Exception ex) { out.print("Zle meno alebo heslo ");}
	}
	
	   protected int getUserID(HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        Integer id = (Integer)(session.getAttribute("ID"));
	        if (id == null) id = 0;
	        return id;
	    }

	   protected void vypisHlavicka(PrintWriter out, HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        out.println("<h2>"+"Pouzivatel: " + session.getAttribute("meno") + "</h2>" + "<strong>ChatRoom</strong>");
	        out.println("<hr>");
	 }

	   protected void vypisData(PrintWriter out, HttpServletRequest request) {
		   try {
		    Statement stmt = con.createStatement();
		    String sql = "SELECT messages.odoslane as cas, users.meno AS meno_usera, messages.text as text_spravy FROM messages "
		                 +" JOIN users ON messages.id_user = users.id" 
		                 + " WHERE users.ban != 1" ;
		    ResultSet rs = stmt.executeQuery(sql);
		    out.println("<html>");
            out.println("<body>");
            out.println("<table border='1'>");
            out.println("<tr><th>Cas</th><th>Meno</th><th>Text</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" 
            + rs.getString("cas") + "</td><td>" 
            +" <strong>" + rs.getString("meno_usera") + "</strong>"+"</td><td>" 
            + rs.getString("text_spravy") + "</td></tr>");
            }

            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
            out.println("</br>");out.println("</br>");
            
            out.println("Napis nieco...");
            
            out.println("<form method='post' action='Servlet'>");
            out.println(" <textarea name='text'></textarea>");
            out.println("<input type='hidden' name='operacia' value='pridajText'> \n");
            out.println("<input type='submit' value='Odosli'>");
            out.println("</form>");
            
            out.println("</br>");
            
            out.println("<form method='post' action='Servlet'>");
            out.println("<input type='hidden' name='operacia' value='logout'>");
            out.println("<input type='submit' value='Odhlasit sa'>");
            out.println("</form>");
            
            out.println("<form method='post' action='Servlet'>");
            out.println("<input type='hidden' name='operacia' value='spravovatPristup'>");
            out.println("<input type='submit' value='Spravovat pristup'>");
            out.println("</form>");



        } catch (Exception ex) {
            out.println("Zly sql prikaz");
        }
    }

	   protected void urobLogout(PrintWriter out, HttpServletRequest request,ServletResponse response ) throws ServletException, IOException {
	        HttpSession session = request.getSession();
	        session.invalidate();
	        ServletResponse resp = response;
			request.getRequestDispatcher("index.html").forward(request, resp);
			
	    }

	   protected void pridajText(PrintWriter out, String id_user, String text, HttpServletRequest request) {
		    try {
		        LocalDateTime now = LocalDateTime.now();
		        HttpSession session = request.getSession();
		        Statement stmt = con.createStatement();
		        String sql = "INSERT INTO messages (id_user, text, odoslane) VALUES (";
		        sql += "'" + session.getAttribute("ID") +"', ";
		        sql += "'" + text +"', ";
		        sql += "'" + now +"')";
		        
		        int pocet = stmt.executeUpdate(sql);
		        if (pocet > 0 ) {
		        	return;
		        }
		        
		    } catch (SQLException e) {
		        out.println(e.getMessage());
		    }
		}
	   
	   protected void spravovatPristup(PrintWriter out) {
		   try {
			   
	            out.println("<form method='post' action='Servlet'>");
	            out.println("<input type='hidden' name='operacia' value='zakrytSpravovanie'>");
	            out.println("<input type='submit' value='Skryt spravcu'>");
	            out.println("</form>");
			    Statement stmt = con.createStatement();
			    String sql = "SELECT  id, meno FROM users WHERE ban != 0";
			                 
			    ResultSet rs = stmt.executeQuery(sql);
			    
	            out.println("<table border = '1'>");
	            out.println("<tr><th>ID</th><th>Meno</th></tr>");
	            while (rs.next()) {
	                out.println("<tr><td>" + rs.getString("id") + "</td><td>" +" <strong>" + rs.getString("meno") + "</strong>"+"</td>"	);
	                out.println("<td>");
		            out.println("<form action='Servlet' method='post'>");
		            out.println("<input type='hidden' name='id' value='" + rs.getString("id") + "'>");
		            out.println("<input type='hidden' name='operacia' value='odstranBan'>");
		            out.println("<input type='submit' value='Unban'>");
		            out.println("</form>");
		            out.println("</td>");
		            out.println("</tr>");
	                	
	            }
	            out.println("</table>");out.println("</br>");
	            
	            out.println("<form method='post' action='Servlet'>");
	            out.println("<input type='hidden' name='operacia' value='pridajBan'>");
	            out.println("<input type='submit' value='Zabanuj dalsieho uzivatela'>");
	            out.println("</form>");
	            


	        } catch (Exception ex) {
	            out.println("Zly sql prikaz");
	        }

	   }
	   
	   protected void pridajBan(PrintWriter out) {
		   		spravovatPristup(out);
			   	out.println("<form action='Servlet' method='post'>");
			    out.print("<label for='meno'>Meno:</label>");
			    out.println("<input type='text' id='meno' name='meno' required><br>");
			    out.println("<input type='hidden' name='operacia' value='ulozPridanieBanu'>");
			    out.println("<input type='submit' value='Pridaj'>");
			    out.println("</form>");
			    
			   
		   }
	   
	   protected void ulozPridanieBanu(PrintWriter out, String meno) {
		   try {
		        String sql = "UPDATE users SET ban=? WHERE meno=?";
		        PreparedStatement stmt = con.prepareStatement(sql);
		        stmt.setInt(1, 1);
		        stmt.setString(2, meno);
		        int pocet = stmt.executeUpdate();
		        if (pocet > 0) {
		        	spravovatPristup(out);
		        	out.println("<strong>Uspesne zabanovany!</strong>");
		        	
		        }else{
		        	out.println("<strong>Nezabanovany!</strong>");
		        	
		        }
		    } catch (Exception e) {
		        out.println(e);
		    }
	   }
	   
	   protected void odstranBan(PrintWriter out, String id) {
		   try {
		        String sql = "UPDATE users SET ban=? WHERE id=?";
		        PreparedStatement stmt = con.prepareStatement(sql);
		        stmt.setInt(1, 0);
		        stmt.setString(2, id);
		        int pocet = stmt.executeUpdate();
		        if (pocet > 0) {
		        	spravovatPristup(out);
		        	out.println("<strong>Uspesny unban!</strong>");
		        	
		        }else{
		        	out.println("<h2>Neuspesny unban!</h2>");
		        	
		        }
		    } catch (Exception e) {
		        out.println(e);
		    }
	   }
}

