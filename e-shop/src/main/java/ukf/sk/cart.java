package ukf.sk;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * Servlet implementation class cart
 */
@WebServlet("/cart")
public class cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private connect connection;
	private Connection con = null;
	private PrintWriter out;
	public HttpSession session;
	private deleterow deleterow;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		session = request.getSession();
		out  = response.getWriter();
		if (request.getParameter("del") != null)
		{
			deleterow = new deleterow(request.getParameter("del"));
		}
		
		try {
			con = connect.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("<html>");
		out.println("<body style=\"background-color:#ffd8e1 ;\">");
    	out.println("<div style=\"width: 90%; height: fit-content; margin:auto; margin-top:50px\">");
    	out.println("<div style=\"width:95%;  margin:auto; display:flex; flex-direction:row; justify-content:space-between\">");
    	out.println("<button type='submit' name ='exit' value ='Logout' style=\"width:200px; height:30px; background-color:#dc787c; border:none; border-radius:5px; color: #00966c; padding:0px 5px;  margin-top:8px; margin-bottom:8px;  text-align: center; text-decoration: none; font-size: 20px;\" onclick='window.open(\"shop?exit=exit\",\"_self\")' >Logout</button>");
    	out.println("<button type='submit' name ='exit' value ='Cart' style=\"width:200px; height:30px; background-color:#dc787c; border:none; border-radius:5px; color: #00966c; padding:0px 5px;  margin-top:8px; margin-bottom:8px;  text-align: center; text-decoration: none; font-size: 20px;\" onclick='window.open(\"shop?buy=true\",\"_self\")' >BUY </button>");
 	     
    	
        out.println("</div>");
      
        
        float allStoim = 0;
        int allKS = 0;
        
        try (PreparedStatement statement = con.prepareStatement("select * from kosik a inner join products b on a.ID_tovaru=b.id where a.ID_pouzivatela="+session.getAttribute("id")+" and a.Status='order';")) {
    	    ResultSet resultSet = statement.executeQuery();
    	    out.println("<div class=\"flex-container\" style=\"width: 55%; height: fit-content; margin:auto; margin-top:25px; background-color:#eaafc6; padding:0px; border:4px solid #01291d;\">");
        	    out.println("<table width='100%'>");
    	    while (resultSet.next()) {
    	    	int id = resultSet.getInt("ID");
    	        String name = resultSet.getString("name");
    	        String image = resultSet.getString("image");
    	        double cena = resultSet.getDouble("cena");
    	        int ks = resultSet.getInt("ks");
    	        allKS += ks;
    	        double stoim = ks*cena;
    	        allStoim += stoim;
    	        double discount = resultSet.getDouble("discount");
    	        out.println("<tr style = 'font-size :2.2em '><td><img src=\"" + image + "\" style=\" height:150px; border: 2px solid #ed3940\"></td><td>"+ks+" ps * </td><td>"+cena+" EUR = </td><td>"+stoim+" EUR</td><td>");
    	        out.println("<buttom onclick='window.open(\"?del="+id+"\",\"_self\")'>delete<buttom> </td></tr>");
    	
	        }
    	    out.println("</table>");
    	//    out.println("<p style='font-size = 2.2 em'> total "+allKS+" ps. "+Math.round(allStoim*100)/100+" EUR  </p>");
     	    
    	    out.println("</div>");
         	  
    	    }
	        catch (SQLException e) {
        	    e.printStackTrace();
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
