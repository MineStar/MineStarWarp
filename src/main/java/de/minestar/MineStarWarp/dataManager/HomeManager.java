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

package de.minestar.MineStarWarp.dataManager;

import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.database.DatabaseManager;
import de.minestar.minestarlibrary.utils.PlayerUtils;

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

        Location loc = player.getLocation();
        String playerName = player.getName().toLowerCase();

        // When the player has set a home once before just update it
        if (homes.containsKey(playerName)) {
            if (dbManager.updateHome(player)) {
                homes.put(playerName, loc);
                PlayerUtils.sendSuccess(player, Core.NAME, "Neues Zuhause gesetzt. Nutze '/home', um dich hier hin zu teleportieren");
            } else
                PlayerUtils.sendError(player, playerName, "Fehler beim setzen des Zuhauses! Bitte an einen Admin wenden!");
        }
        // If not, we have to create a new entry in the database
        else {
            if (dbManager.setHome(player)) {
                homes.put(playerName, loc);
                PlayerUtils.sendSuccess(player, Core.NAME, "Neues Zuhause gesetzt. Nutze '/home', um dich hier hin zu teleportieren");
            } else
                PlayerUtils.sendError(player, playerName, "Fehler beim setzen des Zuhauses! Bitte an einen Admin wenden!");
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
