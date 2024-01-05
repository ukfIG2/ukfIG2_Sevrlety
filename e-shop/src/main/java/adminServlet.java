

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class adminServlet
 */
public class adminServlet extends HttpServlet {
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
		String isAdminString = (String) request.getSession().getAttribute("is_admin");
		boolean is_admin = (isAdminString != null && isAdminString.equals("1"));
		PrintWriter out = response.getWriter();
		if(!is_admin) {
			out.println("No access");
			return;
		}
		try {
			Components components = new Components(out);
			String operacia = request.getParameter("operacia");
			if(operacia.equals("deleteOrder")) {
				deleteOrder(out, Integer.parseInt(request.getParameter("order_id")), components);
			}
			if(operacia.equals("changeOrderState")) {
				changeOrderState(out, request, components);
			}
			if(operacia.equals("deleteUser")) {
				deleteUser(out, request.getParameter("user_id"), components);
			}
			if(operacia.equals("changeUserRole")) {
				changeUserRole(out, request.getParameter("user_id"),request.getParameter("new_role"), components);
			}
			components.createHeader(out, request.getSession());
			showOrdersTable(out, request);
			showUsersTable(out, request);
			components.createFooter(out);
          
       	   
       	   
       }
       catch (Exception e) {
       	out.println(e);
       }
	}
	protected void deleteOrder(PrintWriter out, Integer order_id, Components components) {
		 try {
		        String deleteOrderedItemsSQL = "DELETE FROM ordered_items WHERE order_id = ?";
		        try (PreparedStatement pstmtOrderedItems = con.prepareStatement(deleteOrderedItemsSQL)) {
		            pstmtOrderedItems.setInt(1, order_id);
		            pstmtOrderedItems.executeUpdate();
		        }

		        String deleteOrderSQL = "DELETE FROM orders WHERE id = ?";
		        try (PreparedStatement pstmtOrder = con.prepareStatement(deleteOrderSQL)) {
		            pstmtOrder.setInt(1, order_id);
		            pstmtOrder.executeUpdate();
		        }

		        components.showSuccess(out,"Order and related items successfully deleted.");
		    } catch (SQLException e) {
		        out.println("Error deleting order: " + e.getMessage());
		        e.printStackTrace();
		    }
	}
	
	protected void changeOrderState(PrintWriter out, HttpServletRequest request, Components components) {
	    try {
	        String order_id = request.getParameter("order_id");
	        String new_state = request.getParameter("order_state");

	        String updateOrderStateSQL = "UPDATE orders SET state = ? WHERE id = ?";
	        try (PreparedStatement pstmtUpdateState = con.prepareStatement(updateOrderStateSQL)) {
	            pstmtUpdateState.setString(1, new_state);
	            pstmtUpdateState.setString(2, order_id);
	            int rowsAffected = pstmtUpdateState.executeUpdate();

	            if (rowsAffected > 0) {
	                components.showSuccess(out, "Order state successfully updated.");
	            } else {
	                out.println("No order found for the specified order_id.");
	            }
	        }
	    } catch (NumberFormatException e) {
	        out.println("Invalid order_id format. Please provide a valid integer.");
	    } catch (SQLException e) {
	        out.println("Error updating order state: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	protected void showOrdersTable(PrintWriter out, HttpServletRequest request) throws SQLException {
		Statement stmt = con.createStatement();
		String sql = "SELECT * from orders join users on orders.user_id = users.id";
		ResultSet rs = stmt.executeQuery(sql);
		out.println("<div class=\"relative overflow-x-auto mt-8 rounded-xl\">\r\n"
				+ "<div class='text-xl font-bold text-center'>Orders</div>"
				+ "    <table class=\"w-3/4 m-auto p-16 text-sm text-left rtl:text-right text-gray-500 \">\r\n"
				+ "        <thead class=\"text-xs text-gray-700 uppercase bg-blue-500\">\r\n"
				+ "            <tr>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Order number\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    date\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    User\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Price\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    State\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Action\r\n"
				+ "                </th>\r\n"
				+ "            </tr>\r\n"
				+ "        </thead>\r\n"
				+ "        <tbody>\r\n");
				while(rs.next()) {
					out.println("<tr class=\"bg-white border-b \">\r\n"
							+ "                <th scope=\"row\" class=\"px-6 py-4 font-medium text-gray-900 whitespace-nowrap\">\r\n"
							+ rs.getString("order_num")						 
							+ "                </th>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n"
							+ rs.getString("date")
							+ "                </td>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n"
							+ rs.getString("first_name") + " " + rs.getString("last_name")
							+ "                </td>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n"
							+ rs.getString("price_total")
							+ "                </td>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n"
							+ "<form action='adminServlet' >"
							+ "<input type='hidden' name='operacia' value='changeOrderState'>"
							+ "<input type='hidden' name='order_id' value='"+rs.getString("id")+"'>"
							+ "	<select name='order_state' id=\"countries\" onchange=\"this.form.submit()\" class=\"bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5\">\r\n"
							+ "	 <option selected>--" + rs.getString("state") + "--</option>"
							+ "  <option value=\"Prepairing\">Prepairing</option>\r\n"
							+ "  <option value=\"Shipped\">Shipped</option>\r\n"
							+ "  <option value=\"Delivered\">Delivered</option>\r\n"
							+ "</select>"
							+ "</form>"
							+ "                </td>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n");
					out.println("<form action='adminServlet' method=\"post\" class='m-0'>"
							+ "<input type='hidden' name='operacia' value='deleteOrder'>"
							+ "<input type='hidden' name='order_id' value='"+ rs.getString("id") +"'>"
							+ "<button type=\"submit\" class=\"text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm w-full sm:w-auto px-3 py-1 text-center\">Delete</button></form>"
							+ "</td>\r\n"
							+ " </tr>\r\n"
							);
							
				}
				out.println("</tbody>\r\n"
				+ "    </table>\r\n"
				+ "</div>");
	}
	
	protected void deleteUser(PrintWriter out, String userId, Components components) {
	    try {
	        Integer userIdInt = Integer.parseInt(userId);
	        String deleteOrdersSQL = "DELETE FROM orders WHERE user_id = ?";
	        try (PreparedStatement pstmtDeleteOrders = con.prepareStatement(deleteOrdersSQL)) {
	            pstmtDeleteOrders.setInt(1, userIdInt);
	            pstmtDeleteOrders.executeUpdate();
	        }

	        String deleteUserSQL = "DELETE FROM users WHERE id = ?";
	        try (PreparedStatement pstmtDeleteUser = con.prepareStatement(deleteUserSQL)) {
	            pstmtDeleteUser.setInt(1, userIdInt);
	            int rowsAffected = pstmtDeleteUser.executeUpdate();

	            if (rowsAffected > 0) {
	                components.showSuccess(out,"User successfully deleted.");
	            } else {
	                out.println("No user found for the specified user_id.");
	            }
	        }
	    } catch (NumberFormatException e) {
	        out.println("Invalid user_id format. Please provide a valid integer.");
	    } catch (SQLException e) {
	        out.println("Error deleting user: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	protected void changeUserRole(PrintWriter out, String userId, String newRole, Components components) {
	    try {
	        Integer userIdInt = Integer.parseInt(userId);
	        String sql = "UPDATE users SET is_admin = ? WHERE id = ?";
	        try (PreparedStatement pstmtUpdateRole = con.prepareStatement(sql)) {
	            pstmtUpdateRole.setString(1, newRole);
	            pstmtUpdateRole.setInt(2, userIdInt);
	            int rowsAffected = pstmtUpdateRole.executeUpdate();

	            if (rowsAffected > 0) {
	                components.showSuccess(out, "User role successfully updated.");
	            } else {
	                out.println("No user found for the specified user_id.");
	            }
	        }
	    } catch (NumberFormatException e) {
	        out.println("Invalid user_id format. Please provide a valid integer.");
	    } catch (SQLException e) {
	        out.println("Error updating user role: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	protected void showUsersTable(PrintWriter out, HttpServletRequest request) throws SQLException {
		Statement stmt = con.createStatement();
		String sql = "SELECT * from users";
		ResultSet rs = stmt.executeQuery(sql);
		out.println("<div class=\"relative overflow-x-auto mt-8 rounded-xl\">\r\n"
				+ "<div class='text-xl font-bold text-center'>Users</div>"
				+ "    <table class=\"w-3/4 m-auto p-16 text-sm text-left rtl:text-right text-gray-500 \">\r\n"
				+ "        <thead class=\"text-xs text-gray-700 uppercase bg-blue-500\">\r\n"
				+ "            <tr>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Email\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Name\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Address\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Role\r\n"
				+ "                </th>\r\n"
				+ "                <th scope=\"col\" class=\"px-6 py-3\">\r\n"
				+ "                    Action\r\n"
				+ "                </th>\r\n"
				+ "            </tr>\r\n"
				+ "        </thead>\r\n"
				+ "        <tbody>\r\n");
				while(rs.next()) {
					boolean isAdmin = rs.getBoolean("is_admin");
					String role = (rs.wasNull() || !isAdmin) ? "User" : "Admin";
					out.println("<tr class=\"bg-white border-b \">\r\n"
							+ "                <th scope=\"row\" class=\"px-6 py-4 font-medium text-gray-900 whitespace-nowrap\">\r\n"
							+ rs.getString("email")						 
							+ "                </th>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n"
							+ rs.getString("first_name") + " " + rs.getString("last_name")
							+ "                </td>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n"
							+ rs.getString("address")
							+ "                </td>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n"
							+ "<form action='adminServlet' >"
							+ "<input type='hidden' name='operacia' value='changeUserRole'>"
							+ "<input type='hidden' name='user_id' value='"+rs.getString("id")+"'>"
							+ "	<select name='new_role' id=\"countries\" onchange=\"this.form.submit()\" class=\"bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5\">\r\n"
							+ "	 <option value="+rs.getString("is_admin") + " selected>--" + role  + "--</option>"
							+ "  <option value=\"1\">Admin</option>\r\n"
							+ "  <option value=\"0\">User</option>\r\n"
							+ "</select>"
							+ "</form>"
							+ "                </td>\r\n"
							+ "                <td class=\"px-6 py-4\">\r\n");
					out.println("<form action='adminServlet' method=\"post\" class='m-0'>"
							+ "<input type='hidden' name='operacia' value='deleteUser'>"
							+ "<input type='hidden' name='user_id' value='"+ rs.getString("id") +"'>"
							+ "<button type=\"submit\" class=\"text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm w-full sm:w-auto px-3 py-1 text-center\">Delete</button></form>"
							+ "</td>\r\n"
							+ " </tr>\r\n"
							);
							
				}
				out.println("</tbody>\r\n"
				+ "    </table>\r\n"
				+ "</div>");
	}

}
