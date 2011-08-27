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

    // Key = Name of Warp
    private TreeMap<String, Warp> warps;
    
    private final DatabaseManager dbManager;
    
    private static WarpManager instance;
    
    private WarpManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        warps = dbManager.loadWarpsFromDatabase();
    }
    
    public static WarpManager getInstance(DatabaseManager dbManager) {
        if (instance == null)
            instance = new WarpManager(dbManager);
        return instance;
    }
}
