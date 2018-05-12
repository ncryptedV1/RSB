/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.commands;

import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.RSBApi;
import de.ncrypted.rsb.utils.ISM;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

/**
 * Created at 27.05.2017
 * by ncrypted
 */
public class AbstractCommand implements CommandExecutor {

    private CommandSender sender;
    private boolean isPlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;
        if (sender instanceof Player) {
            isPlayer = true;
            onPlayer((Player) sender, args);
        } else {
            isPlayer = false;
            onConsole(sender, args);
        }
        return true;
    }

    public void onPlayer(Player player, String[] args) {
    }

    public void onConsole(CommandSender sender, String[] args) {
        sendWarn("§cDieser Befehl ist nur für Spieler vorgesehen");
    }

    public RSBApi getApi() {
        return RSB.getApi();
    }

    public void sendNoPerms() {
        sendWarn("§cDu hast keine Berechtigung zu diesem Befehl");
    }

    public void sendMsg(String msg) {
        sender.sendMessage(RSB.getPrefix() + msg);
    }

    public void sendMsg(Player player, String msg) {
        player.sendMessage(RSB.getPrefix() + msg);
    }

    public void sendWarn(String msg) {
        sender.sendMessage(RSB.getWarning() + msg);
    }

    public void sendWarn(Player player, String msg) {
        player.sendMessage(RSB.getWarning() + msg);
    }

    public void setFood(int food) {
        if (!isPlayer) {
            return;
        }
        getPlayer().setFoodLevel(food);
    }

    public void setFlying(boolean fly) {
        if (!isPlayer) {
            return;
        }
        getPlayer().setFlying(fly);
    }

    public void teleport(Location loc) {
        if (!isPlayer) {
            return;
        }
        getPlayer().teleport(loc);
    }

    public void openInv(Inventory inv) {
        if (!isPlayer) {
            return;
        }
        getPlayer().openInventory(inv);
    }

    public void clearInv() {
        if (!isPlayer) {
            return;
        }
        PlayerInventory inv = getInv();
        inv.clear();
        ItemStack air = ISM.getItem(Material.AIR);
        inv.setHelmet(air);
        inv.setChestplate(air);
        inv.setLeggings(air);
        inv.setBoots(air);
    }

    public void setItem(int slot, ItemStack item) {
        if (!isPlayer) {
            return;
        }
        getInv().setItem(slot, item);
    }

    public String getName() {
        return sender.getName();
    }

    public UUID getUUID() {
        if (!isPlayer) {
            return null;
        }
        return getPlayer().getUniqueId();
    }

    public int getEID() {
        if (!isPlayer) {
            return 0;
        }
        return getPlayer().getEntityId();
    }

    public boolean getAllowFlight() {
        if (!isPlayer) {
            return false;
        }
        return getPlayer().getAllowFlight();
    }

    public void setAllowFlight(boolean allow) {
        if (!isPlayer) {
            return;
        }
        getPlayer().setAllowFlight(allow);
    }

    public int getFoodLevel() {
        if (!isPlayer) {
            return 0;
        }
        return getPlayer().getFoodLevel();
    }

    public double getHealth() {
        if (!isPlayer) {
            return 0D;
        }
        return getPlayer().getHealth();
    }

    public void setHealth(double health) {
        if (!isPlayer) {
            return;
        }
        getPlayer().setHealth(health);
    }

    public int getLevel() {
        if (!isPlayer) {
            return 0;
        }
        return getPlayer().getLevel();
    }

    public void setLevel(int level) {
        if (!isPlayer) {
            return;
        }
        getPlayer().setLevel(level);
    }

    public PlayerInventory getInv() {
        if (!isPlayer) {
            return null;
        }
        return getPlayer().getInventory();
    }

    public GameMode getGamemode() {
        if (!isPlayer) {
            return null;
        }
        return getPlayer().getGameMode();
    }

    public void setGamemode(GameMode mode) {
        if (!isPlayer) {
            return;
        }
        getPlayer().setGameMode(mode);
    }

    public Location getLocation() {
        if (!isPlayer) {
            return null;
        }
        return getPlayer().getLocation();
    }

    public World getWorld() {
        if (!isPlayer) {
            return null;
        }
        return getPlayer().getWorld();
    }

    public boolean hasPerm(String perm) {
        return sender.hasPermission(perm);
    }

    public Player getPlayer() {
        if (!isPlayer) {
            return null;
        }
        return ((Player) sender);
    }
}
