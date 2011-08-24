/**
 * 
 */
package com.minestar.MineStarWarp;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.minestar.MineStarWarp.listener.TeleportCommandListener;
import com.minestar.MineStarWarp.listener.WarpCommandListener;

/**
 * @author Meldanor
 * 
 */
public class Main extends JavaPlugin {

    private static Logger log = Logger.getLogger("Minecraft");

    private static final String PLUGIN_NAME = "MineStarWarp";

    public static void writeToLog(String info) {

        log.info("[" + PLUGIN_NAME + "]:" + info);
    }

    public void onDisable() {

        writeToLog("disabled");

    }

    public void onEnable() {

        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS,
                new WarpCommandListener(this.getServer()),
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS,
                new TeleportCommandListener(this.getServer()),
                Event.Priority.Normal, this);
        writeToLog("enabled");
    }

}
