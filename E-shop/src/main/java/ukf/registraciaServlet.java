package ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class registraciaServlet extends HttpServlet {
    Connection con;
    String errorMessage = "";

    public registraciaServlet() {
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
    
    private boolean isValidEmail(String login) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(login);
        return matcher.matches();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String meno = request.getParameter("meno");
        String priezvisko = request.getParameter("priezvisko");
        String adresa = request.getParameter("adresa");
        String login = request.getParameter("login");
        String heslo = request.getParameter("heslo");

        try {
            if (badConnection(out)) {
                return;
            }
            
            if (!isValidEmail(login)) {
                out.println("<p>Login musí byť vo formáte emailovej adresy (napr. abc@xyz.sk).</p>");
                out.println("<a href='index.html'>Skuste to znova</a>");
                return;
            }

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM users WHERE login = ?");
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                out.println("<p>Používateľ s týmto loginom už existuje.</p>");
                out.println("<a href='index.html'>Skuste to znova</a>");
            } else {
                PreparedStatement insertStatement = con.prepareStatement("INSERT INTO users (meno, priezvisko, adresa, login, passwd) VALUES (?, ?, ?, ?, ?)");
                insertStatement.setString(1, meno);
                insertStatement.setString(2, priezvisko);
                insertStatement.setString(3, adresa);
                insertStatement.setString(4, login);
                insertStatement.setString(5, heslo); 
                insertStatement.executeUpdate();

                out.println("<p>Registrácia prebehla úspešne.</p>");
                out.println("<a href='index.html'>Prihlaste sa</a>");
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
