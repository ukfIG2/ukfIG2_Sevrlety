

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
import java.util.UUID;

/**
 * Servlet implementation class cartServlet
 */
public class cartServlet extends HttpServlet {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			Components components = new Components(out);
			String operacia = request.getParameter("operacia");
			if(operacia.equals("deleteItem")) {
				deleteItem(out, request, components);
			}
			if(operacia.equals("createOrder")) {
				createOrder(out,request);
			}
			components.createHeader(out, request.getSession());
			showCart(out, request, components);
			components.createFooter(out);
           
        	   
        	   
        }
        catch (Exception e) {
        	out.println(e);
        }
	}
	
	protected int getUserID(HttpServletRequest request) {
		 HttpSession session = request.getSession();
		 Integer id = (Integer) (session.getAttribute("ID"));
		 if(id == null)
			 id=0;
		 return id; 
	}
	protected void deleteItem(PrintWriter out, HttpServletRequest request, Components components) throws SQLException {
		Statement stmt = con.createStatement();
	    String sql = "DELETE FROM carts WHERE user_id = " + request.getSession().getAttribute("ID")
	            + " AND stock_id = " + request.getParameter("item_id");
	    
	    int rowsAffected = stmt.executeUpdate(sql);

	    if (rowsAffected > 0) {
	        components.showSuccess(out, "Item removed from the cart successfully!");
	    } else {
	        out.print("Item not found in the cart.");
	    }
	}
	
	protected void showCart(PrintWriter out, HttpServletRequest request, Components components) throws SQLException {
		Statement stmt = con.createStatement();
		 String sql = "SELECT c.stock_id,s.image, s.name, c.price, c.count FROM carts c JOIN storage s ON c.stock_id = s.id WHERE c.user_id = "+request.getSession().getAttribute("ID");
		 ResultSet rs = stmt.executeQuery(sql);
		 out.println(
				"<div class=\"grid sm:px-10 lg:px-20 xl:px-32\">"
				+ "  <div class=\"pt-8\">"
				+ "    <p class=\"text-xl font-medium\">Order Summary</p>"
				+ "    <p class=\"text-gray-400\">Check your items.</p>"
				+ "    <div class=\"bg-white rounded-lg shadow-md p-6 mb-4\">"
				+ "	   <table class=\"w-full\">"
				+ "      <thead>"
				+ "          <tr>"
				+ "              <th class=\"text-left font-semibold\">Product</th>"
				+ "              <th class=\"text-left font-semibold\">Price</th>"
				+ "              <th class=\"text-left font-semibold\">Quantity</th>"
				+ "              <th class=\"text-left font-semibold\">Total</th>"
				+ "          </tr>"
				+ "      </thead>"
				+ "    <tbody>");
		 			components.cartItems(out, rs);
						
				out.println("</tbody>"
				+ "    </table>"
				+ "  </div>"
				+ ""
				+ "  </div>"
				+ "  <div class=\"bg-gray-50 px-4 pt-8 lg:mt-0\">"
				+ "      <!-- Total -->");
				out.println("<div class=\"mt-6 flex items-center justify-between\">");
				out.println("<p class=\"text-xl font-bold text-gray-900\">Total price</p>");
				String sql2 = "SELECT SUM(price) as total from carts where user_id = " + request.getSession().getAttribute("ID") ;
				ResultSet rs2 = stmt.executeQuery(sql2);
				if(rs2.next()) {
					Integer total = rs2.getInt("total");
					out.println( "<p class=\"text-2xl font-semibold text-gray-900\">"+total.toString() +"€</p>");
					out.println(""
							+ "     </div>"
							+ "    </div>"
							+ "    <form action='cartServlet'>"
							+ "    <input type='hidden' name='operacia' value='createOrder'>"
							+ "    <input type='hidden' name='total' value='"+total.toString()+"'>"
							+ "    <button type='submit' class=\"mt-4 mb-8 w-full rounded-md bg-gray-900 px-6 py-3 font-medium text-white\">Place Order</button>"
							+ "    </form>"
							+ "  </div>"
							+ "</div>");		
				}
				rs2.close();
				stmt.close();
				
	}
	
	protected void createOrder(PrintWriter out, HttpServletRequest request) {
		int user_id = getUserID(request);
		if (user_id == 0) 
			return;
		try {
			synchronized (this) {
				if (onStock(user_id) == null) {
					String orderNum = newOrderNum(); 
					confirmOrder(user_id, orderNum, request.getParameter("total"));
					
				}
				else {
					out.print("<h2 style='color:red;'>"+onStock(user_id)+"</h2>");
				}
		} 
		}catch(Exception e) {
			out.println(e);
		}
	}
	
	protected String onStock(int user_id) {
		try {
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM carts INNER JOIN storage ON carts.stock_id = storage.id WHERE user_id="+user_id);
	        /*if(!rs.next())
	        	return("You have nothing in cart");*/
	        while(rs.next()) {
	        	if(rs.getInt("storage.count") < rs.getInt("carts.count"))
	        		return("Item "+rs.getString("storage.name")+" is out of stock");
	        }
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected String newOrderNum() {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		return uuidAsString;
	}
	
	protected void confirmOrder(int user_id, String order_number,String total) {
		try {
			Statement stmt = con.createStatement();
			Statement instmt = con.createStatement();
			String sql = "INSERT INTO orders(order_num, date, user_id, price_total, state) VALUES('"+order_number+"', NOW(), "+user_id+", "+total+", 'processing')";
			stmt.executeUpdate(sql);
			ResultSet rs = stmt.executeQuery("SELECT id FROM orders WHERE order_num='"+order_number+"'");
			rs.next(); int order_id = rs.getInt("id");
			stmt.executeUpdate("INSERT INTO ordered_items(order_id, stock_id, price, count) SELECT "+order_id+",carts.stock_id, carts.price, carts.count FROM carts WHERE user_id="+user_id+"");
			rs = stmt.executeQuery("SELECT * FROM storage INNER JOIN carts ON storage.id = carts.stock_id WHERE carts.user_id ="+user_id);
			while(rs.next()) {
				instmt.executeUpdate("UPDATE storage SET count="+(rs.getInt("storage.count")-rs.getInt("carts.count"))+" WHERE storage.id="+rs.getInt("carts.stock_id"));
			}
			stmt.executeUpdate("DELETE FROM carts WHERE user_id="+user_id);
			stmt.close();
			instmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
