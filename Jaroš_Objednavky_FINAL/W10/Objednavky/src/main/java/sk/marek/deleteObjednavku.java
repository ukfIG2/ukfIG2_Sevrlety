package sk.marek;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class deleteObjednavku
 */
public class deleteObjednavku extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String URL = "jdbc:mysql://localhost/objektove-technologie";
    private String login = "root";
    private String pwd = "";     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteObjednavku() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, login, pwd);

            int idToDelete = Integer.parseInt(request.getParameter("id"));

            
            
                // Mazanie záznamu z tabuľky (Spojovacia)
                deleteRecord(con, idToDelete);
                out.println("<p>Zaznam bol uspesne vymazany.</p>");
                out.println("<form action='Servlet_main' method='post'>");
   			 	out.println("<input type='hidden' name='operacia' value='vypisSpojovacej'>");
   			 	out.println("<input type='submit'  value='spat na vypis objednavok'>");
   			 	out.println("</form>");
            
        } catch (Exception e) {
            out.println("Chyba pri vymazávaní záznamu: " + e.getMessage());
        }
	}
	
	
	private void deleteRecord(Connection con, int idToDelete) throws SQLException {
        
        String deleteQueryTovar = "DELETE FROM objednany_tovar WHERE id = ?";
        
        
        try (PreparedStatement pstmtTovar = con.prepareStatement(deleteQueryTovar)) {
            pstmtTovar.setInt(1, idToDelete);
            
            
            int deletedRowsTovar = pstmtTovar.executeUpdate();
            

            if (deletedRowsTovar > 0) {
                System.out.println("Záznam v tabuľke Tovar bol úspešne vymazaný.");
            } else {
                System.out.println("Záznam nebol nájdený.");
            }
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
