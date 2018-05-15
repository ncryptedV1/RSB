/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb;

import de.ncrypted.rsb.database.CacheController;
import de.ncrypted.rsb.database.MySqlInterface;
import de.ncrypted.rsb.exceptions.PlayerNotCachedException;
import de.ncrypted.rsb.utils.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author ncrypted
 */
public class RSBApi {

    private RSB rsb;
    private MySqlInterface mysql;
    private CacheController cache;

    public RSBApi(RSB rsb) {
        this.rsb = rsb;
        mysql = rsb.getMySqlInterface();
        cache = rsb.getCacheController();
    }

    // CASH

    /**
     * Only works for online players
     *
     * @param player player to get cash
     * @return cached cash value
     */
    public long getCash(Player player) throws PlayerNotCachedException {
        if (!isPlayerLoaded(player)) {
            throw new PlayerNotCachedException();
        }
        return cache.getCash().get(player.getUniqueId());
    }

    /**
     * Async load from database
     *
     * @param uuid     uuid to get cash
     * @param consumer cash value in database
     */
    public void getCash(UUID uuid, Consumer<Long> consumer) {
        mysql.getCash(uuid, consumer);
    }

    /**
     * Updates the cash value of a player in database
     * Also updates the cached value, when player is online
     *
     * @param uuid  uuid to assign money at
     * @param money cash value to assign
     */
    public void setCash(UUID uuid, long money) {
        if (money < 0) {
            money = 0;
        }
        if (isPlayerLoaded(uuid)) {
            cache.getCash().put(uuid, money);
            updateSideboard(Bukkit.getPlayer(uuid));
        }
        mysql.setCash(uuid, money);
    }

    /**
     * Internally uses setCash
     *
     * @param player player to add cash to
     * @param money  cash value to add
     */
    public void addCash(Player player, long money) throws PlayerNotCachedException {
        setCash(player.getUniqueId(), getCash(player) + money);
    }

    /**
     * Internally uses setCash
     *
     * @param player player to remove cash from
     * @param money  cash value to remove
     */
    public void removeCash(Player player, long money) throws PlayerNotCachedException {
        setCash(player.getUniqueId(), getCash(player) - money);
    }

    // ACCOUNTS

    /**
     * Creates account in database and cache, when player is online
     *
     * @param player   player to create account for
     * @param consumer created id of the account
     */
    public void createAccount(Player player, Consumer<Integer> consumer) {
        UUID uuid = player.getUniqueId();
        mysql.createAccount(uuid, id -> {
            if (isPlayerLoaded(player)) {
                cache.getAccounts().get(uuid).add(id);
                cache.getAccountToUuid().put(id, uuid);
                cache.getBalances().put(id, 0L);
                cache.getTransfers().put(id, new ArrayList<>());
                updateSideboard(player);
            }
            consumer.accept(id);
        });
    }

    /**
     * Deletes account in database and cache, when holder is online
     *
     * @param id account id
     */
    public void deleteAccount(int id) {
        if (cache.getAccountToUuid().containsKey(id)) {
            updateSideboard(Bukkit.getPlayer(cache.getAccountToUuid().get(id)));
            cache.getAccounts().get(cache.getAccountToUuid().get(id)).remove(id);
            cache.getAccountToUuid().remove(id);
            cache.getBalances().remove(id);
            cache.getTransfers().remove(id);
        }
        mysql.deleteAccount(id);
    }

    /**
     * Only works for online player
     *
     * @param player account holder
     * @return accounts
     */
    public Set<Integer> getAccounts(Player player) throws PlayerNotCachedException {
        if (!isPlayerLoaded(player)) {
            throw new PlayerNotCachedException();
        }
        return cache.getAccounts().get(player.getUniqueId());
    }

    /**
     * Retrieves accounts from database
     *
     * @param uuid     account holder
     * @param consumer accounts
     */
    public void getAccounts(UUID uuid, Consumer<Set<Integer>> consumer) {
        mysql.getAccounts(uuid, consumer);
    }

    /**
     * Only works for online player
     *
     * @param player holder of the account
     * @param id     account id
     * @return whether is player the holder of the account
     */
    public boolean isAccountHolder(Player player, int id) throws PlayerNotCachedException {
        if (!isPlayerLoaded(player)) {
            throw new PlayerNotCachedException();
        }
        return cache.getAccountToUuid().get(id) == player.getUniqueId();
    }

    /**
     * Only works for online player
     *
     * @param player account holder
     * @param id     account
     * @return balance
     */
    public long getBalance(Player player, int id) throws PlayerNotCachedException {
        if (!isPlayerLoaded(player)) {
            throw new PlayerNotCachedException();
        }
        return cache.getBalances().get(id);
    }

    /**
     * Retrieves balance from database
     *
     * @param id       account
     * @param consumer balance
     */
    public void getBalance(int id, Consumer<Long> consumer) {
        mysql.getBalance(id, consumer);
    }

