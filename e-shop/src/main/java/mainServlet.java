import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class mainServlet
 */

public class mainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Connection con;
	 String errorMessage = "";

	 @Override
	 public void init() throws ServletException {
	     super.init();
	     try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        con = DriverManager.getConnection(
	              "jdbc:mysql://localhost/E_SHOP_TG","root","");
	     } catch (Exception e) { errorMessage = e.getMessage();}
	 }

	 protected void doGet(HttpServletRequest request, 
	  HttpServletResponse response) throws ServletException, 
	  IOException {
	     doPost(request, response);
	 }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	 protected void doPost(HttpServletRequest request, HttpServletResponse 
			 response) throws ServletException, IOException {
			   response.setContentType("text/html;charset=UTF-8");
			   PrintWriter out = response.getWriter();

			   try {
			    // overime, ci nenastala niektora z chyb, ktore brania pokracovaniu
			      String operacia = request.getParameter("operacia");
		          if (badConnection(out) || badOperation(operacia, out)) return;
			      Components components = new Components(out);
			      if (operacia.equals("login")) {
			    	  CheckUser(out, request);
		          }
			      if(operacia.equals("register")) {
			    	  if(validateRegistration(out, request)) {
			    		  response.sendRedirect("index.html");
			    	  }else {
			    		  response.sendRedirect("register.html?userExists"); 
			    	  }
			      }
			      if(operacia.equals("addToCart")) {
			    	  addToCart(out, request, components);
			      }
			      if(operacia.equals("showOrders")) {
			    	  components.createHeader(out, request.getSession());
			    	  showOrders(out, request, components);
			    	  components.createFooter(out);
			    	  return;
			      }
			      if(operacia.equals("logout")) {
			    	  logout(out, request);
			    	  response.sendRedirect("index.html");
			      }
			      
			      
			      int user_id = getUserID(request);
		            if (user_id == 0) {
		            	response.sendRedirect("index.html");
		            	vypisNeopravnenyPristup(out); return;
		            }
		            
		            components.createHeader(out, request.getSession());
		            showItems(out, request, components);
		            components.createFooter(out);

			      /*int id = getLogedUser(request, out);
			      if (id == 0) return;*/
			     
			   } catch (Exception e) {out.println(e);}
			   finally {}
			 }
	 
	 private boolean badConnection(PrintWriter out) {
		    if (errorMessage.length() > 0) {
		        out.println(errorMessage);
		        return true;
		    }
		    return false;
		}
	 
	 private boolean badOperation(String operacia, PrintWriter out) throws IOException {
		   if (operacia == null) {
			  
		       vypisNeopravnenyPristup(out);
		       return true;
		   }
		   return false;
		}
	 
	 protected void CheckUser(PrintWriter out, HttpServletRequest request) {
	        try {
	            String email = request.getParameter("email");
	            String password = request.getParameter("password");
	            Statement stmt = con.createStatement();
	            String sql = "SELECT MAX(id) AS iid, COUNT(id) AS count FROM users "+
	                    " WHERE email = '"+email+"' AND password = '"+password+"'";
	            ResultSet rs = stmt.executeQuery(sql);
	            rs.next();
	            HttpSession session = request.getSession();
	            if (rs.getInt("count") == 1) { // ak je user s menom a heslo jeden - OK
	            	sql = "SELECT * FROM users WHERE email = '"+email+"'"; 
	            	rs = stmt.executeQuery(sql); // prečítam jeho údaje z db
	            	rs.next(); // nastavím sa na prvý načítaný záznam
	            	session.setAttribute("ID", rs.getInt("id")); // a do session povkladám data
	            	session.setAttribute("name", rs.getString("first_name") + " " +rs.getString("last_name"));
	            	session.setAttribute("is_admin", rs.getString("is_admin"));
	            } else { // ak je userov 0 alebo viac – autorizacia sa nepodarila
	            	out.println("Prihlasovacie údaje nie sú v poriadku.");
	            	session.invalidate(); // vymazem session
	            }
	            rs.close();
	            stmt.close();
	        }
	        catch (Exception ex) {
	        	out.println(ex.getMessage());
	        }
	   }
	 protected int getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer id = (Integer)(session.getAttribute("ID"));
        if (id == null) id = 0;
        return id;
	 }

	 private int getLogedUser(HttpServletRequest request, PrintWriter out) {
	   HttpSession ses = request.getSession();
	   int id = (Integer)ses.getAttribute("id");
	   if (id == 0) {
	       out.println("Neprihlásený user");
	       vypisNeopravnenyPristup(out);
	   }
	   return id;
	 }
	 private void logout(PrintWriter out, HttpServletRequest request) {
		HttpSession ses = request.getSession();
		ses.invalidate();
	 }

	 protected void vypisNeopravnenyPristup(PrintWriter out) {
	        out.println("Nemáš právo tu byť...");
	 }
	 
	 protected void showItems(PrintWriter out, HttpServletRequest request, Components components) throws SQLException {
		 Statement stmt = con.createStatement();
		 String sql = "SELECT * FROM storage";
		 ResultSet rs = stmt.executeQuery(sql);
		 components.showItems(out, rs);
		 
	 }
	 protected void addToCart(PrintWriter out, HttpServletRequest request, Components components) throws SQLException {
		 int user_id = Integer.parseInt(request.getSession().getAttribute("ID").toString());
		 int stock_id = Integer.parseInt(request.getParameter("item_id"));
		 int price = Integer.parseInt(request.getParameter("price"));
		 try {
		   Statement stmt = con.createStatement();
		   // zistim, ci uz tovar je u daného usera v kosiku
		   String sql = "SELECT count(id) AS count FROM carts WHERE " 
		          + "(user_id='"+user_id+"') AND (stock_id ='" 
		          +stock_id+"')";
		   ResultSet rs = stmt.executeQuery(sql);
		   rs.next();
		   int count = rs.getInt("count");
		   if (count == 0) {
			  // ak nie vlozim ho ko nový záznam
			  sql = "INSERT INTO carts (user_id, stock_id, price, count) values ("+
			        "'" + user_id + "', "+
			        "'" + stock_id + "', "+
			        "'" + price + "', "+
			        "'1') ";
			  int rowsAffected = stmt.executeUpdate(sql);

			    if (rowsAffected > 0) {
			        components.showSuccess(out, "Item added to the cart successfully!");
			    } else {
			        out.print("Item not found in the cart.");
			    }
			}else { // ak sa tovar u usera nachádza, len zvysim count ks
				sql = "UPDATE carts SET count = count + 1, price ='"+price+"' WHERE "+
				"(user_id='"+user_id+"') AND (stock_id ='" 
				+ stock_id+"')";
				int rowsAffected = stmt.executeUpdate(sql);

			    if (rowsAffected > 0) {
			        components.showSuccess(out, "Item added to the cart successfully!");
			    } else {
			        out.print("Item not found in the cart.");
			    }
			} 
			rs.close();
			stmt.close();
		} catch (Exception e) {}
	 }
	 
	 protected void showOrders(PrintWriter out, HttpServletRequest request, Components components) throws SQLException {
		 Statement stmt = con.createStatement();
		 String sql = "SELECT * FROM orders WHERE user_id =" + request.getSession().getAttribute("ID");
		 ResultSet rs = stmt.executeQuery(sql);
		 while(rs.next()) {
			 Statement stmtItem = con.createStatement();
			 String sqlItem = "SELECT storage.name, ordered_items.price, ordered_items.count FROM ordered_items JOIN storage ON ordered_items.stock_id = storage.id WHERE order_id = " + rs.getString("id");
			 ResultSet rsItem = stmtItem.executeQuery(sqlItem);
			 out.println(" <div\r\n"
				 		+ "    class=\"border text-card-foreground w-full md:w-1/2 mx-auto mt-4 bg-white shadow-lg rounded-lg overflow-hidden\"\r\n"
				 		+ "    data-v0-t=\"card\"\r\n"
				 		+ "  >\r\n"
				 		+ "    <div class=\"flex-col space-y-1.5 flex justify-between items-center p-6\">\r\n"
				 		+ "      <h3 class=\"tracking-tight font-semibold text-lg\">Order Summary</h3>\r\n"
				 		+ "      <div class=\"flex items-center\">\r\n"
				 		+ "        <span class=\"text-gray-400 mr-2\">"+rs.getString("order_num")+"</span>\r\n"
				 		+ "        <div class=\"inline-flex items-center rounded-full border text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 border-transparent bg-primary text-primary-foreground hover:bg-primary/80 h-6 w-auto p-2\">\r\n"
				 		+ "         "+rs.getString("date")
				 		+ "        </div>\r\n"
				 		+ "      </div>\r\n"
				 		+ "    </div>\r\n"
				 		+ "    <div class=\"p-6\">\r\n"
				 		+ "      <div class=\"px-6 py-4\">\r\n"
				 		+ "        <div class=\"flex justify-between pb-4 border-b border-gray-200 mb-4\">\r\n"
				 		+ "          <div class=\"font-semibold text-lg\">Item Details</div>\r\n"
				 		+ "        </div>\r\n");
				 			components.orderItems(out, rsItem);
				 		out.println("      </div>\r\n"
				 		+ "    </div>\r\n"
				 		+ "    <div class=\"p-6 flex justify-between items-center px-6 py-4\">\r\n"
				 		+ "      <div class=\"font-semibold text-lg\">Total</div>\r\n"
				 		+ "      <div class=\"font-semibold text-lg\">"+rs.getString("price_total")+"€</div>\r\n"
				 		+ "    </div>\r\n"
				 		+ "  </div>");
				 		stmtItem.close();
		 }
		 stmt.close();
	 }
	 protected boolean validateRegistration(PrintWriter out, HttpServletRequest request) {
	        try {
				Statement stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery("SELECT * FROM users "+
		                " WHERE email = '"+request.getParameter("email")+"'");
		        if(!rs.next()) {
		        	String sql = "INSERT INTO `users`(email,password,first_name,last_name,address,description,is_admin) "
					+ "VALUES('"+request.getParameter("email")+"', '"+request.getParameter("password")+"', '"+request.getParameter("first_name")+"', '"+request.getParameter("last_name")+"', '"+request.getParameter("address")+"',"
							+ "'',0)";
		        	stmt.executeUpdate(sql);
		        	return true;
		        }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;

	 }
}
