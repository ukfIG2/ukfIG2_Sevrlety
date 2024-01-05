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
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Servlet implementation class kosikServlet
 */
public class kosikServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String URL = "jdbc:mysql://localhost/E_SHOP_AK";
	String login = "root";
	String pwd = "";
	String errorMessage = "";
	Connection con;
	Guard g;
	int sum=0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public kosikServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		 try {
		      con.close();
		  } catch (Exception e) {  errorMessage = e.getMessage();   }
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
	
	 private void createHeader(PrintWriter out, HttpServletRequest request){
		   HttpSession ses = request.getSession();
		   String vypis = (String)ses.getAttribute("meno") + " " +
		                  (String)ses.getAttribute("priezvisko");
		   out.println("<div class='d-flex flex-row justify-content-end align-items-center my-5 mx-3'>");		   
		   out.println("<h5 class='pr-3'>"+vypis+"</h6>");
		   
		   out.println("<form action='mainServlet' method='post'>");
		   out.println("<div class='mb-3 px-2'>");
		   out.println("<input type='hidden' name='operacia' value='show'>");
		   out.println("<input type='submit' class='btn btn-primary align-self-center' value='Naspäť'>");
		   out.println(" </div>");
		   out.println("</form>");
		   
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
			   int id = (Integer)ses.getAttribute("ID");
			   int aktCena = 0;  
			   try {
			      Statement stmt = con.createStatement();
			      ResultSet rs = stmt.executeQuery("SELECT kosik.ID, kosik.ID_tovaru, kosik.ID_pouzivatela, kosik.cena, kosik.ks, sklad.nazov FROM kosik INNER JOIN sklad ON kosik.ID_tovaru=sklad.ID WHERE kosik.ID_pouzivatela="+id);
		
			      while (rs.next()) {
			    	  aktCena = rs.getInt("cena")*(100-zlava)/100;
			    	  sum=sum+aktCena;
			    	    out.println("<form action='kosikServlet' method='post'>");
			    	    out.println("<input type='hidden' name='ID' value= '" +
			    	                 rs.getString("ID")+"'>");
			    	    out.println("<input type='hidden' name='ID_tovaru' value='"+
			    	    		rs.getString("ID_tovaru")+"'>");
			    	    out.println("<input type='hidden' name='cena' value='"+
			    	    		aktCena+"'>");
			    	    out.println("<input type='hidden' name='ks' value='"+
			    	    		rs.getString("ks")+"'>");
			    	    out.println("<input type='hidden' name='operacia' value='vyhod'>");
			    	    out.println("<input type='submit' class='btn btn-primary align-self-center mx-3' value='Vyhodiť z košíka'>");
			    	    out.println("<strong>"+rs.getString("nazov")+
			    	                            ": </strong>"+aktCena +" EUR, počet kusov: "+rs.getString("ks"));
			    	    out.println("</form><hr>");
			    	  }
			    	  rs.close(); 
			    	  stmt.close();
		
			    	   out.println("<form action='kosikServlet' method='post'>");
					   out.println("<input type='hidden' class='btn btn-primary align-self-center' name='operacia' value='objednaj'>");
					   out.println("<input type='submit' class='btn btn-primary align-self-center mx-3' value='Objednaj'>");
					   out.println("</form>");
			    	  
			    	} catch (Exception e) {out.println(e.getMessage());}

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
	 
	 private int getLogedUser(HttpServletRequest request, PrintWriter out) {
		   HttpSession ses = request.getSession();
		   int id = (Integer)ses.getAttribute("ID");
		   if (id == 0) {
		       out.println("Neprihlásený user");
		       vypisNeopravnenyPristup(out);
		   }
		   return id;
		}
	 
	 private void vymazPolozku(PrintWriter out, HttpServletRequest request) {
	        try {
	            Statement stmt = con.createStatement();
	            String id = (String)request.getParameter("ID");
	            String sql = "DELETE FROM kosik WHERE ID = " + id;
	            int pocet = stmt.executeUpdate(sql);
	      
	        } catch (Exception e) { out.println(e);}
		    }
	 
	 private void vymazKosik(PrintWriter out, HttpServletRequest request) {
	        try {
	            Statement stmt = con.createStatement();
	            int idUser = getLogedUser(request, out);
	            String id = (String)request.getParameter("ID");
	            String sql = "DELETE FROM kosik WHERE ID_pouzivatela = " + idUser;
	            int pocet = stmt.executeUpdate(sql);
	       
	        } catch (Exception e) { out.println(e);}
		    }
	 
	 private void objednaj(PrintWriter out,HttpServletRequest request) {
	        try {
	        	final String obj_cislo = UUID.randomUUID().toString().replace("-", "");
	        	sum=0;
	        	 HttpSession ses = request.getSession();
				   Integer zlava = (Integer)ses.getAttribute("zlava");
				   int id = getLogedUser(request, out);
				   int aktCena = 0;  
				   int idObj=1;
				   boolean prazdny=false;
				   
				   try {
					      Statement stmtID = con.createStatement();
					      ResultSet rs = stmtID.executeQuery("SELECT max(ID) FROM obj_zoznam");
					     
					      while (rs.next()) {
					    	  idObj = rs.getInt("max(ID)")+1;
					    	  }
					    	  rs.close(); 
					    	  stmtID.close();    	  
					    	} catch (Exception e) {out.println(e.getMessage());}
				   
				   
				   try {
				      Statement stmt = con.createStatement();
				      ResultSet rs = stmt.executeQuery("select * from kosik WHERE ID_pouzivatela= "+id);
				    /*  if (rs.next() == false) {
				          prazdny=true;
				        }*/

				      while (rs.next()) {
				    	  aktCena = rs.getInt("cena");
				    	  int kuscena = rs.getInt("ks")*aktCena;
				    	  sum=sum+kuscena;	 
				    	     Statement stmt2 = con.createStatement();
				    	     String sql = "INSERT INTO obj_polozky (ID_objednavky, ID_tovaru, cena,ks) VALUES (";
				 	            sql += "'" + idObj + "', ";
				 	           sql += "'" + rs.getString("ID_tovaru") + "', ";
					 	            sql += "'" + aktCena + "', "; 
					 	            sql += "'" + rs.getInt("ks") + "') ";
					 	           int pocet = stmt2.executeUpdate(sql);
					 	           System.out.println(pocet);
					 	  
					 	           stmt2.close();  				 	           
				    	  }
				    	  rs.close(); 
				    	  stmt.close();    	  
				    	} catch (Exception e) {out.println(e.getMessage());}	  
				   
				   
	            String stav = "odoslaná";
	            String now = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);
	            
	            
	            if(prazdny) {
	            	 out.print("Kosik je prazdny");
	            }
	            else {
	            	Statement stmt3 = con.createStatement();
		           	 String sql = "INSERT INTO obj_zoznam (obj_cislo, datum_objednavky, ID_pouzivatela, suma, stav) VALUES (";
	 	            sql += "'" + obj_cislo + "', ";
	 	           sql += "'" + now + "', ";
		 	            sql += "'" + id + "', "; 
		 	            sql += "'" + sum + "', "; 
		 	            sql += "'" + stav + "') ";
		 	            int pocet = stmt3.executeUpdate(sql);
		 	           stmt3.close();  	           
		 	           vymazKosik(out, request);
		 	          out.print("Bola vytvorená objkednávka");
	            }
	        } catch (Exception e) { out.println(e);}
	        
	        
	        
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
		     int id = getLogedUser(request, out);
		     if (id == 0) return;
		     createHTMLHead(out, request);		
		     if (operacia.equals("vyhod")) { vymazPolozku(out, request); }
		     if (operacia.equals("objednaj")) { objednaj(out, request); }
		     createHeader(out, request);
		     createBody(out, request);
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
