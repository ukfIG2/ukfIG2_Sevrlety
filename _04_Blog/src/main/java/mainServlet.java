import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_03_Blog";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	private Connection con;

    public mainServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
		try {
			super.init();//Treba toto?
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, username, password);
		} catch (Exception e) {
			System.err.println("V init: " + e);
		}
	}
    
	public void destroy() {
		try {con.close();} catch(Exception e) {System.err.println("V destroy: " + e);	
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if(con==null) {
				out.println("Neni pripojena databaza");
				System.out.println("Pripojenie k databaze \" + databaza + \" NIEje.");
				return;
			} else {System.out.println("Pripojenie k databaze " + databaza + " je.");}
			
            String operacia = request.getParameter("operacia");
            
            System.out.println("Operacia je: " + operacia);
            
            if (operacia == null) { zobrazNeopravnenyPristup(out); return; }
            if (operacia.equals("register")) {registerUser(out, request.getParameter("login"), request.getParameter("pwd"), request.getParameter("confirmPwd"), request.getParameter("Nickname"), response, request);}
            if (operacia.equals("login")) { overUsera(out, request); }
            if (operacia.equals("PosT")) {postSomething(out, request);}
            if (operacia.equals("refreshPage"));
            if (operacia.equals("logout")) { urobLogout(out, request, response); return; }
            
            if ("modifyBans".equals(operacia)) {
                int bannerId = getUserID(request);

                Enumeration<String> parameterNames = request.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String paramName = parameterNames.nextElement();
                    if (paramName.startsWith("banStatus_")) {
                        int userId = Integer.parseInt(paramName.substring("banStatus_".length()));
                        String banStatus = request.getParameter(paramName);
                        updateBanStatus(bannerId, userId, "true".equals(banStatus));
                    }
                }
            }
            
            int user_id = getUserID(request);
            if (user_id == 0) { zobrazNeopravnenyPristup(out); return; }
            
            head(out);
            
            	vypisHlavicka(out, request);
                        
            	vypisPosty(out, request);
            
            	displayPostForm(out);
            	
            	showBanTable(out, request);
            
            legs(out);
            
            out.close();
		} catch (Exception e) {
			System.err.println("Servlet doget " + e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		//nepis nemaz
	}

	protected void zobrazNeopravnenyPristup(PrintWriter out) {
		out.println("Neopravneny pristup");
		out.println("<a href='login.html'>Prihlasenie</a>");
	}
	
	protected void registerUser(PrintWriter out, String login, String pwd, String confirmPwdm, String Nickname, HttpServletResponse response, HttpServletRequest request) {
		try {
			//System.out.println(pwd.equals(confirmPwdm));
			if(isValidPassword(pwd) && pwd.equals(confirmPwdm)){
				Statement stmt = con.createStatement();
				pwd=hashPassword(pwd);
				String sql = "INSERT into Users (Email, Passwords, Nickname) VALUES ('" + login + "', '" + pwd + "', '" + Nickname + "')";
				//System.out.println(sql);
				System.out.println(pwd);
				int pocet = stmt.executeUpdate(sql);
				System.out.println("Pridanych bolo: " + pocet + " userov.");
				try {
					response.sendRedirect(request.getContextPath() + "/login.html");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt.close();
			} else {out.println("Password Invalid");}	//keby niekdo iba poslal zle heslo

		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Chyba v register User" + e);
			try {
				response.sendRedirect(request.getContextPath() + "/e-mail.html");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (SQLException e) {
			System.err.println("Chyba v registerUser: " + e);
			e.printStackTrace();
		} 
	}
	
	public String hashPassword(String pswd) {
		String password = pswd;
		try {
            // Create a MessageDigest object
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Update the message digest with the bytes of the password
            md.update(password.getBytes());

            // Get the hash value
            byte[] hashedBytes = md.digest();

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            String hashedPassword = sb.toString();
            //System.out.println("Hashed Password: " + hashedPassword);
            //System.out.println(hashedPassword.length());
            return hashedPassword;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
		return null;
	
	}
	
	public boolean isValidPassword(String password) {
	    // Define the regular expression for the password criteria
	    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!/\\-_*])(.{8,128})$";

	    // Create a Pattern object
	    Pattern pattern = Pattern.compile(regex);

	    // Create a Matcher object
	    Matcher matcher = pattern.matcher(password);

	    // Print the password
	    System.out.println("Password: " + password);

	    // Check if the password matches the criteria
	    if (matcher.matches()) {
	        System.out.println("Password matches criteria: true");
	    } else {
	        // Print messages for each individual criterion that the password fails to meet
	        if (!password.matches(".*[a-z].*")) {
	            System.err.println("Password must contain at least one lowercase letter.");
	        }
	        if (!password.matches(".*[A-Z].*")) {
	            System.err.println("Password must contain at least one uppercase letter.");
	        }
	        if (!password.matches(".*\\d.*")) {
	            System.err.println("Password must contain at least one digit.");
	        }
	        if (!password.matches(".*[@#$%^&+=!/\\-_*].*")) {
	            System.err.println("Password must contain at least one special character.");
	        }
	        if (!(password.length() >= 8 && password.length() <= 128)) {
	            System.err.println("Password must have a length between 8 and 128 characters.");
	        }

	        System.err.println("Password does not match criteria");
	    }

	    // Return true if the password matches the criteria, otherwise false
	    return matcher.matches();
	}

	protected void overUsera(PrintWriter out, HttpServletRequest request) {
		try {
			String meno = request.getParameter("login");
			String heslo = request.getParameter("pwd");
			Statement stmt = con.createStatement();
			String sql = "SELECT MAX(idUsers) AS iid, COUNT(idUsers) AS pocet FROM Users WHERE email='"+meno+"'AND Passwords = '"+ hashPassword(heslo) +"'";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
						
			HttpSession session = request.getSession();
			if(rs.getInt("pocet")==1) {//existuje jeden, takze OK
				sql = "SELECT idUsers, Nickname FROM Users WHERE Email = '"+meno+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				session.setAttribute("ID", rs.getInt("idUsers"));
				session.setAttribute("meno", rs.getString("Nickname"));
			} else {
				out.println("Autorizacia sa nepodarila. Skontroluj prihlasovacie udaje.");
				session.invalidate(); 		//zmazem sedenie
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();  // Add this line to print the full stack trace
			System.out.println("OverUsera " + e);
		}
	}
	
	protected int getUserID(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer id = (Integer) (session.getAttribute("ID"));
		if(id==null) id=0;//radsej -1
		return id;
	}
	
	protected void vypisHlavicka(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		out.println("<h2>"+session.getAttribute("meno") + "</h2>");
		
		out.println("<form method='post' action='mainServlet'>");
		out.println("<input type='hidden' name='operacia' value='logout'>");
		out.println("<input type='submit' value='logout'>");
		out.println("</form>");
		out.println("<hr>");
	}
	
	protected void vypisPosty(PrintWriter out, HttpServletRequest request) {
	    try {
	        Statement stmt = con.createStatement();
	        int userId = getUserID(request);

	        String sql = "SELECT Users.Nickname AS Nickname, Posts.Text AS Text, Posts.Datetime as DateTime " +
	                     "FROM Posts " +
	                     "INNER JOIN Users ON Users.idUsers = Posts.idUsers " +
	                     "WHERE Posts.idUsers NOT IN " +
	                     "(SELECT idBanned FROM Bans WHERE idBanner = ?) " +
	                     "ORDER BY Posts.Datetime ASC";

	        PreparedStatement pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, userId);

	        ResultSet rs = pstmt.executeQuery();

	        out.println("<div class='blog-container'>");
	        while (rs.next()) {
	            out.println("<div class='blog-post'>");
	            out.println("<p> " + rs.getString("DateTime") + " " + rs.getString("Nickname") + " " + rs.getString("Text") + "</p>");
	            out.println("</div>");
	        }
	        out.println("</div>");

	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	protected void head(PrintWriter out) {
		out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Blog Posts</title>");
        out.println("    <link rel='stylesheet' href='style.css'>");
        out.println("    <script src='script.js' defer></script>");
        out.println("</head>");
        out.println("<body>");
	}
	
	protected void legs(PrintWriter out) {
		out.println("<form id='refreshForm' method='post' action='mainServlet'>");
		out.println("<input type='hidden' name='operacia' value='refreshPage'>");
		out.println("</form>");
        out.println("</body>");
        out.println("</html>");
	}
	
	private void displayPostForm(PrintWriter out) {
	    out.println("<form method='post' action='mainServlet'>");
		    out.println("<label for='TexT'>Vloz post:</label>");
		    out.println("<input type='hidden' name='operacia' value='PosT'>");
		    out.println("<textarea id='TexT' name='TexT' rows='4' cols='50'></textarea><br>");
		    out.println("<input type='submit' value='Post'>");
	    out.println("</form>");
	} 
	
	private void postSomething(PrintWriter out, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
	        int userId = (Integer) session.getAttribute("ID"); 
			
	        if (userId != 0) { 
	            String text = request.getParameter("TexT"); 

	            if (text != null && !text.isEmpty()) {
	                Statement stmt = con.createStatement();

	                String sql = "INSERT INTO Posts (idUsers, Text, Datetime) VALUES (" + userId + ", '" + text + "', NOW())";

	                int rowsAffected = stmt.executeUpdate(sql);

	                if (rowsAffected > 0) {
	                    out.println("Post successful");
	                } else {
	                    out.println("Error posting");
	                }

	                stmt.close();
	            } else {
	                out.println("Text cannot be empty");
	            }
	        } else {
	            out.println("User not logged in");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void urobLogout(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		try {
			response.sendRedirect(request.getContextPath() + "/login.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void showBanTable(PrintWriter out, HttpServletRequest request) {
	    try {
	        int bannerId = getUserID(request);

	        if (bannerId != 0) {
	            Statement stmt = con.createStatement();
	            String sql = "SELECT Users.idUsers, Users.Nickname, Bans.idBan " +
	                         "FROM Users " +
	                         "LEFT JOIN Bans ON Users.idUsers = Bans.idBanned AND Bans.idBanner = ? " +
	                         "WHERE Users.idUsers != ?";

	            PreparedStatement pstmt = con.prepareStatement(sql);
	            pstmt.setInt(1, bannerId);
	            pstmt.setInt(2, bannerId);

	            ResultSet rs = pstmt.executeQuery();

	            out.println("<div class='ban-table'>");
	            out.println("<form method='post' action='mainServlet'>");
	            out.println("<input type='hidden' name='operacia' value='modifyBans'>");

	            out.println("<table border='1'>");
	            out.println("<tr><th>User ID</th><th>Nickname</th><th>Banned</th></tr>");

	            while (rs.next()) {
	                int userId = rs.getInt("idUsers");
	                String nickname = rs.getString("Nickname");
	                int banId = rs.getInt("idBan");

	                out.println("<tr>");
	                out.println("<td>" + userId + "</td>");
	                out.println("<td>" + nickname + "</td>");
	                out.println("<td><input type='radio' name='banStatus_" + userId + "' " +
	                             (banId > 0 ? "checked" : "") + " value='true'>Banned " +
	                             "<input type='radio' name='banStatus_" + userId + "' " +
	                             (banId == 0 ? "checked" : "") + " value='false'>Not Banned</td>");
	                out.println("</tr>");
	            }

	            out.println("</table>");
	            out.println("<input type='submit' value='Submit Changes'>");
	            out.println("</form>");
	            out.println("</div>");

	            rs.close();
	            stmt.close();
	        } else {
	            out.println("User not logged in");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void updateBanStatus(int bannerId, int userId, boolean banned) throws SQLException {
	    String selectSql = "SELECT idBan FROM Bans WHERE idBanner = ? AND idBanned = ?";
	    String insertSql = "INSERT INTO Bans (idBanner, idBanned, Date) VALUES (?, ?, NOW())";
	    String updateSql = "UPDATE Bans SET Date = NOW() WHERE idBanner = ? AND idBanned = ?";
	    String deleteSql = "DELETE FROM Bans WHERE idBanner = ? AND idBanned = ?";

	    try (PreparedStatement selectStmt = con.prepareStatement(selectSql)) {
	        selectStmt.setInt(1, bannerId);
	        selectStmt.setInt(2, userId);

	        ResultSet rs = selectStmt.executeQuery();

	        if (rs.next()) {
	            int banId = rs.getInt("idBan");
	            if (banned) {
	                try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
	                    updateStmt.setInt(1, bannerId);
	                    updateStmt.setInt(2, userId);
	                    updateStmt.executeUpdate();
	                }
	            } else {
	                try (PreparedStatement deleteStmt = con.prepareStatement(deleteSql)) {
	                    deleteStmt.setInt(1, bannerId);
	                    deleteStmt.setInt(2, userId);
	                    deleteStmt.executeUpdate();
	                }
	            }
	        } else {
	            if (banned) {
	                try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
	                    insertStmt.setInt(1, bannerId);
	                    insertStmt.setInt(2, userId);
	                    insertStmt.executeUpdate();
	                }
	            }
	        }
	    }
	}

	
	
	
	
}
