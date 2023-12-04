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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * Servlet implementation class Servlet_main
 */
public class Servlet_main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";  
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet_main() {
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
		if(operacia == null) {
			this.vypisSpojovaciuTabulku(out);
		}
		if(operacia.equals("vypisTovaru")) {
			this.vypis(out);
		}

		if(operacia.equals("vypisZakaznikov")) {
			this.zobrazTabulkuZakaznici(out);
		}
		if(operacia.equals("pridajDoSpojovacej")) {
			this.pridajDoSpojovacej(out,request, response);
		}
		if(operacia.equals("vlozDoTabulky")) {
			this.vlozDoSpojovacejTabulky(out,request, response);
		}
		if(operacia.equals("vypisSpojovacej")) {
			this.vypisSpojovaciuTabulku(out);
			
		}

		
		
	}
	
	
	private void vlozDoSpojovacejTabulky(PrintWriter out,HttpServletRequest request, HttpServletResponse response) {
		try {
			Statement stmt = con.createStatement();
			
	        String id_priezvisko = request.getParameter("priezvisko");
	        int id_zakaznik = Integer.parseInt(id_priezvisko);
	        String id_tovar_nazov = request.getParameter("tovar");
	        int id_tovar = Integer.parseInt(id_tovar_nazov);
	        Date datum = Date.valueOf(request.getParameter("datum"));
	        String dotaz = "INSERT INTO objednany_tovar(id_tovar, id_zakaznik, datum) VALUES (?, ?, ?)";
	        try (PreparedStatement preparedStatement = con.prepareStatement(dotaz)) {
	            preparedStatement.setInt(1, id_tovar);
	            preparedStatement.setInt(2, id_zakaznik);
	            preparedStatement.setDate(3, datum);


	            preparedStatement.executeUpdate();
	        }

	        response.sendRedirect(request.getContextPath() + "/Servlet_main?operacia=vypisSpojovacej");
	    } catch (Exception cE) {
	    	
	    	out.print( cE.toString() );
	        System.out.println("Ovladac nenajdeny: " + cE.toString());
	    }
	}
	
	protected void pridajDoSpojovacej(PrintWriter out,HttpServletRequest request, HttpServletResponse response) {
		try { 
			response.setContentType("text/html");
			out.println("<html><body>");
		    out.println("<form action='Servlet_main' method='get'>");
		    out.println("<input type='hidden' name='operacia' value='vlozDoTabulky'>");
		 	out.println("<label for='priezvisko'>Priezvisko:</label>");
	 		out.println("<select name='priezvisko' id='priezvisko'>");
	 		Statement stmt = con.createStatement();
			String sqlZakaznik = "SELECT * FROM `objektove-technologie`.zakaznici";
		    
		    ResultSet rs = stmt.executeQuery(sqlZakaznik);
		    while(rs.next()) {
				
		        

	 			out.println("<option value='"+ rs.getString("id") + "'>" +rs.getString("priezvisko") + "</option> ");
		    }

		    out.println("</select>");
 			out.println("<label for='tovar'>Tovar:</label>");
	 		out.println("<select name='tovar' id='tovar'>");
 			String sqlTovar = "SELECT * FROM `objektove-technologie`.tovar";
		    ResultSet rsTovar = stmt.executeQuery(sqlTovar);
		    while(rsTovar.next()) {
		    	out.println("<option value='"+ rsTovar.getString("id") + "'>" +rsTovar.getString("nazov") + "</option> ");
		    }
		    out.println("</select>");
		    out.println("<label for='datum'>Datum:</label>");
		    out.println("<input type='date' name='datum' id='datum' required>");
		    
		    out.println("<button type='submit'>Odoslať</button>");
		    out.println("</form>");
		} catch (Exception e) {
			// TODO: handle exception
			out.println(e.getMessage());
		}
	}

	
	protected void zobrazTabulkuZakaznici(PrintWriter out) {
		try {

			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM `objektove-technologie`.zakaznici";
		    
		    ResultSet rs = stmt.executeQuery(sql);
			out.println("<p>Udaje o vsetkych zakaznikoch (vypis tabulky 2)</p>");
			while(rs.next()) {
				
			        
//			        out.println("<p>ID_zakaznika: " + rs.getInt("id") + "</p>");
			        out.println("<p>Meno: " + rs.getString("meno") + "</p>");
			        out.println("<p>Priezvisko: " + rs.getString("priezvisko") + "</p>");
			        out.println("<p>ICO: " + rs.getString("ico") + "</p>");
			        out.println("<p>Adresa: " + rs.getString("adresa") + "</p>");
			        
			        out.println("<br>");
			        out.println("<form action='/Objednavky/zobrazTovar?id=" + rs.getInt("id") + "' method='post'>");
					out.println("<input type='hidden' name='operacia' value='zobrazTovar'>");
					out.println("<input type='submit'  value='zobraz tovar ktore si objednal tento zakaznik'>");
					out.println("</form>");
					
					out.print("<a href='/Objednavky/editZakaznik?id=" + rs.getInt("id") + "'><button>Edit Zakaznik</button></a>");
					out.print("<a href='/Objednavky/deleteZakaznik?id=" + rs.getInt("id") + "'><button>Vymaz Zakaznika</button></a>");
			        out.println("<hr>");
							
			}
			out.print("<a href='/Objednavky/addZakaznik'><button>Pridaj Zakaznika</button></a>");
			out.print("<br>");

			 out.println("<form action='Servlet_main' method='post'>");
			 out.println("<input type='hidden' name='operacia' value='vypisSpojovacej'>");
			 out.println("<input type='submit'  value='zobraz tabulku 1,2,3(prienik vsetkych)'>");
			 out.println("</form>");
			 
			 out.println("<form action='Servlet_main' method='post'>");
			 out.println("<input type='hidden' name='operacia' value='vypisTovaru'>");
			 out.println("<input type='submit'  value='zobraz tabulku 1(vypis tovaru)'>");
			 out.println("</form>");
			 

			out.close();
			stmt.close();
			
			} catch (Exception e) {
			// TODO: handle exception
				System.out.println(e.getMessage());
			}		
	}
	protected void zobrazTabulkuTovar(PrintWriter out) {
		try {

			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM `objektove-technologie`.tovar";
		    
		    ResultSet rs = stmt.executeQuery(sql);
			out.println("<p>Udaje o vsetkych tovaroch (vypis tabulky 1)</p>");
			while(rs.next()) {
				
			        
			        out.println("<p>ID_tovaru: " + rs.getInt("id") + "</p>");
			        out.println("<p>Nazov tovaru: " + rs.getString("nazov") + "</p>");
			        out.println("<p>Cena tovaru: " + rs.getString("cena") + "</p>");
			        out.println("<p>Hodnotenie tovaru: " + rs.getString("hodnotenie") + "</p>");
			        
			        out.println("<br>");
			        out.println("<hr>");
							
			}
			out.println("<form action='/Objednavky/zobrazTovar?id=" + rs.getInt("id") + "' method='post'>");
			 out.println("<input type='hidden' name='operacia' value='zobrazZakaznikov'>");
			 out.println("<input type='submit'  value='zobraz Tovar ktori si objednali tento zakaznik'>");
			 out.println("</form>");
			
			

			out.println("<form action='Servlet_main' method='post'>");
			 out.println("<input type='hidden' name='operacia' value='vypisSpojovacej'>");
			 out.println("<input type='submit'  value='zobraz tabulku 1,2,3(spojovacia tabulka)'>");
			 out.println("</form>");
			out.close();
			stmt.close();
			
			} catch (Exception e) {
			// TODO: handle exception
				System.out.println(e.getMessage());
			}		
	}
	
	protected void vypisSpojovaciuTabulku(PrintWriter out) {
		
//		response.setContentType("text/html");
		

        try {
            Statement stmt = con.createStatement();
            out.println("<p>Spojovacia tabulka </p>");
            String sql = "SELECT "
            		+ "objednany_tovar.id AS id, "
                    + "zakaznici.id as ID_zakaznika, "
                    + "tovar.id as ID_tovaru, "
                    + "zakaznici.meno AS MenoZakaznika, "
                    + "zakaznici.priezvisko AS PriezviskoZakaznika, "
                    + "tovar.nazov AS NazovTovaru, "
                    + "tovar.cena AS CenaTovaru, "
                    + "objednany_tovar.datum AS DatumObjednavky "
                    + "FROM zakaznici "
                    + "INNER JOIN objednany_tovar ON zakaznici.id = objednany_tovar.id_zakaznik "
                    + "INNER JOIN tovar ON objednany_tovar.id_tovar = tovar.id";
            ResultSet resultSet = stmt.executeQuery(sql);


            out.println("<html><body><table border=\"1\">");
            out.println("<tr><th>ID_zakaznika</th><th>ID_tovaru</th><th>MenoZakaznika</th>"
                    + "<th>PriezviskoZakaznika</th><th>NazovTovaru</th><th>CenaTovaru</th>"
                    + "<th>DatumObjednavky</th><th>Operacie</th></tr>");

            while (resultSet.next()) {
                out.println("<tr><td>" + resultSet.getInt("ID_zakaznika") + "</td>"
                        + "<td>" + resultSet.getInt("ID_tovaru") + "</td>"
                        + "<td>" + resultSet.getString("MenoZakaznika") + "</td>"
                        + "<td>" + resultSet.getString("PriezviskoZakaznika") + "</td>"
                        + "<td>" + resultSet.getString("NazovTovaru") + "</td>"
                        + "<td>" + resultSet.getDouble("CenaTovaru") + "</td>"
                        + "<td>" + resultSet.getDate("DatumObjednavky") + "</td>"
                        + "<td>"
                        + "<a href='/Objednavky/editObjednavku?id=" + resultSet.getInt("id") + "'>Edit Objednavku</a>"
                        + " | "
                        + "<a href='/Objednavky/deleteObjednavku?id=" + resultSet.getInt("id") + "'>Vymaz Objednavku</a>"
                        + "</td></tr>" );
            }
            out.println("</table></body></html>");
            
            
            
            
                    
       			 out.println("<form action='Servlet_main' method='post'>");
       			 out.println("<input type='hidden' name='operacia' value='vypisTovaru'>");
       			 out.println("<input type='submit'  value='zobraz tabulku 1(vypis tovaru)'>");
       			 out.println("</form>");
       			 
       			 out.println("<form action='Servlet_main' method='post'>");
       			 out.println("<input type='hidden' name='operacia' value='vypisZakaznikov'>");
       			 out.println("<input type='submit'  value='zobraz tabulku 2( zakaznici vypis zakaznikov)'>");
       			 out.println("</form>");
       			 
       			 
       			 
       			 
       			 out.println("<form action='Servlet_main' method='post'>");
       			 out.println("<input type='hidden' name='operacia' value='pridajDoSpojovacej'>");
       			 out.println("<input type='submit'  value='Pridaj zaznam do spojovacej tabulky(3tabulka)'>");
       			 out.println("</form>");
            
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
	}
	
	
	protected void vypis(PrintWriter out) {
			
		
		try {

			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM tovar";
			ResultSet rs = stmt.executeQuery(sql);
			out.println("<p>Zobrazujem vsetky tovary</p>");
			while(rs.next()) {
							
				out.println("<p>Nazov tovaru: " + rs.getString("nazov") + "</p>");
				out.println("<p>Cena: " + rs.getDouble("cena") + "</p>");
				out.println("<p>Hodnotenie: " + rs.getString("hodnotenie") + "</p>");
			

				out.println("<form action='/Objednavky/zobrazZakaznikov?id=" + rs.getInt("id") + "' method='post'>");
				 out.println("<input type='hidden' name='operacia' value='zobrazZakaznikov'>");
				 out.println("<input type='submit'  value='zobraz zakaznikov ktori si objednali tento produkt'>");
				 out.println("</form>");
				 
				out.print("<a href='/Objednavky/editTovar?id=" + rs.getInt("id") + "'><button>Edit Tovar</button></a>");
				out.print("<a href='/Objednavky/deleteTovar?id=" + rs.getInt("id") + "'><button>Vymaz Tovar</button></a>");
				

				 out.println("<hr>");
			}
			out.print("<a href='/Objednavky/addTovar'><button>Pridaj Tovar</button></a>");
			out.print("<br>");
			

			 
			 out.println("<form action='Servlet_main' method='post'>");
			 out.println("<input type='hidden' name='operacia' value='vypisZakaznikov'>");
			 out.println("<input type='submit'  value='zobraz tabulku 2( zakaznici vypis zakaznikov)'>");
			 out.println("</form>");
			 
			 out.println("<form action='Servlet_main' method='post'>");
			 out.println("<input type='hidden' name='operacia' value='vypisSpojovacej'>");
			 out.println("<input type='submit'  value='zobraz tabulku 1,2,3(prienik vsetkych)'>");
			 out.println("</form>");
			 
			 
			 

			out.close();
			stmt.close();
//			con.close();
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
