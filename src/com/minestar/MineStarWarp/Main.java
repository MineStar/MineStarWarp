/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of MineStarWarp.
 * 
 * MineStarWarp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MineStarWarp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MineStarWarp.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.minestar.MineStarWarp;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.minestar.MineStarWarp.commands.CommandList;
import com.minestar.MineStarWarp.dataManager.BackManager;
import com.minestar.MineStarWarp.dataManager.BankManager;
import com.minestar.MineStarWarp.dataManager.HomeManager;
import com.minestar.MineStarWarp.dataManager.SpawnManager;
import com.minestar.MineStarWarp.dataManager.WarpManager;
import com.minestar.MineStarWarp.database.ConnectionManager;
import com.minestar.MineStarWarp.database.DatabaseManager;
import com.minestar.MineStarWarp.listeners.PlayerBedListener;
import com.minestar.MineStarWarp.listeners.PlayerRespawnListener;
import com.minestar.MineStarWarp.listeners.PlayerTeleportListener;
import com.minestar.MineStarWarp.localization.Localization;
import com.minestar.MineStarWarp.utils.LogUnit;

public class Main extends JavaPlugin {

    private static final String PLUGIN_NAME = "MineStarWarp";

    public static LogUnit log = LogUnit.getInstance(PLUGIN_NAME);

    public static WarpManager warpManager;
    public static HomeManager homeManager;
    public static SpawnManager spawnManager;
    public static BankManager bankManager;
    public static Localization localization;
    public static BackManager backManager;
    public static ArrayList<String> respawn;

    private CommandList commandList;

    public static FileConfiguration config;

    public void onDisable() {
        ConnectionManager.closeConnection();
        warpManager = null;
        homeManager = null;
        spawnManager = null;
        bankManager = null;
        commandList = null;
        localization = null;
        backManager = null;
        respawn = null;
        log.printInfo("disabled");
    }

    public void onEnable() {

        checkConfig();

        if (ConnectionManager.initialize()) {
            localization = Localization.getInstance(config.getString(
                    "language", "de"));
            commandList = new CommandList(getServer());
            DatabaseManager dbManager = new DatabaseManager(getServer());
            warpManager = new WarpManager(dbManager, config);
            homeManager = new HomeManager(dbManager);
            spawnManager = new SpawnManager(dbManager);
            bankManager = new BankManager(dbManager);
            backManager = new BackManager();
            respawn = new ArrayList<String>();

            PluginManager pm = getServer().getPluginManager();

            pm.registerEvent(Type.PLAYER_RESPAWN, new PlayerRespawnListener(),
                    Priority.Normal, this);
            pm.registerEvent(Type.PLAYER_TELEPORT,
                    new PlayerTeleportListener(), Priority.Normal, this);

            // shall the home set when a player uses the bed?
            if (config.getBoolean("home.setHomeUsingBed", true))
                pm.registerEvent(Type.PLAYER_BED_ENTER,
                        new PlayerBedListener(), Priority.Normal, this);

            log.printInfo("enabled");
        }
        else {
            log.printWarning("Can't connect to database!");
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    /**
     * Load the properties from the configFile. If the configFile not exists it
     * create ones
     */
    private void checkConfig() {

        File pluginDir = getDataFolder();
        if (!pluginDir.exists())
            pluginDir.mkdirs();

        File configFile = new File(pluginDir.getAbsolutePath().concat(
                "/config.yml"));

        if (!configFile.exists()) {
            createConfig();
        }
        config = getConfig();
    }

    /**
     * Creates a config and use default values for it. They are stored in a yml
     * format.
     */
    public void createConfig() {

        config = getConfig();
        config.addDefault("warps.default", 0);
        config.addDefault("warps.probe", 2);
        config.addDefault("warps.free", 5);
        config.addDefault("warps.pay", 9);
        config.addDefault("warps.warpsPerPage", 8);

        config.addDefault("banks.banksPerPage", 10);

        config.addDefault("home.setHomeUsingBed", true);

        config.addDefault("language", "de");

        config.options().copyDefaults(true);
        saveConfig();
    }
}
