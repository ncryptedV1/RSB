/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.commands;

import de.ncrypted.rsb.utils.PlayerNotCachedException;
import org.bukkit.entity.Player;

/**
 * @author ncrypted
 */
public class CashCommand extends AbstractCommand {

    @Override
    public void onPlayer(Player player, String[] args) {
        if (!player.hasPermission("rsb.cash")) {
            sendNoPerms();
            return;
        }
        try {
            long money = getApi().getCash(player);
            sendMsg("§6Bargeld: §e" + money + "$");
        } catch (PlayerNotCachedException e) {
            sendNotLoaded();
        }
    }
}
