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
 * Servlet implementation class registerServlet
 */
public class registerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String URL = "jdbc:mysql://localhost/E_SHOP_AK";
	String login = "root";
	String pwd = "";
	String errorMessage = "";
	Connection con = null;
	Guard g;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registerServlet() {
        super();
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
	  
	 
	 
	 private void createBody(PrintWriter out,  
			 HttpServletRequest request) {
			   HttpSession ses = request.getSession();
		
			   out.println("<div class='d-flex flex-column justify-content-center align-items-center my-5'>");
			   out.println("<h2 class='mb-2'>Zaregistrujte sa</h2>");
			    	    out.println("<form action=registerServlet method=post>");
			    	    out.println("<div class='mb-3'>");
			    	    out.println("<input type=hidden name=operacia value=suc>");
			    	    out.println("<label for=login class='form-label'>Email</label><br>");
			    	    out.println("<input type=email name=login id='login' class='form-control'>");  
			    	    out.println(" </div>");
			    	    
			    	    out.println("<div class='mb-3'>");
			    	    out.println("<label for=pwd class='form-label'>Heslo</label><br>");
			    	    out.println("<input type=text name=pwd id='pwd' class='form-control'>");
			    	    out.println("</div>");
			    	    
			    	    out.println("<div class='mb-3'>");
			    	    out.println("<label for=adresa class='form-label'>Adresa</label><br>");
			    	    out.println("<input type=text name=adresa id='adresa' class='form-control'>");
			    	    out.println("</div>");
			    	    out.println("<div class='mb-3'>");
			    	    out.println("<label for=meno class='form-label'>Meno</label><br>");
			    	    out.println("<input type=text name=meno id='meno' class='form-control'>");
			    	    out.println("</div>");
			    	    out.println("<div class='mb-3'>");
			    	    out.println("<label for=priezvisko class='form-label'>Priezvisko</label><br>");
			    	    out.println("<input type=text name=priezvisko id='priezvisko' class='form-control'>");
			    	    out.println("</div>");
			    	    out.println("<div class='mb-3'>");
			    	    out.println("<input type=submit class='btn btn-primary align-self-center' value=Zaregistrovať sa>");
			    	    out.println("</div>");
			    	
			    	    out.println("</form><hr>");
			    	  
		            	 out.println("<form action='index.html' method='post'>");
		            	 out.println("<div class='mb-3'>");
			      		   out.println("<input type='hidden' name='operacia' value='register'>");
			      		   out.println("<input type='submit' class='btn btn-primary align-self-center' value='Vrátiť sa'>");
			      		 out.println("</div>");
			      		   out.println("</form><hr>");
			      		 out.println("</div>");
			    

	 }
	 
	 protected boolean checkLogin(PrintWriter out,String login) {
		 boolean nachadza=false;
		 try {
			  Statement stmt = con.createStatement();
			  ResultSet rs = stmt.executeQuery("select * from users");
			  while (rs.next()) {
		    	  	if(rs.getString("login").equals(login)) nachadza=true;
		    	  }
		    	  rs.close(); 
		    	  stmt.close();
		
		} catch (Exception e) { out.println(e);}
		return nachadza;
	 }
	 
	 private void registruj(PrintWriter out,HttpServletRequest request) {
	        try {
	            Statement stmt = con.createStatement();
	            String login = (String)request.getParameter("login");
	            String pwd = (String)request.getParameter("pwd");
	            String adresa = (String)request.getParameter("adresa");
	            String meno = (String)request.getParameter("meno");
	            String priezvisko = (String)request.getParameter("priezvisko");
	            String zlava = "0";
	            String je_admin = "0";
	            String poznamky = "";
	            if(checkLogin(out, login)) {
	            	out.print("Zvoleny email je uz pouzity, skuste pouzit iny email");
	            	
	            }
	            else {
	            	 String sql = "INSERT INTO users (passwd, login, adresa, zlava, meno, priezvisko, poznamky,je_admin) VALUES (";
	 	            sql += "'" + pwd + "', ";
	 	            sql += "'" + login + "', "; 
	 	            sql += "'" + adresa + "', "; 
	 	            sql += "'" + zlava + "', "; 
	 	            sql += "'" + meno + "', "; 
	 	            sql += "'" + priezvisko + "', "; 
	 	            sql += "'" + poznamky + "', "; 
	 	            sql += "'" + je_admin + "') ";
	 	            int pocet = stmt.executeUpdate(sql);
	 	            out.print("Registrácia úspešná.<br/>");
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
		     if (operacia.equals("suc")) { registruj(out, request); }
		     
		     createHTMLHead(out, request);
		    
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
