package ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class mainServlet extends HttpServlet {
	Connection con;
	String errorMessage = "";

	public mainServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/ E_SHOP_MK ", "root", "");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
	}

	private boolean badConnection(PrintWriter out) {
		if (errorMessage.length() > 0) {
			out.println(errorMessage);
			return true;
		}
		return false;
	}

	private boolean badOperation(String operacia, PrintWriter out) {
		if (operacia == null) {
			vypisNeopravnenyPristup(out);
			return true;
		}
		return false;
	}

	private int getLogedUser(HttpServletRequest request, PrintWriter out) {
		HttpSession ses = request.getSession();
		int id = (Integer) ses.getAttribute("id");
		if (id == 0) {
			out.println("Neprihlásený user");
			vypisNeopravnenyPristup(out);
		}
		return id;
	}

	private void createHeader(PrintWriter out, HttpServletRequest request) {
	    out.println("<head>");
	    out.println("<title>Tesco</title>");
	    out.println("<style>");
	    out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; }");
	    out.println("header { background-color: #333; color: #fff; padding: 20px; text-align: center; }");
	    out.println("nav { background-color: #444; padding: 10px; }");
	    out.println("nav a { color: #fff; text-decoration: none; margin-right: 20px; }");
	    out.println("</style>");
	    out.println("</head>");
	    
	    out.println("<body>");

	    out.println("<header>");
	    out.println("<h1>Tesco</h1>");
	    out.println("<nav>");
	    
	    HttpSession ses = request.getSession();
	    String vypis = (String) ses.getAttribute("meno") + " " + (String) ses.getAttribute("priezvisko");
	    out.println("<span>");
	    out.println("<a href='objednavkaServlet'>" + vypis + "</a>");

	    Boolean isAdmin = (Boolean) ses.getAttribute("admin");
	    if (isAdmin != null && isAdmin) {
	        out.println("<a href='adminServlet'>Admin</a>");
	    }

	    out.println("<a href='kosikServlet'>Košík</a>");
	    out.println("<a href='mainServlet?operacia=logout'>Odhlásiť</a>");
	    out.println("</span></nav>");
	    out.println("</header>");
	}

	private void createBody(PrintWriter out, HttpServletRequest request) {
	    HttpSession ses = request.getSession();
	    double aktCena = 0.0;
	    int count = 0;

	    try {
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("select * from sklad");

	        
	        out.println("<div style='display: flex; flex-wrap: wrap; justify-content: center;'>"); 

	        while (rs.next()) {
	            aktCena = rs.getDouble("cena");
	            String imageURL = rs.getString("img");

	            out.println("<div style='width: 50%; padding: 10px;'>"); 
	            out.println("<form action='mainServlet' method='post'>");
	            out.println("<input type='hidden' name='ID' value= '" + rs.getString("ID") + "'>");
	            out.println("<input type='hidden' name='cena' value='" + aktCena + "'>");
	            out.println("<input type='hidden' name='operacia' value='nakup'>");
	            out.println("<strong>" + rs.getString("nazov") + ":</strong>");
	            out.println(aktCena + " EUR<br>");
	            out.println("<img src='" + imageURL + "' width='100' height='100'><br><br>");
	            out.println("<input type='submit' value='Do košíka'>");
	            out.println("Skladom " + rs.getString("ks") + "ks");
	            out.println("</form>");
	            out.println("</div>");

	            count++;

	            
	            if (count % 2 == 0) {
	                out.println("<div style='width: 100%;'></div>");
	            }
	        }

	        out.println("</div>"); 
	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        out.println(e.getMessage());
	    }
	}

	private void createFooter(PrintWriter out, HttpServletRequest request) {
		out.println("<a href='objednavkaServlet'>Zoznam objednávok</a>");
	}

	private void odhlas(PrintWriter out, HttpServletRequest request) {
		out.println("dakujeme za nakup");
		out.println("<a href=\"index.html\">Domov</a>");
		HttpSession ses = request.getSession();
		ses.invalidate();
	}

	private void pridajDoKosika(int id_user, PrintWriter out, HttpServletRequest request) {
	    int id_tovar = Integer.parseInt(request.getParameter("ID"));
	    double cena = Double.parseDouble(request.getParameter("cena"));
	    try {
	        Statement stmt = con.createStatement();

	        ResultSet rs = stmt.executeQuery("SELECT ks FROM sklad WHERE ID = '" + id_tovar + "'");
	        if (rs.next()) {
	            int ksInStock = rs.getInt("ks");
	            if (ksInStock <= 0) {
	                out.println("Tovar nie je momentálne dostupný na sklade.");
	                return;
	            }
	        } else {
	            out.println("Tovar neexistuje.");
	            return;
	        }
	        rs.close();

	        String sql = "SELECT count(ID) AS pocet FROM kosik WHERE (ID_pouzivatela='" + id_user
	                + "') AND (id_tovaru ='" + id_tovar + "')";
	        rs = stmt.executeQuery(sql);
	        rs.next();
	        int pocet = rs.getInt("pocet");
	        if (pocet == 0) {
	            sql = "INSERT INTO kosik (ID_pouzivatela, id_tovaru, cena, ks) values (" + "'" + id_user + "', " + "'"
	                    + id_tovar + "', " + "'" + cena + "', " + "'1') ";
	            stmt.executeUpdate(sql);
	        } else {
	            sql = "UPDATE kosik SET ks = ks + 1, cena ='" + cena + "' WHERE " + "(ID_pouzivatela='" + id_user
	                    + "') AND (id_tovaru = '" + id_tovar + "')";
	            stmt.executeUpdate(sql);
	        }

	        sql = "UPDATE sklad SET ks = ks - 1 WHERE ID = '" + id_tovar + "'";
	        stmt.executeUpdate(sql);

	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        out.println(e.getMessage());
	    }
	}

	protected void vypisNeopravnenyPristup(PrintWriter out) {
		out.println("Neopravneny pristup!");
	}

	protected boolean uspesneOverenie(PrintWriter out, HttpServletRequest request) {
		String username = request.getParameter("login");
		String password = request.getParameter("pwd");

		try {
			String query = "SELECT id, login, passwd, meno, priezvisko, admin FROM users WHERE login = ? AND passwd = ?";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				HttpSession session = request.getSession();
				session.setAttribute("id", resultSet.getInt("id"));
				session.setAttribute("meno", resultSet.getString("meno"));
				session.setAttribute("priezvisko", resultSet.getString("priezvisko"));
				
				boolean isAdmin = resultSet.getBoolean("admin");
				session.setAttribute("admin", isAdmin);

				return true;
			} else {
				out.println("Neplatné prihlasovacie údaje");
				return false;
			}
		} catch (Exception e) {
			out.println(e.getMessage());
			return false;
		}
	}
	
	private void includeStyles(PrintWriter out) {
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; }");
        out.println("header { background-color: #333; color: #fff; padding: 20px; text-align: center; }");
        out.println("nav { background-color: #444; padding: 10px; }");
        out.println("nav a { color: #fff; text-decoration: none; margin-right: 20px; }");
        out.println("section { padding: 20px; }");
        out.println("form { margin-bottom: 10px; }");
        out.println("img { display: block; margin-bottom: 10px; }");
        out.println("</style>");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ses = request.getSession();
        ses.setMaxInactiveInterval(900);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String operacia = request.getParameter("operacia");
            if (badConnection(out) || badOperation(operacia, out))
                return;
            if (operacia.equals("login")) {
                if (!uspesneOverenie(out, request)) {
                    vypisNeopravnenyPristup(out);
                    return;
                }
            }

            int id = getLogedUser(request, out);
            if (id == 0)
                return;

            if (operacia.equals("logout")) {
                odhlas(out, request);
                return;
            }
            if (operacia.equals("nakup")) {
                pridajDoKosika(id, out, request);
            }

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Tesco</title>");
            includeStyles(out); 
            out.println("</head>");
            out.println("<body>");

            createHeader(out, request); 

            out.println("<section>");
            createBody(out, request); 
            out.println("</section>");

            out.println("<footer>");
            out.println("<p>&copy; 2023 Tesco. All rights reserved.</p>");
            out.println("</footer>");

            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            out.println(e);
        }
    }

}