    /**
     * Changes balance in database and cache, when holder is online
     *
     * @param id      account
     * @param balance balance
     */
    public void setBalance(int id, long balance) {
        if (balance < 0) {
            balance = 0;
        }
        if (cache.getBalances().get(id) != null) {
            cache.getBalances().put(id, balance);
            updateSideboard(Bukkit.getPlayer(cache.getAccountToUuid().get(id)));
        }
        mysql.setBalance(id, balance);
    }

    /**
     * Internally uses setBalance
     *
     * @param player holder
     * @param id     account
     * @param money  amount
     */
    public void addBalance(Player player, int id, long money) throws PlayerNotCachedException {
        setBalance(id, getBalance(player, id) + money);
    }

    /**
     * Internally uses setBalance
     *
     * @param player holder
     * @param id     account
     * @param money  amount
     */
    public void removeBalance(Player player, int id, long money) throws PlayerNotCachedException {
        setBalance(id, getBalance(player, id) - money);
    }

    /**
     * Retrieves account holder from darabase
     * Contains beforehand cache check
     *
     * @param id       account
     * @param consumer holder
     */
    public void getHolder(int id, Consumer<UUID> consumer) {
        if (cache.getAccountToUuid().containsKey(id)) {
            consumer.accept(cache.getAccountToUuid().get(id));
            return;
        }
        mysql.getHolder(id, consumer);
    }

    // TRANSFERS

    /**
     * Adds transfer to database and cache if sender or target holder is online
     * sender value 0 means deposit
     * target value 0 means withdraw
     * sender value -1 means interest
     *
     * @param transaction contains timestamp, sender, target and money
     */
    public void addTransfer(Transaction transaction) {
        int sender = transaction.getSender();
        int target = transaction.getTarget();
        if (cache.getTransfers().containsKey(sender)) {
            List<Transaction> transfers = cache.getTransfers().get(sender);
            transfers.add(0, transaction);
        }
        if (cache.getTransfers().containsKey(target)) {
            List<Transaction> transfers = cache.getTransfers().get(target);
            transfers.add(0, transaction);
        }
        mysql.addTransfer(transaction);
    }

    /**
     * Returns latest 10 transactions to or from an account
     * Only works when account holder is online
     *
     * @param player account holder
     * @param id     account
     * @return transfers
     * @throws PlayerNotCachedException
     */
    public List<Transaction> getTransfers(Player player, int id) throws PlayerNotCachedException {
        if (!isPlayerLoaded(player)) {
            throw new PlayerNotCachedException();
        }
        return cache.getTransfers().get(id);
    }

    /* ADAPTATIONS
       Convenient for transfer logging
     */

    /**
     * Requires balance of target account to act sync
     * Source account holder must be online
     *
     * @param sourcePlayer  online player
     * @param source        source account
     * @param target        target account
     * @param targetBalance target account balance
     * @param money         money to transfer
     * @throws PlayerNotCachedException
     */
    public void transfer(Player sourcePlayer, int source, int target, long targetBalance, long money) throws PlayerNotCachedException {
        removeBalance(sourcePlayer, source, money);
        setBalance(target, targetBalance + money);
        addTransfer(new Transaction(source, target, money));
        updateSideboard(sourcePlayer);
        if (cache.getAccountToUuid().containsKey(target)) {
            updateSideboard(Bukkit.getPlayer(cache.getAccountToUuid().get(target)));
        }
    }

    /**
     * Puts cash into the account
     * Only works for online player
     *
     * @param player account holder
     * @param id     account
     * @param money  cash
     * @throws PlayerNotCachedException
     */
    public void deposit(Player player, int id, long money) throws PlayerNotCachedException {
        removeCash(player, money);
        addBalance(player, id, money);
        addTransfer(new Transaction(0, id, money));
        updateSideboard(player);
    }

    /**
     * Puts money from an account into cash
     *
     * @param player account holder
     * @param id     account
     * @param money  balance to take
     * @throws PlayerNotCachedException
     */
    public void withdraw(Player player, int id, long money) throws PlayerNotCachedException {
        removeBalance(player, id, money);
        addCash(player, money);
        addTransfer(new Transaction(id, 0, money));
        updateSideboard(player);
    }

    // UTILS
    public boolean isPlayerLoaded(Player player) {
        return isPlayerLoaded(player.getUniqueId());
    }

    /**
     * Checks if a player is already cached
     *
     * @param uuid player to check
     * @return is player cached
     */
    public boolean isPlayerLoaded(UUID uuid) {
        if (cache.getCash().containsKey(uuid) && cache.getAccounts().containsKey(uuid)) {
            Set<Integer> accounts = cache.getAccounts().get(uuid);
            for (int id : accounts) {
                if (!cache.getAccountToUuid().containsKey(id) || !cache.getTransfers().containsKey(id) ||
                        !cache.getBalances().containsKey(id)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void updateSideboard(Player player) {
        rsb.getSideboardController().update(player);
    }
}
