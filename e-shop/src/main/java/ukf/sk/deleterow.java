package ukf.sk;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class deleterow {
	private Connection con ;
	
  public deleterow  (String string)
  {
	  String sql = "delete from kosik where ID = "+ string;
	  try {
		con =connect.getConnection();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  Statement stmt;
			try {
				stmt = con.createStatement();
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				
			}
	  
  }
  

   
}
