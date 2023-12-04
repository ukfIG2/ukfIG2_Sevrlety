package sk.marek;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class zobrazZakaznikov
 */
public class zobrazZakaznikov extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public zobrazZakaznikov() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL,login,pwd);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String operacia = request.getParameter("operacia");
		PrintWriter out = response.getWriter();
		String idParam = request.getParameter("id");
		int id = Integer.parseInt(idParam);
		if(operacia == null) {
			out.println("operacia neexistuje");
		}
		if(operacia.equals("zobrazZakaznikov")) {
			this.tovarZakaznik(out, id);
		}
		if(operacia.equals("zobrazZakaznikov")) {
			
		}
	}
	
	protected void tovarZakaznik(PrintWriter out, int id) {
		try {

			Statement stmt = con.createStatement();
			String sql = "SELECT "
		               + "zakaznici.id AS ID_zakaznika,"
		               + "zakaznici.meno AS MenoZakaznika,"
		               + "zakaznici.priezvisko AS PriezviskoZakaznika,"
		               + "zakaznici.ico AS ICO_zakaznika,"
		               + "zakaznici.adresa AS AdresaZakaznika,"		      
		               + "objednany_tovar.datum AS DatumObjednavky "
		               + "FROM zakaznici "
		               + "INNER JOIN objednany_tovar ON zakaznici.id = objednany_tovar.id_zakaznik "
		               + "INNER JOIN tovar ON objednany_tovar.id_tovar = tovar.id "
		               + "WHERE objednany_tovar.id_tovar =" + id;
			ResultSet rs = stmt.executeQuery(sql);
			
			
			while(rs.next()) {
				
				out.println("<p>ID_zakaznika: " + rs.getInt("ID_zakaznika") + "</p>");
				out.println("<p>Meno: " + rs.getString("MenoZakaznika") + "</p>");
				out.println("<p>Priezvisko: " + rs.getString("PriezviskoZakaznika") + "</p>");
				out.println("<p>ICO: " + rs.getString("ICO_zakaznika") + "</p>");
				out.println("<p>Adresa: " + rs.getString("AdresaZakaznika") + "</p>");
				out.println("<p>Datum objednavky: " + rs.getDate("DatumObjednavky") + "</p>");

				out.println("<hr>");
			}
			out.println("<p></p>");
			out.println("<form action='/Objednavky/Servlet_main' method='post'>");
		    out.println("<input type='hidden' name='operacia' value='vypisTovaru'>");
			out.println("<input type='submit'  value='spat na vypis produktov'>");
			 
			out.println("</form>");
			out.close();
	        stmt.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
