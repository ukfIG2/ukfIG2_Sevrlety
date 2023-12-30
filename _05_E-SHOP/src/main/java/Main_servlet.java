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
            
            bottom(out, request);
            //System.out.println(getCenaZaKus(6, 1));
            //System.out.println(ocislujObjednavku(out, request, response));
            
            
           /* if (operacia == null) { zobrazNeopravnenyPristup(out); return; }*/
            if (operacia.equals("register")) {registerUser(out, request.getParameter("name"), request.getParameter("surname"), request.getParameter("Adress"), request.getParameter("login"), request.getParameter("pwd"), request.getParameter("confirmPwd"), response, request);}
            else if (operacia.equals("login")) { overUsera(out, request, response); }
            else if (operacia.equals("pridajDoKosika")) {pridajDoKosika(out, request, response);}
            else if (operacia.equals("updateTovar")) {updateTovar(out, request, response);}
            else if (operacia.equals("urobObjednavku")) {urobObjednavku(out, request, response);}
            else if (operacia.equals("logout")) { urobLogout(out, request, response); return; }
            
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
	
	public void bottom(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		out.println("<footer>");
		if(session.getAttribute("JeAdmin") == null) {}
		else if(session.getAttribute("JeAdmin").equals("0")) {
		out.println("	<a href='Objednavky_servlet'>Préjsť na objednávky</a>");
		} else {}
		out.println("</footer>");

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
    			ukazKosik(out, request, null);
    			
    			
    		}
    		out.println("</div>");
    		out.println("<div class ='Tovary'>");
        	while(rs.next()) {
	        	out.println("	<div class=Tovar>");
	        	out.println("		<img src='" + rs.getString("Fotka") + "' alt='" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + "'>");
	        	out.println("		<p>" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + "</p>");
	        	out.println("		<p> Procesor: " + rs.getString("Procesor") + " Velkosť operačnej pamäte: " + rs.getString("Velkost_operacnej_pamate") + " GB Uhlopriečka: " + rs.getString("Uhlopriecka_displeja") + " palcov </p>");
	        	if(session.getAttribute("ID") == null) {out.println("		<p> Cena tovaru: " + rs.getDouble("Cena") + " EUR </p>");}
	        	else {
	        	out.println("		<p> Cena tovaru: " + getCenaZaKus(Integer.parseInt(rs.getString("idTovaru")), (Integer)session.getAttribute("ID")) + " EUR </p>");
	        	}
	        		if(session.getAttribute("JeAdmin") == null) {}
	        		else if(session.getAttribute("JeAdmin").equals("0")) {
		    	    out.println("    		<div class='button-container'>");
		    	    out.println("        		<form action='Main_servlet'>");
		    	    out.println("					<input type='number' name='pocetKS' value='1' min='1' max='99'>");
			        out.println("                   <input type='hidden' name='idTovaru' value='" + rs.getString("idTovaru") + "'>");
					out.println("					<input type='hidden' name='operacia' value='pridajDoKosika'>");
					out.println("                   <input type='hidden' name='CenaTovaru' value='" + getCenaZaKus(Integer.parseInt(rs.getString("idTovaru")), (Integer)session.getAttribute("ID")) + "'>");
		    	    out.println("            		<button type='submit'>Pridaj do košíka</button>");
		    	   			
		    	    out.println("       		</form>");
		    	    out.println("    		</div>");
		        	}
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
	
	private void pridajDoKosika(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
	    try {
	        HttpSession session = request.getSession();
	        int idUser = (Integer) session.getAttribute("ID");
	        int idTovaru = Integer.parseInt(request.getParameter("idTovaru"));
	        int pocetKS = Integer.parseInt(request.getParameter("pocetKS"));


	        // Check if the record already exists in Kosik for the given user and product
	        String checkSql = "SELECT idKosika, Cena, Pocet_kusov FROM Kosik WHERE ID_Users = ? AND ID_Tovaru = ?";
	        try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
	            checkStmt.setInt(1, idUser);
	            checkStmt.setInt(2, idTovaru);
	            ResultSet resultSet = checkStmt.executeQuery();

	            if (resultSet.next()) {
	                // Record exists, update Pocet_kusov and recalculate Cena
	                int existingPocetKusov = resultSet.getInt("Pocet_kusov");
	                int newPocetKusov = existingPocetKusov + pocetKS;

	                double lastKnownPrice = Double.parseDouble(request.getParameter("CenaTovaru"));
	                double updatedPrice = lastKnownPrice * newPocetKusov;

	                String updateSql = "UPDATE Kosik SET Pocet_kusov = ?, Cena = ? WHERE idKosika = ?";
	                try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
	                    updateStmt.setInt(1, newPocetKusov);
	                    updateStmt.setDouble(2, updatedPrice);
	                    updateStmt.setInt(3, resultSet.getInt("idKosika"));
	                    int rowCount = updateStmt.executeUpdate();
	                    System.out.println("Rows updated: " + rowCount);
	        	        System.out.println("Pocet kusov - " + newPocetKusov);
	        	        System.out.println("updated price - " + updatedPrice);
	        	        System.out.println("last prize - " + lastKnownPrice);
	                    try {
							response.sendRedirect(request.getContextPath() + "/Main_servlet");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                }
	            } else {
	                // Record does not exist, insert a new record
	                // Assuming the initial price is set based on the product's base price
	                double basePrice = Double.parseDouble(request.getParameter("CenaTovaru"));
	                double initialPrice = basePrice * pocetKS;

	                String insertSql = "INSERT INTO Kosik (`ID_Users`, `ID_Tovaru`, Cena, `Pocet_kusov`) VALUES (?, ?, ?, ?)";
	                try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
	                    insertStmt.setInt(1, idUser);
	                    insertStmt.setInt(2, idTovaru);
	                    insertStmt.setDouble(3, initialPrice);
	                    insertStmt.setInt(4, pocetKS);
	                    int rowCount = insertStmt.executeUpdate();
	                    System.out.println("Rows inserted: " + rowCount);
	                    try {
							response.sendRedirect(request.getContextPath() + "/Main_servlet");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                }
	            }
	        } catch (SQLIntegrityConstraintViolationException e) {
	            System.out.println("Chyba Kosiku: " + e.getMessage());
	            // Handle or log the exception
	        }
	    } catch (NumberFormatException e) {
	        System.err.println("NumberFormatException: " + e.getMessage());
	        // Handle or log the exception
	    } catch (SQLException e) {
	        System.err.println("Error in pridajDoKosika: " + e);
	        e.printStackTrace();
	    }
	}


	private void ukazKosik(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			Statement stmt = con.createStatement();
	        String sql = "SELECT K.ID_Users AS UserK, K.ID_Tovaru AS Tovar, K.Cena AS CenaSpolu, T.Znacka, T.Modelova_rada, T.Nazov, K.Pocet_kusov, Fotka, idKosika, T.Cena AS CenaZK FROM Kosik K INNER JOIN Tovar T ON K.ID_Tovaru = T.idTovaru WHERE K.ID_Users =" + session.getAttribute("ID");
	        ResultSet rs = stmt.executeQuery(sql);
	        int IDUSER = 0;
        	
        	while(rs.next()) {
        		out.println("	<div class=Tovar_V_Kosiku>");
        		out.println("		<img src='" + rs.getString("Fotka") + "' alt='" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + "'>");
	        	out.println("		<p>" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + "</p>");
	    	    out.println("        		<form action='Main_servlet'>");
	        	out.println("		<p>Počet kusov: <input type='number' name='pocetKS' value='" + rs.getString("Pocet_kusov") + "' min='1' max='99'>" + " za cenu jedného kusu " + getCenaZaKus(Integer.parseInt(rs.getString("Tovar")), (Integer)session.getAttribute("ID")) + " je dokopy " + rs.getString("CenaSpolu") + " EUR.</p>");
				out.println("                   <input type='hidden' name='idKosika' value='" + rs.getString("idKosika") + "'>");
				out.println("					<input type='hidden' name='operacia' value='updateTovar'>");
	    	    out.println("            		<button type='submit'>Prepočítaj Tovar</button>");
	        	out.println("       		</form>");
        		out.println("	</div>");
        		IDUSER = rs.getInt("UserK");
	        }
        	out.println("<br><br>");
    		out.println("	<div class='button-container'>");
    		out.println("        <form action='Main_servlet'>");
    		out.println("		 	<input type='hidden' name='operacia' value='urobObjednavku'>");
    		out.println("        	<input type='hidden' name='idUser' value='" + IDUSER + "'>");
    		out.println("        	<button type='submit'>Urob objednávku</button>");
    		out.println("        </form>");
    		out.println("	</div>");
		
		rs.close();
		stmt.close();
		
		} catch (Exception e) {
			System.out.println("V doGet 01: " + e);
			}
	}
	
	// Update the existing updateTovar method
	private void updateTovar(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
	    try {
	        HttpSession session = request.getSession();
	        int idUser = (Integer) session.getAttribute("ID");
	        int idKosika = Integer.parseInt(request.getParameter("idKosika"));
	        int newPocetKusov = Integer.parseInt(request.getParameter("pocetKS"));

	        // Get the last known price from the Tovar table
	        String lastKnownPriceSql = "SELECT * FROM Tovar T INNER JOIN Kosik K ON K.ID_Tovaru = T.idTovaru /*WHERE K.idKosika;;*/ INNER JOIN Users U ON K.ID_Users=U.idUsers WHERE K.idKosika = ?";
	        try (PreparedStatement lastKnownPriceStmt = con.prepareStatement(lastKnownPriceSql)) {
	            lastKnownPriceStmt.setInt(1, idKosika);
	            ResultSet lastKnownPriceRs = lastKnownPriceStmt.executeQuery();

	            if (lastKnownPriceRs.next()) {
	                //double lastKnownPrice = lastKnownPriceRs.getDouble("CenaZK");
	            	double lastKnownPrice = getCenaZaKus(Integer.parseInt(lastKnownPriceRs.getString("idTovaru")), (Integer)session.getAttribute("ID"));
	            	
	                // Recalculate the total price
	                double updatedPrice = lastKnownPrice * newPocetKusov;

	                // Update the item in the shopping cart
	                String updateSql = "UPDATE Kosik SET Pocet_kusov = ?, Cena = ? WHERE idKosika = ?";
	                try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
	                    updateStmt.setInt(1, newPocetKusov);
	                    updateStmt.setDouble(2, updatedPrice);
	                    updateStmt.setInt(3, idKosika);
	                    int rowCount = updateStmt.executeUpdate();
	                    System.out.println("Rows updated: " + rowCount);
	                }
	            }

	            lastKnownPriceRs.close();
	        }

	        // Redirect back to the shopping cart page
	        response.sendRedirect(request.getContextPath() + "/Main_servlet");
	    } catch (NumberFormatException | SQLException | IOException e) {
	        System.err.println("Error in updateTovar: " + e);
	        e.printStackTrace();
	    }
	}

	private double getCenaZaKus(int idTovaru, int idUsers) {
	    double cenaZaKus = 0.0;
	    double zlava = 0.0;
	    double finalnaCena = 0.0;

	    try {
	        String sql = "SELECT Cena, Zlava FROM Tovar INNER JOIN Users WHERE idTovaru = ? AND idUsers = ?";
	        try (PreparedStatement stmt = con.prepareStatement(sql)) {
	            stmt.setInt(1, idTovaru);
	            stmt.setInt(2, idUsers);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                cenaZaKus = rs.getDouble("Cena");
	                zlava = rs.getDouble("Zlava");
	            }
	            
	            finalnaCena = Math.round((cenaZaKus / 100) * (100 - zlava) * 100) / 100.0; // Round and divide by 100.0
	            rs.close();
	        }
	    } catch (SQLException e) {
	        System.err.println("Error in getCenaZaKus: " + e);
	        e.printStackTrace();
	    }
	    return finalnaCena;
	}

	private void urobObjednavku(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
	synchronized (this) {
		if (dostatokTovaru(out, request, response)) {
			String CisloObj = ocislujObjednavku(out, request, response);
			zapisObjednavky(CisloObj, request);
		}
	}	
	}
	
	private boolean dostatokTovaru(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		boolean vysledok = true;
		try {
		HttpSession session = request.getSession();
		Statement stmt = con.createStatement();
        String sql = "SELECT Znacka, Modelova_rada, Nazov, K.idKosika, T.idTovaru, K.Pocet_kusov AS KusovVKosiku, T.Pocet_kusov AS KusovNaSklade FROM Kosik K INNER JOIN Tovar T ON K.ID_Tovaru=T.idTovaru WHERE ID_Users =" + session.getAttribute("ID");
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
        	if (rs.getInt("KusovVKosiku") > rs.getInt("KusovNaSklade")) {
        		vysledok = false;
        		//out.println("<p>" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + " máme nasklade iba " + rs.getString("KusovNaSklade") + ", vy chcete " + rs.getString("KusovVKosiku") + ".</p>");
        		out.println("<script>alert('" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + " máme nasklade iba " + rs.getString("KusovNaSklade") + ", vy chcete " + rs.getString("KusovVKosiku") + "');</script>"); 
        	}
        	else {}
        }
        
        rs.close();
		stmt.close();
		} catch (Exception e) {
			System.out.println("V doGet 01: " + e);
			}
        return vysledok;
	}
	
	private String ocislujObjednavku(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
	    String cislo_Objednavky = "0";
	    
	    try {
	        HttpSession session = request.getSession();
	        Statement stmt = con.createStatement();
	        String sql = "SELECT YEAR(CURDATE()) AS current_year, MONTH(CURDATE()) AS current_month, DAY(CURDATE()) AS current_day, (SELECT COUNT(Cislo_objednavky) FROM Zoznam_objednavok WHERE DATE(Datum_objednavky) = CURDATE()) AS order_count";
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        if (rs.next()) { // Move to the first row
	            cislo_Objednavky = rs.getString("current_year") + "/" +
	                              rs.getString("current_month") + "/" +
	                              rs.getString("current_day") + "/" +
	                              String.valueOf(rs.getInt("order_count") + 1);
	        }
	        
	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        System.err.println("V ocisluj_Objednavku: " + e);
	    }
	    
	    return cislo_Objednavky;
	}

	/*private void zapisObjednavky(String CisloObjednavky, HttpServletRequest request) {
	    double Suma_dokopy = 0;
	    try {
	        HttpSession session = request.getSession();
	        Statement stmt = con.createStatement();
	        String sql = "SELECT Znacka, Modelova_rada, Nazov, K.idKosika, T.idTovaru, K.Pocet_kusov AS KusovVKosiku, T.Pocet_kusov AS KusovNaSklade FROM Kosik K INNER JOIN Tovar T ON K.ID_Tovaru=T.idTovaru WHERE ID_Users =" + session.getAttribute("ID") + "; ";

	        ResultSet rs = stmt.executeQuery(sql);

	        while (rs.next()) {
	            try {
	                String updateSql = "UPDATE Tovar SET Pocet_kusov = ? WHERE idTovaru = ?";
	                try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
	                    updateStmt.setInt(1, rs.getInt("KusovNaSklade") - rs.getInt("KusovVKosiku"));
	                    updateStmt.setInt(2, rs.getInt("idTovaru"));
	                    int rowCount = updateStmt.executeUpdate();
	                    System.out.println("Rows updated in UpdateTovar_pocetKuov nasklade: " + rowCount);
	                }
	            } catch (Exception e) {
	                System.err.println("V Zmen mnozstvo tovaru na sklade: " + e);
	            }

	            try {
	                String updateSql = "DELETE FROM Kosik WHERE idKosika = ?";
	                try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
	                    updateStmt.setInt(1, rs.getInt("idKosika"));
	                    int rowCount = updateStmt.executeUpdate();
	                    System.out.println("Rows updated in: Delete From kosik " + rowCount);
	                }
	            } catch (Exception e) {
	                System.err.println("Vo vymazani kosika: " + e);
	            }

	            try {
	                String insertSQL = "INSERT INTO Polozky_objednavky (`Cena_za_kus`, `Pocet_kusov`, `Cislo_objednavky`, idTovaru) VALUES (?, ?, ?, ?)";
	                try (PreparedStatement insertStmt = con.prepareStatement(insertSQL)) {
	                    insertStmt.setDouble(1, getCenaZaKus(rs.getInt("idTovaru"), (Integer) session.getAttribute("ID")));

	                    Suma_dokopy += getCenaZaKus(rs.getInt("idTovaru"), (Integer) session.getAttribute("ID"));

	                    insertStmt.setInt(2, rs.getInt("KusovVKosiku"));
	                    insertStmt.setString(3, CisloObjednavky);
	                    insertStmt.setInt(4, rs.getInt("idTovaru"));
	                    int rowCount = insertStmt.executeUpdate();
	                    System.out.println("Rows inserted: insert into zoznam objednavok" + rowCount);
	                }
	            } catch (Exception e) {
	                System.err.println("Vo vytvoreni polozky pre objednavku: " + e);
	            }
	        } // WHILE in ROWS

	        // Close ResultSet and Statement after the loop
	        rs.close();
	        stmt.close();

	        // Create the order after closing the ResultSet and Statement
	        String insertSql = "INSERT INTO Zoznam_objednavok (`Cislo_objednavky`, `idUsers`, `Datum_objednavky`, suma, `Stav_objednavky`) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)";
	        try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
	            insertStmt.setString(1, CisloObjednavky);
	            insertStmt.setInt(2, (Integer) session.getAttribute("ID"));
	            insertStmt.setDouble(3, Suma_dokopy);
	            insertStmt.setString(4, "Spracuváva SA");
	            int rowCount = insertStmt.executeUpdate();
	            System.out.println("Rows inserted: zoznam objednavok " + rowCount);
	        } catch (Exception e) {
	            System.err.println("Vo vytvorit objednavku " + e);
	        }
	    } catch (Exception e) {
	        System.err.println("V Zapis objednavku: " + e);
	    }
	}*/

	/*private void zapisObjednavky(String CisloObjednavky, HttpServletRequest request) {
	    double Suma_dokopy = 0;
	    try {
	        HttpSession session = request.getSession();
	        String insertSqlZoznam = "INSERT INTO Zoznam_objednavok (`Cislo_objednavky`, `idUsers`, `Datum_objednavky`, suma, `Stav_objednavky`) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)";

	        try (PreparedStatement insertStmtZoznam = con.prepareStatement(insertSqlZoznam)) {
	            Statement stmt = con.createStatement();
	            String sql = "SELECT Znacka, Modelova_rada, Nazov, K.idKosika, T.idTovaru, K.Pocet_kusov AS KusovVKosiku, T.Pocet_kusov AS KusovNaSklade FROM Kosik K INNER JOIN Tovar T ON K.ID_Tovaru=T.idTovaru WHERE ID_Users =" + session.getAttribute("ID") + "; ";

	            ResultSet rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                try {
	                    String updateSql = "UPDATE Tovar SET Pocet_kusov = ? WHERE idTovaru = ?";
	                    try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
	                        updateStmt.setInt(1, rs.getInt("KusovNaSklade") - rs.getInt("KusovVKosiku"));
	                        updateStmt.setInt(2, rs.getInt("idTovaru"));
	                        int rowCount = updateStmt.executeUpdate();
	                        System.out.println("Rows updated in UpdateTovar_pocetKuov nasklade: " + rowCount);
	                    }
	                } catch (Exception e) {
	                    System.err.println("V Zmen mnozstvo tovaru na sklade: " + e);
	                }

	                try {
	                    String updateSql = "DELETE FROM Kosik WHERE idKosika = ?";
	                    try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
	                        updateStmt.setInt(1, rs.getInt("idKosika"));
	                        int rowCount = updateStmt.executeUpdate();
	                        System.out.println("Rows updated in: Delete From kosik " + rowCount);
	                    }
	                } catch (Exception e) {
	                    System.err.println("Vo vymazani kosika: " + e);
	                }

	                // Calculate Suma_dokopy
	                Suma_dokopy += getCenaZaKus(rs.getInt("idTovaru"), (Integer) session.getAttribute("ID"));
	            } // WHILE in ROWS

	            // Close ResultSet and Statement after the loop
	            rs.close();
	            
	            // Create the order after closing the ResultSet and Statement
	            insertStmtZoznam.setString(1, CisloObjednavky);
	            insertStmtZoznam.setInt(2, (Integer) session.getAttribute("ID"));
	            insertStmtZoznam.setDouble(3, Suma_dokopy);
	            insertStmtZoznam.setString(4, "Spracuváva SA");
	            int rowCountZoznam = insertStmtZoznam.executeUpdate();
	            System.out.println("Rows inserted into Zoznam_objednavok: " + rowCountZoznam);

	            // Insert into Polozky_objednavky
	            String insertSqlPolozky = "INSERT INTO Polozky_objednavky (`Cena_za_kus`, `Pocet_kusov`, `Cislo_objednavky`, idTovaru) VALUES (?, ?, ?, ?)";
	            try (PreparedStatement insertStmtPolozky = con.prepareStatement(insertSqlPolozky)) {
	                rs = stmt.executeQuery(sql);

	                while (rs.next()) {
	                    insertStmtPolozky.setDouble(1, getCenaZaKus(rs.getInt("idTovaru"), (Integer) session.getAttribute("ID")));
	                    insertStmtPolozky.setInt(2, rs.getInt("KusovVKosiku"));
	                    insertStmtPolozky.setString(3, CisloObjednavky);
	                    insertStmtPolozky.setInt(4, rs.getInt("idTovaru"));
	                    int rowCountPolozky = insertStmtPolozky.executeUpdate();
	                    System.out.println("Rows inserted into Polozky_objednavky: " + rowCountPolozky);
	                }
	            }

	            // Close Statement after the loop
	            stmt.close();
	        }
	    } catch (Exception e) {
	        System.err.println("V Zapis objednavku: " + e);
	    }
	}*/

	private void zapisObjednavky(String CisloObjednavky, HttpServletRequest request) {
	    double Suma_dokopy = 0;
	    try {
	        HttpSession session = request.getSession();
	        String insertSqlZoznam = "INSERT INTO Zoznam_objednavok (`Cislo_objednavky`, `idUsers`, `Datum_objednavky`, `suma`, `Stav_objednavky`) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)";

	        try (PreparedStatement insertStmtZoznam = con.prepareStatement(insertSqlZoznam)) {
	            // Create the order
	            insertStmtZoznam.setString(1, CisloObjednavky);
	            insertStmtZoznam.setInt(2, (Integer) session.getAttribute("ID"));
	            insertStmtZoznam.setDouble(3, Suma_dokopy);
	            insertStmtZoznam.setString(4, "Spracuváva SA");
	            int rowCountZoznam = insertStmtZoznam.executeUpdate();
	            System.out.println("Rows inserted into Zoznam_objednavok: " + rowCountZoznam);

	            // Insert into Polozky_objednavky
	            String insertSqlPolozky = "INSERT INTO Polozky_objednavky (`Cena_za_kus`, `Pocet_kusov`, `Cislo_objednavky`, `idTovaru`) VALUES (?, ?, ?, ?)";
	            try (PreparedStatement insertStmtPolozky = con.prepareStatement(insertSqlPolozky)) {
	                Statement stmt = con.createStatement();
	                String sql = "SELECT Znacka, Modelova_rada, Nazov, K.idKosika, T.idTovaru, K.Pocet_kusov AS KusovVKosiku, T.Pocet_kusov AS KusovNaSklade FROM Kosik K INNER JOIN Tovar T ON K.ID_Tovaru=T.idTovaru WHERE ID_Users =" + session.getAttribute("ID") + "; ";
	                ResultSet rs = stmt.executeQuery(sql);

	                while (rs.next()) {
	                    // Insert into Polozky_objednavky
	                	insertStmtPolozky.setDouble(1, Math.round(getCenaZaKus(rs.getInt("idTovaru"), (Integer) session.getAttribute("ID")) * rs.getDouble("KusovVKosiku") * 100.0) / 100.0);

	                    insertStmtPolozky.setInt(2, rs.getInt("KusovVKosiku"));
	                    insertStmtPolozky.setString(3, CisloObjednavky);
	                    insertStmtPolozky.setInt(4, rs.getInt("idTovaru"));
	                    int rowCountPolozky = insertStmtPolozky.executeUpdate();
	                    System.out.println("Rows inserted into Polozky_objednavky: " + rowCountPolozky);

	                    // Calculate Suma_dokopy
	                    Suma_dokopy += Math.round(getCenaZaKus(rs.getInt("idTovaru"), (Integer) session.getAttribute("ID")) * rs.getDouble("KusovVKosiku") * 100.0) / 100.0;

	                    // Update Tovar
	                    String updateSqlTovar = "UPDATE Tovar SET Pocet_kusov = ? WHERE idTovaru = ?";
	                    try (PreparedStatement updateStmtTovar = con.prepareStatement(updateSqlTovar)) {
	                        updateStmtTovar.setInt(1, rs.getInt("KusovNaSklade") - rs.getInt("KusovVKosiku"));
	                        updateStmtTovar.setInt(2, rs.getInt("idTovaru"));
	                        int rowCountTovar = updateStmtTovar.executeUpdate();
	                        System.out.println("Rows updated in UpdateTovar_pocetKuov nasklade: " + rowCountTovar);
	                    }

	                    // Delete from Kosik
	                    String deleteSqlKosik = "DELETE FROM Kosik WHERE idKosika = ?";
	                    try (PreparedStatement deleteStmtKosik = con.prepareStatement(deleteSqlKosik)) {
	                        deleteStmtKosik.setInt(1, rs.getInt("idKosika"));
	                        int rowCountKosik = deleteStmtKosik.executeUpdate();
	                        System.out.println("Rows updated in: Delete From kosik " + rowCountKosik);
	                    }
	                } // WHILE in ROWS

	                // Close ResultSet and Statement after the loop
	                rs.close();

	                // Update Zoznam_objednavok with Suma_dokopy
	                String updateSqlZoznamSuma = "UPDATE Zoznam_objednavok SET suma = ? WHERE Cislo_objednavky = ?";
	                try (PreparedStatement updateStmtZoznamSuma = con.prepareStatement(updateSqlZoznamSuma)) {
	                    updateStmtZoznamSuma.setDouble(1, Suma_dokopy);
	                    updateStmtZoznamSuma.setString(2, CisloObjednavky);
	                    int rowCountZoznamSuma = updateStmtZoznamSuma.executeUpdate();
	                    System.out.println("Rows updated in Update Zoznam_objednavok with Suma_dokopy: " + rowCountZoznamSuma);
	                }
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("V Zapis objednavku: " + e);
	    }
	}


	
	
}
