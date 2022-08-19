package perfrt.db;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPool {
    private ConnectionPool() {

    }

    public static HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/perfrt");
        config.setPoolName("poolName");
//        config.setMaximumPoolSize(50);
        config.setMinimumIdle(10);
        
        config.setUsername("root");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", true);
        
        HikariDataSource ds = new HikariDataSource(config);

        return new HikariDataSource(ds);
    }
}

