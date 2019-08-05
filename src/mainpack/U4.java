package mainpack;

import java.sql.*;
import java.util.Random;
import java.util.Vector;

public class U4 {
	
	
	
	public void updatecancelations(Connect conn, int emul_days) throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		//find the 2% of all bookings
		Vector<Integer> vec_roomid=new Vector<Integer>();
		int desired_cancels=0;
		pst=conn.conn.prepareStatement("select roomid from roombooking where cancelationdate is null and checkin>(current_date +"+emul_days+")");// TODO einai se sxolia to upoloipo molis ftiaksw to u3
		result= pst.executeQuery();
		while(result.next()){ vec_roomid.add(result.getInt(1));}
		
		desired_cancels=(int) ( vec_roomid.size()*0.2);
		int room_to_cancel=0;
		for(int i=0;i<desired_cancels;i++){
			room_to_cancel= p.getRandInt(vec_roomid.size());
			
			pst2=conn.conn.prepareStatement("update roombooking set cancelationdate=current_date+"+emul_days+" where roomid=?");
			pst2.setInt(1, vec_roomid.get(room_to_cancel));
			pst2.executeUpdate();
			vec_roomid.remove(room_to_cancel);//diagrafi apo ta vector gia na mi ginetai diploakurwsi
			
			
			
		}
		
		Vector<Integer> vec_hotelbooking_to_cancel=new Vector<Integer>();
		pst=conn.conn.prepareStatement("select hotelbookingid from roombooking "
				+ "--where cancelationdate is not null and checkin>(current_date+ "+emul_days
				+ ")group by hotelbookingid "
				+ "intersect "
				+ "select hotelbookingid from roombooking "
				+ "where cancelationdate is not null and checkin>(current_date+ "+emul_days 
				+ ") group by hotelbookingid ");// TODO einai se sxolia to upoloipo molis ftiaksw to u3
		result= pst.executeQuery();
		while(result.next()){ vec_hotelbooking_to_cancel.add(result.getInt(1));}
		
		for(int j=0;j<vec_hotelbooking_to_cancel.size();j++){
			pst3=conn.conn.prepareStatement("update hotelbooking set cancellationdate=current_date+"+emul_days+" where idhotelbooking=?");
			pst3.setInt(1, vec_hotelbooking_to_cancel.get(j));
			pst3.executeUpdate();
		}
				
		
	}
}
