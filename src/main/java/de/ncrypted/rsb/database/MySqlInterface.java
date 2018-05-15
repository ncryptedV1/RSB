/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.database;

import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.utils.Transaction;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.*;
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

    // CASH
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

            consumer.accept(crs.next() ? crs.getLong(1) : null);
            close(cs, crs);
        });
    }

    // ACCOUNTS
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
            close(cs, crs);
        });
    }

    public void deleteAccount(int id) {
        pool.execute(() -> {
            new CustomStatement(getConn(), "DELETE FROM accounts WHERE id = ?")
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
                result.add(crs.getInt(1));
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
                consumer.accept(crs.getLong(1));
            } else {
                consumer.accept(null);
            }
            close(cs, crs);
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
                consumer.accept(UUID.fromString(crs.getString(1)));
            } else {
                consumer.accept(null);
            }
            close(cs, crs);
        });
    }

    // TRANSFERS
    public void addTransfer(Transaction transaction) {
        pool.execute(() -> {
            new CustomStatement(getConn(),
                    "INSERT INTO transfers (date_recorded, sender, target, money) VALUES (?,?,?,?)")
                    .setTimestamp(1, transaction.getDateRecorded())
                    .setInt(2, transaction.getSender())
                    .setInt(3, transaction.getTarget())
                    .setLong(4, transaction.getMoney())
                    .execUpd();
        });
    }

    public void getTransfers(int id, Consumer<List<Transaction>> consumer) {
        pool.execute(() -> {
            List<Transaction> result = new ArrayList<>();
            CustomStatement cs = new CustomStatement(getConn(),
                    "SELECT * FROM transfers WHERE sender = ? OR target = ? ORDER BY date_recorded DESC")
                    .setInt(1, id)
                    .setInt(2, id);
            CustomResultSet crs = cs.execQuery();
            while (crs.next()) {
                result.add(new Transaction(crs.getTimestamp(1), crs.getInt(2), crs.getInt(3), crs.getLong(4)));
            }
            consumer.accept(result);
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
