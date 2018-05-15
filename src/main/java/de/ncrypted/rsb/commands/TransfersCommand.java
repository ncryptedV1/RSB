/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.commands;

import de.ncrypted.rsb.exceptions.PlayerNotCachedException;
import de.ncrypted.rsb.utils.Transaction;
import de.ncrypted.rsb.utils.Utils;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author ncrypted
 */
public class TransfersCommand extends AbstractCommand {

    @Override
    public void onPlayer(Player player, String[] args) {
        if (!hasPerm("rsb.transfers")) {
            sendNoPerms();
            return;
        }
        if (args.length != 1) {
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
                return;
            }
            List<Transaction> allTransfers = getApi().getTransfers(player, id);
            List<Transaction> transfers = allTransfers.subList(0, allTransfers.size() > 10 ? 10 : allTransfers.size());
            if (transfers.isEmpty()) {
                sendWarn("§cEs existieren bisher noch keine Interaktionen mit dem Konto §4" + Utils.toUserId(id));
                return;
            }
            sendMsg("§6Transaktionen:");
            sendMsg("§8- §eAktion Datum Zeit [Sender] [Empfänger] Betrag");
            for (Transaction transfer : transfers) {
                sendMsg("§8- §e" + transfer.getInfos());
            }
        } catch (PlayerNotCachedException e) {
            sendNotLoaded();
        }
    }
}
