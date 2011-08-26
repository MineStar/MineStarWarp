package com.minestar.MineStarWarp.data;

import java.util.TreeMap;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Warp;

public class WarpManager {

    private TreeMap<String, Warp> warps;
    
    private final Server server;
    private final DatabaseManager dbManager; 
    
    public WarpManager(Server server) {
        this.server = server;
        dbManager = new DatabaseManager(server);
        warps = dbManager.loadWarpsFromDatabase();
    }
}
