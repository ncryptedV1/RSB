/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.events;

import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.utils.InventoryFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author ncrypted
 */
public class PlayerListener implements Listener {

    private RSB rsb;

    public PlayerListener(RSB rsb) {
        this.rsb = rsb;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        rsb.getMySqlInterface().setupPlayer(player);
        rsb.getCacheController().loadCache(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        rsb.getCacheController().clearCache(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.GOLD_BLOCK) {
            return;
        }
        Inventory toOpen = InventoryFactory.getGoldBlock(player);
        if (toOpen != null) {
            player.openInventory(toOpen);
        }
    }
}
