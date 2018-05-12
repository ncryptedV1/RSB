/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb;

import de.ncrypted.rsb.database.CacheController;
import de.ncrypted.rsb.database.MySqlInterface;
import de.ncrypted.rsb.utils.PlayerNotCachedException;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author ncrypted
 */
public class RSBApi {

    private RSB rsb;
    private MySqlInterface mySqlInterface;
    private CacheController cacheController;

    public RSBApi(RSB rsb) {
        this.rsb = rsb;
        mySqlInterface = rsb.getMySqlInterface();
        cacheController = rsb.getCacheController();
    }

    /**
     * Only works when player is online
     *
     * @param player player to get cash
     * @return cached cash value
     */
    public long getCash(Player player) throws PlayerNotCachedException {
        if (!isPlayerLoaded(player)) {
            throw new PlayerNotCachedException();
        }
        return cacheController.getCash().get(player.getUniqueId());
    }

    /**
     * Async load from database
     *
     * @param uuid     uuid to get cash
     * @param consumer cash value in database
     */
    public void getCash(UUID uuid, Consumer<Long> consumer) {
        mySqlInterface.getCash(uuid, consumer);
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
            cacheController.getCash().put(uuid, money);
        }
        mySqlInterface.setCash(uuid, money);
    }

    /**
     * Only works when player is online
     *
     * @param player player to add cash to
     * @param money  cash value to add
     */
    public void addCash(Player player, long money) {
        try {
            setCash(player.getUniqueId(), getCash(player) + money);
        } catch (PlayerNotCachedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Only works when player is online
     *
     * @param player player to remove cash from
     * @param money  cash value to remove
     */
    public void removeCash(Player player, long money) {
        try {
            setCash(player.getUniqueId(), getCash(player) - money);
        } catch (PlayerNotCachedException e) {
            e.printStackTrace();
        }
    }

    private boolean isPlayerLoaded(Player player) {
        return isPlayerLoaded(player.getUniqueId());
    }

    private boolean isPlayerLoaded(UUID uuid) {
        if (cacheController.getCash().containsKey(uuid)) {
            return true;
        }
        return false;
    }
}
