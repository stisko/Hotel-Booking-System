package mainpack;

import java.sql.*;
import java.util.Random;
import java.util.Vector;

public class U2 {
	
	public void insertPerson(Connect conn) throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
		long time_after,time_before,time_whole=0;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		int employees[] = new int[10];
		for(int i=0;i<=9;i++){
			employees[i]=10+i;
		}
		String[] cities= { "Agrinio","Athens","Patra","Thessaloniki","Chania","London","Bristol","Tirana","Berat","Elbasan" };
		String[] countries= {"Greece","United Kingdom","Albania"};
		Date date;
		int doc_client=100000000;
		
		Vector<Integer> vec_idperson=new Vector<Integer>();
		pst=conn.conn.prepareStatement("select idperson from person");
		
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		
		while(result.next()){	vec_idperson.add(result.getInt(1));	}
		int idperson=vec_idperson.lastElement() +1;

		int count_idclient=0;
		pst=conn.conn.prepareStatement("select count(idclient) from client");
		
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);

		
		while(result.next()){	count_idclient=result.getInt(1);	}
		int desired_persons_to_insert =(int) Math.round(count_idclient*(0.05));
		System.out.println(desired_persons_to_insert +" clients will be inserted");

		for (int i=0;i<desired_persons_to_insert;i++){
			
			pst=conn.conn.prepareStatement("insert into person values(?,?,?,?,?,?,?,?)");
			pst.setInt(1, idperson+i);
			pst.setString(2, "Name_"+(p.zipf(50)+1));
			pst.setString(3, "Surname_"+(p.zipf(200)+1));
			pst.setInt(4, p.getRandInt(2));
			
			date= new Date(50+p.getRandInt(41), 1+p.getRandInt(12), 1+p.getRandInt(31));
			pst.setDate(5, date);
			pst.setString(6, "Person_Address_"+idperson);
			int city_sel= p.zipf(cities.length);
			pst.setString(7, cities[city_sel]);
			if(city_sel>=0 && city_sel<=4){
				pst.setString(8, countries[0]);
			}else if(city_sel>=5 && city_sel<=6){
				pst.setString(8, countries[1]);
			}else{
				pst.setString(8, countries[2]);
			}
			
			time_before= System.currentTimeMillis();
			pst.executeUpdate();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			
		}
	}	
}
