/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.commands;

import de.ncrypted.rsb.utils.PlayerNotCachedException;
import de.ncrypted.rsb.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author ncrypted
 */
public class TransferCommand extends AbstractCommand {

    @Override
    public void onPlayer(Player player, String[] args) {
        if (!hasPerm("rsb.transfer")) {
            sendNoPerms();
            return;
        }
        if (args.length != 3) {
            return;
        }
        int source = Utils.parseId(args[0]);
        if (source == -1) {
            sendWarn("§cDie Quell-KontoID §o" + args[0] + "§c ist ungültig");
            return;
        }
        int target = Utils.parseId(args[1]);
        if (target == -1) {
            sendWarn("§cDie Ziel-KontoID §o" + args[1] + "§c ist ungültig");
            return;
        }
        long money = Utils.parseMoney(args[2]);
        if (money == -1) {
            sendWarn("§cDer eingegebene Betrag §o" + args[2] + "§c ist ungültig");
            return;
        }
        try {
            if (!getApi().isAccountHolder(player, source)) {
                sendWarn("§cDu bist nicht der Besitzer des Quell-Kontos §o" + Utils.toUserId(source));
                return;
            }
            long balance = getApi().getBalance(player, source);
            if (money > balance) {
                sendWarn("§cAuf dem Quell-Konto §o" + Utils.toUserId(source) + "§c befinden sich nur §o" + balance +
                        "$");
                return;
            }
            getApi().getHolder(target, holder -> {
                if (holder == null) {
                    sendWarn("§cDas Ziel-Konto §o" + Utils.toUserId(target) + "§c existiert nicht");
                    return;
                }
                getApi().getBalance(target, targetBalance -> {
                    try {
                        getApi().transfer(player, source, target, targetBalance, money);
                        sendMsg("§6Dir wurden §e" + money + "$§6 vom Konto §e" + Utils.toUserId(source) +
                                "§6 abgezogen");
                        sendMsg("§6Du hast §e" + money + "$§6 auf das Konto §e" + Utils.toUserId(target) +
                                "§6 überwiesen");
                        Player targetPlayer = Bukkit.getPlayer(holder);
                        if (targetPlayer == null) {
                            return;
                        }
                        sendMsg(targetPlayer, "§6Der Spieler §e" + player.getName() + "§6 hat dir §e" + money +
                                "$§6 auf das Konto §e" + Utils.toUserId(target) + "§6 überwiesen");
                    } catch (PlayerNotCachedException e) {
                    }
                });
            });
        } catch (PlayerNotCachedException e) {
            sendNotLoaded();
        }
    }
}
