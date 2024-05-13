package lat.jack.etl.utils;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleDBUtil  {

    // Operational Database Configuration
    private static final String OP_DB_URL = EnvironmentVariables.getVariable("OP_DB_URL");
    private static final String OP_DB_USERNAME = EnvironmentVariables.getVariable("OP_DB_USER");
    private static final String OP_DB_PASSWORD = EnvironmentVariables.getVariable("OP_DB_PASSWORD");

    // Data Warehouse Database Configuration
    private static final String DW_DB_URL = EnvironmentVariables.getVariable("DW_DB_URL");
    private static final String DW_DB_USERNAME = EnvironmentVariables.getVariable("DW_DB_USER");
    private static final String DW_DB_PASSWORD = EnvironmentVariables.getVariable("DW_DB_PASSWORD");

    private static final DataSource opDataSource = setupDataSource(OP_DB_URL, OP_DB_USERNAME, OP_DB_PASSWORD);
    private static final DataSource dwDataSource = setupDataSource(DW_DB_URL, DW_DB_USERNAME, DW_DB_PASSWORD);

    private static DataSource setupDataSource(String url, String username, String password) {
        System.out.println("Setting up data source for URL: " + url);
        System.out.println("Setting up data source for Username: " + username);
        System.out.println("Setting up data source for Password: " + password);
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        // Configuration options
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxTotal(20); // Max total connections
        ds.setMaxWaitMillis(5000); // Max wait for a connection
        ds.setMaxOpenPreparedStatements(100);

        return ds;
    }

    public static Connection getOpDataSource() throws SQLException {
        return opDataSource.getConnection();
    }

    public static Connection getDwDataSource() throws SQLException {
        return dwDataSource.getConnection();
    }

}