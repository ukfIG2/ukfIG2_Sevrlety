


// radsej by som si to rozdelil do viac servletov ale da sa to aj takto
package ukf;
import jakarta.servlet.ServletConfig;
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


public class servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private Connection con;
     String url = "jdbc:mysql://localhost/db_app";
 		String name = "root";
 		String pwd = "";
 	
   
    public servlet() {
    }

	
	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,name,pwd);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void destroy() {
		 try {
		        if (con != null) {
		            con.close();
		        }
		    } catch (Exception e) {
		    	System.out.println(e.getMessage());
		    }
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		String operacia = request.getParameter("operacia");
		
		if(operacia == null) {
			operacia = "vypis"; // aby sa nemuselo kilkat na tlacidlo a nepotrebujem pouzit ani jeden html subor
			 
		}
			if(operacia.equals("vypis")) {
				vypis(out);
				
			}
			if(operacia.equals("tab1")) {
				tab1(out);
				
			}
			if(operacia.equals("tab2")) {
				tab2(out);
				
			}
			 if (operacia.equals("mazanie1")) {
		            vymazZTab1(out, request.getParameter("id"));
		            
		        }
			 if (operacia.equals("mazanie2")) {
		            vymazZTab2(out, request.getParameter("id"));
		            
		        }
			 if (operacia.equals("edit1")) {
				 	editTab1(out, request.getParameter("id"));
		            
		        }
			 if (operacia.equals("edit2")) {
		            editTab2(out, request.getParameter("id"));
		            
		        }
			 if (operacia.equals("update1")) {
				 String id = request.getParameter("id");
			     String meno = request.getParameter("meno");
			     String ico = request.getParameter("ico");
			     String adresa = request.getParameter("adresa");
		            up1(out,id, meno, ico, adresa);
			 }
			 if (operacia.equals("update2")) {
				 String id = request.getParameter("id");
				 String nazov = request.getParameter("nazov");
				 String cena = request.getParameter("cena");
				 String hodnotenie = request.getParameter("hodnotenie");
				 	up2(out,id,nazov,cena,hodnotenie);
			 }   
			 if (operacia.equals("insert1")) {
				 String meno = request.getParameter("meno");
				 String ico = request.getParameter("ico");
				 String adresa = request.getParameter("adresa");
				 	in1(out,meno,ico,adresa);
			 }
			 if (operacia.equals("insert2")) {
				 String nazov = request.getParameter("nazov");
				 String cena = request.getParameter("cena");
				 String hodnotenie = request.getParameter("hodnotenie");
				 	in2(out,nazov,cena,hodnotenie);
			 }
			 if (operacia.equals("insert_main")) {
				    insertMain(out);
				}
			 if (operacia.equals("in_main")) {
				 String idZakaznici = request.getParameter("id_zakaznici");
				 String idTovar = request.getParameter("id_tovar");
				 String datum = request.getParameter("datum");
				    inMain(out,idZakaznici,idTovar,datum);
				}
			 if (operacia.equals("deleteMain")) {
				 String idObjednavka = request.getParameter("id_objednavka");
				    delMain(out,idObjednavka);
				}
			 if (operacia.equals("editMain")) {
				 String idObjednavka = request.getParameter("id_objednavka");
				    editMain(out,idObjednavka);
				}
			 if (operacia.equals("upMain")) {
				 String idObjednavka = request.getParameter("id_objednavka");
				 String idZakaznici = request.getParameter("idZakaznici");
				 String meno = request.getParameter("meno");
				 String ico = request.getParameter("ico");
				 String adresa = request.getParameter("adresa");
				 String idTovar = request.getParameter("idTovar");
				 String nazov = request.getParameter("nazov");
				 String cena = request.getParameter("cena");
				 String hodnotenie = request.getParameter("hodnotenie");
				 String datum = request.getParameter("datum");
				 upMain(out, idZakaznici, meno, ico,adresa,idTovar,nazov,cena,hodnotenie,datum, idObjednavka);
				}
			 
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void vypis(PrintWriter out) {
		out.print("<h1>OBJEDNÁVKY</h1>");
		out.print("<hr>");
		out.print("<br>");	
		
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT zakaznici.meno, zakaznici.ico , zakaznici.adresa, tovar.nazov, tovar.cena, tovar.hodnotenie, objednavky.datum,objednavky.id_objednavka "
				    + "FROM tovar "
				    + "JOIN objednavky ON tovar.id_tovar = objednavky.id_tovar "
				    + "JOIN zakaznici ON objednavky.id_zakaznici = zakaznici.id_zakaznici;";
			
				ResultSet rs = stmt.executeQuery(sql);
				
				out.print("<table border='1'>");
				out.print("<tr>");
				out.print("<th>Meno</th>");
				out.print("<th>Ičo</th>");
				out.print("<th>Adresa</th>");
				out.print("<th>Tovar</th>");
				out.print("<th>Cena</th>");
				out.print("<th>Hodnotenie</th>");
				out.print("<th>Dátum</th>");
				out.print("<th>Upraviť</th>");
				out.print("<th>Vymazat</th>");
				out.print("</tr>");
				
			while(rs.next()) {
				String ic = rs.getString("ico");
				if(ic == null||ic.equals("")) {
					ic = "[nemá ičo] ";
				}
				 out.print("<td>" + rs.getString("meno") + "</td>");
				    out.print("<td>" + ic + "</td>");
				    out.print("<td>" + rs.getString("adresa") + "</td>");
				    out.print("<td>" + rs.getString("nazov") + "</td>");
				    out.print("<td>" + rs.getFloat("cena") + "</td>");
				    out.print("<td>" + rs.getFloat("hodnotenie") + "</td>");
				    out.print("<td>" + rs.getDate("datum") + "</td>");
				    out.print("<td> "
				    		+ "<form action='servlet' method='post'>"
				    		+ "<input type='hidden' name='id_objednavka' value='" + rs.getInt("id_objednavka") + "'>"
				    		+ "<input type=hidden value='editMain' name='operacia'>"
				    		+ "<input type='submit' value='Upravit'>"
				    		+ "</form>"
				    		+ "</td>");
				    out.print("<td> "
				    		+ "<form action='servlet' method='post'>"
				    		+ "<input type='hidden' name='id_objednavka' value='" + rs.getInt("id_objednavka") + "'>"
				    		+ "<input type=hidden value='deleteMain' name='operacia'>"
				    		+ "<input type='submit' value='Vymazat'>"
				    		+ "</form>"
				    		+ "</td>");
				    out.print("</tr>");
			}
			out.print("</table>");
			out.print("<hr>");
	
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='tab1' name='operacia'>");
			out.print("<input type='submit' value='Zakaznici'>");
			out.print("</form>");
			
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='tab2' name='operacia'>");
			out.print("<input type='submit' value='Tovar'>");
			out.print("</form>");
			
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='insert_main' name='operacia'>");
			out.print("<input type='submit' value='Pridať do tabuľky'>");
			out.print("</form>");
			stmt.close();
			out.close();
		} catch (Exception e) {
			out.print("Chyba v SQL: "+ e.toString());
		}
	}
	protected void insertMain(PrintWriter out) {
		out.print("<h2>Pridanie noveho zaznamu</h2>");out.print("<hr>");out.print("<br>");
		out.print("<form action='servlet' method='post'>");

		try {
			
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM zakaznici";
			ResultSet rs = stmt.executeQuery(sql);
			out.print("Zakaznik: <select name='id_zakaznici'>");
			while(rs.next()) {
				int idZakaznici = rs.getInt("id_zakaznici");
	            out.print("<option value='"+idZakaznici+"'>" + rs.getString("meno") +"</option>");
			}
			 out.print("</select><br>");
			 
			
		}catch(Exception e) {
			out.print("chyba" + e.toString());
		}
	   
	    
		out.print("Tovar: <select name='id_tovar'>");
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM tovar";
			ResultSet rs = stmt.executeQuery(sql);
			int idTovar;
			while(rs.next()) {
				 idTovar = rs.getInt("id_tovar");

	            out.print("<option value='"+idTovar+"'>" + rs.getString("nazov") + "</option>");
			}
			 out.print("</select><br>");
			 
			out.print("Datum: <input type='date' name='datum'>");out.print("<br>");
			out.print("<input type='hidden' value='in_main' name='operacia'>");
			out.print("<input type='submit' value='pridat zaznam'>");
			stmt.close();
			out.close();
		}catch(Exception e) {
			out.print("chyba"  + e.toString());
		}
		out.print("</form>");
	}
	protected void inMain(PrintWriter out, String idZakaznici, String idTovar, String datum) {
		try {
			Statement stmt = con.createStatement();
			
			String sql = "INSERT INTO objednavky (id_zakaznici, id_tovar, datum) VALUES ('"+idZakaznici+"', '"+idTovar+"', '"+datum+"')";
			int pocet = stmt.executeUpdate(sql);
			if(pocet == 1) {
				out.print("Zaznam bol uspesne pridany!");  
				 out.print("<form action='servlet' method='post'>");
				 out.print("<input type='hidden' value='vypis' name='operacia'>");
				 out.print("<input type='submit' value='Domov'>");
				 out.print("</form>");
			}else {
				out.print("Zaznam nebol pridany!");
				 out.print("<form action='servlet' method='post'>");
				 out.print("<input type='hidden' value='vypis' name='operacia'>");
				 out.print("<input type='submit' value='Domov'>");
				 out.print("</form>");
			}
			
			stmt.close();
			out.close();
		}catch(SQLException e) {
			
			out.print("Chyba: " +e.toString());
		}
	}
	protected void delMain(PrintWriter out, String idObjednavka) {
		try {
			Statement stmt = con.createStatement();
			String sql = "DELETE FROM objednavky WHERE id_objednavka = '"+idObjednavka+"';";
			int pocet = stmt.executeUpdate(sql);
			if(pocet == 1) {
				out.print("Vzmazanie bolo uspesne");
				
				 out.print("<form action='servlet' method='post'>");
				 out.print("<input type='hidden' value='vypis' name='operacia'>");
				 out.print("<input type='submit' value='Domov'>");
				 out.print("</form>");
			}else {
				out.print("Nastala chyba! Vymazanych zaznamov: '"+pocet+"'");
			}
			stmt.close();
			out.close();
		}catch(Exception e) {
			out.print("Chyba: "+e.toString());
		}
	}
	
	protected void editMain(PrintWriter out, String idObjednavka) {		
	    out.print("<h2>Update zaznamu</h2>");
	    out.print("<hr>");
	    out.print("<br>");

	    try {
	    	
	        String sql = "SELECT * FROM tovar JOIN objednavky ON tovar.id_tovar = objednavky.id_tovar "
	                + "JOIN zakaznici ON objednavky.id_zakaznici = zakaznici.id_zakaznici "
	                + "WHERE objednavky.id_objednavka = '" + idObjednavka + "'";

	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	            out.print("<form action='servlet' method='post'>");
	            out.print("<input type='hidden' name='id_zakaznici' value='" + rs.getInt("id_zakaznici") + "'>");

	            out.print("Meno: <select name='meno'>");
	            Statement menoStmt = con.createStatement();
	            ResultSet menoRs = menoStmt.executeQuery("SELECT meno FROM zakaznici");
	            while (menoRs.next()) {
	                String menoOption = menoRs.getString("meno");
	                if (menoOption.equals(rs.getString("meno"))) {
	                    out.print("<option value='"+menoOption+"' selected>"+menoOption+ "</option>");
	                } else {
	                    out.print("<option value='"+menoOption+"'>"+menoOption+"</option>");
	                }
	            }
	            menoRs.close();
	            menoStmt.close();
	            out.print("</select>");
	            out.print("Ico: <select name='ico'>");
	            Statement icoStmt = con.createStatement();
	            ResultSet icoRs = icoStmt.executeQuery("SELECT ico FROM zakaznici");
	            while (icoRs.next()) {
	                String icoOption = icoRs.getString("ico");
	                if (icoOption.equals(rs.getString("ico"))) {
	                    out.print("<option value='"+icoOption+"' selected>"+icoOption+"</option>");
	                } else {
	                    out.print("<option value='"+icoOption+"'>" +icoOption+"</option>");
	                }
	            }
	            icoRs.close();
	            icoStmt.close();
	            
	            out.print("</select>");

	            out.print("Adresa: <select name='adresa'>");
	            Statement adresaStmt = con.createStatement();
	            ResultSet adresaRs = adresaStmt.executeQuery("SELECT adresa FROM zakaznici");
	            while (adresaRs.next()) {
	                String adresaOption = adresaRs.getString("adresa");
	                if (adresaOption.equals(rs.getString("adresa"))) {
	                    out.print("<option value='" + adresaOption + "' selected>" + adresaOption + "</option>");
	                } else {
	                    out.print("<option value='" + adresaOption + "'>" + adresaOption + "</option>");
	                }
	            }
	            adresaRs.close();
	            adresaStmt.close();
	            out.print("</select>");

	            out.print("Nazov tovaru: <select name='nazov'>");
	            Statement nazovStmt = con.createStatement();
	            ResultSet nazovRs = nazovStmt.executeQuery("SELECT nazov FROM tovar");
	            while (nazovRs.next()) {
	                String nazovOption = nazovRs.getString("nazov");
	                if (nazovOption.equals(rs.getString("nazov"))) {
	                    out.print("<option value='" + nazovOption + "' selected>" + nazovOption + "</option>");
	                } else {
	                    out.print("<option value='" + nazovOption + "'>" + nazovOption + "</option>");
	                }
	            }
	            nazovRs.close();
	            nazovStmt.close();
	            out.print("</select>");
	            out.print("Cena tovaru: <input type='text' name='cena' value='" + rs.getFloat("cena") + "'>");
	            out.print("Hodnotenie tovaru: <input type='text' name='hodnotenie' value='" + rs.getFloat("hodnotenie") + "'>");
	            out.print("Datum: <input type='date' name='datum' value='" + rs.getDate("datum") + "'>");
	            out.print("<input type='hidden' name='id_objednavka' value='"+idObjednavka+"'>");
	            out.print("<input type='hidden' name='operacia' value='upMain'>");
	            out.print("<input type='submit' value='Upravit'>");
	            out.print("</form>");
	        }

	        stmt.close();
	        out.close();
	    } catch (Exception e) {
	        out.print("chyba" + e.toString());
	    }
	}

	protected void upMain(PrintWriter out, String idZakaznici, String meno, String ico,String  adresa, String idTovar, String nazov, String cena, String hodnotenie, String datum, String  idObjednavka) { 
	    try {
	        Statement stmt = con.createStatement();

	       	        String sql = "UPDATE objednavky " +
	                     "JOIN tovar ON objednavky.id_tovar = tovar.id_tovar " +
	                     "JOIN zakaznici ON objednavky.id_zakaznici = zakaznici.id_zakaznici " +
	                     "SET " +
	                     "zakaznici.meno = '" + meno + "', " +
	                     "zakaznici.ico = '" + ico + "', " +
	                     "zakaznici.adresa = '" + adresa + "', " +
	                     "tovar.nazov = '" + nazov + "', " +
	                     "tovar.cena = '" + cena + "', " +
	                     "tovar.hodnotenie = '" + hodnotenie + "', " +
	                     "objednavky.datum = '" + datum + "' " +
	                     "WHERE objednavky.id_objednavka = '" + idObjednavka + "'";

	        int pocet = stmt.executeUpdate(sql);

	        if (pocet == 3) { // dlho mi trvalo kym som prisiel na to preco 3 :)
	            out.print("Update sa vykonal spravne!");
	            
	            out.print("<form action='servlet' method='post'>");
				out.print("<input type='hidden' value='vypis' name='operacia'>");
				out.print("<input type='submit' value='Domov'>");
				out.print("</form>");
	        } else {
	            out.print("Chyba! Pocet vykonanych dotazov: "+pocet+ " "+ "id: "+idObjednavka);
	            out.print("<form action='servlet' method='post'>");
				out.print("<input type='hidden' value='vypis' name='operacia'>");
				out.print("<input type='submit' value='Domov'>");
				out.print("</form>");
	        }
	    } catch (Exception e) {
	        out.print("Chyba: " + e.toString());
	    }
	}
	protected void tab1(PrintWriter out) {
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM zakaznici ";
				   
		
				ResultSet rs = stmt.executeQuery(sql);
				
				out.print("<h1>ZÁKAZNÍCI</h1>");
				out.print("<hr>");
				out.print("<br>");	
				
				
			    out.print("<table border='1'>");
			    out.print("<tr>");
			    out.print("<th>Meno</th>");
			    out.print("<th>ičo</th>");
			    out.print("<th>Adresa</th>");
			    out.print("<th>Mazanie/Edit</th>");
			    out.print("</tr>");

			    while(rs.next()) {
			    	String ic = rs.getString("ico");
					if(ic == null ||ic.equals("")) {
						ic = "[nemá ičo] ";
					}
					int id = rs.getInt("id_zakaznici");
					
			        out.print("<tr>");
			        out.print("<td>" + rs.getString("meno") + "</td>");
			        out.print("<td>" + ic + "</td>");
			        out.print("<td>" + rs.getString("adresa") + "</td>");
			        out.print("<td>");
			        out.print("<form action='servlet' method='post'>");
			        out.print("<input type='hidden' name='id' value='"+id+"'>");
			        out.print("<input type='hidden' name='operacia' value='mazanie1'>");
			        out.print("<input type='submit' value='vymaz'>");
			        out.print("</form>");
			        out.print("<form action='servlet' method='post'>");
			        out.print("<input type='hidden' name='id' value='"+id+"'>");
			        out.print("<input type='hidden' name='operacia' value='edit1'>");
			        out.print("<input type='submit' value='editovat'>");
			        out.print("</form>");
			        out.print("</td>");
			        out.print("</tr>");
			    }

			    out.print("</table>");
		

			out.print("<hr><br>");
			out.print("<h3>PRIDAŤ ZAKAZNÍKA</h3>");
			out.print("<br>");
			out.print("<form action='servlet' method='post'>");
			out.print("Meno: <input type='text' name='meno'>");
			out.print(" ICO: <input type='text' name='ico' >");
			out.print(" Adresa: <input type='text' name='adresa' >");
		    out.print("<input type='hidden' name='operacia' value='insert1'>");
			out.print("<input type='submit' value='Pridat Zaznam'>");
			out.print("</form><br>");
			stmt.close();
			out.close();
		} catch (SQLException e) {
			out.print("Chyba v SQL: "+ e.toString());
		}
	}
	protected void tab2(PrintWriter out) {
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM tovar ";
				ResultSet rs = stmt.executeQuery(sql);
				
				out.print("<h1>TOVAR</h1>");
				out.print("<hr>");
				out.print("<br>");	
				
				
			    out.print("<table border='1'>");
			    out.print("<tr>");
			    out.print("<th>Tovar</th>");
			    out.print("<th>Cena</th>");
			    out.print("<th>Hodnotenie</th>");
			    out.print("<th>Mazanie/Edit</th>");
			    out.print("</tr>");
			    while(rs.next()) {
			        int id = rs.getInt("id_tovar");
			        out.print("<tr>");
			        out.print("<td>" + rs.getString("nazov") + "</td>");
			        out.print("<td>" + rs.getString("cena") + "</td>");
			        out.print("<td>" + rs.getString("hodnotenie") + "</td>");
			        out.print("<td>");
			        out.print("<form action='servlet' method='post'>");
			        out.print("<input type='hidden' name='id' value='" + id + "'>");
			        out.print("<input type='hidden' name='operacia' value='mazanie2'>");
			        out.print("<input type='submit' value='vymaz'>");
			        out.print("</form>");
			        out.print("<form action='servlet' method='post'>");
			        out.print("<input type='hidden' name='id' value='" + id + "'>");
			        out.print("<input type='hidden' name='operacia' value='edit2'>");
			        out.print("<input type='submit' value='editovat'>");
			        out.print("</form>");
			        out.print("</td>");
			        out.print("</tr>");
			    }
			    out.print("</table>");
			    
			out.print("<hr><br>");
			out.print("<h3>PRIDAŤ TOVAR</h3>");
			out.print("<br>");
			out.print("<form action='servlet' method='post'>");
			out.print("Nazov: <input type='text' name='nazov'>");
			out.print(" Cena: <input type='text' name='cena' >");
			out.print(" Hodnotenie: <input type='text' name='hodnotenie' >");
		    out.print("<input type='hidden' name='operacia' value='insert2'>");
			out.print("<input type='submit' value='Pridat Zaznam'>");
			out.print("</form><br>");
			stmt.close();
			out.close();
		} catch (Exception e) {
			out.print("Chyba v SQL: "+ e.toString());
		}
	}
	protected void vymazZTab1(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "DELETE zakaznici FROM zakaznici " +
	                "LEFT JOIN objednavky ON zakaznici.id_zakaznici = objednavky.id_zakaznici " +
	                "WHERE zakaznici.id_zakaznici = "+id+" AND objednavky.id_zakaznici IS NULL";
	        
	        int pocet = stmt.executeUpdate(sql);
	        if(pocet == 0) {
	        	out.print("Záznam nemožno vzmazať nakolko už bola vykonaná objednávka s týmto ID!");
	        	
	        	out.print("<form action='servlet' method='post'>");
				out.print("<input type='hidden' value='vypis' name='operacia'>");
				out.print("<input type='submit' value='Domov'>");
				out.print("</form>");
				
	        }else {
	        out.print("Bolo odstranených "+pocet+" záznamov.<br/>");
	        
	        out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
	        }
	        stmt.close();
			out.close();
	    } catch (Exception e) {
	        out.println(e);
	    }
	}
	protected void vymazZTab2(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "DELETE tovar FROM tovar " +
	                "LEFT JOIN objednavky ON tovar.id_tovar = objednavky.id_tovar " +
	                "WHERE tovar.id_tovar = " + id + " AND objednavky.id_tovar IS NULL";
	        
	        int pocet = stmt.executeUpdate(sql);
	        
	        if(pocet == 0) {
	        	out.print("Záznam nemožno vzmazať nakolko už bola vykonaná objednávka s týmto ID!");
	        	
	        	out.print("<form action='servlet' method='post'>");
				out.print("<input type='hidden' value='vypis' name='operacia'>");
				out.print("<input type='submit' value='Domov'>");
				out.print("</form>");
				
	        }else {
	        out.print("Bolo odstranených " + pocet + " záznamov.<br/>");
	        
	        out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
	        }
	        stmt.close();
			out.close();
	    } catch (Exception e) {
	        out.println("Chyba: " + e.toString());
	        out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
	        
	    }
	}
	
	protected void editTab1(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM zakaznici WHERE id_zakaznici = '" + id + "'";
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	            out.print("<form action='servlet' method='post'>");  
	            out.print("<input type='hidden' name ='id' value='" + rs.getInt("id_zakaznici") + "'>");
	            out.print("Meno: <input type='text' name='meno' value='" + rs.getString("meno") + "'>");
	            out.print("Ičo: <input type='text' name='ico' value='" + rs.getString("ico") + "'>");
	            out.print("Adresa: <input type='text' name='adresa' value='" + rs.getString("adresa") + "'>");
	            out.println("<input type='hidden' name='operacia' value='update1'>");
	            out.print("<input type='submit' value='Uložiť zmeny'>");
	            out.print("</form>");
	        }
	    } catch (Exception e) {
	        out.print("Chyba: " + e.toString());
	        out.print("<form action='servlet' method='post'>");
			 out.print("<input type='hidden' value='vypis' name='operacia'>");
			 out.print("<input type='submit' value='Domov'>");
			 out.print("</form>");
	    }
	}
	protected void up1(PrintWriter out, String id,String meno, String ico, String adresa) {
		try {
		Statement stmt = con.createStatement();
		String sql = "UPDATE zakaznici SET meno='" + meno + "', ico='" + ico + "', adresa='" + adresa + "' WHERE id_zakaznici='" + id + "'";
		int pocet = stmt.executeUpdate(sql);
		if(pocet == 0) {
			out.print("Edit neprebehol uspesne.");
			
			 out.print("<form action='servlet' method='post'>");
			 out.print("<input type='hidden' value='vypis' name='operacia'>");
			 out.print("<input type='submit' value='Domov'>");
			 out.print("</form>");
		}else if(pocet == 1){
			out.print("Editacia prebehla uspesne. Pocet upravenych zaznamov: "+pocet);
			 out.print("<form action='servlet' method='post'>");
			 out.print("<input type='hidden' value='vypis' name='operacia'>");
			 out.print("<input type='submit' value='Domov'>");
			 out.print("</form>");
		}
		stmt.close();
		out.close();
	}catch(Exception e) {
		out.print("Chyba: "+e.toString());
	}
	}

	protected void editTab2(PrintWriter out, String id) {
	    try {
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM tovar WHERE id_tovar = '" + id + "'";
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	            out.print("<form action='servlet' method='post'>");
	            out.println("<input type='hidden' name ='id' value='"+rs.getInt("id_tovar")+"'>");
	            out.print("Nazov: <input type='text' name='nazov' value='"+rs.getString("nazov")+"'>");
	            out.print("Cena: <input type='text' name='cena' value='"+rs.getFloat("cena")+"'>");
	            out.print("Hodnotenie: <input type='text' name='hodnotenie' value='"+rs.getFloat("hodnotenie")+"'>");
	            out.println("<input type='hidden' name='operacia' value='update2'>");
	            out.print("<input type='submit' value='Uložiť zmeny'>");
	            out.print("</form>");
	        
	        }
	        stmt.close();
			out.close();
	    } catch (Exception e) {
	        out.print("Chyba: " + e.toString());
	        out.print("<form action='servlet' method='post'>");
			 out.print("<input type='hidden' value='vypis' name='operacia'>");
			 out.print("<input type='submit' value='Domov'>");
			 out.print("</form>");
	    }
	}
	protected void up2(PrintWriter out, String id, String nazov, String cena, String hodnotenie) {
		try {
			Statement stmt = con.createStatement();
			String sql = "UPDATE tovar SET nazov='"+nazov+"', cena='"+cena+"', hodnotenie='"+hodnotenie+"' WHERE id_tovar='"+id+"'";
			int pocet = stmt.executeUpdate(sql);
			if(pocet == 0) {
				out.print("Edit neprebehol uspesne.");
				
				 out.print("<form action='servlet' method='post'>");
				 out.print("<input type='hidden' value='vypis' name='operacia'>");
				 out.print("<input type='submit' value='Domov'>");
				 out.print("</form>");
			}else if(pocet == 1){
				out.print("Editacia prebehla uspesne. Pocet upravenych zaznamov: "+pocet);
				out.print("<form action='servlet' method='post'>");
				out.print("<input type='hidden' value='vypis' name='operacia'>");
				out.print("<input type='submit' value='Domov'>");
				out.print("</form>");
			}
			stmt.close();
			out.close();
		}catch(Exception e) {
			out.print("Uistite sa za su datove typy spravne cena-> cilo atd...");
			 out.print("<form action='servlet' method='post'>");
			 out.print("<input type='hidden' value='vypis' name='operacia'>");
			 out.print("<input type='submit' value='Domov'>");
			 out.print("</form>");
		}
	}
	
	protected void in1(PrintWriter out, String meno, String ico, String adresa) {
		try {
		Statement stmt = con.createStatement();
		if(meno.equals("") || adresa.equals("")) {
			out.print("Je nutne vyplnit povinne polia!");
			
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
		}else {
		if(ico.equals("")) {
			ico="[nemá ičo]";
		}
		String sql = "INSERT INTO zakaznici (meno, ico, adresa) VALUES ('"+meno+"', '"+ico+"', '"+adresa+"');";
		int pocet = stmt.executeUpdate(sql);
		if(pocet == 0) {
			out.print("Nebolo pridane nic.");
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
		}else if(pocet == 1) {
			out.print("Bol pridany jeden zaznam");
			
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
		}
		}
		stmt.close();
		out.close();
		}catch(Exception e) {
			out.print("Chyba: "+e.toString());
		}
	}
	
	protected void in2(PrintWriter out, String nazov, String cena, String hodnotenie) {
		try {
		Statement stmt = con.createStatement();
	
		if(nazov.equals("") || cena.equals("") || hodnotenie.equals("")) {
			out.print("Treba vyplnit vsetky polia!");
		}else {
			String sql = "INSERT INTO tovar (nazov, cena, hodnotenie) VALUES ('"+nazov+"', '"+cena+"', '"+hodnotenie+"')";
			int pocet = stmt.executeUpdate(sql);
		if(pocet == 0) {
			out.print("Nebolo pridane nic.");
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
		}else if(pocet == 1) {
			out.print("Bol pridany jeden zaznam");
			
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
			}
		}
		stmt.close();
		out.close();
		}catch(SQLException e) {
			out.print("Uistite sa ze udaje ktore su zadavane maju spravny typ cena->cislo atd...");
			out.print("<form action='servlet' method='post'>");
			out.print("<input type='hidden' value='vypis' name='operacia'>");
			out.print("<input type='submit' value='Domov'>");
			out.print("</form>");
		}
	}
}


