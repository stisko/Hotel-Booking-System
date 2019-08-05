package mainpack;

import java.sql.*;
import java.util.Date;
import java.util.*;

public class Q2 {

	public void find_duplicate_people(Connect conn, int emul_days) throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		Date date= new Date();
		Vector<Date> vec_reservationdate=new Vector<Date>();
		//86400000
		long time_after,time_before,time_whole=0;
		
		pst=conn.conn.prepareStatement("select distinct reservationdate from hotelbooking where reservationdate<=(current_day+ "+emul_days+")");
		
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		
		while(result.next()){	vec_reservationdate.add(result.getDate(1));	}
		
		for(int i=0;i<100;i++){
			Date reservationdate= vec_reservationdate.get(p.getRandInt(vec_reservationdate.size()));
			pst=conn.conn.prepareStatement("select q2('"+reservationdate+"')");
			
			time_before= System.currentTimeMillis();
			pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			
		}
		System.out.println(time_whole+" ms");
	}
	
	
}
