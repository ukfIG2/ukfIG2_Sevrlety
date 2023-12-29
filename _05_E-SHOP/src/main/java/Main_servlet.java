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

public class Main_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_04_E-SHOP";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	private Connection con;
	
	private String pozicia = "Neprihláseny zákaznik";
       
    public Main_servlet() {
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
				out.println("Neni pripojena databaza.");
				System.err.println("Pripojenie k databaze " + databaza + " NIE-je.");
				return;
			} else {System.out.println("Pripojenie k databaze " + databaza + " je.");}
			
            String operacia = request.getParameter("operacia");
            
            System.out.println("Operacia je: " + operacia);
            
            head(out);
            
            header(out, request);
            
            main(out, request);
            
            bottom(out);
            
            
           /* if (operacia == null) { zobrazNeopravnenyPristup(out); return; }*/
            if (operacia.equals("register")) {registerUser(out, request.getParameter("name"), request.getParameter("surname"), request.getParameter("Adress"), request.getParameter("login"), request.getParameter("pwd"), request.getParameter("confirmPwd"), response, request);}
            if (operacia.equals("login")) { overUsera(out, request, response); }
            /*if (operacia.equals("PosT")) {postSomething(out, request);}
            if (operacia.equals("refreshPage"));*/
            if (operacia.equals("logout")) { urobLogout(out, request, response); return; }
            /*
             
            int user_id = getUserID(request);
            if (user_id == 0) { zobrazNeopravnenyPristup(out); return; }
            
            head(out);
            
            	vypisHlavicka(out, request);
                        
            	vypisPosty(out, request);
            
            	displayPostForm(out);
            	
            	showBanTable(out, request);
            
            legs(out);*/
            
            out.close();
		} catch (Exception e) {
			System.err.println("Servlet doGet " + e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//nepis nemaz													
		doGet(request, response);
	}

	public void head(PrintWriter out) {
		out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>E-shop NOOTEBUKY</title>");
        out.println("    <link rel='stylesheet' href='style.css'>");
        out.println("    <script src='script.js' defer></script>");
        out.println("</head>");
        out.println("<body>");
	}
	
	public void bottom(PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
	}
	
	public void header(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("Prihlaseny pouzivatel, je " + session.getAttribute("meno"));
		out.println("<header>");
		out.println("	<div class='pozdrav'>");
		if(session.getAttribute("meno") == null) {
			out.println("		<h1>Ahoj " + pozicia + "</h1>");
		} else {
		out.println("		<h1>Ahoj " + pozicia + " " + session.getAttribute("meno") + "</h1>");
		}
		out.println("	</div>");

		if(pozicia.equals("Neprihláseny zákaznik")) {
	    out.println("    <div class='button-container'>");
	    out.println("        <form action='login.html'>");
	    out.println("            <button type='submit'>Prihlás sa</button>");
	    out.println("        </form>");
	    out.println("    </div>");
		} else if(pozicia.equals("Prihlaseny používateľ")) {
		    out.println("    <div class='button-container'>");
		    out.println("		<form method='post' action='Main_servlet'>");
			out.println("			<input type='hidden' name='operacia' value='logout'>");
			out.println("			<input type='submit' value='logout'>");
		    out.println("        </form>");
		    out.println("    </div>");
		}
	    
		if(session.getAttribute("JeAdmin") == null) {
	    out.println("    <div class='button-container'>");
	    out.println("        <form action='register.html'>");
	    out.println("            <button type='submit'>Registruj sa </button>");
	    out.println("        </form>");
	    out.println("    </div>");
		} else {}
		if (session.getAttribute("JeAdmin") != null && session.getAttribute("JeAdmin").equals("1")) {
		    out.println("    <div class='button-container'>");
		    out.println("        <form action='Admin_servlet'>");
		    out.println("            <button type='submit'>Dig Administrátor </button>");
		    out.println("        </form>");
		    out.println("    </div>");
		} else {}
		out.println("</header>");
	}
																					 
	protected void registerUser(PrintWriter out, String name, String surname, String Adress, String login, String pwd, String confirmPwd, HttpServletResponse response, HttpServletRequest request) {
	    try {
	        if (isValidPassword(pwd) && pwd.equals(confirmPwd)) {
	            pwd = hashPassword(pwd);

	            String sql = "INSERT INTO Users (`Meno`, `Priezvisko`, E_mail, `Adresa`, `Password`) VALUES (?, ?, ?, ?, ?)";
	            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
	                pstmt.setString(1, name);
	                pstmt.setString(2, surname);
	                pstmt.setString(3, login); 
	                pstmt.setString(4, Adress); 
	                pstmt.setString(5, pwd);

	                int rowCount = pstmt.executeUpdate();
	                System.out.println("Rows affected: " + rowCount);

	            } catch (SQLIntegrityConstraintViolationException e) {
	            	System.out.println("Chyba v register User, rovnaky email." + e);
	    			try {
	    				response.sendRedirect(request.getContextPath() + "/e-mail.html");
	    			} catch (IOException e1) {
	    				e1.printStackTrace();
	    			}				
	    			}
	        } else {
	            out.println("Invalid Password");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error in registerUser: " + e);
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
	
	protected void overUsera(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		try {
			String meno = request.getParameter("login");
			String heslo = request.getParameter("pwd");
			Statement stmt = con.createStatement();
			String sql = "SELECT MAX(idUsers) AS iid, COUNT(idUsers) AS pocet FROM Users WHERE E_mail='"+meno+"'AND Password = '"+ hashPassword(heslo) +"'";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
						
			HttpSession session = request.getSession();
			if(rs.getInt("pocet")==1) {//existuje jeden, takze OK
				sql = "SELECT idUsers, Meno, Admin FROM Users WHERE E_mail = '"+meno+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				session.setAttribute("ID", rs.getInt("idUsers"));
				session.setAttribute("meno", rs.getString("Meno"));
				session.setAttribute("JeAdmin", rs.getString("Admin"));
				System.out.println("IduseraJe " + session.getAttribute("ID"));
				System.out.println("MenoUseraJe " + session.getAttribute("meno"));
				System.out.println("JeUserAdmin? " + session.getAttribute("JeAdmin"));
				pozicia = "Prihlaseny používateľ";
				
				response.sendRedirect(request.getContextPath() + "/Main_servlet");
				
			} else {
				out.println("Autorizacia sa nepodarila. Skontroluj prihlasovacie udaje.");
				session.invalidate(); 		//zmazem sedenie
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("OverUsera " + e);
		}
	}
	
	protected void urobLogout(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			response.sendRedirect(request.getContextPath() + "/Main_servlet");
	
			session.invalidate();
			pozicia = "Neprihláseny zákaznik";
			System.out.println("IduseraJe " + session.getAttribute("ID"));
			System.out.println("MenoUseraJe " + session.getAttribute("meno"));
			System.out.println("JeUserAdmin? " + session.getAttribute("JeAdmin"));
			//response.sendRedirect(request.getContextPath() + "/Main_servlet");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IduseraJe " + session.getAttribute("ID"));
			System.out.println("MenoUseraJe " + session.getAttribute("meno"));
			System.out.println("JeUserAdmin? " + session.getAttribute("JeAdmin"));
		}
	}
	
	private void main(PrintWriter out, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM Tovar";
	        ResultSet rs = stmt.executeQuery(sql);
        	out.println("<main>");
    		out.println("<div class ='Kosik'>");
    		out.println("<h2>Košík:</h2>");
    		if(session.getAttribute("JeAdmin") == null) {
            	out.println("<h3>Ak chceš pridávať veci do košíka, PRIHLÁS SA, ak si neni PRIHLÁSENÝ, REGISTRUJ SA.</h3>");
    		}
    		else {
    			
    			
    			
    		}
    		out.println("</div>");
    		out.println("<div class ='Tovary'>");
        	while(rs.next()) {
	        	out.println("	<div class=Tovar>");
	        	out.println("		<img src='" + rs.getString("Fotka") + "' alt='" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + "'>");
	        	out.println("		<p>" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + "</p>");
	        	out.println("		<p> Procesor: " + rs.getString("Procesor") + " Velkosť operačnej pamäte: " + rs.getString("Velkost_operacnej_pamate") + " GB Uhlopriečka: " + rs.getString("Uhlopriecka_displeja") + " palcov </p>");
	        	out.println("		<p> Cena tovaru: " + rs.getString("Cena") + " EUR </p>");
	        	out.println("	</div>");
	        }
    		out.println("</div>");
	        out.println("</main>");
		
		rs.close();
		stmt.close();
		
		} catch (Exception e) {
			System.out.println("V doGet 01: " + e);
			}
		
	}
	
	
	
	
	
	
}
