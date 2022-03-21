import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {
    
    private static BasicDataSource ds = new BasicDataSource();
    


    static {
//      Dotenv dotenv=Dotenv.configure().ignoreIfMissing().ignoreIfMalformed().load();
    	String user="root";//dotenv.get("db_user");
    	String password="password"; //dotenv.get("db_pass");
    	String dbName="perfrt"; //dotenv.get("db_name");
    	String dbHost="localhost"; //dotenv.get("db_host");
    	String dbPort="3306"; //dotenv.get("db_port");	    	
    	
    	String myUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName; 
    	
        ds.setUrl(myUrl);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    private DBCPDataSource(){ }
}