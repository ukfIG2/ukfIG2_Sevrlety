

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Servlet implementation class CRUD_DELETE
 */
public class CRUD_DELETE extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CRUD_DELETE() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		
		try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(CRUD.URL, CRUD.username, CRUD.password);
	        Statement stmt = con.createStatement();

	        // Assuming 'id' is the primary key column
	        String sql = "DELETE FROM " + CRUD.tabulka + " WHERE id = '" + id + "'";
	        //int rowsAffected = stmt.executeUpdate(sql);
	        stmt.executeUpdate(sql);
	        response.sendRedirect(request.getContextPath() + "/CRUD");
	        /*if (rowsAffected > 0) {
	            // Successfully deleted
	            response.sendRedirect(request.getContextPath() + "/CRUD"); // Redirect back to the main page
	        } else {
	            // Deletion failed
	            response.getWriter().println("Error deleting row.");
	        }*/

	        stmt.close();
	        con.close();
	    } catch (Exception e) {
	        System.out.println("Dodrbalo sa: " + e);
	    }
		//
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
