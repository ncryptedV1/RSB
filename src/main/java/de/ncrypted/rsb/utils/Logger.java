/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.utils;

import org.bukkit.Bukkit;

/**
 * @author ncrypted
 */
public class Logger {

    public static void info(String msg) {
        Bukkit.getLogger().info(msg);
    }

    public static void err(String msg) {
        Bukkit.getLogger().severe(msg);
    }

    public static void warn(String msg) {
        Bukkit.getLogger().warning(msg);
    }
}
