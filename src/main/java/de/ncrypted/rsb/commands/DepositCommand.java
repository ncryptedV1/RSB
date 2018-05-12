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
public class DepositCommand extends AbstractCommand {

    @Override
    public void onPlayer(Player player, String[] args) {
        if (!hasPerm("rsb.deposit")) {
            sendNoPerms();
            return;
        }
        if (args.length != 2) {
            return;
        }
        int id = Utils.parseId(args[0]);
        if (id == -1) {
            sendWarn("§cDie eingegebene KontoID §o" + args[0] + "§c ist ungültig");
            return;
        }
        long money = Utils.parseMoney(args[1]);
        if (money == -1) {
            sendWarn("§cDer eingegebene Betrag §o" + args[1] + "§c ist ungültig");
            return;
        }
        try {
            if (!getApi().isAccountHolder(player, id)) {
                sendWarn("§cDu bist nicht der Besitzer des Kontos §o" + Utils.toUserId(id));
                return;
            }
            long cash = getApi().getCash(player);
            if (money > cash) {
                sendWarn("§cDu besitzt nur §o" + cash + "$§c Bargeld");
                return;
            }
            getApi().removeCash(player, money);
            getApi().addBalance(player, id, money);
            sendMsg("§6Dir wurden §e" + money + "$§6 vom Bargeld abgezogen");
            sendMsg("§6Es wurden §e" + money + "$§6 auf das Konto §e" + Utils.toUserId(id) + "§6 eingezahlt");
        } catch (PlayerNotCachedException e) {
            sendWarn("§cDeine Daten werden noch geladen...\nBitte warte einen Moment");
        }
    }
}
