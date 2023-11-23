

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
 * Servlet implementation class CRUD_INSERT
 */
public class CRUD_INSERT extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CRUD_INSERT() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String datum = request.getParameter("Datum");
        String prijem = request.getParameter("Prijem");
        String vydaj = request.getParameter("Vydaj");
        String poznamka = request.getParameter("Poznamka");
        
        datum = (datum.isEmpty()) ? "DEFAULT" : "'" + datum + "'";
        prijem = (prijem.isEmpty()) ? "0" : "'" + prijem + "'";
        vydaj = (vydaj.isEmpty()) ? "0" : "'" + vydaj + "'";
        poznamka = (poznamka.isEmpty()) ? "NULL" : "'" + poznamka + "'";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(CRUD.URL, CRUD.username, CRUD.password);
            Statement stmt = con.createStatement();

            String sql = "INSERT INTO " + CRUD.tabulka + " (Datum, Prijem, Vydaj, Poznamka) VALUES (" + datum + ", "
                    + prijem + ", " + vydaj + ", " + poznamka + ")";
            stmt.executeUpdate(sql);
            response.sendRedirect(request.getContextPath() + "/CRUD");


            

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Pokazilo sa v INSERT: " + e);
        }
    }
	}


