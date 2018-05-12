/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.database;

import com.google.common.collect.Maps;
import de.ncrypted.rsb.RSB;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author ncrypted
 */
public class CacheController {

    private Map<UUID, Long> cash = Maps.newConcurrentMap();
    private RSB rsb;

    public CacheController(RSB rsb) {
        this.rsb = rsb;
    }

    public void loadCache(Player player) {
        UUID uuid = player.getUniqueId();
        rsb.getMySqlInterface().getCash(uuid, money -> {
            cash.put(uuid, money);
        });
    }

    public Map<UUID, Long> getCash() {
        return cash;
    }
}
