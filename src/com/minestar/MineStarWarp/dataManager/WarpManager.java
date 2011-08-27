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
