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
public class AccountCommand extends AbstractCommand {

    @Override
    public void onPlayer(Player player, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!hasPerm("rsb.account.create")) {
                    sendNoPerms();
                    return;
                }
                getApi().createAccount(player, id -> {
                    sendMsg("§6Ein Konto mit der ID §e" + Utils.toUserId(id) + "§6 wurde erstellt");
                });
            } else {
                if (!hasPerm("rsb.account.balance")) {
                    sendNoPerms();
                    return;
                }
                int id = Utils.parseId(args[0]);
                if (id == -1) {
                    sendWarn("§cDie eingegebene KontoID §o" + args[0] + "§c ist ungültig");
                    return;
                }
                try {
                    if (!getApi().isAccountHolder(player, id)) {
                        sendWarn("§cDu bist nicht der Besitzer des Kontos §o" + Utils.toUserId(id));
                    }
                    sendMsg("§6Konto: §e" + Utils.toUserId(id));
                    sendMsg("§6Kontostand: §e" + getApi().getBalance(player, id));
                } catch (PlayerNotCachedException e) {
                    sendNotLoaded();
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                if (!hasPerm("rsb.account.delete")) {
                    sendNoPerms();
                    return;
                }
                int id = Utils.parseId(args[1]);
                if (id == -1) {
                    sendWarn("§cDie eingegebene KontoID §o" + args[1] + "§c ist ungültig");
                    return;
                }
                try {
                    if (!getApi().isAccountHolder(player, id)) {
                        sendWarn("§cDu bist nicht der Besitzer des Kontos §o" + Utils.toUserId(id));
                        return;
                    }
                    long balance = getApi().getBalance(player, id);
                    getApi().deleteAccount(id);
                    getApi().addCash(player, balance);
                    sendMsg("§6Das Konto mit der ID §e" + id + "§6 wurde gelöscht");
                    sendMsg("§6Dir wurden §e" + balance + "$§6 zum Bargeld hinzugefügt");
                } catch (PlayerNotCachedException e) {
                    sendNotLoaded();
                }
            }
        }
    }
}
