package me.rezscripts.rpgexperience.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.rezscripts.rpgexperience.RPGCore;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolManager {

    private HikariDataSource dataSource;

    private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;

    private int minimumIdle;
    private int poolSize;
    private long connectionTimeout;
    private String testQuery;

    public ConnectionPoolManager() {
        initialize();
        setupPool();
        for (int k = 0; k < 5; k++)
            testConn();
    }

    private void initialize() {
        hostname = "localhost";
        port = "3306";
        database = RPGCore.TEST_REALM ? "rpgexperience_test" : "rpgexperience_server";
        username = "root";
        password = ""; //example password
        minimumIdle = 5;
        poolSize = 15;
        connectionTimeout = 15000; //millis
        testQuery = "show tables";
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(poolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setConnectionTestQuery(testQuery);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    private void testConn() {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("SHOW DATABASES;");
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, conn);
        }
        System.out.println("Ran test query in " + (System.currentTimeMillis() - start) + "ms");
    }

    public void close(AutoCloseable... toClose) {
        for (AutoCloseable a : toClose) {
            if (a != null) {
                try {
                    a.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            try {
                dataSource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}