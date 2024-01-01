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
 * Servlet implementation class zoznamObjServlet
 */
public class zoznamObjServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String URL = "jdbc:mysql://localhost/E_SHOP_AK";
	String login = "root";
	String pwd = "";
	String errorMessage = "";
	Connection con;
	Guard g;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public zoznamObjServlet() {
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
			   int id=getLogedUser(request, out);
			   try {
			      Statement stmt = con.createStatement();
			      ResultSet rs = stmt.executeQuery("SELECT * FROM obj_zoznam WHERE ID_pouzivatela="+id);
			      out.println("<div class='d-flex flex-column justify-content-center align-items-center'>");	
				   out.println("<h2>Zoznam objednávok</h2>");
			      while (rs.next()) {
			    	    out.println("<form action='zoznamObjServlet' method='post'>");
			    	    out.println("<input type='hidden' name='IDobjednavky' value= '" +
		    	                 rs.getString("ID")+"'>");
			    	    out.println("<input type='hidden' name='obj_cislo' value= '" +
			    	                 rs.getString("obj_cislo")+"'>");
			    	    out.println("<input type='hidden' name='datum_objednavky' value='"+
			    	    		rs.getString("datum_objednavky")+"'>");
			    	    out.println("<input type='hidden' name='suma' value='"+
			    	    		rs.getString("suma")+"'>");
			    	    out.println("<input type='hidden' name='stav' value='"+
			    	    		rs.getString("stav")+"'>");
			    	    out.println("<input type='hidden' name='operacia' value='zobrazPolozky'>");
			    	    out.println("<input type='submit' class='btn btn-primary align-self-center mx-3' value='Položky objednávky'>");
			    	    out.println("<strong>"+rs.getString("obj_cislo")+
			    	                            ": </strong>"+rs.getString("suma") +" EUR, dátum objednávky: "+rs.getString("datum_objednavky")+", stav: "+rs.getString("stav"));
			    	    out.println("</form><hr>");
			    	  }
			    	  rs.close(); 
			    	  stmt.close();
		
			 
			    	  
			    	} catch (Exception e) {out.println(e.getMessage());}
			   out.println("</div>");
	 }
	 
	 private void zobrazPolozky(PrintWriter out,  
			 HttpServletRequest request) {
			   HttpSession ses = request.getSession();
			   String IDobj = request.getParameter("IDobjednavky");
			   int id=getLogedUser(request, out);
			   out.println("<div class='d-flex flex-column justify-content-center align-items-center'>");	
			   out.println("<h2>Položky objednávky</h2>");
			   try {
			      Statement stmt = con.createStatement();
			      ResultSet rs = stmt.executeQuery("SELECT obj_polozky.ID, obj_polozky.ID_tovaru, obj_polozky.cena, obj_polozky.ks, sklad.nazov FROM obj_polozky INNER JOIN sklad ON obj_polozky.ID_tovaru=sklad.ID WHERE obj_polozky.ID_objednavky="+IDobj);
			      while (rs.next()) {
			    	    out.println("<form action='zoznamObjServlet' method='post'>");
	
			    	    out.println("<p class='mx-3'><strong>"+rs.getString("nazov")+
			    	                            ": </strong>"+rs.getString("cena") +" EUR, počet kusov: "+rs.getString("ks")+"</p>");
			    	    out.println("</form><hr>");
			    	  }
			    	  rs.close(); 
			    	  stmt.close();
		
			    	   out.println("<form action='zoznamObjServlet' method='post'>");
					   out.println("<input type='hidden' name='operacia' value='show'>");
					   out.println("<input type='submit' class='btn btn-primary align-self-center mx-3' value='Naspäť'>");
					   out.println("</form>");
			    	  
			    	} catch (Exception e) {out.println(e.getMessage());}
			   out.println("</div>");
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
		     createHTMLHead(out, request);
		     createHeader(out, request);

		     int id = getLogedUser(request, out);
		     if (id == 0) return;
		   
		     if (operacia.equals("zobrazPolozky")) { zobrazPolozky(out, request);}
		     else  createBody(out, request);
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
