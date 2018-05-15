/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.utils;

import com.google.common.collect.Lists;
import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.RSBApi;
import de.ncrypted.rsb.exceptions.PlayerNotCachedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * @author ncrypted
 */
public class InventoryFactory {

    private static RSBApi api = RSB.getApi();

    public static Inventory getGoldBlock(Player player) {
        try {
            List<Integer> accounts = Lists.newArrayList(RSB.getApi().getAccounts(player));
            Inventory inv = Bukkit.createInventory(null, (accounts.size() / 9 + 1) * 9, "§9Kontenübersicht");
            for (int i = 0; i < accounts.size(); i++) {
                int id = accounts.get(i);
                inv.setItem(i, ISM.getItem(Material.GOLD_INGOT, "§6" + Utils.toUserId(id),
                        "§5Kontostand: §b" + api.getBalance(player, id) + "$",
                        "§5Transaktionen: §b" + api.getTransfers(player, id).size()));
            }
            return inv;
        } catch (PlayerNotCachedException e) {
        }
        return null;
    }
}
