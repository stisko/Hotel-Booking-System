package mainpack;

import java.sql.*;
import java.util.*;

public class Q3 {
	
	public void getStats(Connect conn, int emul_days) throws SQLException{
		PreparedStatement pst,pst2,pst3;
		long time_after,time_before,time_whole=0;
		ResultSet result;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		Vector<String> vec_typename=new Vector<String>();
		
		pst=conn.conn.prepareStatement("select typename from roomtype");
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		while(result.next()){	vec_typename.add(result.getString(1));	}
		
		String typename;
		
		for(int i=0;i<10;i++){
			typename= vec_typename.get(p.getRandInt(vec_typename.size()));
			pst=conn.conn.prepareStatement("select q3('"+typename +"')");
			time_before= System.currentTimeMillis();
			pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			
		}
	}
	
}
