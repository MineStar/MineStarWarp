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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.database.DatabaseManager;

/**
 * Is responsible for handling all home commands.
 * 
 * @author Meldanor
 * 
 */
public class HomeManager {

    // Key = Username, so only one player can have a home
    private TreeMap<String, Location> homes;

    // Is used to persistate the data
    private final DatabaseManager dbManager;

    /**
     * Creats a HomeManager handling all data belongs to the homes of the player
     */
    public HomeManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        homes = dbManager.loadHomesFromDatabase();
    }

    /**
     * Used for the command <br>
     * /setHome
     * 
     * @param player
     *            The command caller
     */
    public void setHome(Player player) {

        // When the player has set a home once before just update it
        Location loc = player.getLocation();
        String playerName = player.getName().toLowerCase();
        if (homes.containsKey(playerName)) {
            if (dbManager.updateHome(player)) {
                homes.put(playerName, loc);
                player.sendMessage(ChatColor.GRAY
                        + Main.localization.get("homeManager.homeSet"));
            }
            else {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get("homeManager.error"));
                Main.log.printWarning("User " + player.getName()
                        + " cannot update home at " + loc.getWorld().getName()
                        + " " + loc.getX() + " " + loc.getY() + " "
                        + loc.getZ());
            }
        }
        // If not, we have to create a new entry in the database
        else {
            if (dbManager.setHome(player)) {
                homes.put(playerName, loc);
                player.sendMessage(ChatColor.GRAY
                        + Main.localization.get("homeManager.homeSet"));
            }
            else {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get("homeManager.error"));

                Main.log.printWarning("User " + player.getName()
                        + " cannot set home at " + loc.getWorld().getName()
                        + " " + loc.getX() + " " + loc.getY() + " "
                        + loc.getZ());
            }
        }
    }

    /**
     * Used for the command <br>
     * /home
     * 
     * @param player
     *            Command caller
     * @return Null when the player didn't have a home
     */
    public Location getPlayersHome(Player player) {
        return homes.get(player.getName().toLowerCase());
    }
}
