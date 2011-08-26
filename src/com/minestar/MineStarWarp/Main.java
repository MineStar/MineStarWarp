/**
 * 
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

/**
 * @author Meldanor
 * 
 */
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
