import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String databaza = "Skuska_Letecke_Treningy";
	public final static String URL = "jdbc:mysql://localhost/" + databaza;
	public final static String username = "root";
	public final static String password = "";
	private Connection con;
       

    public Main_servlet() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		try {
			super.init();//Treba toto?
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, username, password);
		} catch (Exception e) {
			System.err.println("V init: " + e);
		}
	}
    
	public void destroy() {
		try {con.close();} catch(Exception e) {System.err.println("V destroy: " + e);	
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		int show = 0;
		try {
			if(con==null) {
				out.println("Neni pripojena databaza.");
				System.err.println("Pripojenie k databaze " + databaza + " NIE-je.");
				return;
			} else {System.out.println("Pripojenie k databaze " + databaza + " je.");}
            String operacia = request.getParameter("operacia");
            System.out.println("Operacia je: " + operacia);
            
            if (operacia == null) { zobrazNeopravnenyPristup(out); return; }
            if (operacia.equals("register")) {registerUser(out, request.getParameter("name"), request.getParameter("surname"), request.getParameter("Email"), request.getParameter("pwd"), response, request, "Customer");}
            else if (operacia.equals("login")) { overUsera(out, request, response); }
            else if (operacia.equals("zobraz")) {show=1;}
          
            else if(operacia.equals("deleteLietadlo")) {DELETElietadla(request, response); return;}
            else if(operacia.equals("showTableForUpdate")) {showTableForUpdateLietadla(out, request); return;}
            else if(operacia.equals("updateLietadlo")) {updateLietadlo(out, request, request.getParameter("vyrobca"), request.getParameter("model"), request.getParameter("oznacenie"), request.getParameter("aktivne"), request.getParameter("idLietadla"), response); return;}
            else if(operacia.equals("showTableForInsert")) {showTableForInsert(out, request);}
            else if(operacia.equals("InsertLietadla")) {insertLietadla(out, request, request.getParameter("vyrobca"), request.getParameter("model"), request.getParameter("oznacenie"), response); return;}
			else if (operacia.equals("showTableForInsertInstruktora")) {showTableForInsertInstruktora(out, request);}
			else if (operacia.equals("InsertInstruktora")) {insertInstructor(out, request, request.getParameter("meno"), request.getParameter("priezvisko"), request.getParameter("email"), request.getParameter("poznamky"), response); return;}
			else if (operacia.equals("showTableForUpdateInstruktor")) {showTableForUpdateInstruktor(out, request);}
			else if (operacia.equals("updateInstruktora")) {updateInstruktora(out, request, request.getParameter("meno"), request.getParameter("priezvisko"), request.getParameter("email"), request.getParameter("aktivne"), request.getParameter("poznamky"), request.getParameter("idInstruktora"), response); return;}
            else if (operacia.equals("deleteInstruktora")) {deleteInstruktora(out, request, response); return;}
            
            else if (operacia.equals("showTableForInsertRECORD")) {showTableForInsertRECORD(out, request);}
            else if (operacia.equals("InsertRECORD")) {InsertRECORD(out, request, request.getParameter("datum"), request.getParameter("casOd"), request.getParameter("casDo"), request.getParameter("instruktor"), request.getParameter("lietadlo"), response); return;}
			else if (operacia.equals("showTableForUpdateRECORD")) {showTableForEdirRECORD(out, request);}
			else if (operacia.equals("updateRECORD")) {updateRECORD(out, request, request.getParameter("datum"), request.getParameter("casOd"), request.getParameter("casDo"), request.getParameter("instruktor"), request.getParameter("lietadlo"), response); return;}
            else if (operacia.equals("deleteRECORD")) {deleteRECORD(out, request, response); return;}
            
            head(out);
            header(out, request);
            if(show==0) {}
            else {main(out, request);}
            bottom(out, request);
            
            out.close();
		} catch (Exception e) {
			System.err.println("Servlet doGet " + e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void head(PrintWriter out) {
		out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Niečo s lietadlami</title>");
        out.println("    <link rel='stylesheet' href='style.css'>");
        out.println("    <script src='script.js' defer></script>");
        out.println("</head>");
        out.println("<body>");
	}
	
	public void bottom(PrintWriter out, HttpServletRequest request) {
		out.println("<footer>");
		out.println("</footer>");
        out.println("</body>");
        out.println("</html>");
	}
	
	public void header(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("Prihlaseny pouzivatel, je " + session.getAttribute("meno"));
		out.println("<header>");
		out.println("	<div class='pozdrav'>");
		out.println("    </div>");
		out.println("</header>");
	}
	
	protected void zobrazNeopravnenyPristup(PrintWriter out) {
		out.println("Neopravneny pristup");
		out.println("<a href='login.html'>Prihlasenie</a>");
	}
	
	protected void registerUser(PrintWriter out, String name, String surname, String email, String pwd, HttpServletResponse response, HttpServletRequest request, String pozicia) {
            String sql = "INSERT INTO Users (`Name`, `Surname`, Email, `Position`, `Password`) VALUES (?, ?, ?, ?, ?)";
	            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
	                pstmt.setString(1, name);
	                pstmt.setString(2, surname);
	                pstmt.setString(3, email); 
	                pstmt.setString(4, pozicia); 
	                pstmt.setString(5, pwd);

	                int rowCount = pstmt.executeUpdate();
	                System.out.println("Rows affected: " + rowCount);

	            } catch (SQLIntegrityConstraintViolationException e) {
	            	System.err.println("Chyba v register User, rovnaky email." + e);
	    			try {
	    				response.sendRedirect(request.getContextPath() + "/e-mail.html");
	    			} catch (IOException e1) {
	    				System.err.println("Chyba v user IO" + e1);
	    				e1.printStackTrace();
	    			}				
	    			} catch (SQLException e2) {
	    				System.out.println("Chyba v user SQL" + e2);
					e2.printStackTrace();
				}
	       	}
	
	protected void overUsera(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String email = request.getParameter("login");
		String pwd = request.getParameter("pwd");
		String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, email);
			pstmt.setString(2, pwd);
			pstmt.execute();
			if (pstmt.getResultSet().next()) {
				session.setAttribute("meno", pstmt.getResultSet().getString("Name"));
				session.setAttribute("priezvisko", pstmt.getResultSet().getString("Surname"));
				session.setAttribute("Email", pstmt.getResultSet().getString("Email"));
				session.setAttribute("id", pstmt.getResultSet().getString("idUsers"));
				session.setAttribute("pozicia", pstmt.getResultSet().getString("Position"));
				response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
			} else {
				System.out.println("Nespravne meno alebo heslo");
				response.sendRedirect(request.getContextPath() + "/login.html");
			};
		} catch (SQLException e) {
			System.err.println("Chyba v overUsera SQL " + e);
		} catch (IOException e) {
			System.err.println("Chyba v overUsera IO" + e);
		}
	}
	
	public void main(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("pozicia").equals("Admin")) {
			String operacia = request.getParameter("operacia");
			if (operacia.equals("zobraz")) {
				SELECTlietadla(out, request);
				SELECTinstruktori(out, request);
			} 
			else if (operacia.equals("showTableForUpdate")) {
				showTableForUpdateLietadla(out, request);
			}
			else if(operacia.equals("showTableForInsert")) {
				showTableForInsert(out, request);
			}
			else if(operacia.equals("showTableForInsertInstruktora")){
				showTableForInsertInstruktora(out, request);
			}
			else if(operacia.equals("showTableForUpdateInstruktor")) {
				showTableForInsert(out, request);
			}
			}
		
		if (session.getAttribute("pozicia").equals("Customer")) {
			String operacia = request.getParameter("operacia");
			if (operacia.equals("zobraz")) {
					SELECTrecords(out, request);
			} 
			else if (operacia.equals("showTableForUpdateRECORD")) {
				showTableForEdirRECORD(out, request);
			} else if (operacia.equals("showTableForInsertRECORD")) {
				showTableForInsertRECORD(out, request);
			
			}
		}		
		} 
	
	public void SELECTlietadla(PrintWriter out, HttpServletRequest request) {
		try {
			System.out.println("Select lietadla");
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Planes";
			ResultSet rs = stmt.executeQuery(sql);
			out.println("<div class='table'>");
			out.println("<h2>Zoznam lietadiel</h2>");
			out.println("	<table>");
			out.println("		<tr>");
			out.println("			<th>idLietadla</th>");
			out.println("			<th>Výrobca lietadla</th>");
			out.println("			<th>Model lietadla</th>");
			out.println("			<th>Označenie lietadla</th>");
			out.println("			<th>Lietadlo aktivne?</th>");
			out.println("		</tr>");
			while (rs.next()) {
				out.println("		<tr>");
				out.println("			<td>" + rs.getString("idPlanes") + "</td>");
				out.println("			<td>" + rs.getString("Designation") + "</td>");
				out.println("			<td>" + rs.getString("Manufacturer") + "</td>");
				out.println("			<td>" + rs.getString("Model") + "</td>");
				out.println("			<td>" + (rs.getString("Active").equals("1") ? "Áno": "Nie") + "</td>");
				out.println("			<td> <form method='post' action='Main_servlet'> <input type='hidden' name='operacia' value='showTableForUpdate'>"
						+ 					"<input type='hidden' name='idLietadla' value='" + rs.getString("idPlanes") + "'> <input type='submit' value='Uprav'> </form> </td>");
				out.println("			<td> <form method='post' action='Main_servlet'> <input type='hidden' name='operacia' value='deleteLietadlo'>"
									+ 		"<input type='hidden' name='idLietadla' value='" + rs.getString("idPlanes")	+ "'> <input type='submit' value='Vymaž'> </form> </td>");
				out.println("		</tr>");
			}
			out.println("	</table>");
			out.println("	<form method='post' action='Main_servlet'>");
			out.println("		<input type='hidden' name='operacia' value='showTableForInsert'>");
			out.println("		<input type='submit' value='Pridaj lietadlo'>");
			out.println("	</form>");
			out.println("</div>");
		} catch (SQLException e) {
			System.err.println("Chyba v SELECTlietadla " + e);
			e.printStackTrace();
		}
	}
	
	public void DELETElietadla(HttpServletRequest request, HttpServletResponse response) {
		try {
            System.out.println("Delete lietadla");
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM Planes WHERE idPlanes = " + request.getParameter("idLietadla");
            stmt.executeUpdate(sql);
            try {
				response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (SQLException e) {
            System.err.println("Chyba v DELETElietadla " + e);
            e.printStackTrace();
        }
    }
	
	public void showTableForUpdateLietadla(PrintWriter out, HttpServletRequest request) {
		try {
			String IDLietadla = request.getParameter("idLietadla");
			System.out.println("Select lietadla");
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Planes WHERE idPlanes = " + IDLietadla + "";
			ResultSet rs = stmt.executeQuery(sql);
			out.println("<div class='table'>");
			out.println("<h2>Edit lietadlo</h2>");
			out.println("	<table>");
			out.println("		<tr>");
			out.println("			<th>idLietadla</th>");
			out.println("			<th>Výrobca lietadla</th>");
			out.println("			<th>Model lietadla</th>");
			out.println("			<th>Označenie lietadla</th>");
			out.println("			<th>Lietadlo aktivne?</th>");
			out.println("		</tr>");
			while (rs.next()) {
	            out.println("		<tr>");
	            out.println("			<form method='post' action='Main_servlet'>");
	            out.println("				<input type='hidden' name='operacia' value='updateLietadlo'>");
	            out.println("				<input type='hidden' name='idLietadla' value='" + rs.getString("idPlanes") + "'>");
	            	out.println("				<td>" + rs.getString("idPlanes") + "</td>");
	            	out.println("				<td><input type='text' name='vyrobca' value='" + rs.getString("Manufacturer") + "'></td>");
	            	out.println("				<td><input type='text' name='model' value='" + rs.getString("Model") + "'></td>");
	            	out.println("				<td><input type='text' name='oznacenie' value='" + rs.getString("Designation") + "'></td>");
	            	out.println("				<td><input type='text' name='aktivne' value='" + rs.getString("Active") + "'></td>");
	            	out.println("				<td><input type='submit' value='Uprav'></td>");
	            	out.println("			</form>");
				out.println("		</tr>");
			}
			out.println("	</table>");
			out.println("</div>");
		} catch (SQLException e) {
			System.err.println("Chyba v SELECTlietadla " + e);
			e.printStackTrace();
		}
	}
	
	public void updateLietadlo(PrintWriter out, HttpServletRequest request, String vyrobca, String model, String oznacenie, String aktivne, String idLietadla, HttpServletResponse response) {
		String sql = "Update Planes SET Manufacturer = ?, Model = ?, Designation = ?, Active = ? WHERE idPlanes = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, vyrobca);
			pstmt.setString(2, model);
			pstmt.setString(3, oznacenie);
			pstmt.setString(4, aktivne);
			pstmt.setString(5, idLietadla);
			pstmt.executeUpdate();
			try {
				response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}  catch (SQLException e) {
				System.err.println("Chyba v overUsera SQL " + e);
			}
		}
		
	public void showTableForInsert(PrintWriter out, HttpServletRequest request) {
		try {
			Statement stmt = con.createStatement();
			out.println("<div class='table'>");
			out.println("<h2>Edit lietadlo</h2>");
			out.println("	<table>");
			out.println("		<tr>");
			out.println("			<th>Výrobca lietadla</th>");
			out.println("			<th>Model lietadla</th>");
			out.println("			<th>Označenie lietadla</th>");
			out.println("		</tr>");
	            out.println("		<tr>");
	            out.println("			<form method='post' action='Main_servlet'>");
	            out.println("				<input type='hidden' name='operacia' value='InsertLietadla'>");
	            	out.println("				<td><input type='text' name='vyrobca' value=''></td>");
	            	out.println("				<td><input type='text' name='model' value=''></td>");
	            	out.println("				<td><input type='text' name='oznacenie' value=''></td>");
	            	out.println("				<td><input type='submit' value='Vytvor'></td>");
	            	out.println("			</form>");
				out.println("		</tr>");
			out.println("	</table>");
			out.println("</div>");
	}
		catch (SQLException e) {
            System.err.println("Chyba v SELECTlietadla " + e);
            e.printStackTrace();
        }
	}
	
	public void insertLietadla(PrintWriter out, HttpServletRequest request, String vyrobca, String model, String oznacenie, HttpServletResponse response) {
        String sql = "INSERT INTO Planes (Manufacturer, Model, Designation, Active) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, vyrobca);
            pstmt.setString(2, model);
            pstmt.setString(3, oznacenie);
            pstmt.setString(4, "1");
            pstmt.executeUpdate();
            try {
                response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }  catch (SQLException e) {
                System.err.println("Chyba v overUsera SQL " + e);
            }
        }
		
	public void SELECTinstruktori(PrintWriter out, HttpServletRequest request) {
		try {
            System.out.println("Select instruktori");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Users WHERE Position = 'Instructor'";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<div class='table'>");
            out.println("<h2>Zoznam instruktorov</h2>");
            out.println("	<table>");
            out.println("		<tr>");
            out.println("			<th>id</th>");
            out.println("			<th>Meno</th>");
            out.println("			<th>Priezvisko</th>");
            out.println("			<th>Email</th>");
            out.println("			<th>Aktívny</th>");
            out.println("			<th>Poznamky</th>");
            out.println("		</tr>");
            while (rs.next()) {
                out.println("		<tr>");
                out.println("			<td>" + rs.getString("idUsers") + "</td>");
                out.println("			<td>" + rs.getString("Name") + "</td>");
                out.println("			<td>" + rs.getString("Surname") + "</td>");
                out.println("			<td>" + rs.getString("Email") + "</td>");
                out.println("			<td>" + (rs.getString("Active").equals("1") ? "Áno": "Nie") + "</td>");
                out.println("			<td>" + rs.getString("Note") + "</td>");
                out.println("		</tr>");
                out.println("		<tr>");
                out.println("			<form method='post' action='Main_servlet'>");
                out.println("				<input type='hidden' name='operacia' value='showTableForUpdateInstruktor'>");
                out.println("				<input type='hidden' name='idInstruktora' value='" + rs.getString("idUsers") + "'>");
                out.println("				<td><input type='submit' value='Uprav'></td>");
                out.println("			</form>");
                out.println("			<form method='post' action='Main_servlet'>");
                out.println("				<input type='hidden' name='operacia' value='deleteInstruktora'>");
                out.println("				<input type='hidden' name='idInstruktora' value='" + rs.getString("idUsers") + "'>");
                out.println("				<td><input type='submit' value='Vymaž'></td>");
                out.println("			</form>");
                out.println("		</tr>");                 
            }
			out.println("	</table>");
			out.println("	<form method='post' action='Main_servlet'>");
			out.println("		<input type='hidden' name='operacia' value='showTableForInsertInstruktora'>");
			out.println("		<input type='submit' value='Pridaj inštruktora'>");
			out.println("	</form>");
			out.println("</div>");
		} catch (SQLException e) {
			System.err.println("Chyba v SELECTlietadla " + e);
			e.printStackTrace();
		}
	}
	
	public void showTableForInsertInstruktora(PrintWriter out, HttpServletRequest request) {
            out.println("<div class='table'>");
            out.println("<h2>Insert Instruktora</h2>");
            out.println("	<table>");
            out.println("		<tr>");
            out.println("			<th>Meno</th>");
            out.println("			<th>Priezvisko</th>");
            out.println("			<th>Email</th>");
            out.println("			<th>Poznamky</th>");
            out.println("		</tr>");
                out.println("		<tr>");
                out.println("			<form method='post' action='Main_servlet'>");
                out.println("				<input type='hidden' name='operacia' value='InsertInstruktora'>");
                	out.println("				<td><input type='text' name='meno' value=''></td>");
                	out.println("				<td><input type='text' name='priezvisko' value=''></td>");
                	out.println("				<td><input type='email' name='email' value=''></td>");
                	out.println("				<td><input type='text' name='poznamky' value=''></td>");
                	out.println("				<td><input type='submit' value='Vytvor instruktora'></td>");
                	out.println("			</form>");
                out.println("		</tr>");
            out.println("	</table>");
            out.println("</div>");
	}
	
	public void insertInstructor(PrintWriter out, HttpServletRequest request, String meno, String priezvisko,
			String email, String poznamky, HttpServletResponse response) {
		String sql = "INSERT INTO Users (Name, Surname, Email, Position, Note, Active) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, meno);
			pstmt.setString(2, priezvisko);
			pstmt.setString(3, email);
			pstmt.setString(4, "Instructor");
			pstmt.setString(5, poznamky);
			pstmt.setString(6, "1");
			pstmt.executeUpdate();
			try {
				response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e) {
			System.err.println("Chyba v overUsera SQL " + e);
		}
	}

	public void showTableForUpdateInstruktor(PrintWriter out, HttpServletRequest request) {
		try {
            String IDInstruktora = request.getParameter("idInstruktora");
            System.out.println("Select lietadla");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Users WHERE idUsers = " + IDInstruktora + "";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<div class='table'>");
            out.println("<h2>Edit lietadlo</h2>");
            out.println("	<table>");
            out.println("		<tr>");
            out.println("			<th>Meno</th>");
            out.println("			<th>Priezvisko</th>");
            out.println("			<th>Email</th>");
            out.println("			<th>Aktívny</th>");
            out.println("			<th>Poznamky</th>");
            out.println("		</tr>");
            while (rs.next()) {
                out.println("		<tr>");
                out.println("			<form method='post' action='Main_servlet'>");
                out.println("				<input type='hidden' name='operacia' value='updateInstruktora'>");
                out.println("				<input type='hidden' name='idInstruktora' value='" + rs.getString("idUsers") + "'>");
                out.println("				<td><input type='text' name='meno' value='" + rs.getString("Name") + "'></td>");
                out.println("				<td><input type='text' name='priezvisko' value='" + rs.getString("Surname") + "'></td>");
                out.println("				<td><input type='text' name='email' value='" + rs.getString("Email") + "'></td>");
                out.println("				<td><input type='text' name='aktivne' value='" + rs.getString("Active") + "'></td>");
                out.println("				<td><input type='text' name='poznamky' value='" + rs.getString("Note") + "'></td>");
                out.println("				<td><input type='submit' value='Uprav'></td>");
                out.println("			</form>");
            }
            out.println("	</table>");
            out.println("</div>");
            } catch (SQLException e) {
				System.err.println("Chyba v SELECTlietadla " + e);
				e.printStackTrace();
			}
		}
            
   public void updateInstruktora(PrintWriter out, HttpServletRequest request, String meno, String priezvisko, String email, String aktivne, String poznamky, String idInstruktora, HttpServletResponse response) {
	   String sql = "Update Users SET Name = ?, Surname = ?, Email = ?, Active = ?, Note = ? WHERE idUsers = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, meno);
			pstmt.setString(2, priezvisko);
			pstmt.setString(3, email);
			pstmt.setString(4, aktivne);
			pstmt.setString(5, poznamky);
			pstmt.setString(6, idInstruktora);
			pstmt.executeUpdate();
			try {
				response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e) {
			System.err.println("Chyba v overUsera SQL " + e);
		}
   }
   
	public void deleteInstruktora(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println("Delete Instruktora");
			Statement stmt = con.createStatement();
			String sql = "DELETE FROM Users WHERE idUsers = " + request.getParameter("idInstruktora");
			stmt.executeUpdate(sql);
			try {
				response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			System.err.println("Chyba v DELETElietadla " + e);
			e.printStackTrace();
		}
	}

	public void SELECTrecords(PrintWriter out, HttpServletRequest request) {
		try {
            System.out.println("Select records");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM records INNER JOIN Users ON records.Instructor=Users.idUsers INNER JOIN Planes ON records.Lietadlo=Planes.idPlanes;  ;";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<div class='table'>");
            out.println("<h2>Zaznamy</h2>");
            out.println("	<table>");
            out.println("		<tr>");
            out.println("			<th>idZaznamu</th>");
            out.println("			<th>Dátum</th>");
            out.println("			<th>Čas od</th>");
            out.println("			<th>Čas do</th>");
            out.println("			<th>Inštruktor</th>");
            out.println("			<th>Lietadlo</th>");
            out.println("		</tr>");
            while (rs.next()) {
            	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            	String color = "white";
            	if (rs.getString("Date").compareTo(today) < 0) {
					color = "red";
				} else if (rs.getString("Date").compareTo(today) == 0) {
					color = "yellow";
				} else if (rs.getString("Date").compareTo(today) > 0) {
					color = "green";
            	}	//make tr with style of color
            	out.println("		<tr style='background-color:" + color + "'>");
               // out.println("		<tr>");
                out.println("			<td>" + rs.getString("idRecord") + "</td>");
                out.println("			<td>" + rs.getString("Date") + "</td>");
                out.println("			<td>" + rs.getString("Time_From") + "</td>");
                out.println("			<td>" + rs.getString("Time_TO") + "</td>");
                out.println("			<td>" + (rs.getString("Name") + " " + rs.getString("Surname")) + "</td>");
                out.println("			<td>" + (rs.getString("Manufacturer") + " " + rs.getString("Model") + " " + rs.getString("Designation")) + "</td>");
              
                out.println("			<td> <form method='post' action='Main_servlet'> <input type='hidden' name='operacia' value='showTableForUpdateRECORD'>"
                        + 					"<input type='hidden' name='idZaznamu' value='" + rs.getString("idRecord") + "'> <input type='submit' value='Uprav'> </form> </td>");
                out.println("			<td> <form method='post' action='Main_servlet'> <input type='hidden' name='operacia' value='deleteRECORD'>"
                                    + 		"<input type='hidden' name='idZaznamu' value='" + rs.getString("idRecord")	+ "'> <input type='submit' value='Vymaž'> </form> </td>");
                out.println("		</tr>");
            }
            out.println("	</table>");
               out.println("	<form method='post' action='Main_servlet'>");
               out.println("		<input type='hidden' name='operacia' value='showTableForInsertRECORD'>");
               out.println("		<input type='submit' value='Pridaj zaznam'>");
               out.println("	</form>");
               out.println("</div>");
               
                
	
		} catch (SQLException e) {
			System.err.println("Chyba v SELECTlietadla " + e);
            e.printStackTrace();
		}
	}

	public void showTableForInsertRECORD(PrintWriter out, HttpServletRequest request) {
		try {
			Statement stmt = con.createStatement();
			out.println("<div class='table'>");
			out.println("<h2>Edit lietadlo</h2>");
			out.println("	<table>");
			out.println("		<tr>");
			out.println("			<th>Dátum</th>");
			out.println("			<th>Čas od</th>");
			out.println("			<th>Čas do</th>");
			out.println("			<th>Inštruktor</th>");
			out.println("			<th>Lietadlo</th>");
			out.println("		</tr>");
			out.println("		<tr>");
			out.println("			<form method='post' action='Main_servlet'>");
			out.println("				<input type='hidden' name='operacia' value='InsertRECORD'>");
			out.println("				<td><input type='date' name='datum' value=''></td>");
			out.println("				<td><input type='time' name='casOd' value=''></td>");
			out.println("				<td><input type='time' name='casDo' value=''></td>");
			
			out.println("				<td><select name='instruktor'>");
			Statement stmt2 = con.createStatement();
			String sql2 = "SELECT * FROM Users WHERE Position = 'Instructor' AND Active = '1'";
			ResultSet rs2 = stmt2.executeQuery(sql2);
			while (rs2.next()) {
				out.println("				<option value='" + rs2.getString("idUsers") + "'>" + rs2.getString("Name")
						+ " " + rs2.getString("Surname") + "</option>");
			}
			out.println("				</select></td>");
			
			out.println("				<td><select name='lietadlo'>");
			Statement stmt3 = con.createStatement();
				String sql3 = "SELECT * FROM Planes WHERE Active = '1'";
				ResultSet rs3 = stmt3.executeQuery(sql3);
				while (rs3.next()) {
					out.println("				<option value='" + rs3.getString("idPlanes") + "'>"
							+ rs3.getString("Manufacturer") + " " + rs3.getString("Model") + " "
							+ rs3.getString("Designation") + "</option>");
				}
				out.println("				</select></td>");

			out.println("				<td><input type='submit' value='Vytvor'></td>");
			out.println("			</form>");

			out.println("		</tr>");
			out.println("	</table>");
			out.println("</div>");
		} catch (SQLException e) {
			System.err.println("Chyba v SELECTlietadla " + e);
			e.printStackTrace();
		}
	}
	
	public void InsertRECORD(PrintWriter out, HttpServletRequest request, String datum, String casOd, String casDo, String instruktor, String lietadlo, HttpServletResponse response) {
        int jeMozne = 1;
		
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM records WHERE Lietadlo = " + lietadlo + " AND Date = '" + datum + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("Time_From").compareTo(casOd) > 0 || rs.getString("Time_TO").compareTo(casDo) < 0) {
					jeMozne = 0;
				}
				System.out.println(rs.getString("Time_From"));
				System.out.println(casOd);
			}
		} catch (SQLException e) {
			System.err.println("Chyba v SELECTlietadla " + e);
			e.printStackTrace();
		}

		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM records WHERE Instructor = " + instruktor + " AND Date = '" + datum + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("Time_From").compareTo(casOd) > 0 || rs.getString("Time_TO").compareTo(casDo) < 0) {
					jeMozne = 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("Chyba v SELECTlietadla " + e);
			e.printStackTrace();
		}
	
		if(jeMozne==1) {
		
		
		String sql = "INSERT INTO records (Date, Time_From, Time_TO, Instructor, Lietadlo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, datum);
            pstmt.setString(2, casOd);
            pstmt.setString(3, casDo);
            pstmt.setString(4, instruktor);
            pstmt.setString(5, lietadlo);
            pstmt.executeUpdate();
            try {
                response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Chyba v overUsera SQL " + e);
		}
	} else {
		out.println("Nie je mozne vytvorit zaznam");
	}
    }
		
	public void showTableForEdirRECORD(PrintWriter out, HttpServletRequest request) {
			try {
            String IDZaznamu = request.getParameter("idZaznamu");
            System.out.println("Select lietadla");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM records WHERE idRecord = " + IDZaznamu + "";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<div class='table'>");
            out.println("<h2>Edit lietadlo</h2>");
            out.println("	<table>");
            out.println("		<tr>");
            out.println("			<th>Dátum</th>");
            out.println("			<th>Čas od</th>");
            out.println("			<th>Čas do</th>");
            out.println("			<th>Inštruktor</th>");
            out.println("			<th>Lietadlo</th>");
            out.println("		</tr>");
            while (rs.next()) {
                out.println("		<tr>");
                out.println("			<form method='post' action='Main_servlet'>");
                out.println("				<input type='hidden' name='operacia' value='updateRECORD'>");
                out.println("				<input type='hidden' name='idZaznamu' value='" + rs.getString("idRecord") + "'>");
                out.println("				<td><input type='date' name='datum' value='" + rs.getString("Date") + "'></td>");
                out.println("				<td><input type='time' name='casOd' value='" + rs.getString("Time_From") + "'></td>");
                out.println("				<td><input type='time' name='casDo' value='" + rs.getString("Time_TO") + "'></td>");
                out.println("				<td><select name='instruktor'>");
                Statement stmt2 = con.createStatement();
                String sql2 = "SELECT * FROM Users WHERE Position = 'Instructor' AND Active = '1'";
                ResultSet rs2 = stmt2.executeQuery(sql2);
                while (rs2.next()) {
                    out.println("				<option value='" + rs2.getString("idUsers") + "'>" + rs2.getString("Name")
                            + " " + rs2.getString("Surname") + "</option>");
                }
                out.println("				</select></td>");
                
                out.println("				<td><select name='lietadlo'>");
                Statement stmt3 = con.createStatement();
                String sql3 = "SELECT * FROM Planes WHERE Active = '1'";
                ResultSet rs3 = stmt3.executeQuery(sql3);
                while (rs3.next()) {
                    out.println("				<option value='" + rs3.getString("idPlanes") + "'>"
                            + rs3.getString("Manufacturer") + " " + rs3.getString("Model") + " "
                            + rs3.getString("Designation") + "</option>");
                }
                out.println("				</select></td>");
                out.println("				<td><input type='submit' value='Uprav'></td>");
                out.println("			</form>");
                out.println("		</tr>");
            }
	            out.println("	</table>");
	            out.println("</div>");
	                        } catch (SQLException e) {
	                        	                System.err.println("Chyba v SELECTlietadla " + e);
	                        	                e.printStackTrace();
	                        	                
	                        }}
	
		public void updateRECORD(PrintWriter out, HttpServletRequest request, String datum,
								String casOd, String casDo, String instruktor, String lietadlo,
								HttpServletResponse response) {
							String sql = "Update records SET Date = ?, Time_From = ?, Time_TO = ?, Instructor = ?, Lietadlo = ? WHERE idRecord = ?";
							try (PreparedStatement pstmt = con.prepareStatement(sql)) {
								pstmt.setString(1, datum);
								pstmt.setString(2, casOd);
								pstmt.setString(3, casDo);
								pstmt.setString(4, instruktor);
								pstmt.setString(5, lietadlo);
								pstmt.setString(6, request.getParameter("idZaznamu"));
								pstmt.executeUpdate();
								try {
									response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} catch (SQLException e) {
								System.err.println("Chyba v overUsera SQL " + e);
							}
						}
	
	public void deleteRECORD(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        try {
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM records WHERE idRecord = " + request.getParameter("idZaznamu");
            System.out.println(sql);
            stmt.executeUpdate(sql);
            try {
                response.sendRedirect(request.getContextPath() + "/Main_servlet?operacia=zobraz");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Chyba v DELETElietadla " + e);
            e.printStackTrace();
        }
    }


}
