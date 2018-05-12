/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.database;

import de.ncrypted.rsb.RSB;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
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
            close(cs, crs);
        });
    }

    public void createAccount(UUID uuid, Consumer<Integer> consumer) {
        pool.execute(() -> {
            CustomStatement cs = new CustomStatement(getConn(), "INSERT INTO accounts (uuid,balance) VALUES (?,0)",
                    true)
                    .setString(1, uuid.toString())
                    .execUpd();
            CustomResultSet crs = cs.getGeneratedKeys();
            if (crs.next()) {
                consumer.accept(crs.getInt(1));
            } else {
                consumer.accept(null);
            }
        });
    }

    public void deleteAccount(int id) {
        pool.execute(() -> {
            CustomStatement cs = new CustomStatement(getConn(), "DELETE FROM accounts WHERE id = ?")
                    .setInt(1, id)
                    .execUpd();
        });
    }

    public void getAccounts(UUID uuid, Consumer<Set<Integer>> consumer) {
        pool.execute(() -> {
            Set<Integer> result = new HashSet<>();
            CustomStatement cs = new CustomStatement(getConn(), "SELECT id FROM accounts WHERE uuid = ?")
                    .setString(1, uuid.toString());
            CustomResultSet crs = cs.execQuery();
            while (crs.next()) {
                result.add(crs.getInt("id"));
            }
            consumer.accept(result);
            close(cs, crs);
        });
    }

    public void getBalance(int id, Consumer<Long> consumer) {
        pool.execute(() -> {
            CustomStatement cs = new CustomStatement(getConn(), "SELECT balance FROM accounts WHERE id = ?")
                    .setInt(1, id);
            CustomResultSet crs = cs.execQuery();
            if (crs.next()) {
                consumer.accept(crs.getLong("balance"));
            } else {
                consumer.accept(null);
            }
        });
    }

    public void setBalance(int id, long balance) {
        pool.execute(() -> {
            new CustomStatement(getConn(), "UPDATE accounts SET balance = ? WHERE id = ?")
                    .setLong(1, balance)
                    .setInt(2, id)
                    .execUpd();
        });
    }

    public void getHolder(int id, Consumer<UUID> consumer) {
        pool.execute(() -> {
            CustomStatement cs = new CustomStatement(getConn(), "SELECT uuid FROM accounts WHERE id = ?")
                    .setInt(1, id);
            CustomResultSet crs = cs.execQuery();
            if (crs.next()) {
                consumer.accept(UUID.fromString(crs.getString("uuid")));
            } else {
                consumer.accept(null);
            }
            close(cs, crs);
        });
    }

    // UTILS //
    private void close(CustomStatement cs, CustomResultSet crs) {
        if (crs != null) {
            crs.close();
        }
        if (cs != null) {
            cs.close();
        }
    }

    private Connection getConn() {
        return mysql.getConnection();
    }
}
