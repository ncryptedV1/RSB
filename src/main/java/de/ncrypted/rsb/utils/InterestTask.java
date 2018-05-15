/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.utils;

import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.database.CacheController;
import de.ncrypted.rsb.exceptions.PlayerNotCachedException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * @author ncrypted
 */
public class InterestTask extends BukkitRunnable {

    private RSB rsb;
    private CacheController cache;

    public InterestTask(RSB rsb) {
        this.rsb = rsb;
        cache = rsb.getCacheController();
    }

    @Override
    public void run() {
        for (Map.Entry<Integer, Long> entry : cache.getBalances().entrySet()) {
            int id = entry.getKey();
            Player holder = Bukkit.getPlayer(cache.getAccountToUuid().get(id));
            try {
                int interest = (int) Math.floor(
                        entry.getValue() * rsb.getConfigHandler().getFloating("interest") / 100);
                RSB.getApi().addBalance(holder, id, interest);
                RSB.getApi().addTransfer(new Transaction(-1, id, interest));
            } catch (PlayerNotCachedException e) {
            }
        }
    }
}
