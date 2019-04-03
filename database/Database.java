package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public Database()
	{
		super();
	}
//---  Constant Values   ----------------------------------------------------------------------
	
	/** */
	private static final String DB_DIRECTORY = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/";  	//get the current directory of the user (ie: where the program is installed)
	/** static final database name -> so that other classes can access the database */
	public static final String DB_NAME = "PLEIN_AIR_DATABASE";
	
//---  Static Variables   -------------------------------------------------------------------
	
	/** */
	private static String name;
	/** */
	private static Connection connection;
	
	//keeping track of if the database has been d 
	private static boolean db_is_initialized;
	
	public static HashMap<String, String> FIELD_TYPE_CONVERT;

	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * initializes the db
	 */
	
	public static void initialize() {
		if(db_is_initialized) {
			return;
		}
		else {
			db_is_initialized = true;
		}
		
		name = DB_NAME;
   	 	connection = null;

		FIELD_TYPE_CONVERT = new HashMap<String, String>();
		FIELD_TYPE_CONVERT.put("lString", "varChar(300)");
		FIELD_TYPE_CONVERT.put("sString", "varChar(60)");
		FIELD_TYPE_CONVERT.put("Date", "varChar(10)");
		
        connect();

        
        if (connection != null) {
            System.out.println("connected to database --> "+ DB_DIRECTORY + name + ".db");

        	Statement state = null;
			try {
				state = connection.createStatement();
	        	//create table types as needed
	        	for(TableType table : TableType.values()) {
	        		//if a table type isn't already in the database -> create it
	        		if(getTable(table.toString()) == null) {
							state.execute(table.sqlCreateTable);
	        		}
	        		else {
	        			System.out.println("Attempted to Add Duplicate Database Entry of Type " + table.toString() + ".");
	        		}
	        	}
        	}
			catch(Exception e) {
				e.printStackTrace();
        		System.out.println(e.getMessage());
        	}
        }
        else {
        	System.out.println("Connection to database " + DB_DIRECTORY + name + ".db" + " could not be established");
        	db_is_initialized = false;
        }
	}
	
	/**
	 * Method that takes dynamic Table Types retrieved from the metaFields database table
	 * and adds them to the Database as tables for adding Schedulables to.
	 *  
	 * @param tableType - String object representing which database table to include (the header name of this table type)
	 * @param fields - String[] representing the title of each database column
	 * @param fieldTypes - String[] representing the data type (varChar(60), etc.) for each database column
	 */
	
	public static void includeTableType(String tableType, String[] fields, String[] fieldTypes) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		if(connection == null) {
			System.out.println("no connection can be established to the database");
			return;
		}
		if(getTable(tableType) != null) {
			System.out.println(tableType + " already extant during includeTableType");
			return;
		}
		String[] convert = new String[fieldTypes.length];
		for(int i = 0; i < convert.length; i++) {
			convert[i] = FIELD_TYPE_CONVERT.get(fieldTypes[i]);
		}
		String sqp = TableType.generateCreateTableSQL(tableType, fields, convert);
		System.out.println(sqp);
		Statement state = null;
		try {
			state = connection.createStatement();
    		state.execute(sqp);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * Gets connection to database 
	 * 
	 * 
	 */
	
	private static void connect() {
		try {
			if(connection == null || connection.isClosed()) {
		        try {
		            // db parameters
		            String url = "jdbc:sqlite:"+ DB_DIRECTORY + name + ".db";
		            // create a connection to the database
		            connection = DriverManager.getConnection(url);
		        } catch (SQLException e) {
		        	e.printStackTrace();
		            System.out.println("Error in Connecting to Database: Getting Connection");
		        }
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error in Connecting to Database: Querying if Connection was Closed");
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Get result set of the given table name.
	 * 
	 * @param table_name
	 * @return
	 */
	
	public static ResultSet getTable(String table_name) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		if(connection == null) {
			System.out.println("no connection can be established to the database");
			return null;
		}
		Statement state=null;
		ResultSet result=null;
		try {
			state=connection.createStatement();
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
	
	public static boolean checkUserExists(String username) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		if(connection == null) {
			System.out.println("no connection can be established to the database");
			return false;
		}
		List<String[]> users = search(TableType.users, username, null, null, null, null, null);
		if(users == null)
			return false;
		return(users.size() != 0);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	
	public static boolean checkValidPassword(String username, String password) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		List<String[]> users = search(TableType.users, username, null, null, null, null, null);
		if(users.size() != 0) {
			String salt = users.get(0)[5];
			String real_salted_pass = users.get(0)[4];
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
	
	public static boolean addEntry(TableType table, String ... values) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		if(connection == null) {
			System.out.println("no connection can be established to the database");
			return false;
		}
		if(table.fields.length != values.length) {
			System.out.println("error inserting into table "+ table.toString() + ": number of fields provided does not equal number of fields needed");
			return false;
		}
		//check if entry exists in database
		try {
			PreparedStatement prep = connection.prepareStatement(table.sqlInsertTable);
			for(int i = 0; i < values.length; i++) {
				prep.setString(i + 1, values[i]);
			}
			prep.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("-----");
			System.out.println("ERROR inserting into table -> " + table.toString() + " ...");
			System.out.println("primary key: " + values[0] + " already exists in "+ table.toString() +" table");
			System.out.println("-----");
			return false;
		}
		return true;
	}
	
	/**
	 * This method adds an entry into the table described by tableTitle, using the String[]
	 * types as the header for each column in that tableTitle table from the database and
	 * the String[] values representing the data to be stored therein.
	 * 
	 * @param tableTitle - String object representing the table in the database to interact with
	 * @param types - String[] object representing the headers for which values should be added in the database
	 * @param values - String[] object representing the values to be added to corresponding column headers in the database
	 * @return - returns a Boolean value representing the result of this operation.
	 */
	
	public static boolean addEntry(String tableTitle, String[] types, String[] values) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		if(connection == null) {
			System.out.println("no connection can be established to the database");
			return false;
		}
		try {
			if(values.length < types.length) {
				String[] copy = new String[types.length];
				for(int i = 0; i < values.length; i++){
					copy[i] = values[i];
				}
				for(int i = values.length; i < types.length; i++) {
					copy[i] = "null";
				}
				values = copy;
			}

			List<String[]> check = search(tableTitle, types, values);
			if(check != null && check.size() != 0) {
				System.out.println("Attempted to Add Duplicate Database Entry of Type " + tableTitle + ".");
				return false;
			}
			//must reconnect here for some reason
			connect();
			PreparedStatement prep = connection.prepareStatement(TableType.generateCreateTableInsertionSQL(tableTitle, types));
			for(int i = 0; i < values.length; i++) {
				prep.setString(i + 1, values[i].replaceAll(" ", " "));
			}
			prep.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("-----");
			System.out.println("ERROR inserting into table -> " + tableTitle + " ...");
			System.out.println("primary key: " + values[0] + " already exists in "+ tableTitle +" table");
			System.out.println("-----");
			return false;
		}
		return true;
	}
	
// --- deleter method --------------------------------------------------------------------------------
		
	/**	
	 * This method deletes entries from the defined table
	 * 
	 * BE CAREFULL -> this will delete everything that matches the search keys
	 * 
	 * @param table	- TableType object you wish to delete from
	 * @param searchKeys - String ... (var args) values representing the search parameters for deletion
	 * @return - Returns a boolean value representing the result of deletion; true if successful, false otherwise
	 */
	
	public static boolean deleteEntry(TableType table, String... searchKeys) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		if(connection == null) {
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
				PreparedStatement prep = connection.prepareStatement(sqlDelete);
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

	public static boolean deleteEntry(String tableType, String[] fields, String[] values) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		if(connection == null) {
			System.out.println("no connection can be established to the database");
			return false;
		}
		if(fields.length != values.length) {
			System.out.println("error deleting from table "+ tableType + ": number of search fields provided does not equal number of fields needed");
			return false;
		}
		try {
			String sqlDelete = "DELETE FROM " + tableType + " WHERE";
			int nullCount = 0;
			boolean priorSearch = false;
			for(int i = 0; i < fields.length; i++) {
				if(fields[i] != null) {
					if(priorSearch) {
						sqlDelete += " AND";
					}
					sqlDelete += " " + fields[i] + "='" + values[i] + "'";
					priorSearch = true;
				}else {
					nullCount++;
				}
			}
			sqlDelete += ";";
			if(nullCount != values.length) {
				PreparedStatement prep = connection.prepareStatement(sqlDelete);
				prep.executeUpdate();
			}else {
				System.out.println("error deleting table: " + tableType + " --> no search keys defined");
				return false;
			}
		}catch(Exception e) {
			System.out.println("error deleting from table: " + tableType);
			return false;
		}
		return true;
	}
	
//--- entryEdit method -----------------------------------------------------------------------
	
	/**
	 * 		edits an entry in the db by finding the entry first based on search keys and table type, 
	 * 
	 * @param table				the table you wish to edit
	 * @param editIndex			the index of the element you wish to edit
	 * @param newValue			the string which you want to replace the previous value with
	 * @param searchKeys		the search keys to find the entry you want to edit
	 * @return
	 */

	public static boolean editEntry(TableType table, int editIndex, String newValue, String... searchKeys) {
		List<String[]> old_entry = search(table, searchKeys);
		if(old_entry.size() == 0) {
			System.out.println("error editing entry in table " + table.toString() + ", no entries found with given search keys");
			return false;
		}else if(old_entry.size() > 1) {
			System.out.println("error editing entry in table " + table.toString() + ", more than one entry found with the given search keys");
			return false;
		}else if(editIndex >= table.fields.length || editIndex < 0) {
			System.out.println("error editing entry in table " + table.toString() + ", the index at which you are trying to edit is invalid");
			return false;
		}else if(table == TableType.users) {		//you are not allowed to edit a user
			System.out.println("you are not allowed to edit the user table, only delete from it or add to it");
			return false;
		}else if(editIndex == 0) {
			System.out.println("you are not allowed to edit the the username of any entry");
			return false;
		}else {
			//delete the old entry
			boolean del_flag = deleteEntry(table, searchKeys);
			if(!del_flag) {
				System.out.println("error editing entry in table " + table.toString() + ", error deleting old entry from table");
				return false;
			}
			String[] old_vals = old_entry.get(0);
			String[] new_vals = old_vals;
			new_vals[editIndex] = newValue;
			//return the status of the addEntry
			return addEntry(table, new_vals);
		}
	}
	
//--- Search Methods  -------------------------------------------------------------------------
	
	/**	
	 * This method searches the defined table based on search parameters
	 * 
	 * @param table	- TableType object you wish to query
	 * @param searchKeys - String ... (var args) values representing the fields you wish to search by 
	 * @return - Returns a List<<r>String[]> object containing elements that matched your search
	 */

	public static List<String[]> search(TableType table, String... searchKeys) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		ResultSet result = null;
		if(connection == null) {
			System.out.println("no connection can be established to the database");
			return null;
		}
		if(searchKeys.length != table.fields.length) {
			System.out.println("error searching table "+ table.toString() + ": number of search fields provided " + searchKeys.length + " does not equal number of fields needed " + table.fields.length);
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
				}
				else {
					nullCount++;
				}
			}
			sqlSearch += ";";
			if(nullCount != searchKeys.length) {
				Statement state = null;
				try {
					state = connection.createStatement();
					result = state.executeQuery(sqlSearch);
					return ResultSetToList(result);
				}catch(SQLException sqlE2) {
					connection.close();
					return null;
				}
			}
			else {
				System.out.println("error searching table: " + table.toString() + " --> no search keys defined");
				return null;
			}
		}catch(Exception e) {
			System.out.println("error searching in table: " + table.toString());
			return null;
		}
	}
	
	/**
	 * This method queries the database in the specified tableType section given the defined
	 * fields and values for each to narrow down the results by. If fields is longer than
	 * search, then the extraneous field terms are left out.
	 * 
	 * @param tableType - String object representing the table in the database to interact with
	 * @param fields - String[] containing the column headers for each piece of data stored in this table in the database
	 * @param search - String[] containing the search terms for each column header to possess when searching
	 * @return - Returns a List<String[]> object containing a list of all matching rows in the database
	 */
	
	public static List<String[]> search(String tableType, String[] fields, String[] search){
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		try {
			String sqlSearch = "SELECT * FROM " + tableType + (fields.length == 0 ? "" : " WHERE");
			boolean priorSearch = false;
			ResultSet result = null;
			for(int i = 0; i < fields.length; i++) {
				if(fields[i] != null) {
					if(i < search.length) 
						sqlSearch += (priorSearch ? " AND" : "") + " " + fields[i].replaceAll(" ", "_") + "='" + search[i].replaceAll(" ", "_") + "'";
//						sqlSearch += (priorSearch ? " AND" : "") + " " + fields[i] + "='" + search[i] + "'";
					
					priorSearch = true;
				}
			}
			sqlSearch += ";";
			Statement state = null;
			try {
				state = connection.createStatement();
				result = state.executeQuery(sqlSearch);
				return ResultSetToList(result);
			}
			catch(SQLException sqlE2) {
				sqlE2.printStackTrace();
				System.out.println("Error in Search of Table " + tableType);
				connection.close();
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("error searching in table: " + tableType);
			return null;
		}
	}
	
//---  Print Methods   ------------------------------------------------------------------------
	
	/**	
	 * This method prints the defined table to console
	 * 
	 * @param type - TableType object representing the table you want to print
	 */
	
	public static void printTable(TableType type) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		DBTablePrinter.printTable(connection, type.toString());
	}
	
	/**	
	 * This method prints the defined table to console
	 * 
	 * @param type - TableType object representing the table you want to print
	 */
	
	public static void printTable(String table) {
		if(!db_is_initialized) {
			initialize();
		}
		connect();
		DBTablePrinter.printTable(connection, table);
	}
	
// -- Helper Methods --------------------------------------------------------------------------
	
	/**	
	 * Helper method that returns a list of String[] object based on the input ResultSet object
	 * 
	 * @param set - ResultSet object
	 * @return - Returns a List<<r>String[]> object containing values from a table entry
	 */
	
	public static List<String[]> ResultSetToList(ResultSet set){
		try {
			int nCol = set.getMetaData().getColumnCount();
			List<String[]> table = new ArrayList<>();
			while( set.next()) {
			    String[] row = new String[nCol];
			    for( int iCol = 1; iCol <= nCol; iCol++ ){
			    	String obj = set.getString( iCol );
			        row[iCol-1] = (obj == null) ? null: obj;
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
