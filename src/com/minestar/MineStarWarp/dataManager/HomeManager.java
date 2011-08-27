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

package com.minestar.MineStarWarp.dataManager;

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
