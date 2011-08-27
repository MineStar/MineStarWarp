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
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.minestar.MineStarWarp.dataManager.ConnectionManager;

public class Main extends JavaPlugin {

    private static Logger log = Logger.getLogger("Minecraft");

    private static final String PLUGIN_NAME = "MineStarWarp";

    public static Configuration configFile;

    public Main() {
    }

    public static void writeToLog(String info) {

        log.info("[" + PLUGIN_NAME + "]:" + info);
    }

    public void onDisable() {

        writeToLog("disabled");
        
        saveConfig();
    }

    public void onEnable() {

        writeToLog("enabled");
        
        loadConfig();
        
        ConnectionManager.initialize();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        return true;
    }

    public void loadConfig() {
        File pluginDir = new File("plugins/MineStarWarp/Config.yml");
        pluginDir.mkdirs();
        configFile = new Configuration(pluginDir);
    }

    public void saveConfig() {

    }

}
