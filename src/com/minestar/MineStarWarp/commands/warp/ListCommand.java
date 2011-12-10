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

import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.Warp;
import com.minestar.MineStarWarp.commands.ExtendedCommand;

public class ListCommand extends ExtendedCommand {

    private final int warpsPerPage;

    public ListCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        warpsPerPage = Main.config.getInt("warps.warpsPerPage", 8);
        this.description = Main.localization.get("listCommand.description");
    }

    @Override
    /**
     * Representing the command <br>
     * /warp list <br>
     * Sending the player a list of all warps the player can use. 
     * If warp list my is used it sends a list of warps the player has created
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is page number
     */
    public void execute(String[] args, Player player) {

        // /warp list was used
        if (args.length == 0)
            showAllWarps(player, 1);
        // warp list PARAMATER was used
        else if (args.length == 1) {
            // /warp list my
            if (args[0].equalsIgnoreCase("my"))
                showPlayersWarp(player, player.getName());
            // warp list #
            else if (args[0].matches("\\d*"))
                showAllWarps(player, Integer.parseInt(args[0]));
            // warp list playerName
            else if (UtilPermissions.playerCanUseCommand(player,
                    "minestarwarp.command.listPlayer"))
                showPlayersWarp(player, args[0]);
        }
        else
            player.sendMessage(getHelpMessage());
    }

    /**
     * When player use the command '/warp list #' or '/warp list' without an
     * paramater
     * 
     * @param player
     * @param page
     */
    private void showAllWarps(Player player, int page) {
        // sorted result
        TreeMap<String, Warp> warps;
        // how many pages of warps the player can see
        int maxPageNumber = (int) Math.nextUp(Main.warpManager
                .countWarpsCanUse(player) / (double) warpsPerPage);
        if (maxPageNumber == 0) {
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("listCommand.noWarps"));
            return;
        }
        if (page <= maxPageNumber) {
            warps = Main.warpManager
                    .getWarpsForList(page, warpsPerPage, player);
            player.sendMessage(Main.localization.get("listCommand.pageHead",
                    Integer.toString(page), Integer.toString(maxPageNumber)));
            Main.warpManager.showWarpList(player, warps);
        }
        else
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("listCommand.highPage",
                            String.valueOf(maxPageNumber)));

    }

    /**
     * When player use the command '/warp list my' or '/warp list playername'
     * 
     * @param player
     * @param targetName
     */
    private void showPlayersWarp(Player player, String targetName) {
        TreeMap<String, Warp> warps = Main.warpManager
                .getWarpsPlayerIsOwner(targetName);
        if (warps != null) {
            player.sendMessage(ChatColor.AQUA + " Spieler '" + targetName
                    + "' hat "
                    + Main.warpManager.countWarpsCreatedBy(targetName) + "/"
                    + Main.warpManager.getMaximumWarp(targetName)
                    + " private Warps");
            Main.warpManager.showWarpList(player, warps);
        }

        else
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("listCommand.noMyWarps"));
    }
}
