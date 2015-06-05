package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlManager{

	private static SqlManager manager = new SqlManager();
	private Connection connection;
	private Statement executor;
	
	private static final String DRIVER = "jdbc:sqlserver://";
	
	private String connectionUrl = "jdbc:sqlserver://localhost;"; 
	private String server = "localhost";
	
	private String user = "Admin123"; 
	private String password = "123Admin";
	private String database;

	private SqlManager(){
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (isConnected()) {
			disconnect();
		}
		
		super.finalize();
	}

	public static void connect() {
		
		try {
			
			disconnect();
			
			System.out.println("Connecting " + manager.connectionUrl);
			
			manager.connection = DriverManager.getConnection(
					manager.connectionUrl, 
					manager.user, 
					manager.password);
			
			manager.executor = manager.connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void connect(String connectionUrl, String user, String password) {
		
		setConnectionUrl(connectionUrl);
		setUser(user);
		setPassword(password);
			
		connect();
	}

	public static void disconnect() {
		
		try {
			
			if (manager.connection != null && !manager.connection.isClosed()) {
				
				System.out.println("Closing connection " + manager.connectionUrl);
				manager.connection.close();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getConnectionUrl() {
		return manager.connectionUrl;
	}

	public static void setConnectionUrl(String connectionUrl) {
		if (!manager.connectionUrl.equals(connectionUrl)) {
			disconnect();
		}
		manager.connectionUrl = connectionUrl;
	}

	public static String getServer() {
		return manager.server;
	}

	public static void setServer(String server) {
		if (!manager.server.equals(server)) {
			disconnect();
		}
		manager.server = server;
		setConnectionUrl(DRIVER + manager.server + ";"); 
	}

	public static String getUser() {
		return manager.user;
	}

	public static void setUser(String user) {
		if (!manager.user.equals(user)) {
			disconnect();
		}
		manager.user = user;
	}

	public static String getPassword() {
		return manager.password;
	}

	public static void setPassword(String password) {
		if (!manager.password.equals(password)) {
			disconnect();
		}
		manager.password = password;
	}

	public static String getDatabase() {
		return manager.database;
	}

	public static void setDatabase(String database) {
		manager.database = database;
	}

	public static boolean isConnected() {
		
		if (manager.connection != null) {
			try {
				return !manager.connection.isClosed();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	public static List<String> getDatabaseList(List<String> dbs) {
		
		if (dbs == null) {
			dbs = new ArrayList<>();
		}
		
		try {
			
			System.out.println("Retrieving database list from " + manager.connectionUrl);
			
			ResultSet rs = manager.executor.executeQuery("SELECT * FROM sys.databases ORDER BY name;");

			while (rs.next()) {
				dbs.add(rs.getString("name"));
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dbs;
	}
	
	public static List<String> getTableList(List<String> tbls) {
		
		if (tbls == null) {
			tbls = new ArrayList<>();
		}
		
		try {
			
			System.out.println("Retrieving table list from " + manager.connectionUrl);
			
			ResultSet rs = manager.executor.executeQuery(String.format(
					"SELECT * FROM %s.information_schema.tables "
					+ " ORDER BY TABLE_NAME;", manager.database));

			while (rs.next()) {
				tbls.add(rs.getString("TABLE_NAME"));
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tbls;
	}
	
	public static List<Map<String, String>> getTableData(String table, List<Map<String, String>> data) {
		
		if (data == null) {
			data = new ArrayList<>();
		}
				
		try {
			
			System.out.println("Retrieving table content from " + manager.connectionUrl);
			
			ResultSet rs = manager.executor.executeQuery(String.format("SELECT TOP 500 * FROM %s.dbo.%s;", manager.database, table));
			
			ResultSetMetaData md = rs.getMetaData();

			while (rs.next()) {
				Map<String, String> row = new HashMap<>();
				
				for (int i = 1; i <= md.getColumnCount(); i++) {
					row.put(md.getColumnName(i), rs.getString(i));
				}
				
				data.add(row);
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static List<Map<String, String>> getTableMetadata(String table) {
		
		List<Map<String, String>> data = new ArrayList<>();
				
		try {
			
			System.out.println("Retrieving table metadata from " + manager.connectionUrl);
			
			ResultSet rs = manager.executor.executeQuery(String.format("SELECT * FROM %s.INFORMATION_SCHEMA.COLUMNS "
					+ " WHERE TABLE_NAME = '%s'", manager.database, table));
			
			ResultSetMetaData md = rs.getMetaData();

			while (rs.next()) {
				Map<String, String> row = new HashMap<>();
				
				for (int i = 1; i <= md.getColumnCount(); i++) {
					row.put(md.getColumnName(i), rs.getString(i));
				}
				
				data.add(row);
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static void createDB(String name){
		try {
			manager.executor.execute("Create Database " + name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void parseMetadata(ResultSet rs){
		
		try {
			ResultSetMetaData md = rs.getMetaData();
			
			for (int i = 1; i <= md.getColumnCount(); i++) {
				System.out.println(md.getColumnName(i));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
			
	}
	
	
}
