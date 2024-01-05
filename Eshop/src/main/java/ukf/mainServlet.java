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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class mainServlet
 */
public class mainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String URL = "jdbc:mysql://localhost/E_SHOP_AK";
	String login = "root";
	String pwd = "";
	Connection con = null;
	String errorMessage = "";
	Guard g;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init();
	     try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        con = DriverManager.getConnection(
	              URL,login,pwd);
	     } catch (Exception e) { errorMessage = e.getMessage();}
	     System.out.println("Inited");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		 try {
		      con.close();
		  } catch (Exception e) {  errorMessage = e.getMessage();   }
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
	 
	 protected void vypisNeopravnenyPristup(PrintWriter out) {
	        out.println("Neoprávnený prístup");
	    }
	 
	 private void createHTMLHead(PrintWriter out,  
			 HttpServletRequest request) {
			   HttpSession ses = request.getSession();		    	  
			    	    out.println("<!DOCTYPE html>");
			    	    out.println("<html>");
			    	    out.println("<head>");
			    	    out.println("<meta charset=\"UTF-8\">");
			    	    out.println("<title>Insert title here</title>");
			    	    out.println("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">");
			    	    out.println("</head>");
			    	    out.println("<body>");
	 }
	 
	 private void createHTMLFooter(PrintWriter out,  
			 HttpServletRequest request) {
			   HttpSession ses = request.getSession();		    	  
			    	    out.println("<script src=\"js/bootstrap.bundle.min.js\"></script>");
			    	    out.println("</body>");
			    	    out.println("</html>");
	 }

	 private int getLogedUser(HttpServletRequest request, PrintWriter out) {
		   HttpSession ses = request.getSession();
		   int id = (Integer)ses.getAttribute("ID");
		   if (id == 0) {
		       out.println("Neprihlásený user");
		       vypisNeopravnenyPristup(out);
		   }
		   return id;
		}
	 
	 private boolean jeAdmin(HttpServletRequest request, PrintWriter out) {
		   HttpSession ses = request.getSession();
		   int admin = (Integer)ses.getAttribute("je_admin");
		   boolean je_admin=false;
		   if (admin == 1) {
		      je_admin=true;
		   }
		   return je_admin;
		}
	 
	 private void createHeader(PrintWriter out, HttpServletRequest request){
		   HttpSession ses = request.getSession();
		   String vypis = (String)ses.getAttribute("meno") + " " +
		                  (String)ses.getAttribute("priezvisko");
		   out.println("<div class='d-flex flex-row justify-content-end align-items-center my-5 mx-3'>");		   
		   out.println("<h5 class='pr-3'>"+vypis+"</h6>");
		   
		   out.println("<form action='kosikServlet' method='post'>");
		   out.println("<div class='mb-3 px-2'>");
		   out.println("<input type='hidden' name='operacia' value='show'>");
		   out.println("<input type='submit' class='btn btn-primary align-self-center' value='Košík'>");
		   out.println(" </div>");
		   out.println("</form>");
		   
		   out.println("<form action='mainServlet' method='post'>");
		   out.println("<div class='mb-3 px-2'>");
		   out.println("<input type='hidden' name='operacia' value='logout'>");
		   out.println("<input type='submit' class='btn btn-primary align-self-center' value='Odhlásiť'>");
		   out.println(" </div>");
		   out.println("</form>");
		   
		   out.println("<form action='zoznamObjServlet' method='post'>");
		   out.println("<div class='mb-3 px-2'>");
		   out.println("<input type='hidden' name='operacia' value='zobraz'>");
		   out.println("<input type='submit' class='btn btn-primary align-self-center' value='Zoznam objednávok'>");
		   out.println(" </div>");
		   out.println("</form>");
		   	   
		   out.println("</div>");
		}
	 
	
	   
	 
	 private void createHeaderAdmin(PrintWriter out, HttpServletRequest request){
		   HttpSession ses = request.getSession();
		   String vypis = (String)ses.getAttribute("meno") + " " +
		                  (String)ses.getAttribute("priezvisko");
		   out.println("<div class='d-flex flex-row justify-content-end align-items-center my-5 mx-3'>");		   
		   out.println("<h5 class='pr-3'>"+vypis+"</h6>");   
		   out.println("<form action='mainServlet' method='post'>");
		   out.println("<div class='mb-3 px-2'>");
		   out.println("<input type='hidden' name='operacia' value='logout'>");
		   out.println("<input type='submit' class='btn btn-primary align-self-center' value='Odhlásiť'>");
		   out.println(" </div>");
		   out.println("</form>");		   	   
		   out.println("</div>");
		}

	 private void createBody(PrintWriter out,  
			 HttpServletRequest request) {
			   HttpSession ses = request.getSession();
			   Integer zlava = (Integer)ses.getAttribute("zlava");
			   int aktCena = 0;  
			   out.println("<div class='d-flex flex-column justify-content-center align-items-center'>");	
			   out.println("<h2>Tovar</h2>");
			   try {
			      Statement stmt = con.createStatement();
			      ResultSet rs = stmt.executeQuery("select * from sklad");
			      while (rs.next()) {
			    	  out.println("<div class='d-flex flex-row justify-content-between align-items-center w-50 bg-light opacity-25 rounded mb-3 p-3'>");	
			    	    aktCena = rs.getInt("cena")*(100-zlava)/100;
			    	    out.println("<form action='mainServlet' method='post'>");		    	  
			    	    out.println("<img src='"+rs.getString("img")+"' alt='obrazok' width=100>");
			    	    out.println("<div class='my-3'>");
			    	    out.println("<input type='hidden' name='ID' value= '" +
			    	                 rs.getString("ID")+"'>");
			    	    out.println("<input type='hidden' name='cena' value='"+
			    	                 aktCena+"'>");
			    	    out.println("<input type='hidden' name='operacia' value='nakup'>");
			    	    out.println("<input type='submit' class='btn btn-primary align-self-center' value='Do košíka'>");
			    	    out.println("</div>");
			    	    out.println("</form><hr>");
			    	    out.println("<div class='d-flex flex-column justify-content-end align-items-end'>");	
			    	    out.println("<h4>"+rs.getString("nazov")+"</h4>");
			    	    out.println("<h5>"+aktCena+" EUR</h5>");
			    	    out.println("</div>");
			    	    out.println("</div>");
			    	  }
			    	  rs.close(); 
			    	  stmt.close();
			    	} catch (Exception e) {out.println(e.getMessage());}
			   out.println(" </div>");

	 }
	 
	 private void zobrazUserov(PrintWriter out,  
			 HttpServletRequest request) {
			   HttpSession ses = request.getSession();
			   int id = getLogedUser(request, out);
			   out.println("<div class='d-flex flex-column justify-content-start align-items-start mx-3'>");	
			   out.println("<h2>Použivatelia</h2>");
			   try {
			      Statement stmt = con.createStatement();
			      ResultSet rs = stmt.executeQuery("select * from users");
			      while (rs.next()) {
			    	  	if(rs.getInt("ID")==id) continue;
			    	  	else {
			    	  		 out.println("<div class='d-flex flex-row justify-content-between align-items-start w-75'>");	
			    	  		String ad="";
			    	  		if(rs.getInt("je_admin")==0) ad="používateľ";
			    	  		else ad="admin";
			    	  		 out.println("<p><strong>"+rs.getString("login")+
	    	                            ": </strong>"+ rs.getString("meno")+" "+ rs.getString("priezvisko")+", adresa: "+ rs.getString("adresa")+", poznamky: "+ rs.getString("poznamky")+", práva: "+ad+"</p>");
				    	    out.println("<form action='mainServlet' method='post'>");
				    	    out.println("<input type='hidden' name='IDuser' value= '" +
				    	                 rs.getInt("ID")+"'>");
				    	    out.println("<input type='hidden' name='je_admin' value='"+
				    	    			rs.getInt("je_admin")+"'>");
				    	    out.println("<input type='hidden' name='operacia' value='zmenPrava'>");
				    	    out.println("<input type='submit' class='btn btn-primary align-self-end' value='Zmeň práva použivateľa'>");
				    	   
				    	    out.println("</form>");
				    	    out.println("</div>");
			    	  	}
			    	    
			    	  }
			    	  rs.close(); 
			    	  stmt.close();
			    	} catch (Exception e) {out.println(e.getMessage());}
			   out.println("</div><hr>");
	 }
	 
	 private void zobrazObj(PrintWriter out,  
			 HttpServletRequest request) {
			   HttpSession ses = request.getSession();
			   int id = getLogedUser(request, out);
			   try {
			      Statement stmt = con.createStatement();
			      ResultSet rs = stmt.executeQuery("select * from obj_zoznam");
			      out.println("<div class='d-flex flex-column justify-content-start align-items-start mx-3'>");	
				   out.println("<h2>Objednávky</h2>");
			      while (rs.next()) {
			    	  /*	if(rs.getInt("ID_pouzivatela")==id) continue;
			    	  	else {*/
			    	  		 out.println("<div class='d-flex flex-row justify-content-between align-items-center w-75'>");	
			    	  		  out.println("<p><strong>"+rs.getString("obj_cislo")+
	    	                            ":</strong>"+rs.getString("suma") +" EUR, dátum objednávky: "+rs.getString("datum_objednavky")+", stav: "+rs.getString("stav")+"</p>");
				    	    out.println("<form action='mainServlet' method='post'>");
				    	    out.println("<input type='hidden' name='IDobj' value= '" +
				    	                 rs.getInt("ID")+"'>");
				 
				    	    out.println("<input type='hidden' name='operacia' value='zmenStav'>");
				
				    	  
				    	    out.println("</form><hr>");
				    	    
				    	 
				    	    out.println("<form action='mainServlet' method='post'>");
				    	    out.println("<input type='hidden' name='IDobj' value= '" +
				    	                 rs.getInt("ID")+"'>");
				    	    out.println("<input type='hidden' name='operacia' value='zmenStav'>");
				    	    
				    	    out.println("<select name='stav' class='form-select' id='stav'> "
				    	    		+ "<option value='odoslaná'>Odoslaná</option>"
				    	    		+ "<option value='spracovaná'>Spracovaná</option>"
				    	    		+ "<option value='zaplatená'>Zaplatená</option>"
				    	    		+ "</select>");
				    	    out.println("<div class='d-flex flex-row align-items-center'>");	
				    	    out.println("<input type='submit' class='btn btn-primary align-self-center' value='Zmeň stav objednávky'>");
				    	    out.println("</div>");
				    	    out.println("</form><hr>");
				    	    
				    	    out.println("<form action='mainServlet' method='post'>");
				    	    out.println("<input type='hidden' name='IDobj' value= '" +
				    	                 rs.getString("ID")+"'>");
				    	    out.println("<input type='hidden' name='operacia' value='vymazObj'>");  
				    	    out.println("<input type='submit' class='btn btn-primary align-self-center' value='Vymaž objednávku'>");
				    	    out.println("</form><hr>");
				    	    
				    	    out.println("</div>");
				   
				    	    
			    	  	}
			    	    
//			    	  }
			    	  rs.close(); 
			    	  stmt.close();
			    	  out.println("</div>");
			    	} catch (Exception e) {out.println(e.getMessage());}

	 }
	 
	 private void vymazObj(PrintWriter out, HttpServletRequest request) {
	        try {
	            Statement stmt = con.createStatement();
	            int idUser = getLogedUser(request, out);
	            String id = request.getParameter("IDobj");
	            String sql = "DELETE FROM obj_zoznam WHERE ID = " + id;
	            int pocet = stmt.executeUpdate(sql);
	       
	        } catch (Exception e) { out.println(e);}
		    }
	 
	 private void vymazPoloz(PrintWriter out, HttpServletRequest request) {
	        try {
	            Statement stmt = con.createStatement();
	            int idUser = getLogedUser(request, out);
	            String id = request.getParameter("IDobj");
	            String sql = "DELETE FROM obj_polozky WHERE ID_objednavky = " + id;
	            int pocet = stmt.executeUpdate(sql);
	       
	        } catch (Exception e) { out.println(e);}
		    }
	 
	 
	 private void zmenPrava(PrintWriter out, 
			 HttpServletRequest request) {
			   int IDuser = Integer.parseInt(request.getParameter("IDuser"));
			   int je_admin = Integer.parseInt(request.getParameter("je_admin"));
			   int nastavAdmin=0;
			   if(je_admin==0) nastavAdmin=1;
			   else nastavAdmin=0;
			   try {
			     Statement stmt = con.createStatement();
			     
			     String SQL = "UPDATE users SET je_admin = "+nastavAdmin+" WHERE ID= "+IDuser;
			     stmt.executeUpdate(SQL);
		
			
			    			 stmt.close();
			    			} catch (Exception e) {}
			    			}
	 
	 private void zmenStav(PrintWriter out, 
			 HttpServletRequest request) {
			   int IDobj = Integer.parseInt(request.getParameter("IDobj"));
			   String stav = (String)request.getParameter("stav");
			   try {
			     Statement stmt = con.createStatement();
			     
			     String SQL = "UPDATE obj_zoznam SET stav = '"+stav+"' WHERE ID="+IDobj;
			     stmt.executeUpdate(SQL);
		
			
			    			 stmt.close();
			    			} catch (Exception e) {}
			    			} 
	 
	 private void odhlas(PrintWriter out, 
             HttpServletRequest request) {
		 out.println("<div class='d-flex flex-column justify-content-center align-items-center mt-5'>");	
		   out.println("<h2>Odhlásenie úspešné</h2>");
out.println("<form action='index.html' method='post'>");
out.println("<input type='submit' class='btn btn-primary align-self-center' value='Späť na prihlasovaciu stránku'>");
out.println("</form>");
out.println("</div>");
HttpSession ses = request.getSession();
ses.invalidate();


}
	 
	 protected Connection dajSpojenie(HttpServletRequest request) {
		 try {
		 
		    HttpSession ses = request.getSession();
		
		    Connection c = (Connection)ses.getAttribute("spojenie"); 
		    if (c == null) {
		        c = DriverManager.getConnection(URL, login, pwd);
		        ses.setAttribute("spojenie", c); 
		        g = new Guard(c);
		    } 
		    return c; 
		  } catch(Exception e) {return null;}     
		}

	 
	 
	 private void pridajDoKosika(int id_user, PrintWriter out, 
			 HttpServletRequest request) {
			   int id_tovar = Integer.parseInt(request.getParameter("ID"));
			   int cena = Integer.parseInt(request.getParameter("cena"));
			   try {
			     Statement stmt = con.createStatement();
			     String sql = "SELECT count(ID) AS pocet FROM kosik WHERE " 
			           + "(ID_pouzivatela='"+id_user+"') AND (id_tovaru ='" 
			           +id_tovar+"')";
			     ResultSet rs = stmt.executeQuery(sql);
			     rs.next();
			     int pocet = rs.getInt("pocet");
			     if (pocet == 0) {
			    	  sql = "INSERT INTO kosik (ID_pouzivatela, id_tovaru, cena, ks) values ("+
			    	        "'" + id_user + "', "+
			    	        "'" + id_tovar + "', "+
			    	        "'" + cena + "', "+
			    	        "'1') ";
			    	   stmt.executeUpdate(sql);
			    	} else { 
			    		  sql = "UPDATE kosik SET ks = ks + 1, cena ='"+cena+"' WHERE "+"(ID_pouzivatela='"+id_user+"') AND (id_tovaru ='" + id_tovar+ "')";
			    			  stmt.executeUpdate(sql);
			    			 } 
			    			 rs.close();
			    			 stmt.close();
			    			} catch (Exception e) {}
			    			}
	 
	 
	 protected void OverUsera(PrintWriter out, HttpServletRequest request) {
		 try {
	            String meno = request.getParameter("login");
	            String heslo = request.getParameter("pwd");
	            Statement stmt = con.createStatement();
	            String sql = "SELECT MAX(ID) AS iid, COUNT(ID) AS pocet FROM users "+
	                    " WHERE login = '"+meno+"' AND passwd = '"+heslo+"'";
	            ResultSet rs = stmt.executeQuery(sql);
	            rs.next();
	           HttpSession ses = request.getSession();
	            if (rs.getInt("pocet") == 1) {
	              sql = "SELECT ID, meno, priezvisko, zlava, je_admin FROM users WHERE login = '"+meno+"'"; 
	              rs = stmt.executeQuery(sql); 
	              rs.next();
	              ses.setAttribute("ID", rs.getInt("ID")); 
	              ses.setAttribute("meno", rs.getString("meno"));
	              ses.setAttribute("priezvisko", rs.getString("priezvisko"));
	              ses.setAttribute("zlava", rs.getInt("zlava"));
	              ses.setAttribute("je_admin", rs.getInt("je_admin"));
	            } else {
	              out.println("Prihlasovacie údaje nie sú v poriadku.");
	              ses.invalidate(); 
	            }
	            rs.close();
	            stmt.close();
	           } catch (Exception ex) { out.println(ex.getMessage()); }
           }
	 
	


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  response.setContentType("text/html;charset=UTF-8");
		  PrintWriter out = response.getWriter();

		  try {
			  con = dajSpojenie(request);
		     String operacia = request.getParameter("operacia");
		     if (badConnection(out) || badOperation(operacia, out)) return;
		     if (operacia.equals("login")) {
		         OverUsera(out, request);
		     }

		     int id = getLogedUser(request, out);
		     if (id == 0) return;	     
		    createHTMLHead(out, request);
		     if(jeAdmin(request, out)) {
		    	 if (operacia.equals("logout")) { odhlas(out, request); return; }
		    	 createHeaderAdmin(out, request);
		    	 if (operacia.equals("zmenPrava")) { zmenPrava(out, request); }
		    	 if (operacia.equals("zmenStav")) { zmenStav(out, request); }
		    	 if (operacia.equals("vymazObj")) {vymazObj(out, request); vymazPoloz(out, request);}
		    	
		    	 zobrazUserov(out, request);
		    	 zobrazObj(out, request);
		     }
		     else {
		    	   if (operacia.equals("logout")) { odhlas(out, request); return; }
		    	 createHeader(out, request);		  
			     if (operacia.equals("nakup")) { pridajDoKosika(id, out, request); }			     
			     createBody(out, request);	    	 
		     }
		     createHTMLFooter(out, request);		     
		  } catch (Exception e) {out.println(e);}

	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
