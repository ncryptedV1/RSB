/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ncrypted
 */
public class MySqlController {

    private final String HOST;
    private final int PORT;
    private final String USER;
    private final String PASSWORD;
    private final String DATABASE;
    private HikariDataSource dataSource;
    private RSB rsb;

    public MySqlController(RSB rsb) {
        this.rsb = rsb;
        HOST = rsb.getConfigHandler().getMessage("mysql-host");
        PORT = Integer.valueOf(rsb.getConfigHandler().getNumber("mysql-port"));
        USER = rsb.getConfigHandler().getMessage("mysql-user");
        PASSWORD = rsb.getConfigHandler().getMessage("mysql-password");
        DATABASE = rsb.getConfigHandler().getMessage("mysql-database");
        connect();
    }

    public void connect() {
        if (isConnected()) {
            return;
        }
        try {
            Connection creationCon = DriverManager.getConnection(
                    "jdbc:mysql://" + HOST + ":" + PORT + "/?user=" + USER + "&password=" + PASSWORD);
            new CustomStatement(creationCon, "CREATE DATABASE IF NOT EXISTS " + DATABASE).execUpd();
            creationCon.close();
        } catch (SQLException e) {
            Logger.err("Could not establish connection to MySql database: " + e.getMessage());
            Logger.err("Cancelling startup...");
            rsb.stopStartup();
            return;
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        dataSource = new HikariDataSource(config);
        rsb.getMySqlInterface().createTables();
    }

    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        dataSource.close();
    }

    public boolean isConnected() {
        return dataSource != null && !dataSource.isClosed();
    }

    public Connection getConnection() throws SQLException {
        if (isConnected())
            return dataSource.getConnection();
        return null;
    }
}
