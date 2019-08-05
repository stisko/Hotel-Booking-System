package mainpack;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class main {
	public static Connect conn;

	public static void main(String[] args) throws SQLException {
		StandardInputRead sid= new StandardInputRead();
		String db_name;

		while(true){
			int choice=printMenu();
			switch(choice){
			case 0:
				System.out.println("Exiting program...");
				return;
			case 1:
				db_name= sid.readString("Enter the database name\n");
				login(db_name);
				break;
			case 2:
				insertHotels();
				break;
			case 3:
				insertRoomtype();
				break;
			case 4:
				insertRoomrate();
				break;
			case 5:
				insertRoom();
				break;
			case 6:
				insertTravelAgent();
				break;
			case 7:
				insertEmployeePerson();
				break;
			case 8:
				insertClientPerson();
				break;
			case 9:
				insertCreditCard();
				break;
			case 10:
				insertFacility();
				break;
			case 11:
				insertHotelFacility();
				break;
			case 12:
				insertRoomfacility();
				break;
			case 13:
				deleteBase();
				break;
			case 14:
				insertHotels();
				insertRoomtype();
				insertRoomrate();
				insertRoom();
				insertTravelAgent();
				insertEmployeePerson();
				insertClientPerson();
				insertCreditCard();
				insertFacility();
				insertHotelFacility();
				insertRoomfacility();
				break;
			case 15:
				U1 objU1= new U1();
				int sel= sid.readPositiveInt("Give the emulation days: ");
				for(int i=0;i<sel;i++){
					objU1.insertClientPerson(conn);
					System.out.println("Finished Day "+(i+1));
				}
				break;
			case 16:
				U2 objU2 = new U2();
				int selU2= sid.readPositiveInt("Give the emulation days: ");
				for(int i=0;i<selU2;i++){
					objU2.insertPerson(conn);
					System.out.println("Finished Day "+(i+1));
				}
				break;
			case 17:
				U3 objU3 = new U3();
				int selU3= sid.readPositiveInt("Give the emulation days: ");
				for(int i=0;i<selU3;i++){
					objU3.insertBooking(conn);
					System.out.println("Finished Day "+(i+1));
				}
				break;
			case 18:
				U4 objU4 = new U4();
				int selU4= sid.readPositiveInt("Give the emulation days: ");
				for(int i=0;i<selU4;i++){
					objU4.updatecancelations(conn, i);
					System.out.println("Finished Day "+(i+1));
				}
				break;
			case 19:
				Q1 objQ1 = new Q1();
				int selQ1= sid.readPositiveInt("Give the emulation days: ");
				for(int i=0;i<selQ1;i++){
					objQ1.find_persons_by_criteria(conn, selQ1);
					System.out.println("Finished Day "+(i+1));
				}
				break;
			case 20:
				break;
			case 21:
				break;
			default :
				System.out.println("Wrong selection. Please try again...");
			}
		}
	}

	public static void insertHotels() throws SQLException{
		int hotelID;
		PreparedStatement pst;
		String name, address,city,country,star,phone, fax;
		String[] cities= { "Agrinio","Athens","Patra","Thessaloniki","Chania","London","Bristol","Tirana","Berat","Elbasan" };
		String[] countries= {"Greece","United Kingdom","Albania"};
		String[] stars = {"one star","two stars","three star","four stars","five stars"};
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		
		pst= conn.conn.prepareStatement("Insert into hotel values (?,?,?,?,?,?,?,?)");
		for(int i=1;i<=100;i++){
			hotelID=i;
			name="hotelname_<"+i+">";
			address="hoteladdress_<"+i+">";
			phone="hotelphone_<"+i+">";
			fax="hotelfax_<"+i+">";
			int city_sel= p.zipf(cities.length);
			city=cities[city_sel];
			if(city_sel>=0 && city_sel<=4){
				country= countries[0];
			}else if(city_sel>=5 && city_sel<=6){
				country=countries[1];
			}else{
				country=countries[2];
			}
			star=stars[p.zipf(stars.length)];
			
			pst.setInt(1,hotelID);
			pst.setString(2,name);
			pst.setString(3,star);
			pst.setString(4, address);
			pst.setString(5,city);
			pst.setString(6,country);
			pst.setString(7,phone);
			pst.setString(8,fax);
			pst.executeUpdate();
		}
	}
	
	public static void insertRoomtype() throws SQLException{
		PreparedStatement pst;
		pst= conn.conn.prepareStatement("Insert into roomtype values(?,?)");
		for(int i=1;i<=10;i++){
			pst.setString(1,"typename_"+i);
			pst.setInt(2,0);
			pst.executeUpdate();
		}
	}
	
	public static void insertRoomrate() throws SQLException{
		PreparedStatement pst;
		Vector<Integer> vec_idhotel=new Vector<Integer>();
		Vector<String> vec_typename=new Vector<String>();
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		ResultSet result;
		
		pst=conn.conn.prepareStatement("select idhotel from Hotel");
		result= pst.executeQuery();
		
		while(result.next()){
			vec_idhotel.add(result.getInt(1));
		}
		
		pst= conn.conn.prepareStatement("select typename from roomtype");
		result=pst.executeQuery();
		
		while(result.next()){
			vec_typename.add(result.getString(1));
		}
		
		pst= conn.conn.prepareStatement("insert into roomrate values(?,?,?,?)");
		for(int i=0;i<vec_idhotel.size();i++){
			for(int j=0;j<vec_typename.size();j++){
				float discount;
				if(Math.random()<=0.5){
					discount= 0;
				}else if(Math.random()<=0.3){
					discount= (float) 0.2;
				}else{
					discount= (float) 0.5;
				}
				
				
				pst.setInt(1, vec_idhotel.elementAt(i));
				pst.setString(2, vec_typename.elementAt(j));
				pst.setFloat(3,35 +(int)(p.getRand()*((150 - 35)+1)));
				pst.setFloat(4, discount);
				pst.executeUpdate();
			}
		}
	}
	
	public static void insertRoom() throws SQLException{
		PreparedStatement pst;
		ResultSet result;
		int floors[]={4,5,6,7,8};
		int rooms[]= new int[20]; for(int i=0;i<20;i++){rooms[i]=10+i;}// dwmatia apo [10,29]
		int idroom=1;
		int roomnumber=0;
		float area;
		float vacant_val;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		Vector<Integer> vec_idhotel=new Vector<Integer>();
		pst=conn.conn.prepareStatement("select idhotel from hotel");
		result=pst.executeQuery();
		while(result.next()){	vec_idhotel.add(result.getInt(1));	}
		
		Vector<String> vec_typename=new Vector<String>();
		pst=conn.conn.prepareStatement("select typename from roomtype");
		result=pst.executeQuery();
		while(result.next()){	vec_typename.add(result.getString(1));	}
		
		
		pst=conn.conn.prepareStatement("insert into room values(?,?,?,?,?,?)");
		//idroom, roomnumber, vacant, area, hotelid, roomtype
		for(int pr=0;pr<102;pr++){
			if(pr==0){
				System.out.print("[");
			}else if (pr==101){
				System.out.println("]");
			}else{
				System.out.print(" ");
			}
		}
		System.out.print("[");
		for(int i=1;i<=vec_idhotel.size();i++){
			int floor= floors[p.zipf(floors.length)];
			for(int j=1;j<=floor;j++){
				int room=rooms[p.zipf(rooms.length)]; //se kathe orofo tyxaio arithmo dwmatiwn
				for (int r=1;r<=room;r++){
					pst.setInt(1, idroom);
					roomnumber=100*j+r;
					pst.setInt(2, roomnumber);
					area=10+p.getRandInt(11);
					pst.setFloat(4, area);
					pst.setInt(5, i);
					pst.setString(6, vec_typename.get(p.zipf(vec_typename.size())));
		
					vacant_val=(float) p.getRand();
					if(vacant_val<=0.4){
						pst.setInt(3, 1);
					}else{
						pst.setInt(3, 0);
					}
					//pst.setInt(3, 1);
					
					
					pst.executeUpdate();
					idroom++;
				}
			}
			System.out.print(".");
			//System.out.print(i+"%\r");
		}
		System.out.println("]");
	}
	
	public static void insertTravelAgent() throws SQLException{
		PreparedStatement pst;
		
		pst=conn.conn.prepareStatement("insert into travelagent values(?,?,?)");
		for(int i=1;i<=50;i++){
			pst.setInt(1, i);
			pst.setString(2, "travel_agent_name_"+i);
			pst.setString(3, "travel_agent_email_"+i);
			pst.executeUpdate();
		}
	}
	
	public static void insertEmployeePerson() throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		int employees[] = new int[10];
		for(int i=0;i<=9;i++){
			employees[i]=10+i;
		}
		String[] cities= { "Agrinio","Athens","Patra","Thessaloniki","Chania","London","Bristol","Tirana","Berat","Elbasan" };
		String[] countries= {"Greece","United Kingdom","Albania"};
		
		String roles[]= {"receptionist", "manager"};
		int manager_positions[]={2,3,4,5};
		Date date;
		Vector<Integer> vec_idhotel=new Vector<Integer>();
		pst=conn.conn.prepareStatement("select idhotel from hotel");
		result=pst.executeQuery();
		while(result.next()){	vec_idhotel.add(result.getInt(1));	}
		
		int cnt_idperson=1;
		for(int i=1;i<=vec_idhotel.size();i++){
			int num_employees=employees[p.zipf(employees.length)];
			int num_managers= manager_positions[p.zipf(manager_positions.length)];
			int cnt_manager=0;
			pst=conn.conn.prepareStatement("insert into person values(?,?,?,?,?,?,?,?)");
			for (int j=1;j<=num_employees;j++){
				pst.setInt(1, cnt_idperson);
				pst.setString(2, "Name_"+(p.zipf(50)+1));
				pst.setString(3, "Surname_"+(p.zipf(200)+1));
				pst.setInt(4, p.getRandInt(2));
				
				date= new Date(50+p.getRandInt(41), 1+p.getRandInt(12), 1+p.getRandInt(31));
				pst.setDate(5, date);
				pst.setString(6, "Person_Address_"+cnt_idperson);
				int city_sel= p.zipf(cities.length);
				pst.setString(7, cities[city_sel]);
				if(city_sel>=0 && city_sel<=4){
					pst.setString(8, countries[0]);
				}else if(city_sel>=5 && city_sel<=6){
					pst.setString(8, countries[1]);
				}else{
					pst.setString(8, countries[2]);
				}
				pst.executeUpdate();
				
				pst3=conn.conn.prepareStatement("insert into employee values(?,?,?,?)");
				pst3.setInt(1, cnt_idperson);
				if(cnt_manager<=num_managers){	//eisagwgi manager
					pst3.setString(2, roles[1]);
					pst3.setNull(3,3);
					cnt_manager++;
				}else{							//eisagwgi receptionist
					pst3.setString(2, roles[0]);
					Vector<Integer> vec_idEmployee=new Vector<Integer>();
					pst2=conn.conn.prepareStatement("select idEmployee from employee where emp_role='manager'");
					result=pst2.executeQuery();
					while(result.next()){	vec_idEmployee.add(result.getInt(1));	}
					pst3.setInt(3, vec_idEmployee.get(p.getRandInt(vec_idEmployee.size())));
				}
				pst3.setInt(4, i);
				pst3.executeUpdate();
				cnt_idperson++;
			}
		}	
	}
	public static void insertClientPerson() throws SQLException{
		PreparedStatement pst,pst2,pst3;
		ResultSet result;
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
		result=pst.executeQuery();
		while(result.next()){	vec_idperson.add(result.getInt(1));	}
		int idperson=vec_idperson.lastElement() +1;
		
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
			pst.executeUpdate();
			
//			Vector<String> vec_clientdoc=new Vector<String>();
//			pst=conn.conn.prepareStatement("select documentclient from client");
//			result=pst.executeQuery();
//			while(result.next()){	vec_clientdoc.add(result.getString(1));	}
//			StringTokenizer str= new StringTokenizer("ID_");
			
			pst=conn.conn.prepareStatement("insert into client values(?,?)");
			pst.setInt(1, idperson+i);
			pst.setString(2, "ID_"+doc_client);
			doc_client++;
			
			pst.executeUpdate();
		}
	}
	
	public static void insertCreditCard() throws SQLException{
		PreparedStatement pst,pst2;
		ResultSet result;
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		Vector<Integer> vec_idclient=new Vector<Integer>();
		pst=conn.conn.prepareStatement("select idclient from client");
		result=pst.executeQuery();
		while(result.next()){	vec_idclient.add(result.getInt(1));	}
		
		
		boolean doesnt_has_credit;
		DecimalFormat dec_format= new DecimalFormat("0000");
		int a,b,c,d;
		String card_num;
		for(int i=0;i<vec_idclient.size();i++){
			doesnt_has_credit= new Random().nextInt(5)==0;	//20% pithanotita na min exei karta
			
			if (!doesnt_has_credit){
				int cred_cnt=1+p.zipf(5);
				
				pst2=conn.conn.prepareStatement("select fname, lname from person where idperson="+vec_idclient.get(i));
				result=pst2.executeQuery();
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
					pst.setInt(5, vec_idclient.get(i));
					pst.executeUpdate();
				}
			}
		}
	}
	
	public static void insertFacility() throws SQLException{
		PreparedStatement pst;
		pst=null;
	
		pst=conn.conn.prepareStatement("Insert into facility values(?,?,?)");
		counter(pst, 1, 0, 0, 0, 0, 100, 1);
		counter(pst, 1, 0, 0, 0, 0, 60, 0);
		
		
	}
	
	
	public static int counter(PreparedStatement pst, int n0, int n1, int n2, int n3, int n4, int stop,int type) throws SQLException{
		String testSt, par_test = null;
		int a,b,c,d,e;
		int cnt=0;
		a=0;
		
		for(b = n1;b<=n1+4;b++){
			for (c = n2;c<=n2+3;c++){
				for(d = n3;d<=n3+2;d++){
					for(e = n4;e<=2;e++){
						
						if(a==0){
							testSt="1";
							a=1;
							par_test= "1";
						}else if(d==0){
							testSt=a+"_"+e;
							par_test= "1";
						}else if(c==0){
							testSt=a+"_"+e+"_"+d;
							par_test= a+"_"+e;
						}else if(b==0){
							testSt = a+"_"+e+"_"+d+"_"+c;
							par_test= a+"_"+e+"_"+d;
						}else{
							testSt = a+"_"+e+"_"+d+"_"+c+"_"+b;
							par_test= a+"_"+e+"_"+d+"_"+c;
						}
						if(type==1){
							//System.out.println("Hotel_facility"+testSt);
							pst.setString(1, "Hotel_facility_"+testSt);
							pst.setString(2, "Hotel_facility_"+par_test);
						}else{
							//System.out.println("Room_facility"+testSt);
							pst.setString(1, "Room_facility_"+testSt);
							pst.setString(2, "Room_facility_"+par_test);
						}
						pst.setString(3, "description_"+testSt);
						pst.executeUpdate();
						cnt++;
						System.out.println(testSt+"\t"+cnt+"\t"+par_test);
						if(cnt==stop){
							System.out.println("mpainei");
							return 1;
						}
					}
					n4=1;
					n3=1;
				}
				n2=1;
			}
			n1=1;
		}
		return 0;
	}
	
	public static void insertHotelFacility() throws SQLException{
		PreparedStatement pst,pst2;
		ResultSet result;
		Vector<String> vec_namefacility=new Vector<String>();
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		pst2=conn.conn.prepareStatement("select namefacility from facility where namefacility like 'Hotel_facility%'");
		result= pst2.executeQuery();
		while(result.next()){	vec_namefacility.add(result.getString(1));	}
		int fac_cnt;
		pst=conn.conn.prepareStatement("insert into hotelfacilities values (?,?,?)");
		
		for(int i=1;i<=100;i++){
			fac_cnt= 10+ p.getRandInt(40);
			Vector<Integer> temp_namefacility=new Vector<Integer>();
			for(int j=0;j<fac_cnt;j++){
				temp_namefacility.add(p.getRandInt(100));
				for(int k=0;k<j;k++){
					System.out.println("i="+i+" j="+j+" k="+k);
					if (temp_namefacility.get(k).equals(temp_namefacility.get(j))){
						temp_namefacility.setElementAt(p.getRandInt(100), j);
						System.out.println("IDIO");
						k=-1;
					}
				}
				pst.setInt(1, i);
				
				System.out.println(temp_namefacility.get(j));
				String namefac= vec_namefacility.get(temp_namefacility.get(j));
				
				pst.setString(2, namefac);
				pst.setString(3, "desc_"+namefac);
				pst.executeUpdate();
			}
		}
	}
	
	public static void insertRoomfacility() throws SQLException{
		PreparedStatement pst,pst2;
		ResultSet result;
		Vector<String> vec_namefacility=new Vector<String>();
		ZipfGenerator p=new ZipfGenerator(new Random(System.currentTimeMillis()));
		
		pst2=conn.conn.prepareStatement("select namefacility from facility where namefacility like 'Room_facility%'");
		result= pst2.executeQuery();
		while(result.next()){	vec_namefacility.add(result.getString(1));	}
		int fac_cnt;
		
		
		Vector<Integer> vec_idroom=new Vector<Integer>();
		pst2=conn.conn.prepareStatement("select idroom from room");
		result= pst2.executeQuery();
		while(result.next()){	vec_idroom.add(result.getInt(1));	}
		
		
		pst=conn.conn.prepareStatement("insert into roomfacilities values (?,?,?)");
		for(int i=vec_idroom.firstElement();i<=vec_idroom.size();i++){
			fac_cnt= p.getRandInt(6);
			Vector<Integer> temp_namefacility=new Vector<Integer>();
			for(int j=0;j<fac_cnt;j++){
				temp_namefacility.add(p.getRandInt(vec_namefacility.size()));
				for(int k=0;k<j;k++){
					System.out.println("i="+i+" j="+j+" k="+k);
					if (temp_namefacility.get(k).equals(temp_namefacility.get(j))){
						temp_namefacility.setElementAt(p.getRandInt(vec_namefacility.size()), j);
						System.out.println("IDIO");
						k=-1;
					}
				}
				pst.setInt(1, i);
				
				System.out.println(temp_namefacility.get(j));
				String namefac= vec_namefacility.get(temp_namefacility.get(j));
				
				pst.setString(2, namefac);
				pst.setString(3, "desc_"+namefac);
				pst.executeUpdate();
			}
		}
	}
	
	
	public static void login(String db_name){
		
		try {
			conn = new Connect(db_name);
		}catch (Exception e) {
			System.out.println("Cannot establish connection with database");
			e.printStackTrace();
		}
	}

	public static void deleteBase() throws SQLException{
		PreparedStatement pst;
		
		pst= conn.conn.prepareStatement("Delete from roomfacilities");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from hotelfacilities");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from facility");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from creditcard");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from client");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from employee");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from person");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from room");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from roomrate");
		pst.                                                                                               execute();
		pst= conn.conn.prepareStatement("Delete from hotel");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from roomtype");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from travelagent");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from roombooking");
		pst.execute();
		pst= conn.conn.prepareStatement("Delete from hotelbooking");
		pst.execute();
		
	}

	public static int printMenu(){
		StandardInputRead sid= new StandardInputRead();
		
		System.out.println("\t\tMain Menu");
		System.out.println("1)\tLogin");
		System.out.println("2)\tInsert Hotel");
		System.out.println("3)\tInsert Roomtype");
		System.out.println("4)\tInsert Roomrate");
		System.out.println("5)\tInsert Room");
		System.out.println("6)\tInsert TravelAgent");
		System.out.println("7)\tInsert employee-person");
		System.out.println("8)\tInsert client-person");
		System.out.println("9)\tInsert creditCard");
		System.out.println("10)\tInsert facility");
		System.out.println("11)\tInsert hotelfacility");
		System.out.println("12)\tInsert roomfacility");
		System.out.println("13)\tDelete Base");
		System.out.println("14)\tAll");
		System.out.println("15)\tU1");
		System.out.println("16)\tU2");
		System.out.println("17)\tU3");
		System.out.println("18)\tU4");
		System.out.println("19)\tQ1");
		System.out.println("20)\tQ2");
		System.out.println("21)\tQ3");
		System.out.println("0)\tExit program");
		int choice= sid.readPositiveInt("Enter your choice: ");
		return choice;
		
	
	}

}