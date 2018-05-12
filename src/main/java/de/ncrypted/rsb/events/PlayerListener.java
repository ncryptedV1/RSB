/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.events;

import com.google.common.collect.HashMultimap;
import de.ncrypted.rsb.RSB;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author ncrypted
 */
public class PlayerListener implements Listener {

    private static HashMultimap<Object, Object> animalFood = HashMultimap.create();

    static {
        animalFood.put(EntityType.COW, Material.WHEAT);
        animalFood.put(EntityType.SHEEP, Material.WHEAT);
        animalFood.put(EntityType.CHICKEN, Material.SEEDS);
        animalFood.put(EntityType.PIG, Material.CARROT);
        animalFood.put(EntityType.HORSE, Material.WHEAT);
        animalFood.put(EntityType.HORSE, Material.SUGAR);
        animalFood.put(EntityType.HORSE, Material.HAY_BLOCK);
        animalFood.put(EntityType.HORSE, Material.APPLE);
        animalFood.put(EntityType.HORSE, Material.GOLDEN_CARROT);
        animalFood.put(EntityType.HORSE, Material.GOLDEN_APPLE);
        animalFood.put(EntityType.WOLF, Material.BONE);
        animalFood.put(EntityType.OCELOT, Material.RAW_FISH);
    }

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
    public void onKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Animals) {
            Animals animal = (Animals) entity;
            Player killer = animal.getKiller();
            if (killer == null) {
                return;
            }
            int punish = rsb.getConfigHandler().getNumber("kill-animal");
            RSB.getApi().removeCash(killer, punish);
            killer.sendMessage(RSB.getWarning() + "§cDu hast ein Tier getötet");
            killer.sendMessage(RSB.getWarning() + "§cAls Strafe wurden dir §o" + punish + "$ §cabgezogen");
        } else if (entity instanceof Monster) {
            Monster monster = (Monster) entity;
            Player killer = monster.getKiller();
            if (killer == null) {
                return;
            }
            int bonus = rsb.getConfigHandler().getNumber("kill-monster");
            RSB.getApi().addCash(killer, bonus);
            killer.sendMessage(RSB.getPrefix() + "§6Du hast ein Monster getötet");
            killer.sendMessage(RSB.getPrefix() + "§6Als Bonus wurden dir §o" + bonus + "$ §6geschenkt");
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Material mat = event.getItem().getType();
        if (isFlesh(mat)) {
            int punish = rsb.getConfigHandler().getNumber("eat-flesh");
            RSB.getApi().removeCash(player, punish);
            player.sendMessage(RSB.getWarning() + "§cDu hast Fliesch gegessen");
            player.sendMessage(RSB.getWarning() + "§cAls Strafe wurden dir §o" + punish + "$ §cabgezogen");
        }
    }

    @EventHandler
    public void onFeed(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        int countBefore = player.getItemInHand().getAmount();
        EntityType entityType = event.getRightClicked().getType();
        if (player.getItemInHand() == null) {
            return;
        }
        Material mat = player.getItemInHand().getType();

        if (isFeedable(entityType, mat)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(rsb, () -> {
                boolean fed = false;
                if (player.getItemInHand() == null && countBefore == 1) {
                    fed = true;
                }
                int count = player.getItemInHand().getAmount();
                if (countBefore > 1 && count == countBefore - 1) {
                    fed = true;
                }
                if (fed) {
                    int bonus = rsb.getConfigHandler().getNumber("feed-animal");
                    RSB.getApi().addCash(player, bonus);
                    player.sendMessage(RSB.getPrefix() + "§6Du hast ein Tier gefüttert");
                    player.sendMessage(RSB.getPrefix() + "§6Als Bonus wurden dir §o" + bonus + "$ §6geschenkt");
                }
            }, 1);
        }
    }

    private boolean isFlesh(Material mat) {
        if (mat == Material.ROTTEN_FLESH || mat == Material.COOKED_BEEF || mat == Material.RAW_BEEF ||
                mat == Material.PORK || mat == Material.GRILLED_PORK || mat == Material.MUTTON ||
                mat == Material.COOKED_MUTTON) {
            return true;
        }
        return false;
    }

    private boolean isFeedable(EntityType entityType, Material mat) {
        for (Object food : animalFood.get(entityType)) {
            if (mat == food) {
                return true;
            }
        }
        return false;
    }
}
