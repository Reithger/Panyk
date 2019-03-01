package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


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
	private static final String DB_DIRECTORY = "C:/sqlite3/databases/";	//TODO: Can't hardcode an address in final
	
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
                	//create table types as needed
                	for(TableType table : TableType.values()) {
                		//if a table type isnt already in the database -> create it
                		if(this.getTable(table.toString()) == null) {
                			state.execute(table.sqlCreateTable);
                		}
                	}	
                }               
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());  
        }
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * Gets connection to database 
	 * 
	 * @return	connecttion to database
	 */
	
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
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Get result set of the given table name.
	 * 
	 * @param table_name
	 * @return
	 */
	
	public ResultSet getTable(String table_name) {
		if(this.connect() ==  null) {
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
	
//---  Adder Method   ------------------------------------------------------------------------
	
	
	/**	adds an entry into a desired table
	 * 
	 * @param table		table you wish to insert into
	 * @param values	values of fields you wish to insert into table
	 * @return	if insertion was successful
	 */
	public boolean addEntry(TableType table, String... values) {
		if(this.connect() == null) {
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
		}catch(Exception e) {
			System.out.println("-----");
			System.out.println("ERROR inserting id: " + values[0] + "  into " + table.toString() + " database");
			System.out.println("primary key " + values[0] + " already exists in table");
			System.out.println("-----");
			return false;
		}
		return true;
	}
	
// --- deleter method --------------------------------------------------------------------------------
	
	
	/**	deletes entries from tables
	 * 
	 * @param table		table you wish to delete from
	 * @param searchKeys	search parameters for deletion
	 * @return	if deletion was successful
	 */
	public boolean deleteEntry(TableType table, String... searchKeys) {
		if(this.connect() == null) {
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
	
//--- search method  ------------------------------------------------------------------------
	
	
	/**	searches tables based on search parameters
	 * 
	 * @param table		table you wish to query
	 * @param searchKeys	fields you wish to search by 
	 * @return	list of string array containing elements that matched your search
	 */
	public List<String[]> search(TableType table, String... searchKeys) {
		ResultSet result = null;
		if(this.connect() == null) {
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
	
	
//---  Print Method   ------------------------------------------------------------------------
	
	
	/**	prints table to console
	 * 
	 * @param type	table you wish to print
	 */
	public void printTable(TableType type) {
		DBTablePrinter.printTable(this.connection, type.toString());
	}
	
// -- helper methods -------------------------------------------------------------------------
	
	
	/**	returns list off string arrays based on input ResultSet
	 * 
	 * @param set	
	 * @return	list of string arrays containing values from a table entry
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
			System.out.println("error converting ResultSet to 2D array");
		}
		return null;
	} 
	
}//end class DataBase---------------------------------------------------------------------------------------------------------
