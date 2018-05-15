/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.database;

import com.google.common.collect.Maps;
import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.utils.Transaction;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author ncrypted
 */
public class CacheController {

    // Concurrent Maps to synchronize the cache handling and api changes / reads
    private Map<UUID, Long> cash = Maps.newConcurrentMap();
    private Map<UUID, Set<Integer>> accounts = Maps.newConcurrentMap();
    private Map<Integer, UUID> accountToUuid = Maps.newConcurrentMap();
    private Map<Integer, Long> balances = Maps.newConcurrentMap();
    private Map<Integer, List<Transaction>> transfers = Maps.newConcurrentMap();
    private RSB rsb;
    private MySqlInterface mysql;

    public CacheController(RSB rsb) {
        this.rsb = rsb;
        mysql = rsb.getMySqlInterface();
    }

    public void loadCache(Player player, Runnable onFinish) {
        CachingWaiter waiter = new CachingWaiter(onFinish);
        UUID uuid = player.getUniqueId();
        if (cash.containsKey(uuid)) {
            return;
        }
        mysql.getCash(uuid, money -> {
            cash.put(uuid, money);
        });

        mysql.getAccounts(uuid, result -> {
            accounts.put(uuid, result);
            for (Integer id : result) {
                accountToUuid.put(id, uuid);
                mysql.getBalance(id, balance -> {
                    balances.put(id, balance);
                    waiter.gotBalances();
                });
                mysql.getTransfers(id, transfer -> {
                    transfers.put(id, transfer);
                    waiter.gotTransfers();
                });
            }
        });
    }

    public void clearCache(Player player) {
        UUID uuid = player.getUniqueId();
        if (!cash.containsKey(uuid)) {
            return;
        }
        for (Integer id : accounts.get(uuid)) {
            transfers.remove(id);
            balances.remove(id);
            accountToUuid.remove(id);
        }
        accounts.remove(uuid);
        cash.remove(uuid);
    }

    public Map<UUID, Long> getCash() {
        return cash;
    }

    public Map<UUID, Set<Integer>> getAccounts() {
        return accounts;
    }

    public Map<Integer, UUID> getAccountToUuid() {
        return accountToUuid;
    }

    public Map<Integer, Long> getBalances() {
        return balances;
    }

    public Map<Integer, List<Transaction>> getTransfers() {
        return transfers;
    }

    private class CachingWaiter {
        private boolean balances = false;
        private boolean transfers = false;
        private Runnable runnable;

        public CachingWaiter(Runnable runnable) {
            this.runnable = runnable;
        }

        public void gotBalances() {
            balances = true;
            check();
        }

        public void gotTransfers() {
            transfers = true;
            check();
        }

        private void check() {
            if (balances && transfers) {
                runnable.run();
            }
        }
    }
}
