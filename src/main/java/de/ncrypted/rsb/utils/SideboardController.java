/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.utils;

import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.exceptions.PlayerNotCachedException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ncrypted
 */
public class SideboardController {

    private static String title;
    private static Map<String, Integer> scores = new HashMap<>();

    static {
        title = "§8» §bRewinside§8.§btv";

        scores.put("§2", 9);
        scores.put("§7Bargeld§8:", 8);
        scores.put("§8➟ §e%cash%", 7);
        scores.put("§3", 6);
        scores.put("§7Konten§8:", 5);
        scores.put("§8➟ §a%accounts%", 4);
        scores.put("§4", 3);
        scores.put("§7Gesamtkontostand§8:", 2);
        scores.put("§8➟ §b%balance_all%", 1);
    }

    private RSB rsb;

    public SideboardController(RSB rsb) {
        this.rsb = rsb;
    }

    public void update(Player player) {
        Bukkit.getScheduler().runTask(rsb, () -> {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            Scoreboard board = player.getScoreboard();
            Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
            if (objective != null) {
                objective.unregister();
            }
            objective = board.registerNewObjective("rsb", "dummy");
            objective.setDisplayName(title);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            for (Map.Entry<String, Integer> score : scores.entrySet()) {
                objective.getScore(handleKey(player, handleKey(player, score.getKey()))).setScore(score.getValue());
            }

            player.setScoreboard(board);
        });
    }

    private String handleKey(Player player, String key) {
        try {
            int balance = 0;
            for (int id : RSB.getApi().getAccounts(player)) {
                balance += RSB.getApi().getBalance(player, id);
            }
            return key.replaceFirst("%cash%", String.valueOf(RSB.getApi().getCash(player)))
                    .replaceFirst("%accounts%", String.valueOf(RSB.getApi().getAccounts(player).size()))
                    .replaceFirst("%balance_all%", String.valueOf(balance));
        } catch (PlayerNotCachedException e) {
        }
        return "";
    }
}
