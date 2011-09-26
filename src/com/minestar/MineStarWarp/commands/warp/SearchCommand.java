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

package com.minestar.MineStarWarp.commands.warp;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.Warp;
import com.minestar.MineStarWarp.commands.Command;

public class SearchCommand extends Command {

    public SearchCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization.get("searchCommand.description");
    }

    @Override
    /**
     * Representing the command <br>
     * /warp search <br>
     * The player can search for any warps he can use by only tipping a word that is similiar to it
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the query
     */
    public void execute(String[] args, Player player) {

        String query = args[0];

        // Getting all warps that contains the query
        HashMap<String, Warp> result = Main.warpManager.getSimiliarWarps(query,
                player);

        // When at least one warp is found
        if (result != null) {
            player.sendMessage(ChatColor.YELLOW
                    + Main.localization.get("searchCommand.matches", query));
            // Sending all warps per Line to the player
            for (Entry<String, Warp> entry : result.entrySet()) {
                Warp warp = entry.getValue();
                String warpName = entry.getKey();

                // Output Formation START
                String owner = null;
                ChatColor color = null;

                if (warp.isOwner(player.getName())) {
                    color = ChatColor.AQUA;
                    owner = "you";
                }
                else if (warp.isPublic())
                    color = ChatColor.GREEN;
                else
                    color = ChatColor.RED;

                if (owner == null)
                    owner = warp.getOwner();

                int x = warp.getLoc().getBlockX();
                int y = warp.getLoc().getBlockY();
                int z = warp.getLoc().getBlockZ();
                // Output Formation END
                player.sendMessage(color + "'" + warpName + "'"
                        + ChatColor.WHITE + " by " + owner + " @(" + x + ", "
                        + y + ", " + z + ")");
            }
        }
        // When no warp was found
        else
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("searchCommand.noMatches", query));
    }
}
