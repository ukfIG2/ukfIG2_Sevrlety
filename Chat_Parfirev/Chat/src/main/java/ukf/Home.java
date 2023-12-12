package ukf;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Statement stm;
	Connection con;
	PrintWriter out;
       
    public Home() {
        super();
    }
    
    public void init(ServletConfig config){
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		con = DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "");
    		stm = con.createStatement();
    		System.out.println("Connected");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		String operation = request.getParameter("operation");
		writeHead(response);
		if(con == null) {
			out.println("<h3>Žiaľ, aktuálne máme problém z databázou.</h3><p>Preto aktualne čet nefunguje. Kontaktujte prosím administratora.</p>");
			return;
		} else if(operation == null && session.isNew()) {
			try {
				write403();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (operation.equals("login")) {
			try {
				ResultSet rs = stm.executeQuery("SELECT COUNT(*), id, nickname FROM users WHERE "
						+ "nickname = '" + request.getParameter("login") + "' AND "
						+ "password = '" + request.getParameter("password") + "';");
				rs.next();
				if(rs.getInt("COUNT(*)") == 1) {
					session.setAttribute("ID", rs.getInt("id"));
					session.setAttribute("nickname", rs.getString("nickname"));
					System.out.println("Prihalseny");
					writeChat(request, session, response);
				} else{
					out.println("Nesprávne prihasováce údaje, <a href=\"index.html\">zopakujte prihlasenie</a> ešte raz.");
					System.out.println("Neprihlaseny");
					session.invalidate();
				};
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if(operation.equals("message")) {
			try {
				stm.execute("INSERT INTO messages VALUES (null, " + session.getAttribute("ID").toString() + ", '" + request.getParameter("message" )+ "', now());");
				writeChat(request, session, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if(operation.equals("logout")) {
			session.invalidate();
			out.print("<h2>Odhlasili ste sa</h2>");
		} else if(operation.equals("ban")) {
			try {
				stm.execute("INSERT INTO bans VALUES (" + session.getAttribute("ID").toString() + ", '" + request.getParameter("user")+ "');");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				writeChat(request, session, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(operation.equals("unban")) {
			try {
				stm.execute("DELETE FROM bans WHERE users_id = " + session.getAttribute("ID").toString() + " AND blocked_user = " + request.getParameter("user")+ ";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writeChat(request, session, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			write403();
		}
		writeFooter(response, session);
	}

	
	void write403() {
		out.println("<h3>Zdá sa, že dostali ste sa sem omýlom.</h3><p>Overte správnosť linku alebo <a href=\"index.html\">prihlaste sa</a>.</p>");
	}
	
	void writeChat(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, SQLException {
		out = response.getWriter();
		out.print("	<div class=\"display: flex; flex-direction: row;\">"
				+ "		<h2>Čet</h2>\n"
				+ "		<a href=\"Home?operation=logout\"><button class=\"btn btn-primary mb-3\">Odhlasiť sa</button></a>"
				+ "		<a><button class=\"btn btn-primary mb-3\" data-bs-toggle=\"modal\" data-bs-target=\"#unban\">Unban</button></a>"
				+ "	</div>");
		out.print("	<form class=\"row g-3\" action=\"Home\">\n"
				+ "		<input type=\"hidden\" name=\"operation\" value=\"message\">\n"
				+ "		<div class=\"mb-3\">\n"
				+ "			<label for=\"message\" class=\"form-label\">Správa</label>\n"
				+ "			<textarea class=\"form-control\" id=\"message\" name=\"message\" rows=\"3\"></textarea>\n"
				+ "		</div>\n"
				+ "		<button type=\"submit\" class=\"btn btn-primary mb-3\">Odoslať</button>\n"
				+ "	</form>\n");
		out.print("	<div class\"row\">");
		ResultSet bans = stm.executeQuery("SELECT * FROM bans WHERE users_id = " + session.getAttribute("ID").toString() + ";");
		HashSet<String> banlist = new HashSet();
		while(bans.next()) banlist.add(bans.getString("blocked_user"));
		ResultSet messages = stm.executeQuery("SELECT * FROM messages INNER JOIN users ON users.id = messages.users_id ORDER BY messages.id DESC;");
		while(messages.next()) {
			if (!Arrays.asList(banlist.toArray()).contains(messages.getString("users_id")))
			out.print("		<p><a href=\"Home?operation=ban&user=" + messages.getString("users_id") + "\">-</a>"
					+ " <b>" + messages.getString("datetime") + "</b> <b style=\"color:#" + messages.getString("color_hex")+ ";\">@" 
					+ messages.getString("nickname") + ": </b>" +  messages.getString("text") + "</p>");
		}
		out.print("	</div>");
		
	}
	
	void writeHead(HttpServletResponse response) throws IOException {
		out = response.getWriter();
		out.print("<html>\n"
				 + "<head>\n"
				 + "	<meta charset=\"UTF-8\">\r\n"
				 + "	<title>Čet</title>\r\n"
				 + "	<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css\"\r\n"
				 + "	rel=\"stylesheet\" integrity=\"sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN\" crossorigin=\"anonymous\">\r\n"
				 + "	<link rel=\"stylesheet\" src=\"main.css\">\n"
				 + "</head>\n"
				 + "<body>"
				 + "<div class=\"container\">");
	}
	
	void writeFooter(HttpServletResponse response, HttpSession session) throws IOException {
		out = response.getWriter();
		out.print("<!-- Unban modal -->\r\n"
				+ "<div class=\"modal fade\" id=\"unban\" tabindex=\"-1\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\r\n"
				+ "  <div class=\"modal-dialog\">\r\n"
				+ "    <div class=\"modal-content\">\r\n"
				+ "      <div class=\"modal-header\">\r\n"
				+ "        <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\r\n"
				+ "      </div>\r\n"
				+ "      <div class=\"modal-body\">\r\n"
				+ "      	<form action=\"Home\">"
				+ "				<input type=\"hidden\" name=\"operation\" value=\"unban\">");
		try {
			ResultSet bans = stm.executeQuery("SELECT * FROM bans INNER JOIN users ON users.id = bans.blocked_user "
					+ "WHERE users_id = " + session.getAttribute("ID").toString() + ";");
			while(bans.next()) {
				out.print("<input id=\"" + bans.getString("blocked_user") + "\"type=\"radio\" name=\"user\" value=\"" + bans.getString("blocked_user") + "\">"
						+ "<label for=\"" + bans.getString("blocked_user") + "\">" + bans.getString("nickname") + "</label>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.print("				<input class=\"btn btn-primary mb-3\" type=\"submit\">"
				+ "		 	</form>"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </div>\r\n"
				+ "</div>"
				+ "</div>"
				+ "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js\" "
				+ "integrity=\"sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL\" crossorigin=\"anonymous\"></script>"
				+ "</body>\n"
				+ "</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
