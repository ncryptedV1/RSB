/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.commands;

import de.ncrypted.rsb.utils.PlayerNotCachedException;
import de.ncrypted.rsb.utils.Utils;
import org.bukkit.entity.Player;

/**
 * @author ncrypted
 */
public class AccountsCommand extends AbstractCommand {

    @Override
    public void onPlayer(Player player, String[] args) {
        if (!hasPerm("rsb.accounts")) {
            sendNoPerms();
            return;
        }
        sendMsg("§6Konten:");
        try {
            for (int id : getApi().getAccounts(player)) {
                sendMsg("§8- §e" + Utils.toUserId(id) + " §o" + getApi().getBalance(player, id) + "$");
            }
        } catch (PlayerNotCachedException e) {
            sendNotLoaded();
        }
    }
}
