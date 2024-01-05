import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;

public class Components extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Components(PrintWriter out) {
		out.println("<script src=\"https://cdn.tailwindcss.com\"></script>");
		out.println("<script>const closePopUp = () => document.getElementById('popup').remove();</script>");
		out.println(
				"<script>"
				+ "function toggleUserDropDown(){\n"
				+ "let dropDown = document.getElementById('userDropDown')\n"
				+ "if (dropDown.style.visibility === \"hidden\") {\r\n"
				+ "            dropDown.style.visibility = \"visible\";\r\n"
				+ "        } else {\r\n"
				+ "            dropDown.style.visibility = \"hidden\";\r\n"
				+ "        }"
				+ "}\n"
				+ "</script>");
	}
	public void createHeader(PrintWriter out, HttpSession session) {
		String role = ("0".equals(session.getAttribute("is_admin"))) ? "User" : "Admin";
		out.println("<header class='w-full justify-center items-center h-16 flex bg-blue-800'>");
		out.println("<div class='w-3/4 flex justify-between'>");
		out.println("<a href='mainServlet?operacia' class='text-white text-xl font-bold cursor-pointer hover:text-blue-500'>E-shop</a>");
		out.println("<div class=\"flex gap-4 relative\">"
						+ "<a class=\"flex mr-6 items-center\" href=\"cartServlet?operacia=showCart\">"
						+ "<svg xmlns=\"http://www.w3.org/2000/svg\" class=\"text-white hover:text-blue-500\" fill=\"none\" viewBox=\"0 0 24 24\" width=\"32\" height=\"32\" stroke=\"currentColor\">"
						+ "<path stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"2\" d=\"M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z\" />"
						+ "</svg>"
						+ "</a>"
						+ "<button onclick=\"toggleUserDropDown()\">"
						+ "<svg class=\"text-white hover:text-blue-500\" viewBox=\"0 0 32 32\" width=\"32\" height=\"32\" stroke=\"currentColor\" xmlns=\"http://www.w3.org/2000/svg\">"
						+ "<path stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"2\" d=\"M16,29A13,13,0,1,1,29,16,13,13,0,0,1,16,29ZM16,5A11,11,0,1,0,27,16,11,11,0,0,0,16,5Z\"/><path stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"2\" d=\"M16,17a5,5,0,1,1,5-5A5,5,0,0,1,16,17Zm0-8a3,3,0,1,0,3,3A3,3,0,0,0,16,9Z\"/><path stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"2\" d=\"M25.55,24a1,1,0,0,1-.74-.32A11.35,11.35,0,0,0,16.46,20h-.92a11.27,11.27,0,0,0-7.85,3.16,1,1,0,0,1-1.38-1.44A13.24,13.24,0,0,1,15.54,18h.92a13.39,13.39,0,0,1,9.82,4.32A1,1,0,0,1,25.55,24Z\"/></g></g></svg>"
						+ "</button>"
						+ "<div id=\"userDropDown\" style=\"visibility:hidden;\" class=\"absolute px-2 top-6 right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none\" role=\"menu\" aria-orientation=\"vertical\" aria-labelledby=\"menu-button\" tabindex=\"-1\">"
						+ "    <div class=\"py-1\" role=\"none\">"
						+ "     <span>Name:</span>"
						+ "      <div class=\"text-gray-700 block px-4 py-2 text-sm\" role=\"menuitem\" tabindex=\"-1\" id=\"menu-item-0\">"+session.getAttribute("name")+"</div>"
						+ "      <span>Role:</span>"
						+ "      <div class=\"text-gray-700 block px-4 py-2 text-sm\" role=\"menuitem\" tabindex=\"-1\" id=\"menu-item-1\">"+ role + "</div>");
						if(role.equals("Admin"))
						out.println( "      <form class=\"hover:bg-blue-200 text-center m-0\" action=\"adminServlet\" method=\"POST\" role=\"none\">"
						+ "		   <input type=\"hidden\" name=\"operacia\" value=\"showOrders\">"
						+ "        <button type=\"submit\" class=\"text-gray-700 block w-full px-4 py-2 text-center text-sm\" role=\"menuitem\" tabindex=\"-1\" id=\"menu-item-3\">Admin Dashboard</button>"
						+ "      </form>");
						out.println("      <form class=\"hover:bg-blue-200 text-center m-0\" action=\"mainServlet\" method=\"POST\" role=\"none\">"
						+ "		   <input type=\"hidden\" name=\"operacia\" value=\"showOrders\">"
						+ "        <button type=\"submit\" class=\"text-gray-700 block w-full px-4 py-2 text-center text-sm\" role=\"menuitem\" tabindex=\"-1\" id=\"menu-item-3\">My Orders</button>"
						+ "      </form>"
						+ "      <form class=\"hover:bg-red-200 text-center m-0\" action=\"mainServlet\" method=\"POST\" role=\"none\">"
						+ "		   <input type=\"hidden\" name=\"operacia\" value=\"logout\">"
						+ "        <button type=\"submit\" class=\"text-gray-700 block w-full px-4 py-2 text-center text-sm\" role=\"menuitem\" tabindex=\"-1\" id=\"menu-item-3\">Sign out</button>"
						+ "      </form>"
						+ "    </div>"
						+ "  </div>"
						
					+ "</div>"
				+ "</div>"
				+ "</header>");
	}
	public void createFooter(PrintWriter out) {
		 out.println("<hr class=\"my-6 border-gray-200 sm:mx-auto dark:border-gray-700 lg:my-8\" />"
	      + "<span class=\"block text-sm text-gray-500 sm:text-center dark:text-gray-400\">© 2023 <a  class=\"hover:underline\">Tomas™</a>. All Rights Reserved.</span>");
				 
	}
	public void showItems(PrintWriter out, ResultSet rs) throws SQLException {
		out.println("<div class='grid md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-4 w-3/4 mx-auto mt-4'>");
		if(rs == null) {
			out.println("Niesu žiadne položky");
			return;
		}
		while(rs.next()) {
			this.item(out, rs);
		}
		out.println("</div>");
	}
	
	
	public void item(PrintWriter out, ResultSet rs) throws SQLException {
		out.println("<div class='border rounded-xl min-h-[350px]'>");
		if(rs.getString("image").toString().length() == 0) {
			out.println("<div class='bg-gray-200 rounded-t-xl w-full h-3/4 text-center flex justify-center items-center'>"
					+ "<svg fill=\"#000000\" width=\"32px\" height=\"32px\" viewBox=\"0 0 32 32\" id=\"icon\" xmlns=\"http://www.w3.org/2000/svg\"><defs><style>.cls-1{fill:none;}</style></defs><title>no-image</title><path d=\"M30,3.4141,28.5859,2,2,28.5859,3.4141,30l2-2H26a2.0027,2.0027,0,0,0,2-2V5.4141ZM26,26H7.4141l7.7929-7.793,2.3788,2.3787a2,2,0,0,0,2.8284,0L22,19l4,3.9973Zm0-5.8318-2.5858-2.5859a2,2,0,0,0-2.8284,0L19,19.1682l-2.377-2.3771L26,7.4141Z\"/><path d=\"M6,22V19l5-4.9966,1.3733,1.3733,1.4159-1.416-1.375-1.375a2,2,0,0,0-2.8284,0L6,16.1716V6H22V4H6A2.002,2.002,0,0,0,4,6V22Z\"/><rect id=\"_Transparent_Rectangle_\" data-name=\"&lt;Transparent Rectangle&gt;\" class=\"cls-1\" width=\"32\" height=\"32\"/></svg>");
			out.println("</div>");
		}else {
			out.println("<img class='w-full h-3/4 rounded-t-xl object-cover' src='" + rs.getString("image")+ "'>");
		}
			//name & price
			out.println("<div class='w-full flex justify-between items-center px-4 my-2'>");
				out.println("<h2 class='font-bold text-xl text-start'>" + rs.getString("name") + "</h2>");
				out.println("<span>" + rs.getString("price") + "€</span>");
			out.println("</div>");
			//add to cart
			out.println("<div class='px-4 flex justify-between items-center'>");
				out.println("<p>"+ rs.getString("count")+"pcs</p>");
				if(rs.getInt("count")!= 0) out.println("<form action='mainServlet' method='post' class='m-0'>"
						+ "<input type='hidden' name='operacia' value='addToCart'>"
						+ "<input type='hidden' name='item_id' value='"+ rs.getString("id") +"'>"
						+ "<input type='hidden' name='price' value='"+ rs.getString("price") +"'>"
						+ "<button type=\"submit\" class=\"text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center\">Add to cart</button></form>");
			out.println("</div>");
		out.println("</div>");
	}
	public void cartItems(PrintWriter out, ResultSet rs) throws SQLException {
		while(rs.next()) {	
			out.println("<tr>"
			+ "<td class=\"py-4\">"
			+ "    <div class=\"flex items-center\">"
			+ "        <img class=\"h-16 w-16 mr-4 object-cover\" src=\""+rs.getString("image")+"\" alt=\"Product image\">"
			+ "        <span class=\"font-semibold truncate\">"+rs.getString("name")+"</span>"
			+ "    </div>"
			+ "</td>"
			+ "<td class=\"py-4\">"+ rs.getString("price")+"€</td>"
			+ "<td class=\"py-4\">"
			+ "    <div class=\"flex items-center\">"
			+ "        <span class=\"text-center w-8\">"+rs.getString("count")+"</span>"
			+ "    </div>"
			+ "</td>"
			+ "<td class=\"py-4\">"+rs.getInt("count")*rs.getInt("price")+"€</td>");
			out.println("<td><form action='cartServlet' class='m-0'>"
					+ "<input type='hidden' name='operacia' value='deleteItem'>"
					+ "<input type='hidden' name='item_id' value='"+ rs.getString("stock_id") +"'>"
					+ "<button type=\"submit\" class=\"text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm w-full sm:w-auto px-3 py-1 text-center\">Delete</button></form>");
		out.println("</td>"
				+ "</tr>");
			}
	}
	
	public void orderItems(PrintWriter out, ResultSet rs) throws SQLException {
		while(rs.next()) {
			out.println("<div class=\"flex justify-between pb-4 border-b border-gray-200 mb-4\">\r\n"
					+ "          <div class=\"font-semibold text-sm\">"+ rs.getString("name") +"</div>\r\n"
					+ "          <div class=\"text-gray-500\">qty: "+ rs.getString("count") +"</div>\r\n"
					+ "          <div class=\"font-semibold text-sm\">" + rs.getString("price") + "€</div>\r\n"
					+ "        </div>\r\n");			
		}
	}
	
	public void showSuccess(PrintWriter out, String message) {
		out.print("<div id='popup' class='bg-green-500 flex justify-center items-center h-12'>" + message
					+ "<button onclick='closePopUp()'>"
					+ "<svg xmlns=\"http://www.w3.org/2000/svg\"  viewBox=\"0 0 128 128\" width=\"24px\" height=\"24px\">    <path d=\"M 64 6 C 48.5 6 33.9 12 23 23 C 12 33.9 6 48.5 6 64 C 6 79.5 12 94.1 23 105 C 34 116 48.5 122 64 122 C 79.5 122 94.1 116 105 105 C 116 94 122 79.5 122 64 C 122 48.5 116 33.9 105 23 C 94.1 12 79.5 6 64 6 z M 64 12 C 77.9 12 90.900781 17.399219 100.80078 27.199219 C 110.70078 36.999219 116 50.1 116 64 C 116 77.9 110.60078 90.900781 100.80078 100.80078 C 90.900781 110.60078 77.9 116 64 116 C 50.1 116 37.099219 110.60078 27.199219 100.80078 C 17.299219 91.000781 12 77.9 12 64 C 12 50.1 17.399219 37.099219 27.199219 27.199219 C 36.999219 17.299219 50.1 12 64 12 z M 50.5625 47.5 C 49.8 47.5 49.05 47.800391 48.5 48.400391 C 47.3 49.600391 47.3 51.499609 48.5 52.599609 L 59.800781 64 L 48.400391 75.300781 C 47.200391 76.500781 47.200391 78.4 48.400391 79.5 C 49.000391 80.1 49.8 80.400391 50.5 80.400391 C 51.2 80.400391 51.999609 80.1 52.599609 79.5 L 64 68.199219 L 75.300781 79.5 C 75.900781 80.1 76.700391 80.400391 77.400391 80.400391 C 78.100391 80.400391 78.9 80.1 79.5 79.5 C 80.7 78.3 80.7 76.400781 79.5 75.300781 L 68.199219 64 L 79.5 52.699219 C 80.7 51.499219 80.699609 49.600391 79.599609 48.400391 C 78.399609 47.200391 76.500391 47.200391 75.400391 48.400391 L 64 59.800781 L 52.699219 48.400391 C 52.099219 47.800391 51.325 47.5 50.5625 47.5 z\"/></svg>"
					+ "</button>"
				+ "</div>");
	}
	
	
	
}
