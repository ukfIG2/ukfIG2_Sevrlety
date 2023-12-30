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

public class Objednavky_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_04_E-SHOP";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	private Connection con;
	
	private String pozicia = "Neprihláseny zákaznik";
       
    public Objednavky_servlet() {
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
			out.println("		<h1>Ahoj " + "</h1>");
		} else {
		out.println("		<h1>Ahoj " + " " + session.getAttribute("meno") + "</h1>");
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
																					 	
	private void main(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		try {
			Statement stmt = con.createStatement();
	        String sql = "SELECT Z.idUsers, Z.Cislo_objednavky, U.Meno, U.Priezvisko, Z.Datum_objednavky, Z.suma, Z.Stav_objednavky, U.Zlava \n"
	        		+ "FROM `Zoznam_objednavok` Z \n"
	        		+ "INNER JOIN Users U ON Z.idUsers = U.idUsers\n"
	        		+ "WHERE Z.idUsers = " + session.getAttribute("ID");
	        ResultSet rs = stmt.executeQuery(sql);
		out.println("<div class='main'>");
		out.println("	<div class='Tabulka'>");
		out.println("		<h2>Tabulka objednavok</h2>");
		out.println("		<table>");
		out.println("			<tr>");
		out.println("				<th>" + "Cislo_objednavky" + "</th>");
		out.println("				<th>" + "Meno" + "</th>");
		out.println("				<th>" + "Priezvisko" + "</th>");
		out.println("				<th>" + "Datum_objednavky" + "</th>");
		out.println("				<th>" + "suma" + "</th>");
		out.println("				<th>" + "Stav_objednavky?" + "</th>");
		out.println("				<th>" + "Má zľavu?" + "</th>");
		out.println("			</tr>");
		while (rs.next()) {
			out.println("			<tr>");
			out.println("				<th>" + rs.getString("Cislo_objednavky") + "</th>");
			out.println("				<th>" + rs.getString("Meno") + "</th>");
			out.println("				<th>" + rs.getString("Priezvisko") + "</th>");
			out.println("				<th>" + rs.getString("Datum_objednavky") + "</th>");
			out.println("				<th>" + rs.getString("suma") + "</th>");
			out.println("				<th>" + rs.getString("Stav_objednavky") + "</th>");
			out.println("				<th>" + rs.getDouble("Zlava") + "</th>");

			out.println("			</tr>");
		}
		out.println("		</table>");
		out.println("	</div>");
		//out.println("</main>");
		rs.close();
		stmt.close();
		} catch (Exception e) {
			System.out.println("V doGet 01: " + e);
			}
		
		try {
			Statement stmt = con.createStatement();
	        String sql = "SELECT Z.idUsers, T.idTovaru AS IDTOVAU, Z.Cislo_objednavky as Cislo_objednavky, T.Znacka AS Znacka, T.Modelova_rada AS Modelova_rada, T.Nazov AS Nazov, P.Pocet_kusov AS Pocet_tovarov, T.Cena AS CenaZaKus FROM `Zoznam_objednavok` Z INNER JOIN Polozky_objednavky P ON P.Cislo_objednavky=Z.Cislo_objednavky INNER JOIN Tovar T ON T.idTovaru=P.idTovaru WHERE idUsers=" + session.getAttribute("ID") + " ORDER BY Cislo_objednavky";
	        ResultSet rs = stmt.executeQuery(sql);
		out.println("<div class='main'>");
		out.println("	<div class='Tabulka'>");
		out.println("		<h2>Tabulka objednavok</h2>");
		out.println("		<table>");
		out.println("			<tr>");
		out.println("				<th>" + "Cislo_objednavky" + "</th>");
		out.println("				<th>" + "Názov tovaru" + "</th>");
		out.println("				<th>" + "Cena za kus" + "</th>");
		out.println("				<th>" + "Pošet kusov tovaru" + "</th>");
		out.println("			</tr>");
		while (rs.next()) {
			out.println("			<tr>");
			out.println("				<th>" + rs.getString("Cislo_objednavky") + "</th>");
			out.println("				<th>" + rs.getString("Znacka") + " " + rs.getString("Modelova_rada") + " " + rs.getString("Nazov") + "</th>");

			out.println("				<th>" + (Math.round(getCenaZaKus(rs.getInt("IDTOVAU"), (Integer) session.getAttribute("ID"))  * 100.0) /100));
			out.println("				<th>" + rs.getString("Pocet_tovarov") + "</th>");
			
			out.println("			</tr>");
		}
		out.println("		</table>");
		out.println("	</div>");
		//out.println("</main>");
		rs.close();
		stmt.close();
		} catch (Exception e) {
			System.out.println("V doGet 01: " + e);
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
}
