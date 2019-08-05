package mainpack;

import java.sql.*;
import java.util.Random;
import java.util.Vector;
import java.util.Date;
public class Q1 {
	
	
	public void find_persons_by_criteria(Connect conn, int emul_date) throws SQLException{
		String[] cities= { "Agrinio","Athens","Patra","Thessaloniki","Chania","London","Bristol","Tirana","Berat","Elbasan" };
		PreparedStatement pst;
		int desired_questions=0,all_clients=0;
		
		long time_after,time_before,time_whole=0;
		
		Vector<Integer> vec_idclient=new Vector<Integer>();
		//Vector<String> vec_typename=new Vector<String>();
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		ResultSet result;
		
		
		pst=conn.conn.prepareStatement("select idclient from client ");
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		
		while(result.next()){
			vec_idclient.add(result.getInt(1));
		}
		desired_questions= (int) (0.2*vec_idclient.size());
		
		
		for(int i=0;i<desired_questions;i++){
			int idclient= vec_idclient.get(p.getRandInt(vec_idclient.size()));
			String city = cities[p.getRandInt(cities.length)];
			Date checkin = new Date();
			Date checkout= new Date();
			checkin.setTime(checkin.getTime()+(86400000*p.getRandInt(100))+emul_date*86400000);
			checkout.setTime(checkin.getTime()+86400000*(1+p.getRandInt(5)+emul_date*86400000));
			
			
			pst=conn.conn.prepareStatement("select q1(?,?,?,?)");
			pst.setInt(1, idclient);
			pst.setString(2, city);
			java.sql.Date checkin_dt= new java.sql.Date(checkin.getTime());
			java.sql.Date checkout_dt= new java.sql.Date(checkout.getTime());
			pst.setDate(3, checkin_dt);
			pst.setDate(4, checkout_dt);
			
			
			time_before= System.currentTimeMillis();
			pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			
		}
		System.out.println(time_whole+" ms");
	}
}
