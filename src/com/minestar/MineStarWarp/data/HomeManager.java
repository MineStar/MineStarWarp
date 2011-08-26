package com.minestar.MineStarWarp.data;

import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Server;

public class HomeManager {
    
    // Key = Username, so only one player can have a home
    private TreeMap<String, Location> homes;
    
    private final Server server;
    private final DatabaseManager dbManager;
    
    public HomeManager(Server server, DatabaseManager dbManager) {
        this.server = server;
        this.dbManager = dbManager;
        homes = dbManager.loadHomesFromDatabase();
    }
}
