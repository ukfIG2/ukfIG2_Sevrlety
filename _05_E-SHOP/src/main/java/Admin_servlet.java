import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;

public class Admin_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_04_E-SHOP";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	private Connection con;
       
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
			
            String operacia = request.getParameter("operacia");
            
            System.out.println("Operacia je: " + operacia);
            
            head(out);
            
            bottom(out);
            
           /* if (operacia == null) { zobrazNeopravnenyPristup(out); return; }
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
