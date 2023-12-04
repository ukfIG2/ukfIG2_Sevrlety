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
 * Servlet implementation class zobrazTovar
 */
public class zobrazTovar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";  
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public zobrazTovar() {
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
		if(operacia.equals("zobrazTovar")) {
			this.zakaznikTovar(out, id);
		}
		if(operacia.equals("zobrazZakaznikov")) {
			
		}
	}
	
	protected void zakaznikTovar(PrintWriter out, int id) {
		try {
			Statement stmt = con.createStatement();
	
			String sql = "SELECT "
			        + "tovar.id AS ID_tovaru,"
			        + "tovar.nazov AS Nazov_tovaru,"
			        + "tovar.cena AS Cena_tovaru,"
			        + "tovar.hodnotenie AS Hodnotenie_tovaru,"
			        + "objednany_tovar.datum AS Datum_objednavky "
			        + "FROM tovar "
			        + "INNER JOIN objednany_tovar ON tovar.id = objednany_tovar.id_tovar "
			        + "WHERE objednany_tovar.id_zakaznik =" + id;
	
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
//	        	out.println("<p>ID Zakaznika: " + id + "</p>");
	            out.println("<p>ID tovaru: " + rs.getInt("ID_tovaru") + "</p>");
	            out.println("<p>Nazov tovaru: " + rs.getString("Nazov_tovaru") + "</p>");
	            out.println("<p>Cena tovaru: " + rs.getDouble("Cena_tovaru") + "</p>");
	            out.println("<p>Hodnotenie tovaru: " + rs.getString("Hodnotenie_tovaru") + "</p>");
	            out.println("<p>Datum objednavky: " + rs.getDate("Datum_objednavky") + "</p>");

	            out.println("<hr>");
	        }
	        out.println("<p></p>");
	        out.println("<form action='/Objednavky/Servlet_main' method='post'>");
		    out.println("<input type='hidden' name='operacia' value='vypisZakaznikov'>");
			out.println("<input type='submit'  value='spat na vypis zakaznikov'>");
			 
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
