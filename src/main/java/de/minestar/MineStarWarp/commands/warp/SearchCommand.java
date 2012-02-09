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

package de.minestar.MineStarWarp.commands.warp;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.Warp;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class SearchCommand extends AbstractCommand {

    private WarpManager wManager;

    public SearchCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Nach bestimmten Warpnamen suchen";
        this.wManager = wManager;
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
        Map<String, Warp> result = wManager.getSimiliarWarps(query, player);

        // When at least one warp is found
        if (result != null) {
            ChatUtils.printInfo(player, pluginName, ChatColor.YELLOW, "Treffer für '" + query + "' :");
            // Sending all warps per Line to the player
            wManager.showWarpList(player, result);
        }
        // When no warp was found
        else
            ChatUtils.printError(player, pluginName, "Keine Treffer gefunden für '" + query + "'!");
    }
}
