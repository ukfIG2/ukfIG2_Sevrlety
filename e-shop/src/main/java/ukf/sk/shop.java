package ukf.sk;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
 * Servlet implementation class shop
 */
@WebServlet("/shop")
public class shop extends HttpServlet {
	private String URL = "jdbc:mysql://localhost/E_SHOP_AL";
	private String user = "root";
	private String pass = "" ;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String meno, id ;
	private connect connection;
	private Connection con = null;
	private PrintWriter out;
		public HttpSession session;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public shop() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void connect_old () throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print("ER3"+URL+" "+ user +" "+ pass);
		}
try {
	con = DriverManager.getConnection(URL, user, pass);
} catch (Exception e) {
	out.print("ER4"+URL+" "+ user +" "+ pass);
	}
}
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		session = request.getSession();
		out  = response.getWriter();
		//System.out.println(session.getAttribute("id"));
		if (request.getParameter("exit") != null)
			if (request.getParameter("exit").equals("exit"))
				{
						session.invalidate();
						session = request.getSession();
						
					
				}
		
		
	try {
		con = connect.getConnection();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	if (request.getParameter("buy") != null)
			if (request.getParameter("buy").equals("true"))
				{

					buy();
						
					
				}
	if (session.getAttribute("id")==null)
		{
		
		out.println("<html lang='en'>\n<body  style=\"background-color:#ffeeee\">");
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	  
		out.println("<div style=\"display:flex; flex-direction:column; background-color:#ebcacb; width:600px; border: 2px solid #c13e53;  padding: 30px 20px; margin:auto; justify-content: center; aligh-items:center; margin-top:180px;\">");
		out.println("<h2 style=\"margin:auto; color:#440012; margin-bottom:60px\">Login</h2>");
		out.println("<form method='POST' name ='auth' action='shop'>");
		out.println("<div style=\"justify-content: center; 	display:flex; flex-direction:row; margin:auto; margin-bottom: 50px; width: 520px; justify-content: space-between\">");
		out.println("<div style=\"display:flex; flex-direction:row;\">");
		out.println("<label for=\"login\" style=\"color:#440012; margin-right:10px\">Login</login>");
		out.println("<input type = 'email' name = 'login' style=\"color:#7a0132; background-color:#bf768a; border:none\"/>");
		out.println("</div>");
		out.println("<div style=\"display:flex; flex-direction:row\">");
		out.println("<label for=\"password\" style=\"color:#440012; margin-right:10px\">Password</label>");
		out.println("<input type = 'password' name = 'pass' style=\"color:#440a37; background-color:#bf768a; border:none\"/><br>");
		out.println("</div></div>");
		out.println("<div style=\"display:flex; flex-direction:column\">");
		out.print("<input type = 'submit' name = 'submite' value = 'Login' style=\"border:none; border-radius:5px; color:#4c2312; font-weight:900; background-color:#e8a978; text-aligh:center; font-size:18px; width:120px; height:30px; margin:auto; margin-bottom:8px;cursor: pointer\"/>");
		out.print("<input type = 'button' name = 'register' value = 'Registration' style=\"border:none; border-radius:5px; color:#ae4157; background-color:#d2919e; text-aligh:center; font-size:15px; width:100px; margin:auto; height:25px;cursor: pointer\"/>");
		out.print("</div>\n</form>\n</div>\n</body>\n</html>");
     	}
	else 
	{
		String cartNum="";
		try (PreparedStatement statement = con.prepareStatement("SELECT count(*) as kolvo FROM `kosik` WHERE `ID_pouzivatela` = "+session.getAttribute("id")+" and `Status` ='order'")) {
			System.out.println("SELECT count(*) as kolvo FROM `kosik` WHERE `ID_pouzivatela` = "+session.getAttribute("id")+" and `Status` ='order'");
				
    	    ResultSet rSet = statement.executeQuery();
    	    rSet.next();
    	    cartNum = rSet.getString("kolvo");
    	    }  
		catch (SQLException e) {
        	    e.printStackTrace();
        	}
		out.println("<!DOCTYPE html>");
		out.println("<html lang='en'>");
		out.println("<body style=\"background-color:#ffd8e1 ;\">");
    	out.println("<div style=\"width: 90%; height: fit-content; margin:auto; margin-top:50px\">");
    	int dopStroka = 0;
    	out.println("<div style=\"width:95%;  margin:auto; display:flex; flex-direction:row; justify-content:space-between\">");
    	out.println("<button type='submit' name ='exit' value ='Logout' style=\"width:200px; height:30px; background-color:#dc787c; border:none; border-radius:5px; color: #00966c; padding:0px 5px;  margin-top:8px; margin-bottom:8px;  text-align: center; text-decoration: none; font-size: 20px;\" onclick='window.open(\"shop?exit=exit\",\"_self\")' >Logout</button>");
    	out.println("<button type='submit' name ='exit' value ='Cart' style=\"width:200px; height:30px; background-color:#dc787c; border:none; border-radius:5px; color: #00966c; padding:0px 5px;  margin-top:8px; margin-bottom:8px;  text-align: center; text-decoration: none; font-size: 20px;\" onclick='window.open(\"cart\",\"_self\")' >To Cart "+cartNum+"</button>");
 	     
    	
        out.println("</div>");
      
		try (PreparedStatement statement = con.prepareStatement("SELECT * FROM products")) {
    	    ResultSet resultSet = statement.executeQuery();
    	    while (resultSet.next()) {
    	    	int id = resultSet.getInt("id");
    	        String name = resultSet.getString("name");
    	        String image = resultSet.getString("image");
    	        double price = resultSet.getDouble("price");
    	        String description = resultSet.getString("description");
    	        double discount = resultSet.getDouble("discount");
    	        
    	    if(dopStroka == 0) {
    	    	out.println("<div style=\"display:flex; margin-bottom:25px; flex-direction:row; justify-content:space-between\">");
    	    }
    	    out.println("<div style=\"width:240px; height:auto; background-color:#ffccd5;padding:10px; border:3px solid #6e1f3b; border-radius:10px; \">");
	        out.println("<img src=\"" + image + "\" style=\"width:235px; height:300px; border: 2px solid #ed3940\">");
	        out.println("<div style=\"height:80px;\">");
	        out.println("<h2 style=\"text-align:center; margin-top:5px; font-family: cursive;\">" + name + "</h2>");
	        out.println("</div>");
	        if (discount > 0){
	            out.println("<h3 style=\"text-align:center; vertical-align: baseline; font-family: serif;\"> <s>"+price+"</s>" + "  EUR" +"</h3>");
    	    }else
    	    {
	        	out.println("<h3 style=\"text-align:center; vertical-align: baseline; font-family: serif;\"> "+price+"</s>" + "  EUR" +"</h3>");
	        	}
//	        out.println("<div style=\"display:flex; flex-direction:column; position:absolute; margin:auto; margin-top:50px;\">");
	        out.println("<a href=\"prod?id=" + id + "\" style=\"text-decoration:none\">");
	        out.println("<div style=\"width:200px; margin-left:20px; background-color: #dc787c;font-family: Georgia, serif; border: none; color: #2b2b2b; padding: 15px 0px;  margin-top:20px;  text-align: center; text-decoration: none; font-size: 16px;\">To page</div>");
	        out.println("</a>");
	     //   out.print("<input type=\"button\" style=\"width:200px; margin-left:20px; background-color: #abccff;font-family: Georgia, serif; border: none; color: #2b2b2b; padding: 15px 0px;  margin-top:20px;  text-align: center; text-decoration: none; font-size: 16px;\" value=\"To cart\">");
//	        out.println("</div>");
	        out.println("</div>");
	        dopStroka++;
	        if (dopStroka == 5) {
	            out.println("</div>");
	            dopStroka = 0;
	            }
	        }
    	    }
	        catch (SQLException e) {
        	    e.printStackTrace();
        	}
		

    				out.println("<button name ='exit' value = 'exit' type='submit' onclick='window.open(\"?exit=exit\")'> logout</button>");
        			
    				out.println("</body>\n</html>");	
	}

	       
		
		
           
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		session = request.getSession();
		out  = response.getWriter();
		
	if (request.getParameter("submite")!=null) 
			if( request.getParameter("submite").equals("Login"))
		checkUser(request,response);
		
	doGet(request,response);


	}


	
	public void buy ()
	{
		String sql ="update kosik set Status = 'payed' where Status = 'order' and ID_pouzivatela =" + session.getAttribute("id");
		   Statement stmt;
					try {
						stmt = con.createStatement();
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						out.print(e.toString());
					}
		
	}
	public void checkUser(HttpServletRequest request, HttpServletResponse response)
	{
		String sql = "SELECT * FROM users where login='"+request.getParameter("login") + "' and passwd ='"+request.getParameter("pass")+"' ";
		try {
			con = connect.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				session.setAttribute("id", rs.getInt("id"));
		    	session.setAttribute("meno", rs.getString("meno"));
		    	session.setAttribute ("admin", rs.getInt("admin"));	
		    	session.setAttribute ("con",connection );	
		    
						}
			else 
				{
			
				}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// out.println(sql);
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//
//
//
//	 private boolean destroy(PrintWriter out) {
//		 try {
//		      con.close();
//		  } catch (Exception ex) {
//			  System.out.println("Chyba : " + ex.toString());
//		  }
//		  super.destroy();
//		}
//	 private boolean badOperation(String operacia, PrintWriter out) {
//		   if (operacia == null) {
//		       vypisNeopravnenyPristup(out);
//		       return true;
//		   }
//		   return false;
//		}
// 
//	 private int getLogedUser( HttpServletRequest request, PrintWriter out) {
//		 session = request.getSession();
//		   int id = (Integer)session.getAttribute("id");
//		   if (id == 0) {
//		       out.println("Neprihlásený user");
//		       vypisNeopravnenyPristup(out);
//		   }
//		   return id;
//		}
//
//	 private void vypisNeopravnenyPristup(PrintWriter out2) {
//		// TODO Auto-generated method stub
//		
//	}
//	private void createHeader(PrintWriter out,  HttpServletRequest request){
//		 session = request.getSession();
//		   // user je prihlaseny a mam jeho data
//		   String vypis = (String)session.getAttribute("meno") + " " +
//		                  (String)session.getAttribute("priezvisko");
//		   out.println("<p align=right>"+vypis);
//
//		   // odkaz na kosik
//		   out.println("<a href='kosikServlet'>Košík</a>");
//
//		   // tlacidlo na logout
//		   out.println("<form action='mainServlet' method='post'>");
//		   out.println("<input type='hidden' name='operacia' value='logout'>");
//		   out.println("<input type='submit' value='Odhlásiť'>");
//		   out.println("<hr>");
//		}
//	 private void createBody(PrintWriter out, HttpServletRequest request) {
//		 session = request.getSession();
//			   Integer zlava = (Integer)session.getAttribute("zlava");
//			   int aktCena = 0;  
//			   try {
//				   Statement stmt = con.createStatement();
//			      ResultSet rs = stmt.executeQuery("select * from sklad");
//			      while (rs.next()) {
//			    	    aktCena = rs.getInt("cena")*(100-zlava)/100;
//			    	    out.println("<form action='mainServlet' method='post'>");
//			    	    out.println("<input type='hidden' name='ID' value= '" +
//			    	                 rs.getString("ID")+"'>");
//			    	    out.println("<input type='hidden' name='cena' value='"+
//			    	                 aktCena+"'>");
//			    	    out.println("<input type='hidden' name='operacia' value='nakup'>");
//			    	    out.println("<input type='submit' value='Do košíka'>");
//			    	    out.println("&nbsp;&nbsp;<strong>"+rs.getString("nazov")+
//			    	                            ":</strong>"+ aktCena+" EUR");
//			    	    out.println("</form><hr>");
//			    	  }
//			    	  rs.close(); 
//			    	  stmt.close();
//			   } catch (Exception e) {out.println(e.getMessage());}
//			 }
//	 private void createFooter (PrintWriter out, HttpServletRequest request) {
//		 
//	 }
//
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public shop() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		session = request.getSession();
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		session = request.getSession();
//		response.setContentType("text/html;charset=UTF-8");
//		  PrintWriter out = response.getWriter();
//
//		  try {
//		   // overime, ci nenastala niektora z chyb, ktore brania pokracovaniu
//		     String operacia = request.getParameter("operacia");
//		     if (badConnection(out) || badOperation(operacia, out)) return;
//		     if (operacia.equals("login")) {
////		         if (!uspesneOverenie(request, out)) {
////		              vypisNeopravnenyPristup(out);
////		              return;
////		         }
//		     }
//
//		     int id = getLogedUser(request, out);
//		     if (id == 0) return;
//		     // spracovanie údajov z formulárov podľa operácie
//
//		     createHeader(out, request);
//		     createBody(out, request);
//		     createFooter(out, request);
//		  } catch (Exception e) {out.println(e);}
//
//		// TODO Auto-generated method stub
////		doGet(request, response);
//	}
//
//}

