package ukf.sk;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



/**
 * Servlet implementation class prod
 */
@WebServlet("/prod")
public class prod extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public HttpSession session;
	private PrintWriter out;
	private Connection con ;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public prod() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
		session = request.getSession();
	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try  {
        	con = connect.getConnection();
		
             if (session.getAttribute("id") == null) {
                response.sendRedirect("shop");
                return;
            }
        	out.println("<body  style=\"background-color:#ffeeee\">");
        	out.println("<div style=\"width:100%;height:60px;\">");
        	out.println("<div style=\"width:95%;  margin:auto; display:flex; flex-direction:row; justify-content:space-between\">");
        	out.println("<form id=\"logoutForm\" action=\"Logout\" method=\"post\">");
        	out.println("<input type=\"submit\" style=\"width:auto; height:30px; background-color:#dc787c; border:none; border-radius:5px; color: #00966c; padding:0px 5px;  margin-top:8px; margin-bottom:8px;  text-align: center; text-decoration: none; font-size: 20px;\" value=\"Logout\">");
        	out.println("</form>");
        	out.println("<button type='submit' name ='exit' value ='exit' style=\"width:200px; height:30px; background-color:#dc787c; border:none; border-radius:5px; color: #00966c; padding:0px 5px;  margin-top:8px; margin-bottom:8px;  text-align: center; text-decoration: none; font-size: 20px;\" onclick='window.open(\"shop\",\"_self\")' >HOME</button>");
	        out.println("<a href=\"Cart\" style=\"text-decoration:none\">");
	        out.println("<input type=\"button\" style=\"width:auto; height:30px; background-color:#dc787c; border:none; border-radius:5px; color: #00966c; padding:0px 5px;  margin-top:8px; margin-bottom:8px;  text-align: center; text-decoration: none; font-size: 20px;\" value=\"Cart\">");
 	        out.println("</a>");
	        out.println("</div>");
	        out.println("<hr style=\"height:2px; border:none; background-image: linear-gradient(to right, #134738 2%, #198556 50%, #134738 98%);\">");
        	out.println("</div>");
        	out.println("<a href=\"Main\" style=\"text-decoration:none;\">");
	        out.println("</a>");
        	out.println("<div class=\"flex-container\" style=\"width: 55%; height: fit-content; margin:auto; margin-top:25px; background-color:#eaafc6; padding:0px; border:4px solid #01291d;\">");
        	    
        	String id = request.getParameter("id");
        	try (PreparedStatement statement = con.prepareStatement("SELECT * FROM  products WHERE id = ?")) {
        	    statement.setString(1, id);
        	    ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
        	        	
        	         	  	 String name = resultSet.getString("name");
        	        String imageLink = resultSet.getString("image");
        	       // int count = resultSet.getInt("count");
        	        double price = resultSet.getDouble("price");
        	        double discount = resultSet.getDouble("discount");
        	        String description = resultSet.getString("description");
        	        double newprice = Math.round((price-(Math.ceil((discount * 0.01 * price) * 100)*0.01)) * 100)*0.01;
       	          	out.println("<h2 style=\"text-align:center; margin:auto; margin-bottom:5px; font-size:30px; font-family: 'Lucida Console', 'Courier New', monospace;\">" + name + "</h2>");
        	        out.println("<div style=\"display:flex; flex-direction:row; width:98%; justify-content: space-between; padding:5px\">");
        	        out.println("<div style=\"display:flex; flex-direction:column; width:30%;\">");
            	    out.println("<img src=\"" + imageLink + "\" style=\"width:98%; margin-left:1%; border: 3px solid #014230; margin-bottom:10px;\">");
            	    out.println("</div>");
            	    out.println("<div style=\"width:70%\">");
        	        out.println("<p style=\"margin-left:20px; font-size: 1.3em; line-height:1.3em\">"+description+"</p>");
        	        out.println("</div>");
        	        out.println("</div>");
        	        out.println("<div style=\"display:flex; flex-direction:row; width:70%; margin-left:15%; align-items: center; justify-content:center;\">");
        	        if (discount > 0) {
        	            out.println("<h3 style=\"text-align:center; font-size:20px; vertical-align: baseline; font-weight:500; font-family: 'Lucida Console', 'Courier New', monospace;\">€ <s>" + price + "</s>" + " " + newprice + "</h3>");
        	        } else {
        	            out.println("<h3 style=\"text-align:center; font-size:20px; vertical-align: baseline; font-weight:500; font-family: 'Lucida Console', 'Courier New', monospace;\">€ " + price + "</h3>");
        	        }
        	        out.println("<p style=\"margin-left:100px; margin-right:10px; font-size:20px\">Enter count:</p>");
        	        out.println("<form action ='' method='POST' >");
            	        
        	        out.print("<input type=\"number\" id=\"Count\" name=\"Count\" min=\"1\" max='20' style=\"width:100px; height:35px; font-size:18px; color:#7cfcd9; background-color:#d56ec4\" value = '1' required/>");
        	 //       out.println("<a href=\"Cart\" style=\"text-decoration:none\">");
        	        out.println("<input type = 'submit' style=\"width:75px; border-radius:10px; margin-left:35%; background-color: #d56ec4; border: none; color: #2b2b2b; padding: 15px 0px;  margin-top:20px;  text-align: center; text-decoration: none; font-size: 24px;\" value=\"To cart\">");
        	  //      out.println("</a>");
        	        out.println("</form>");
        	        out.println("</div>");
                	
               	    }
        	} catch (SQLException e) {
        		out.println("<h1>"+ e.toString()+"ERROR</h1>");
        	}

        	out.println("</div></div>");
        	out.println("</body>");
        }catch (Exception e) {
    	    e.printStackTrace();
    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		session = request.getSession();
		response.setContentType("text/html;charset=UTF-8");
	  
		try {
			con = connect.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  String sql = "insert into kosik (`ID_pouzivatela`,`ID_tovaru`,`cena`,`ks`,`Status`) select "+session.getAttribute("id")+","+request.getParameter("id")+",(100 - products.discount)/100*products.price,"+request.getParameter("Count")+",'order' from products where id="+request.getParameter("id")+";";
			   Statement stmt;
			try {
				stmt = con.createStatement();
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print(e.toString());
			}
			response.sendRedirect("shop");	
			return;
		
		
	}

}
