package mainpack;

import java.sql.*;
import java.util.Random;
import java.util.Vector;

public class U3 {
	 public long time_after;
	public  long time_before;
	public long time_whole;
	
	public void insertBooking(Connect conn) throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
		//long time_after,time_before,time_whole=0;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		
		
		Vector<Integer> vec_idperson=new Vector<Integer>();
		int count_clients=0, desired_clients;
		Vector<Integer> vec_idclient=new Vector<Integer>();
		
		pst= conn.conn.prepareStatement("select idclient from client");
		
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		
		while(result.next()){	vec_idclient.add(result.getInt(1));	}

		count_clients=vec_idclient.size();
		
		
		
		desired_clients= (int) Math.round(0.2*count_clients);


		float bookings, prob_payed, total_rates;
		int desired_bookings = 0, days_to_stay;
		double totalamount, payed;


		for(int i=0;i<desired_clients;i++){
			bookings= (float) Math.random(); 
			totalamount=0;
			payed=0;
			
			
			if(bookings<=0.2){
				desired_bookings= 3;
			}else if(bookings<= 0.3){
				desired_bookings= 2;
			}else if(bookings<=0.5){
				desired_bookings= 1;
			}
			
			float days;
			days= (float) Math.random();
			if(days<=0.05){
				days_to_stay = 5;
			}else if(days <= 0.1){
				days_to_stay= 4;
			}else if(days <= 0.15){
				days_to_stay= 3;
			}else if(days <= 0.3){
				days_to_stay= 2;
			}else{
				days_to_stay= 1;
			}

			Vector<Integer> vec_roomid=new Vector<Integer>(); // returns ola ta rooms tou ksenodoxeiou

			Vector<Integer> vec_hotelbookingID=new Vector<Integer>();
			pst=conn.conn.prepareStatement("select idhotelbooking from hotelbooking");
			
			time_before= System.currentTimeMillis();
			result=pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			
			while(result.next()){	vec_hotelbookingID.add(result.getInt(1));	}

			
			pst=conn.conn.prepareStatement("select idperson from person");
			
			time_before= System.currentTimeMillis();
			result=pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			while(result.next()){	vec_idperson.add(result.getInt(1));	}
			
			Date curr_date = null; 
			pst=conn.conn.prepareStatement("select current_date");
			
			
			time_before= System.currentTimeMillis();
			result=pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			while(result.next()){	curr_date= result.getDate(1);	}


			Date checkout_date= null;
			Date cancelation_date= null;
			pst=conn.conn.prepareStatement("select current_date +  "+(days_to_stay+1));
			time_before= System.currentTimeMillis();
			result=pst.executeQuery();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			while(result.next()){	checkout_date= result.getDate(1);	}

			vec_roomid=findhotel(conn, desired_bookings);
			
			int idhotelbooking=0;
			if(vec_hotelbookingID.isEmpty()){
				idhotelbooking=1;
			}else{
				idhotelbooking = vec_hotelbookingID.size()+1;
			}

			pst= conn.conn.prepareStatement("insert into hotelbooking values(?,?,?,?,?,?,?)");
			//System.err.println(vec_hotelbookingID.lastElement() + "  "+(vec_hotelbookingID.lastElement()+1));
			pst.setInt(1, idhotelbooking);
			pst.setDate(2, curr_date);
			pst.setDate(3, cancelation_date );
			pst.setDouble(4,  totalamount); //totalamount
			pst.setDouble(5, 1);
			pst.setInt(6, 1);
			pst.setInt(7, vec_idclient.get(p.getRandInt(count_clients)));
			
			time_before= System.currentTimeMillis();
			pst.executeUpdate();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);
			
			
			total_rates=0;
			
			for(int j=0;j<desired_bookings;j++){
				pst2=conn.conn.prepareStatement("insert into roombooking values(?,?,?,?,?,?,?)");
				pst2.setInt(1, idhotelbooking);
				System.out.println(vec_roomid.size());
				int random_index= p.getRandInt(vec_roomid.size());
				int idroom= vec_roomid.get(random_index);
				vec_roomid.remove(random_index);
				System.err.println(idroom);
				pst2.setInt(2, idroom);
				pst2.setInt(3, vec_idperson.get(p.getRandInt(vec_idperson.size())));
				pst2.setDate(4, curr_date);
				pst2.setDate(5, checkout_date);
				pst2.setDate(6, cancelation_date);
				float [] rate_disc= getRateDiscount(conn, idroom);
				total_rates=total_rates + (rate_disc[0] - rate_disc[0]*rate_disc[1]);
				pst2.setFloat(7, rate_disc[0]);
				
				time_before= System.currentTimeMillis();
				pst2.executeUpdate();
				time_after = System.currentTimeMillis();
				time_whole = time_whole + (time_after-time_before);
				
			}
			totalamount= total_rates*days_to_stay;
			prob_payed= (float) Math.random();
			if(prob_payed<0.4){
				payed= totalamount;
			}else{
				payed= totalamount*0.2;
			}
			pst3=conn.conn.prepareStatement("update hotelbooking set payed=?, totalamount=? where idhotelbooking=?");
			pst3.setFloat(1, (float) payed);
			pst3.setFloat(2, (float) totalamount);
			pst3.setInt(3, (vec_hotelbookingID.size()+1));
			
			time_before= System.currentTimeMillis();
			pst3.executeUpdate();
			time_after = System.currentTimeMillis();
			time_whole = time_whole + (time_after-time_before);

			
			//vec_hotelbookingID.clear();
			vec_roomid.clear();
		}
		System.out.println(time_whole+" ms");
	}
	
	
	public float[] getRateDiscount(Connect conn, int idroom) throws SQLException{
		float rate = 0,discount = 0;
		PreparedStatement pst;
		ZipfGenerator p = new ZipfGenerator(new Random(System.currentTimeMillis()));
		ResultSet result;
		
		
		pst=conn.conn.prepareStatement("select rate,discount from room join roomrate on (roomrate.roomtype=room.roomtype and room.hotelid=roomrate.hotelid) "
				+ "where idroom="+idroom);
		
		
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);

		while(result.next()){	 rate=result.getFloat(1);	discount= result.getFloat(2);}
		float a[]={rate,discount};
		return a;
	}
	
	
	public  Vector<Integer> findhotel(Connect conn, int desired_bookings) throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		Vector<Integer> vec_roomid=new Vector<Integer>();
		
		
		int hotel_to_book= p.getRandInt(101);
		
		pst= conn.conn.prepareStatement("select hotelid, idroom from room "
				+ "where vacant=1 and hotelid="+hotel_to_book+
				" group by hotelid, idroom	"
				+ "except "
				+ "select idhotel, roomid from hotel join room on (hotel.idhotel=room.hotelid) join roombooking on (room.idroom=roombooking.roomid) "
				+ "where checkoutdate>current_date "
				+ "group by idhotel, roomid");
		
		time_before= System.currentTimeMillis();
		result=pst.executeQuery();
		time_after = System.currentTimeMillis();
		time_whole = time_whole + (time_after-time_before);
		while(result.next()){	vec_roomid.add(result.getInt(2));	}
		
		if(vec_roomid.isEmpty()){
			findhotel(conn, desired_bookings);

		}
		if(vec_roomid.size()>=desired_bookings){
			return vec_roomid;
		}else{
			findhotel(conn, desired_bookings);
		}
		//findhotel(conn, desired_bookings);
		System.err.println("ftanei sto mpourdelo");
		return findhotel(conn, desired_bookings);
		
	}
}
