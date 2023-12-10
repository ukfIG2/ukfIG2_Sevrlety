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

import com.mysql.cj.Session;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "ais";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	private Connection con;
	private String my_error="";
       
    public Servlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
		try {
			super.init();//Treba toto?
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, username, password);
		} catch (Exception e) {
			System.out.println("V init: " + e);
		}
	}
    
	public void destroy() {
		try {con.close();} catch(Exception e) {System.out.println("V destroy: " + e);	
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (con == null) { out.println(my_error);  return; }
            String operacia = request.getParameter("operacia");
            if (operacia == null) { zobrazNeopravnenyPristup(out); return; }
            if (operacia.equals("login")) { overUsera(out, request); }
            int user_id = getUserID(request);
            if (user_id == 0) { zobrazNeopravnenyPristup(out); return; }
            vypisHlavicka(out, request);
            if (operacia.equals("potvrdit")) {zapisPotvrdenia(out, request); }
            if (operacia.equals("logout")) { urobLogout(out, request); return; }
            vypisData(out, request);
		} catch (Exception e) {
			System.out.println("Servlet doget " + e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void zobrazNeopravnenyPristup(PrintWriter out) {
		out.println("Neopravneny pristup");
		out.println("<a href='index.html'>Prihlasenie</a>");
	}
	
	protected void overUsera(PrintWriter out, HttpServletRequest request) {
		try {
			String meno = request.getParameter("login");
			String heslo = request.getParameter("pwd");
			Statement stmt = con.createStatement();
			String sql = "SELECT MAX(id) AS iid, COUNT(id) AS pocet FROM users WHERE email='"+meno+"'AND passwd = '"+heslo+"'";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
						
			HttpSession session = request.getSession();
			if(rs.getInt("pocet")==1) {//existuje jeden, takze OK
				sql = "SELECT id, meno, priezvisko FROM users WHERE email = '"+meno+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				session.setAttribute("ID", rs.getInt("id"));
				session.setAttribute("meno", rs.getString("meno"));
				session.setAttribute("priezvisko", rs.getString("priezvisko"));
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
		out.println("<h2>"+session.getAttribute("meno")+" "+session.getAttribute("priezvisko")+"</h2>");
		out.println("<hr>");
	}
	
	protected void vypisData(PrintWriter out, HttpServletRequest request) {
		try {
			Statement stmt = con.createStatement();
			String sql = "Select znamky.id, znamky.datum, predmet, znamka, videne From znamky INNER JOIN predmety ON (znamky.predmet_id = predmety.id) WHERE user_id = "+getUserID(request);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {//samostatny formular, pre kazdy riadok/znamku
				out.println("<form method='post' action='Servlet'>");
				out.println(rs.getString("datum"));
				out.println("<b>"+rs.getString("predmet")+"</b>");
				out.println(rs.getString("znamka"));
				if(rs.getString("videne") == null) {//tlacidlo, ak to nevidel aj s hidden
					out.print("<input type='hidden' name='operacia' value='potvrdit'>");
					out.print("<input type='hidden' name='id' value='"+rs.getString("id")+"'>");
					out.print("<input type='submit' value='potvrd videnie'>");
				}else {	//vypis datumu, ak znamku potvrdil
					out.println("videl: "+rs.getString("videne"));
				}
				out.println("</form>");//ukoncenie formu
			}
			rs.close();
			stmt.close();
			out.println("<br /><br />");
			out.println("<form method='post' action='Servlet'>");
			out.println("<input type='hidden' name='operacia' value='logout'>");
			out.println("<input type='submit' value='logout'>");
			out.println("</form>");
		} catch(Exception e) {System.out.println("vypisData: "+e);}
	}
	
	//zapis studenta za videl znamku
	protected void zapisPotvrdenia(PrintWriter out, HttpServletRequest request) {
		try {
			 Statement stmt = con.createStatement();
			 String sql = "UPDATE znamky SET videne = NOW() WHERE id="+request.getParameter("id");
			 stmt.executeUpdate(sql);
			 stmt.close();
		} catch (Exception e){
			System.err.println("ZapisPotvrdenia "+e);
		}
	}
	
	//odhlasenie
	protected void urobLogout(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		out.println("bye bye<br>");
		out.println("<a href='index.html'>Domov</a>");
	}
	
	
	
	
	
	
}
