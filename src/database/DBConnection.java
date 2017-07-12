package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
public static Connection getConnection() throws Exception{
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/fertilizer", "root", "root");		
	return con;
}
public static void ConnectionClose(Connection con) throws Exception {
	con.close();
}
}
