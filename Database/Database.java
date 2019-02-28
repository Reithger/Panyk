
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//address stored as string
//phone number stored as string
//contact stored as string
//type stored as sting
//participant(s) stored as string

public class Database {
	
	public static final String DB_DIRECTORY = "C:/sqlite3/databases/";
	
	public String name;
	public Connection connection;
	
	//initializes a new database with name db_name--------------------------------------------------------------------
	public Database(String db_name) {
		this.name = db_name;
   	 	this.connection = null;
		
        String url = "jdbc:sqlite:"+ DB_DIRECTORY + db_name + ".db";
 
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {

                System.out.println("A new database successfully created..."); 
                System.out.println("new database path --> "+ DB_DIRECTORY + db_name + ".db");
                //connect to db
                this.connection = this.connect();
                //if connection was successful
                if(this.connection != null) {
                	Statement state = this.connection.createStatement();
                	//create the user table
                	if(this.getTable("users") == null) {
                		//id, username, password, fname, lname, DOB, createdAt
                		state.execute("CREATE TABLE users(id int,"+"username varchar(60),"+"password varchar(60),"+"fname varchar(60),"+"lname varchar(60),"+"DoB date,"+"createdAt date,"+"primary key(id));");
                	}
                	//create trips table
                	if(this.getTable("trips") == null) {
                		//id, userID, tripTitle, destination, startDate, endDate, 
                		state.execute("CREATE TABLE trips(id int,"+"userID int,"+"tripTitle varchar(60),"+"destination varchar(60),"+"startDate date,"+"endDate date,"+"primary key(id));");
                	}
                	//create scheduleItems table
                	if(this.getTable("scheduleItems") == null) {
                		//id, tripID, type
                		state.execute("CREATE TABLE scheduleItems(id int,"+"userID int,"+"type varchar(60),"+"primary key(id));");
                	}
                	//create contacts table
                	if(this.getTable("contacts") == null) {
                		//id, scheduleItemID, fname, lname, company, jobTitle, displayAs, email, webpage, PhoneNumber, Address
                		state.execute("CREATE TABLE contacts(id int,"+"scheduleItemID int,"+"fname varchar(60),"+"lname varchar(60),"+"company varchar(60),"+"jobTitle varchar(60),"+"displayAs varchar(60),"+"email varchar(60),"+"webpage varchar(60),"+"phoneNumber varchar(60),"+"address varchar(60),"+"primary key(id));");
                	}
                	//create transportation table
                	if(this.getTable("transportation") == null) {
                		//id, scheduleItemID, startTime, endTime
                		state.execute("CREATE TABLE transportation(id int,"+"scheduleItemID int,"+"startTime date,"+"endTime date,"+"primary key(id));");
                	}
                	if(this.getTable("accommodations") == null) {
                		//id, tripID, scheduleItemID, name, checkIn, checkOut, paid, address, contact
                		state.execute("CREATE TABLE accommodations(id int,"+"tripID int,"+"scheduleItemID int,"+"name varchar(60),"+"checkIn date,"+"checkOut date,"+"paid boolean,"+"address varchar(60),"+"contact varchar(60),"+"primary key(id));");
                	}
                	if(this.getTable("reservations") == null) {
                		//id, scheduleItemID, tripID, contact, participants, startDate, endDate, address, type
                		state.execute("CREATE TABLE reservations(id int,"+"scheduleItemID int,"+"tripID int,"+"contact varchar(60),"+"participants varchar(60),"+"startDate date,"+"endDate date,"+"address varchar(60),"+"type varchar(60),"+"primary key(id));");
                	}
                	
                }               
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());  
        }
	}
	
	//gets connection to database------------------------------------------------------------------------------------------
	public Connection connect() {
		if(this.connection == null) {
	        try {
	            // db parameters
	            String url = "jdbc:sqlite:"+ DB_DIRECTORY + this.name + ".db";
	            // create a connection to the database
	            this.connection = DriverManager.getConnection(url);
	            System.out.println("connection to database established");
	            return this.connection;
	            
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        System.out.println("connection to database failed");
	        return null;
		}else {
			return this.connection;
		}

	}
	
	//get result set the given table name------------------------------------------------------------------------------------------
	public ResultSet getTable(String table_name) {
		if(this.connect() ==  null) {
			System.out.println("no connection can be established to the database");
			return null;
		}
		Statement state=null;
		ResultSet result=null;
		try {
			state=this.connection.createStatement();
		}catch(SQLException sqlE1) {}
		
		try {
			result=state.executeQuery("SELECT * FROM " + table_name);
		}catch(SQLException sqlE2) {}
		
		return result;
	}
	
	//adds user to the database-------------------------------------------------------------------------------------------
	public void addUser(int id, String username, String password, String firstName, String lastName, Date DoB, Date createdAt){
		if(this.connect() == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		try {
			PreparedStatement prep = this.connection.prepareStatement("INSERT INTO users(id,username,password,fname,lname,DoB,createdAt) values(?,?,?,?,?,?,?)");
			prep.setInt(1, id);
			prep.setString(2, username);
			prep.setString(3,  password);
			prep.setString(4,  firstName);
			prep.setString(5,  lastName);
			prep.setDate(6, DoB);
			prep.setDate(7, createdAt);
			prep.executeUpdate();
		}catch(Exception e) {
			System.out.println("-----");
			System.out.println("ERROR inserting id: " + id + "  into user database");
			System.out.println(e.getMessage());
			System.out.println("-----");
		}
	}
	
	//adds trip to database----------------------------------------------------------------------------------------------
	public void addTrip(int id, int userID, String tripTitle, String destination, Date startDate, Date endDate) {
		//id, userID, tripTitle, destination, startDate, endDate, 
		if(this.connect() == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		try {
			PreparedStatement prep = this.connection.prepareStatement("INSERT INTO trips(id,userID,tripTitle,destination,startDate,endDate) values(?,?,?,?,?,?)");
			prep.setInt(1, id);
			prep.setInt(2, userID);
			prep.setString(3,  tripTitle);
			prep.setString(4,  destination);
			prep.setDate(5,  startDate);
			prep.setDate(6, endDate);
			prep.executeUpdate();
		}catch(Exception e) {
		System.out.println("-----");
		System.out.println("ERROR inserting id: " + id + "  into trips database");
		System.out.println(e.getMessage());
		System.out.println("-----");
		}
	}
	
	//adds schedule item to database----------------------------------------------------------------------------------------------
	public void addScheduleItem(int id, int userID, String type) {
		//id, tripID, type 
		if(this.connect() == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		try {
			PreparedStatement prep = this.connection.prepareStatement("INSERT INTO scheduleItems(id,userID,type) values(?,?,?)");
			prep.setInt(1, id);
			prep.setInt(2, userID);
			prep.setString(3, type);
			prep.executeUpdate();
		}catch(Exception e) {
		System.out.println("-----");
		System.out.println("ERROR inserting id: " + id + "  into scheduleItems database");
		System.out.println(e.getMessage());
		System.out.println("-----");
		}
	}
	
	//adds contact to database----------------------------------------------------------------------------------------------
	public void addContact(int id, int scedID, String fname, String lname, String company, String jobTitle, String displayAs, String email, String webpage, String phoneNumber, String address) {
		//id, scheduleItemID, fname, lname, company, jobTitle, displayAs, email, webpage, PhoneNumber, Address 
		if(this.connect() == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		try {
			PreparedStatement prep = this.connection.prepareStatement("INSERT INTO contacts(id,scheduleItemID,fname,lname,company,jobTitle,displayAs,email,webpage,phoneNumber,address) values(?,?,?,?,?,?,?,?,?,?,?)");
			prep.setInt(1, id);
			prep.setInt(2, scedID);
			prep.setString(3, fname);
			prep.setString(4, lname);
			prep.setString(5, company);
			prep.setString(6, jobTitle);
			prep.setString(7, displayAs);
			prep.setString(8, email);
			prep.setString(9, webpage);
			prep.setString(10, phoneNumber);
			prep.setString(11, address);
			prep.executeUpdate();
		}catch(Exception e) {
		System.out.println("-----");
		System.out.println("ERROR inserting id: " + id + "  into contacts database");
		System.out.println(e.getMessage());
		System.out.println("-----");
		}
	}
	
	
	//adds transportation to database----------------------------------------------------------------------------------------------
	public void addTransportation(int id, int scedID, Date startTime, Date endTime) {
		//id, scheduleItemID, startTime, endTime
		if(this.connect() == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		try {
			PreparedStatement prep = this.connection.prepareStatement("INSERT INTO transportation(id,scheduleItemID,startTime,endTime) values(?,?,?,?)");
			prep.setInt(1, id);
			prep.setInt(2, scedID);
			prep.setDate(3, startTime);
			prep.setDate(4, endTime);
			prep.executeUpdate();
		}catch(Exception e) {
		System.out.println("-----");
		System.out.println("ERROR inserting id: " + id + "  into transportation database");
		System.out.println(e.getMessage());
		System.out.println("-----");
		}
	}
	
	//adds accommodations to database----------------------------------------------------------------------------------------------
	public void addAccommodation(int id, int tripID, int scedID, String name, Date checkIn, Date checkOut, Boolean paid, String address, String contact) {
		//id, tripID, scheduleItemID, name, checkIn, checkOut, paid, address, contact
		if(this.connect() == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		try {
			PreparedStatement prep = this.connection.prepareStatement("INSERT INTO accommodations(id,tripID,scheduleItemID,name,checkIn,checkOut,paid,address,contact) values(?,?,?,?,?,?,?,?,?)");
			prep.setInt(1, id);
			prep.setInt(2, tripID);
			prep.setInt(3, scedID);
			prep.setString(4, name);
			prep.setDate(5, checkIn);
			prep.setDate(6, checkOut);
			prep.setBoolean(7, paid);
			prep.setString(8, address);
			prep.setString(9, contact);
			prep.executeUpdate();
		}catch(Exception e) {
		System.out.println("-----");
		System.out.println("ERROR inserting id: " + id + "  into accommodation database");
		System.out.println(e.getMessage());
		System.out.println("-----");
		}
	}
	
	
	//adds accommodations to database----------------------------------------------------------------------------------------------
	public void addReservation(int id, int scedID, int tripID, String contact, String participant, Date startDate, Date endDate, String address, String type) {
		//id, scheduleItemID, tripID, contact, participants, startDate, endDate, address, type
		if(this.connect() == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		try {
			PreparedStatement prep = this.connection.prepareStatement("INSERT INTO reservations(id,scheduleItemID,tripID,contact,participants,startDate,endDate,address,type) values(?,?,?,?,?,?,?,?,?)");
			prep.setInt(1, id);
			prep.setInt(2, scedID);
			prep.setInt(3, tripID);
			prep.setString(4, contact);
			prep.setString(5, participant);
			prep.setDate(6, startDate);
			prep.setDate(7, endDate);
			prep.setString(8, address);
			prep.setString(9, type);
			prep.executeUpdate();
		}catch(Exception e) {
		System.out.println("-----");
		System.out.println("ERROR inserting id: " + id + "  into reservation database");
		System.out.println(e.getMessage());
		System.out.println("-----");
		}
	}
	
	
	//test delete from database - works
//	public void DeleteUser(int ID) {
//		if(this.connection == null) {
//			if(this.connect() == null) {
//				System.out.println("no connection can be established to the database");
//				return;
//			}
//		}
//		try {
//			PreparedStatement prep = this.connection.prepareStatement("DELETE FROM users WHERE id='"+ID+"';");
//			prep.executeUpdate();
//		}catch(Exception e) {
//			System.out.println("error deleting user");
//		}
//	}
	
	//##########################################################################################################################################################################################################
	//###########################################################################################################################################################################################################
	
	//print all users to console--------------------------------------------------------------------------------------------
	public void printUsers() {
		DBTablePrinter.printTable(this.connection, "users");
	}
	
	//print trips table--------------------------------------------------------------------------------------------------------
	public void printTrips() {
		DBTablePrinter.printTable(this.connection, "trips");
	}
	
	//print schedule items table--------------------------------------------------------------------------------------------------------
	public void printScheduleItems() {
		DBTablePrinter.printTable(this.connection, "scheduleItems");
	}
	
	//print contacts table--------------------------------------------------------------------------------------------------------
	public void printContacts() {
		DBTablePrinter.printTable(this.connection, "contacts");
	}
	
	//print transportation table--------------------------------------------------------------------------------------------------------
	public void printTransportation() {
		DBTablePrinter.printTable(this.connection, "transportation");
	}
	
	//print accommodations table--------------------------------------------------------------------------------------------------------
	public void printAccommodations() {
		DBTablePrinter.printTable(this.connection, "accommodations");
	}
	
	
	//print reservations table--------------------------------------------------------------------------------------------------------
	public void printReservations() {
		DBTablePrinter.printTable(this.connection, "reservations");
	}
	
	
}//end class DataBase---------------------------------------------------------------------------------------------------------
