package perform.db;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPool {
    private ConnectionPool() {

    }

    public static HikariDataSource getDataSource() {
    	try (InputStream input = new FileInputStream("../../perform/.env")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            String db_host= prop.getProperty("db_host").replace("\"", "");
            String db_port= prop.getProperty("db_port").replace("\"", "");
            String db_name= prop.getProperty("db_name").replace("\"", "");
            String db_user = prop.getProperty("db_user").replace("\"", "");
            String db_pass= prop.getProperty("db_pass").replace("\"", "");
            String db_url = "jdbc:mysql://" + db_host + ":" + db_port + "/" + db_name;
            
            System.out.println(db_url);
            System.out.println(db_user);
            System.out.println(db_pass);
            
            HikariConfig config = new HikariConfig();
//            config.setJdbcUrl("jdbc:mysql://localhost:3306/perform");
            config.setJdbcUrl(db_url);
            config.setPoolName("poolName");
//            config.setMaximumPoolSize(50);
            config.setMinimumIdle(10);
            
            config.setUsername(db_user);
            config.setPassword(db_pass);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", true);
            
            HikariDataSource ds = new HikariDataSource(config);

            return new HikariDataSource(ds);

        } catch (IOException ex) {
        	System.out.println("ERROR: Cannot read .env properties file!");
            ex.printStackTrace();
        }
    	return null;
        
    }
}

