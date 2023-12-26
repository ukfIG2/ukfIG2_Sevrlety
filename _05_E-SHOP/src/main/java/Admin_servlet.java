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

/**
 * Servlet implementation class Admin_servlet
 */
public class Admin_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_04_E-SHOP";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	private Connection con;
		
	private String pozicia = "Neprihláseny zákaznik";

    public Admin_servlet() {
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
		
			/*HttpSession session = request.getSession();
			out.println(session.getAttribute("meno"));
			if(session.getAttribute("JeAdmin").equals("1")) {
				out.println("Je admin");
			} else {
				out.println("Nie si Admin. Zmizni");
			}*/
			

			head(out);
			header(out, request);
			main(out);
			bottom(out);
			
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
		out.println("<header>");
		out.println("	<div class='pozdrav'>");
		if(session.getAttribute("meno") == null) {
			out.println("		<h1>Ahoj " + pozicia + "</h1>");
		} else {
			pozicia = "Prihlaseny používateľ";
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
	    
		    out.println("    <div class='button-container'>");
		    out.println("        <form action='Main_servlet'>");
		    out.println("            <button type='submit'>Naspak na tovar </button>");
		    out.println("        </form>");
		    out.println("    </div>");
			out.println("</header>");   			
	}
	
	private void main(PrintWriter out) {
		out.println("<main>");
		
		out.println("</main>");
	}
	
	
	
	
}
