

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

public class Tovar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "_02_Objednavky";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	
	public final static String tT = "Tovar";
	public final static String tTid = "idTovar";
	public final static String tTnazov = "Nazov_tovaru";
	public final static String tTcena = "Cena_tovaru";
	public final static String tThodnotenie = "Hodnotenie";
	
	private Connection con;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tovar() {
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
PrintWriter out = response.getWriter();
		
		if(con==null) {
			out.println("Neni pripojena databaza");
			System.out.println("Pripojenie neni");
			return;
		}
		else {System.out.println("Pripojenie je");}
		
		String operacia = request.getParameter("operacia");
		if(operacia ==null) {													//asi neni najstasnejsie riesenie
			operacia=""; System.out.println("Prazdna operacia");
			//return;
		}
		else {System.out.println("Operacia "+operacia);}

		switch(operacia) {
		case "Vymazat":
			VymazPolozku(out, request.getParameter(tTid));
			break;
		case "Pridat":
			PridatPolozku(out, request.getParameter(tTnazov), request.getParameter(tTcena), request.getParameter(tThodnotenie));
			System.out.println("Operacia pridat prebehla");
			break;
		case "Upravit":
			System.out.println("case upravit");
			UpravitPolozku(out, request.getParameter(tTid));
			break;
		case "UlozitUpravu":
            String id = request.getParameter(tTid);
            String nazov = request.getParameter(tTnazov);
            String cena = request.getParameter(tTcena);
            String hodnotenie = request.getParameter(tThodnotenie);

            UlozitUpravu(out, id, nazov, cena, hodnotenie);
            break;
		}

		VypisDatabazu(out);
		if(operacia.equals("addForm")) {ZobrazFormularPrePridanie(out);}
			
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
