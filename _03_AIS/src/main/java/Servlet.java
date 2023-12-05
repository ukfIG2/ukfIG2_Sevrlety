

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



/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;
	public final static String databaza = "ais";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
		try {
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
		response.setContentType("text/html;charset=UTD-8");
		PrintWriter out = response.getWriter();
		try {
			if(con==null) {out.println("chyba spojenia"); return;}
			String operacia = request.getParameter("operacia");
			if(operacia==null) {zobrazNeopravnenyPristup(out); return;}
			if(operacia.equals("login")) {overUsera(out); return;}
			int user_id = getUserID(request);
			if(user_id==0) {zobrazNeopravnenyPristup(out); return;}
			vypisData(out, request);
			if(operacia.equals("potvrdit")) {zapisPotvrdenia(out, request);}
			if(operacia.equals("logout")) {urobLogout(out, request); return;}
			vypisData(out, request);
		} catch (Exception e) {
			System.out.println("Servlet doget " + e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			if(rs.getInt("pocet"))
		} catch (Exception e) {
			System.out.println("OverUsera " + e);
		}
	}

}
