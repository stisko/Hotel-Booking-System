package mainpack;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

public class U1 {
	
	public void insertClientPerson( Connect conn) throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
		long time_before,time_after,time_whole = 0,time_whole_credit=0, time_credit;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		int employees[] = new int[10];
		for(int i=0;i<=9;i++){
			employees[i]=10+i;
		}
		String[] cities= { "Agrinio","Athens","Patra","Thessaloniki","Chania","London","Bristol","Tirana","Berat","Elbasan" };
		String[] countries= {"Greece","United Kingdom","Albania"};
		Date date;
		
		
		Vector<Integer> vec_idperson=new Vector<Integer>();
		pst=conn.conn.prepareStatement("select idperson from person");
		
		
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		
		while(result.next()){	vec_idperson.add(result.getInt(1));	}
		
		
		
		int idperson, doc_client;
		if(vec_idperson.isEmpty()){
			doc_client=100000000;
			idperson=1;
		}else{
			Vector<Integer> vec_idclient=new Vector<Integer>();
			pst2= conn.conn.prepareStatement("select idclient from client");
			time_before= System.currentTimeMillis();
			result=pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			while(result.next()){	vec_idclient.add(result.getInt(1));	}
			
			doc_client=100000000+ vec_idclient.lastElement();
			idperson=vec_idperson.lastElement() +1;
		}
		
		for (int i=0;i<100;i++){

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
			
			
			pst=conn.conn.prepareStatement("insert into client values(?,?)");
			pst.setInt(1, idperson+i);
			pst.setString(2, "ID_"+doc_client);
			doc_client++;
			
			
			time_before= System.currentTimeMillis();
			pst.executeUpdate();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);


			boolean doesnt_has_credit;
			doesnt_has_credit= new Random().nextInt(5)==0;	//20% pithanotita na min exei karta
			if (!doesnt_has_credit){
				
				time_credit = insertCreditCard(conn, idperson+i);
				time_whole_credit= time_whole_credit+time_credit;
				time_whole= time_whole+time_credit;
			}
		}
		System.out.println("Creditcard: "+time_whole + "ms");
		System.out.println(time_whole);
	}


	public static long insertCreditCard(Connect conn, int idperson) throws SQLException{
		PreparedStatement pst,pst2;
		ResultSet result;
		long time_after, time_before,time_whole=0;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		Vector<Integer> vec_idclient=new Vector<Integer>();
		
		DecimalFormat dec_format= new DecimalFormat("0000");
		int a,b,c,d;
		String card_num;
	
		
		boolean doesnt_has_credit= new Random().nextInt(5)==0;	//20% pithanotita na min exei karta
		int cred_cnt=1+p.zipf(5);
			
		pst2=conn.conn.prepareStatement("select fname, lname from person where idperson="+idperson);
		time_before= System.currentTimeMillis();
		result=pst2.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		
		String fname=null,lname=null;
		while(result.next()){	fname=result.getString(1); lname=result.getString(2);	}
		
		pst= conn.conn.prepareStatement("insert into creditcard values(?,?,?,?,?)");
		for(int j=1;j<=cred_cnt;j++){
			String cardtype= "cardtype_"+(1+p.getRandInt(5));
			pst.setString(1, cardtype);
			a=p.getRandInt(10000);
			b=p.getRandInt(10000);
			c=p.getRandInt(10000);
			d=p.getRandInt(10000);
			card_num=dec_format.format(a)+"-"+dec_format.format(b)+"-"+dec_format.format(c)+"-"+dec_format.format(d);
			pst.setString(2,card_num);
			Date date= new Date(115+ p.getRandInt(3) , 1+ p.getRandInt(12), 1+p.getRandInt(30));
			pst.setDate(3, date);
			pst.setString(4, lname+" "+fname);
			pst.setInt(5, idperson);
			
			
			time_before= System.currentTimeMillis();
			pst.executeUpdate();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			
			
			
		}
		//System.out.println("Creditcard: "+time_whole + "ms");
		return time_whole;
	}
}





