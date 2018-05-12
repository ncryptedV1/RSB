/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.config;

import de.ncrypted.rsb.RSB;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ncrypted
 */
public class ConfigHandler {

    // declaration -> value (message)
    private Map<String, String> messages = new HashMap<>();
    private Map<String, Integer> numbers = new HashMap<>();
    private Map<String, Double> floatings = new HashMap<>();
    private RSB rsb;

    public ConfigHandler(RSB rsb) {
        this.rsb = rsb;
        load();
    }

    public void load() {
        // Prefixes
        loadMessage("prefix");
        loadMessage("warning-prefix");

        // MySql
        loadMessage("mysql-host");
        loadNumber("mysql-port");
        loadMessage("mysql-user");
        loadMessage("mysql-password");
        loadMessage("mysql-database");

        // Bonus and punish
        loadNumber("feed-animal");
        loadNumber("kill-monster");
        loadNumber("kill-animal");
        loadNumber("eat-flesh");
        loadFloating("interest");
    }

    private void loadMessage(String path) {
        String msg = rsb.getConfigController().getString(path);

        String conv = ChatColor.translateAlternateColorCodes('&', msg);
        if (getPrefix() != null)
            conv = conv.replace("[PREFIX]", getPrefix());
        if (getWarning() != null)
            conv = conv.replace("[WARNING]", getWarning());

        messages.put(path, conv);
    }

    private void loadNumber(String path) {
        numbers.put(path, rsb.getConfigController().getInt(path));
    }

    private void loadFloating(String path) {
        floatings.put(path, rsb.getConfigController().getDouble(path));
    }

    public String getMessage(String name) {
        return messages.get(name);
    }

    public Integer getNumber(String name) {
        return numbers.get(name);
    }

    public Double getFloating(String name) {
        return floatings.get(name);
    }

    public String getPrefix() {
        return getMessage("prefix");
    }

    public String getWarning() {
        return getMessage("warning-prefix");
    }
}
