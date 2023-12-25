import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            
            header(out);
            
            bottom(out);
            
           /* if (operacia == null) { zobrazNeopravnenyPristup(out); return; }*/
            if (operacia.equals("register")) {registerUser(out, request.getParameter("name"), request.getParameter("surname"), request.getParameter("Adress"), request.getParameter("login"), request.getParameter("pwd"), request.getParameter("confirmPwd"), response, request);}
           // if (operacia.equals("login")) { overUsera(out, request); }
            /*if (operacia.equals("PosT")) {postSomething(out, request);}
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

	protected void head(PrintWriter out) {
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
	
	protected void bottom(PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
	}
	
	protected void header(PrintWriter out) {
		out.println("<header>");
		out.println("	<div class='pozdrav'>");
		out.println("		<h1>Ahoj " + pozicia + "</h1>");
		out.println("	</div>");

	    out.println("    <div class='button-container'>");
	    out.println("        <form action='login.html'>");
	    out.println("            <button type='submit'>Prihlás sa</button>");
	    out.println("        </form>");
	    out.println("    </div>");
	    
	    out.println("    <div class='button-container'>");
	    out.println("        <form action='register.html'>");
	    out.println("            <button type='submit'>Registruj sa </button>");
	    out.println("        </form>");
	    out.println("    </div>");
		;
		out.println("</header>");
	}
																					 
	protected void registerUser(PrintWriter out, String name, String surname, String Adress, String login, String pwd, String confirmPwd, HttpServletResponse response, HttpServletRequest request) {
	    try {
	        if (isValidPassword(pwd) && pwd.equals(confirmPwd)) {
	            pwd = hashPassword(pwd);

	            String sql = "INSERT INTO Users (`Meno`, `Priezvisko`, `E-mail`, `Adresa`, `Password`) VALUES (?, ?, ?, ?, ?)";
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
	
	
	
	
	
	
	
	
	
	
	
}
