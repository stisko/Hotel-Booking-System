package mainpack;


import java.sql.*;
public class Connect {
	
	public Connection conn;
	private String db;
	
	
	public Connect(String db) throws Exception{
		
		this.db=db;
		//Trying to get the driver
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("org.postgresql.Driver");
			
		}
		catch (java.lang.ClassNotFoundException e) {
			java.lang.System.err.print("ClassNotFoundException: Postgres Server JDBC ");
			java.lang.System.err.println(e.getMessage());
			throw new Exception("No JDBC Driver found in Server");
		}
		
		//Trying to connectpostgresql:/
		try {
			
			String mysqlURL= "jdbc:postgresql://localhost:5432/"+db+"?searchpath=public";
			conn = DriverManager.getConnection(mysqlURL,"postgres","1111");
     		
			
			//conn.setCatalog(db);
			System.out.println("Connection with: "+db+"!!");
		}
		catch (SQLException E) {

			java.lang.System.out.println("SQLException: " + E.getMessage());
			java.lang.System.out.println("SQLState: " + E.getSQLState());
			java.lang.System.out.println("VendorError: " + E.getErrorCode());
		
		}
	}
	
	
	//Close Conn
	public void close() throws SQLException{
		
		try {
			conn.close();
			System.out.println("Connection close ");
		} catch (SQLException E) {
			
			
			java.lang.System.out.println("SQLException: " + E.getMessage());
			java.lang.System.out.println("SQLState: " + E.getSQLState());
			java.lang.System.out.println("VendorError: " + E.getErrorCode());
			throw E;
		}
		
	}
	
	

}
