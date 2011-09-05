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

import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;

public class SpawnManager {

    private TreeMap<String, Location> spawns;
    private final DatabaseManager dbManager;

    public SpawnManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        spawns = dbManager.loadSpawnsFromDatabase();
    }

    /**
     * Sets the spawn for the world. If already exists it just updates the
     * coordinates.
     * 
     * @param player
     *            The command caller
     */
    public void setSpawn(Player player) {

        Location loc = player.getLocation();
        String worldName = loc.getWorld().getName().toLowerCase();
        if (spawns.containsKey(worldName)) {
            if (dbManager.updateSpawn(loc)) {
                spawns.put(worldName, loc);
                player.sendMessage(ChatColor.AQUA
                        + "Spawn location has updated for world "
                        + loc.getWorld().getName());
                loc.getWorld().setSpawnLocation(loc.getBlockX(),
                        loc.getBlockY(), loc.getBlockZ());
            }
            else
                player.sendMessage(ChatColor.RED
                        + "ERROR: Can't set the spawn for this world!");
        }
        else {
            if (dbManager.addSpawn(loc)) {
                spawns.put(worldName, loc);
                player.sendMessage(ChatColor.AQUA
                        + "Spawn location was set for world "
                        + loc.getWorld().getName());

                loc.getWorld().setSpawnLocation(loc.getBlockX(),
                        loc.getBlockY(), loc.getBlockZ());
            }
            else
                player.sendMessage(ChatColor.RED
                        + "ERROR: Can't set the spawn for this world!");
        }
    }

    /**
     * When the name is exactly matching a key, its value is returned. If not,
     * the first element with a key that starts with the name is returned. When
     * both failed, null is returned
     * 
     * @param worldName
     *            The world name
     * @return Location for the worlds spawn if exist. If not, null is returned
     */
    public Location getSpawn(String worldName) {

        Location loc = spawns.get(worldName);
        if (loc != null)
            return loc;
        else {
            for (Entry<String, Location> entry : spawns.entrySet())
                if (entry.getKey().toLowerCase()
                        .startsWith(worldName.toLowerCase()))
                    return entry.getValue();
        }
        return null;
    }

    /**
     * Get the spawn for the world with the index 0 in server.getWorlds() list
     * 
     * @param server
     *            The server having the worlds
     * @return Null if the spawn does not exist (but it should!), else the
     *         locations
     */
    public Location getMainSpawn(Server server) {

        String mainWorldName = server.getWorlds().get(0).getName();
        
        Location loc = spawns.get(mainWorldName);
        if (loc == null)
            Main.log.printWarning("The main world does not have a spawn location!");
        return loc;
    }
}
