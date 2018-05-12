/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.database;

import de.ncrypted.rsb.RSB;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author ncrypted
 */
public class MySqlInterface {

    private ExecutorService pool = Executors.newCachedThreadPool();
    private RSB rsb;
    private MySqlController mysql;

    public MySqlInterface(RSB rsb) {
        this.rsb = rsb;
        mysql = rsb.getMySqlController();
    }

    public void createTables() {
        CustomStatement cs = new CustomStatement(getConn(),
                "CREATE TABLE IF NOT EXISTS cash(uuid VARCHAR(36) NOT NULL, money BIGINT, PRIMARY KEY(uuid))");
        cs.execUpd();
    }

    public void setupPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        pool.execute(() -> {
            new CustomStatement(getConn(), "INSERT IGNORE INTO cash (uuid,money) VALUES (?,0)")
                    .setString(1, uuid.toString())
                    .execUpd();
        });
    }

    public void setCash(UUID uuid, long cash) {
        pool.execute(() -> {
            new CustomStatement(getConn(), "UPDATE cash SET money = ? WHERE uuid = ?")
                    .setLong(1, cash).setString(2, uuid.toString()).execUpd();
        });
    }

    public void getCash(UUID uuid, Consumer<Long> consumer) {
        pool.execute(() -> {
            CustomStatement cs = new CustomStatement(getConn(), "SELECT money FROM cash WHERE uuid = ?")
                    .setString(1, uuid.toString());
            CustomResultSet crs = cs.execQuery();

            consumer.accept(crs.next() ? crs.getLong("money") : null);
            close(crs, cs);
        });
    }

    // UTILS //
    private void close(CustomResultSet rs, CustomStatement cs) {
        if (rs != null) {
            rs.close();
        }
        if (cs != null) {
            cs.close();
        }
    }

    private Connection getConn() {
        try {
            return mysql.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
