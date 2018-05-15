/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.config;

import de.ncrypted.rsb.RSB;
import de.ncrypted.rsb.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author ncrypted
 */
public class ConfigController {

    private File file;
    private FileConfiguration cfg;
    private RSB rsb;

    public ConfigController(RSB rsb) {
        this.rsb = rsb;
        file = new File("plugins/" + rsb.getDescription().getName(), "config.yml");
        cfg = Utf8YamlConfiguration.loadConfiguration(file);
        load();
    }

    public void load() {
        rsb.saveDefaultConfig();
        try {
            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Logger.err("Couldn't load config file!");
            Logger.err("Cancelling startup...");
            rsb.stopStartup();
        }
    }

    public Object get(String path) {
        return cfg.get(path);
    }

    public void set(String path, Object obj) {
        cfg.set(path, obj);

        save();
    }

    public Set<String> getKeys(boolean subKeys) {
        return cfg.getKeys(subKeys);
    }

    public void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            Logger.err("Couldn't save config file!");
        }
    }

    // String
    public String getString(String path) {
        Object def = rsb.getConfig().get(path);
        return getString(path, def == null ? "" : (String) def);
    }

    public String getString(String path, String standard) {
        Object obj = get(path);

        return (obj instanceof String) ? (String) obj : standard;
    }

    // Integer
    public int getInt(String path) {
        Object def = rsb.getConfig().get(path);
        return getInt(path, def == null ? 0 : (int) def);
    }

    public int getInt(String path, int standard) {
        Object obj = get(path);

        return (obj instanceof Integer) ? (int) obj : standard;
    }

    // Double
    public double getDouble(String path) {
        Object def = rsb.getConfig().get(path);
        return getDouble(path, def == null ? 0D : (double) def);
    }

    public double getDouble(String path, double standard) {
        Object obj = get(path);

        return (obj instanceof Double) ? (double) obj : standard;
    }

    // Float
    public float getFloat(String path) {
        Object def = rsb.getConfig().get(path);
        return getFloat(path, def == null ? 0F : (float) def);
    }

    public float getFloat(String path, float standard) {
        Object obj = get(path);

        return (obj instanceof Float) ? (float) obj : standard;
    }

    // Bukkit
    public void setLocation(String path, Location loc) {
        set(path + ".world", loc.getWorld().getName());
        set(path + ".x", loc.getX());
        set(path + ".y", loc.getY());
        set(path + ".z", loc.getZ());
        set(path + ".yaw", loc.getYaw());
        set(path + ".pitch", loc.getPitch());
    }

    public Location getLocation(String path, boolean yawPitch) {
        String world = getString(path + ".world");
        double x = getDouble(path + ".x");
        double y = getDouble(path + ".y");
        double z = getDouble(path + ".z");
        float yaw = 0F;
        float pitch = 0F;
        if (yawPitch) {
            yaw = getFloat(path + ".yaw");
            pitch = getFloat(path + ".pitch");
        }
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}
