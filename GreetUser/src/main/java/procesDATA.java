

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.ProtectionDomain;

/**
 * Servlet implementation class procesDATA
 */
public class procesDATA extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public procesDATA() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*String meno = request.getParameter("meno");
		String priezisko = request.getParameter("priezvisko");
		PrintWriter out = response.getWriter();
		out.print("Ahoj "+meno+" "+priezisko);
		out.close();
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());*/
		
		String meno = request.getParameter("meno");
		String heslo = request.getParameter("heslo");
		PrintWriter out = response.getWriter();
		if(heslo.equals("1234")) {
			out.print("Ahoj "+meno);
			out.close();
		}
		else {
			out.print("Zmiznzi");
			out.close();
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
