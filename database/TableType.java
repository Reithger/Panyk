package Database;



/**	enumeration definition for each table type in the database
 * 
 * @author Regan Lynch
 *
 */
public enum TableType {
	
	//for all enumerations format is..
		// <field name>, <field type>..... 
	//final string in enumeration is the key field (if length of arguments is not even)
		//in this format there can only be one key field
	
	
	//id, username, password, fname, lname, DOB, createdAt
	users("id","varchar(60)", "username","varchar(60)", "password","varchar(60)", "fname","varchar(60)", "lname","varchar(60)", "DoB", "varchar(60)", "createdAt", "varchar(60)", "id"),
	
	//id, userID, tripTitle, destination, startDate, endDate
	trips("id", "varchar(60)", "userID", "varchar(60)", "tripTitle", "varchar(60)", "destination", "varchar(60)", "startDate", "varchar(60)", "endDate", "varchar(60)", "id" ),
	
	//id, tripID, type
	scheduleItem("id", "varchar(60)", "tripID", "varchar(60)", "type", "varchar(60)", "id"),
	
	//id, scheduleItemID, fname, lname, company, jobTitle, displayAs, email, webpage, PhoneNumber, Address
	contacts("id", "varchar(60)", "scheduleItemID", "varchar(60)", "fname", "varchar(60)", "lname", "varchar(60)", "company", "varchar(60)", "jobTitle", "varchar(60)", "displayAS", "varchar(60)", "email", "varchar(60)", "webpage", "varchar(60)", "phoneNumber", "varchar(60)", "address", "varchar(60)", "id"),
	
	//id, scheduleItemID, startTime, endTime
	transportation("id", "varchar(60)", "scheduleItemID", "varchar(60)", "startTime", "varchar(60)", "endTime", "varchar(60)", "id"),
	
	//id, tripID, scheduleItemID, name, checkIn, checkOut, paid, address, contact
	accommodations("id", "varchar(60)", "tripID", "varchar(60)", "scheduleItemID", "varchar(60)", "name", "varchar(60)", "checkIn", "varchar(60)", "checkOut", "varchar(60)", "paid", "boolean", "address", "varchar(60)", "contact", "varchar(60)", "id"),
	
	//id, scheduleItemID, tripID, contact, participants, startDate, endDate, address, type
	reservations("id", "varchar(60)", "scheduleItemID", "varchar(60)", "contact", "varchar(60)", "startDate", "varchar(60)", "endDate", "varchar(60)", "address", "varchar(60)", "type", "varchar(60)", "id");
	
	
	
	public String[] fields;
	public String[] fieldTypes;
	public String keyField;
	
	public String sqlCreateTable;			//sql statement for creating the table
	public String sqlInsertTable;			//sql statement for inserting into the table
	
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
	
	/**
	 *  generates the sql statement required to initialize a table type
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
	 * generate the sql statement required for insertion into a database
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
		this.sqlInsertTable = sql;
	}
	
	
}
