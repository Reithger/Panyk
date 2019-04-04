package database;

/**	
 * Enumeration definition for each table type in the database
 * 
 * @author Regan Lynch
 *
 */

public enum TableType {
	
	/*
	 * for all enumerations format is:
	 * 		<field name>, <field type>, ...
	 * Final string in enumeration is the key field (if length of arguments is not even).
	 * In this format there can only be one key field.
	 */
	
//---  Enumerations   -------------------------------------------------------------------------
	
	/** username, fname, lname, createdAt, salted_password, salt */			//KEY = username	(index 0)
	users("username","varchar(60)", "fname","varchar(60)", "lname","varchar(60)", "createdAt", "varchar(60)", "salted_password","varchar(60)","salt", "varchar(60)", "username"),
	
	/** username, tripTitle, destination, startDate, endDate, description */				
	trips("username", "varchar(60)", "tripTitle", "varchar(60)", "destination", "varchar(60)", "startDate", "varchar(60)", "endDate", "varchar(60)", "description", "varchar(60)"),
	
	/** username, tripTitle, item, type */									
	scheduleItem("username", "varchar(60)","tripTitle", "varchar(60)", "item", "varchar(60)", "type", "varchar(60)"),
	
	/** username, tripTitle, item, fname, lname, company, jobTitle, PhoneNumber, Address */				
	contacts("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "name", "varchar(60)", "description", "varchar(60)", "phoneNumber", "varchar(60)", "address", "varchar(60)"),
	
	//username, type
	shed_arc_types("username", "varchar(60)", "type", "varchar(60)");
	
	
	/** username, tripTitle, item, startTime, endTime, mode */			
	//transportation("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "startTime", "varchar(60)", "endTime", "varchar(60)", "mode", "varchar(60)"),
	
	/** username, tripTitle, item, name, checkIn, checkOut, paid, address*/
	//accommodations("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "name", "varchar(60)", "checkIn", "varchar(60)", "checkOut", "varchar(60)", "paid", "boolean", "address", "varchar(60)"),
	
	/** username, tripTitle, item, name, startDate, endDate, address*/
	//reservations("username", "varchar(60)", "tripTitle", "varchar(60)", "name", "varchar(60)", "startDate", "varchar(60)", "endDate", "varchar(60)", "address", "varchar(60)");
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	public String[] fields;
	/** */
	public String[] fieldTypes;
	/** */
	public String keyField;
	/** sql statement for creating the table*/
	public String sqlCreateTable;
	/** sql statement for inserting into the table*/
	public String sqlInsertTable;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the TableType type
	 * 
	 * @param entries - String ... (var args) object
	 */
	
	private TableType(String... entries) {
		//check if there is a key field
		this.keyField = (entries.length % 2 == 1)? entries[entries.length - 1]: null; 
		
		this.fields = new String[(entries.length/2)];			//here, integer rounding takes care of odd number division
		this.fieldTypes = new String[(entries.length/2)];
		for(int i = 0; i < this.fields.length; i++) {
			this.fields[i] = entries[i * 2];
			this.fieldTypes[i] = entries[(i * 2) + 1];
		}
		this.generateCreateTableSQL();
		this.generateTableInsertionSQL();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 *  This method generates the sql statement required to initialize a TableType object
	 */
	
	private void generateCreateTableSQL() {
		String sql = "CREATE TABLE " + this.toString() + "(";
		for(int i = 0; i < this.fields.length; i++) {
			sql += this.fields[i] + " " + this.fieldTypes[i];
			if(i != this.fields.length - 1) {
				sql += ",";
			}
		}
		if(this.keyField != null) {
			sql += ",primary key(" + this.keyField + ")";
		}
		sql += ");";
		this.sqlCreateTable = sql;
	}
	
	/**
	 * This method generates an sql statement used to submit a new Table Type to the database
	 * to create a new type of repository for information. Takes in two String[] objects, the
	 * first to describe the title for each column in this new repository and the second to
	 * define the data type of each.
	 * 
	 * @param tableTitle - String object representing the name of the new Table Type being added to the database
	 * @param fieldsDyn - String[] representing the column titles for the new Table Type in the database
	 * @param fieldTypesDyn - String[] representing the data types for each title in the new Table Type
	 * @return - Returns a String object representing the generated sql statement.
	 */
	
	public static String generateCreateTableSQL(String tableTitle, String[] fieldsDyn, String[] fieldTypesDyn) {

		String sql = "CREATE TABLE " + tableTitle + "(";
		for(int i = 0; i < fieldsDyn.length; i++) {
			sql += fieldsDyn[i].replaceAll(" ", "_") + " " + fieldTypesDyn[i] + (i + 1 < fieldsDyn.length ? "," : ");");
		}
		return sql;
	}

	/**
	 * This method generate the sql statement required for insertion into a database
	 */
	
	private void generateTableInsertionSQL() {
		String sql = "INSERT INTO " + this.toString() + "(";
		String valueStr = "values(";
		for(int i = 0; i < this.fields.length; i++) {
			sql += this.fields[i];
			valueStr += "?";
			if(i != this.fields.length - 1) {
				sql += ",";
				valueStr += ",";
			}
		}
		valueStr += ")";
		sql += ") ";
		sql = sql + valueStr;
		sqlInsertTable = sql;
	}
	
	/**
	 * This method creates the sql statement required to insert a new entry into the
	 * database for a given Table Type with its column titles (provided separately).
	 * 
	 * Format is 'INSERT INTO [tableTitle] ([title1],[title2],...) (?,?,...)'
	 * 
	 * Used in tandem with another process that will fill in the '?' part using a
	 * PreparedStatement object's .setString method. 
	 * 
	 * @param tableTitle - String object representing the Table Type being inserted into.
	 * @param fieldsDyn - String[] representing the column titles for this Table Type
	 * @return - Returns a String object representing the sql statement for inserting data into the database.
	 */
	
	public static String generateCreateTableInsertionSQL(String tableTitle, String[] fieldsDyn) {
		String sql = "INSERT INTO " + tableTitle + "(";
		String valueStr = "values(";
		for(int i = 0; i < fieldsDyn.length; i++) {
			sql += fieldsDyn[i].replaceAll(" ", "_") + (i+1 < fieldsDyn.length ? "," : ")");
			valueStr += "?" + (i + 1 < fieldsDyn.length ? "," : ")");
		}
		return sql + " " + valueStr;
	}
	
}

