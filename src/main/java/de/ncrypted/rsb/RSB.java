/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb;

import de.ncrypted.rsb.commands.*;
import de.ncrypted.rsb.config.ConfigController;
import de.ncrypted.rsb.config.ConfigHandler;
import de.ncrypted.rsb.database.CacheController;
import de.ncrypted.rsb.database.MySqlController;
import de.ncrypted.rsb.database.MySqlInterface;
import de.ncrypted.rsb.events.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author ncrypted
 */
public class RSB extends JavaPlugin {

    private static ConfigController configController;
    private static ConfigHandler configHandler;
    private static MySqlController mySqlController;
    private static MySqlInterface mySqlInterface;
    private static CacheController cacheController;
    private static RSBApi api;

    private static boolean stopStartup = false;

    public static RSBApi getApi() {
        return api;
    }

    public static String getPrefix() {
        return configHandler.getPrefix();
    }

    public static String getWarning() {
        return configHandler.getWarning();
    }

    public void stopStartup() {
        stopStartup = true;
    }

    @Override
    public void onEnable() {
        configController = new ConfigController(this);
        if (stopStartup) {
            return;
        }
        configHandler = new ConfigHandler(this);
        if (stopStartup) {
            return;
        }
        mySqlController = new MySqlController(this);
        if (stopStartup) {
            return;
        }
        mySqlInterface = new MySqlInterface(this);
        cacheController = new CacheController(this);
        api = new RSBApi(this);

        getCommand("cash").setExecutor(new CashCommand());
        getCommand("account").setExecutor(new AccountCommand());
        getCommand("accounts").setExecutor(new AccountsCommand());
        getCommand("deposit").setExecutor(new DepositCommand());
        getCommand("withdraw").setExecutor(new WithdrawCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("transfer").setExecutor(new TransferCommand());
        getCommand("transfers").setExecutor(new TransfersCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        getMySqlController().disconnect();
    }

    public ConfigController getConfigController() {
        return configController;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public MySqlController getMySqlController() {
        return mySqlController;
    }

    public MySqlInterface getMySqlInterface() {
        return mySqlInterface;
    }

    public CacheController getCacheController() {
        return cacheController;
    }
}
