package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import controller.Encryptor;

/**
 * This class serves as the facilitator for accessing a database of information.
 * 
 * all values currently stored as strings in the database
 * 
 * @author Regan Lynch
 *
 */

public class Database {
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** */
	private static final String DB_DIRECTORY = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/";  	//get the current directory of the user (ie: where the program is installed)
	/** static final database name -> so that other classes can access the database */
	public static final String DB_NAME = "PLEIN_AIR_DATABASE";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String name;
	/** */
	private Connection connection;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the Database type; initializes a new database with name db_name.
	 * 
	 * @param db_name - String object
	 */
	
	public Database() {
		this.name = DB_NAME;
   	 	this.connection = null;
		
        this.connect();

        if (this.connection != null) {

            System.out.println("connected to database --> "+ DB_DIRECTORY + this.name + ".db");

        	Statement state = null;
			try {
				state = this.connection.createStatement();
	        	//create table types as needed
	        	for(TableType table : TableType.values()) {
	        		//if a table type isnt already in the database -> create it
	        		if(this.getTable(table.toString()) == null) {
							state.execute(table.sqlCreateTable);
	        		}
	        	}
        	}catch(Exception e) {
        		System.out.println(e.getMessage());
        	}             
        }else {
        	System.out.println("connecttion to database " + DB_DIRECTORY + this.name + ".db" + " could not be established");
        }
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * Gets connection to database 
	 * 
	 * @return	connecttion to database
	 */
	
	public void connect() {
		if(this.connection == null) {
	        try {
	            // db parameters
	            String url = "jdbc:sqlite:"+ DB_DIRECTORY + this.name + ".db";
	            // create a connection to the database
	            this.connection = DriverManager.getConnection(url);
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Get result set of the given table name.
	 * 
	 * @param table_name
	 * @return
	 */
	
	public ResultSet getTable(String table_name) {
		if(this.connection == null) {
			System.out.println("no connection can be established to the database");
			return null;
		}
		Statement state=null;
		ResultSet result=null;
		try {
			state=this.connection.createStatement();
		}catch(SQLException sqlE1) {
			return null;
		}
		try {
			result=state.executeQuery("SELECT * FROM " + table_name);
		}catch(SQLException sqlE2) {
			return null;
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	
	public boolean checkUserExists(String username) {
		List<String[]> users = search(TableType.users, null, username, null, null, null, null, null, null);
		return(users.size() != 0);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	
	public boolean checkValidPassword(String username, String password) {
		List<String[]> users = search(TableType.users, null, username, null, null, null, null, null, null);
		if(users.size() != 0) {
			String salt = users.get(0)[7];
			String real_salted_pass = users.get(0)[6];
			String calc_salted_pass = Encryptor.createSaltedHash(password, salt);
			return real_salted_pass.equals(calc_salted_pass);
		}
		return false;
	}
	
//---  Adder Method   ------------------------------------------------------------------------
	
	/**	
	 * This method adds an entry into the defined table
	 * 
	 * @param table	- TableType object you wish to insert the String ... values into
	 * @param values - String ... (var args) values of fields you wish to insert into table
	 * @return - Returns a boolean value representing whether or not the insertion was successful
	 */
	
	public boolean addEntry(TableType table, String ... values) {
		if(this.connection == null) {
			System.out.println("no connection can be established to the database");
			return false;
		}
		if(table.fields.length != values.length) {
			System.out.println("error inserting into table "+ table.toString() + ": number of fields provided does not equal number of fields needed");
			return false;
		}
		//check if entry exists in database
		try {
			PreparedStatement prep = this.connection.prepareStatement(table.sqlInsertTable);
			for(int i = 0; i < values.length; i++) {
				prep.setString(i + 1, values[i]);
			}
			prep.executeUpdate();
		} catch(Exception e) {
			System.out.println("-----");
			System.out.println("ERROR inserting id: " + values[0] + "  into " + table.toString() + " database");
			System.out.println("primary key " + values[0] + " already exists in table");
			System.out.println("-----");
			return false;
		}
		return true;
	}
	
// --- deleter method --------------------------------------------------------------------------------
		
	/**	
	 * This method deletes entries from the defined table
	 * 
	 * @param table	- TableType object you wish to delete from
	 * @param searchKeys - String ... (var args) values representing the search parameters for deletion
	 * @return - Returns a boolean value representing the result of deletion; true if successful, false otherwise
	 */
	
	public boolean deleteEntry(TableType table, String... searchKeys) {
		if(this.connection == null) {
			System.out.println("no connection can be established to the database");
			return false;
		}
		if(searchKeys.length != table.fields.length) {
			System.out.println("error deleting from table "+ table.toString() + ": number of search fields provided does not equal number of fields needed");
			return false;
		}
		try {
			String sqlDelete = "DELETE FROM " + table.toString() + " WHERE";
			int nullCount = 0;
			boolean priorSearch = false;
			for(int i = 0; i < searchKeys.length; i++) {
				if(searchKeys[i] != null) {
					if(priorSearch) {
						sqlDelete += " AND";
					}
					sqlDelete += " " + table.fields[i] + "='" + searchKeys[i] + "'";
					priorSearch = true;
				}else {
					nullCount++;
				}
			}
			sqlDelete += ";";
			if(nullCount != searchKeys.length) {
				PreparedStatement prep = this.connection.prepareStatement(sqlDelete);
				prep.executeUpdate();
			}else {
				System.out.println("error deleting table: " + table.toString() + " --> no search keys defined");
				return false;
			}
		}catch(Exception e) {
			System.out.println("error deleting from table: " + table.toString());
			return false;
		}
		return true;
	}
	
//--- Search Methods  -------------------------------------------------------------------------
	
	/**	
	 * This method searches the defined table based on search parameters
	 * 
	 * @param table	- TableType object you wish to query
	 * @param searchKeys - String ... (var args) values representing the fields you wish to search by 
	 * @return - Returns a List<<r>String[]> object containing elements that matched your search
	 */

	public List<String[]> search(TableType table, String... searchKeys) {
		ResultSet result = null;
		if(this.connection == null) {
			System.out.println("no connection can be established to the database");
			return null;
		}
		if(searchKeys.length != table.fields.length) {
			System.out.println("error searching table "+ table.toString() + ": number of search fields provided does not equal number of fields needed");
			return null;
		}
		try {
			String sqlSearch = "SELECT * FROM " + table.toString() + " WHERE";
			int nullCount = 0;
			boolean priorSearch = false;
			for(int i = 0; i < searchKeys.length; i++) {
				if(searchKeys[i] != null) {
					if(priorSearch) {
						sqlSearch += " AND";
					}
					sqlSearch += " " + table.fields[i] + "='" + searchKeys[i] + "'";
					priorSearch = true;
				}else {
					nullCount++;
				}
			}
			sqlSearch += ";";
			if(nullCount != searchKeys.length) {
				Statement state=null;
				try {
					state=this.connection.createStatement();
					result=state.executeQuery(sqlSearch);
					return ResultSetToList(result);
				}catch(SQLException sqlE2) {
					return null;
				}
			}else {
				System.out.println("error searching table: " + table.toString() + " --> no search keys defined");
				return null;
			}
		}catch(Exception e) {
			System.out.println("error searching in table: " + table.toString());
			return null;
		}
	}
	
//---  Print Methods   ------------------------------------------------------------------------
	
	/**	
	 * This method prints the defined table to console
	 * 
	 * @param type - TableType object representing the table you want to print
	 */
	
	public void printTable(TableType type) {
		DBTablePrinter.printTable(this.connection, type.toString());
	}
	
// -- Helper Methods --------------------------------------------------------------------------
	
	/**	
	 * Helper method that returns a list of String[] object based on the input ResultSet object
	 * 
	 * @param set - ResultSet object
	 * @return - Returns a List<<r>String[]> object containing values from a table entry
	 */
	
	private static List<String[]> ResultSetToList(ResultSet set){
		try {
			int nCol = set.getMetaData().getColumnCount();
			List<String[]> table = new ArrayList<>();
			while( set.next()) {
			    String[] row = new String[nCol];
			    for( int iCol = 1; iCol <= nCol; iCol++ ){
			            String obj = set.getString( iCol );
			            row[iCol-1] = (obj == null) ?null: obj;
			    }
			    table.add( row );
			}
			return table;
		} catch (SQLException e) {
			System.out.println("error converting ResultSet to list");
		}
		return null;
	} 
	
}//end class DataBase---------------------------------------------------------------------------------------------------------
