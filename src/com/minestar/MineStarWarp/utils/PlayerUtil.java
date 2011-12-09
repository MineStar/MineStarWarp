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

package com.minestar.MineStarWarp.utils;

import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PlayerUtil {

    private static Comparator<OfflinePlayer> c = new Comparator<OfflinePlayer>() {
        public int compare(OfflinePlayer o1, OfflinePlayer o2) {
            return o1.getName().toLowerCase()
                    .compareTo(o2.getName().toLowerCase());
        }
    };

    /**
     * Searching for a player having the case insensitive name. If not found,
     * the first player that contains the name is returned.
     * 
     * @param server
     *            The server where the player is connected to
     * @param name
     *            The name of the player
     * @return Null if no player who contains the name or have the case
     *         insensitive name
     */
    public static Player getPlayer(Server server, String name) {

        name = name.toLowerCase();
        // name is inside the players real name or the players display name
        for (Player tempPlayer : server.getOnlinePlayers()) {
            if (tempPlayer.getName().toLowerCase().contains(name)
                    || tempPlayer.getDisplayName().toLowerCase().contains(name))
                return tempPlayer;
        }

        return null;
    }

    public static String getOfflinePlayer(Server server, String name) {
        // Every player who was on the server
        OfflinePlayer[] players = server.getOfflinePlayers();
        OfflinePlayer target = server.getOfflinePlayer(name);

        int i = Arrays.binarySearch(players, target, c);
        return i >= 0 ? players[i].getName() : null;
    }
}
