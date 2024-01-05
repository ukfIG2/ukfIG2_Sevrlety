package ukf.sk;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
    private static String URL = "jdbc:mysql://localhost/E_SHOP_AL";
    private static String login = "root";
    private static String pwd = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, login, pwd);
    }
}
