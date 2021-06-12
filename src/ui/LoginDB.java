package ui;

import java.sql.*;
import java.util.*;

public class LoginDB {
	Connection conn=null;
	PreparedStatement pstmt = null;
	Statement stmt = null;
   	ResultSet rs = null;
   	ArrayList<User> plist = new ArrayList<User>();
   	
   	void getConnaction() {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://www.antsstory.site:23306/aliceDB";
			String id = "kjy";
			String pwd = "alice1234 ";
 
			try {
				conn = DriverManager.getConnection(url, id, pwd);
			} catch (SQLException e) {

				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	} 
}
