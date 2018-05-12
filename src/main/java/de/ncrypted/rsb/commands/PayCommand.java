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
public class PayCommand extends AbstractCommand {

    @Override
    public void onPlayer(Player player, String[] args) {
        if (!hasPerm("rsb.pay")) {
            sendNoPerms();
            return;
        }
        if (args.length != 2) {
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sendWarn("§cDer Spieler §o" + args[0] + "§c ist nicht online");
            return;
        }
        long money = Utils.parseMoney(args[1]);
        if (money == -1) {
            sendWarn("§cDer eingegebene Betrag §o" + args[1] + "§c ist ungültig");
            return;
        }
        try {
            long cash = getApi().getCash(player);
            if (money > cash) {
                sendWarn("§cDu besitzt nur §o" + cash + "$§c Bargeld");
                return;
            }
            getApi().removeCash(player, money);
            getApi().addCash(target, money);
            sendMsg("§6Dir wurden §e" + money + "$§6 abgezogen");
            sendMsg("§6Dem Spieler §e" + target.getName() + "§6 wurden §e" + money + "$§6 geschickt");
            sendMsg(target, "§6Der Spieler §e" + player.getName() + "§6 hat dir §e" + money + "$§6 geschickt");
        } catch (PlayerNotCachedException e) {
            sendWarn("§cDeine Daten werden noch geladen...\nBitte warte einen Moment");
        }
    }
}
