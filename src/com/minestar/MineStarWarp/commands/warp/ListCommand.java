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
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.Warp;
import com.minestar.MineStarWarp.commands.ExtendedCommand;

public class ListCommand extends ExtendedCommand {

    private final int warpsPerPage;

    public ListCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        warpsPerPage = Main.config.getInt("warps.warpsPerPage", 8);
        this.description = Main.localization.get(LIST_DESCRIPTION);
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

        TreeMap<String, Warp> warps;
        boolean showPlayersWarps = args.length != 0
                && args[0].equalsIgnoreCase("my");
        if (showPlayersWarps) {
            warps = Main.warpManager.getWarpsPlayerIsOwner(player.getName());
            if (warps != null) {
                player.sendMessage(ChatColor.AQUA
                        + Main.localization.get(LIST_USED_SLOTS,
                                Main.warpManager.usedWarpSlots(player)));
                showWarpList(player, warps);
            }

            else
                player.sendMessage(ChatColor.RED
                        + Main.localization.get(LIST_NO_MY_WARPS));
        }
        else {
            if (args.length != 0 && !args[0].matches("\\d*")) {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get(LIST_PAGE_NUMBER));
                return;
            }
            int maxPageNumber = (int) Math.ceil(Main.warpManager
                    .countWarpsCanUse(player) / (double) warpsPerPage);
            if (maxPageNumber == 0) {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get(LIST_NO_WARPS));
                return;
            }
            int pageNumber = args.length == 0 ? 1 : Integer.parseInt(args[0]);
            if (pageNumber <= maxPageNumber) {
                warps = Main.warpManager.getWarpsForList(pageNumber,
                        warpsPerPage, player);
                player.sendMessage(Main.localization.get(LIST_PAGE_HEAD,
                        Integer.toString(pageNumber),
                        Integer.toString(maxPageNumber)));
                showWarpList(player, warps);
            }
            else
                player.sendMessage(ChatColor.RED
                        + Main.localization.get(LIST_HIGH_PAGE,
                                Integer.toString(maxPageNumber)));

        }
    }

    /**
     * This sends all the warps in a good format to the player
     * 
     * @param player
     *            The reciever
     * @param warps
     *            The warps to present
     */
    private void showWarpList(Player player, TreeMap<String, Warp> warps) {

        for (String warpName : warps.keySet()) {

            Warp warp = warps.get(warpName);
            boolean isOwner = warp.isOwner(player.getName());
            String creator = isOwner ? "you" : warp.getOwner();
            Location loc = warp.getLoc();
            int x = (int) Math.round(loc.getX());
            int y = loc.getBlockY();
            int z = (int) Math.round(loc.getZ());
            ChatColor color;

            if (isOwner)
                color = ChatColor.AQUA;
            else if (warp.isPublic())
                color = ChatColor.GREEN;
            else
                color = ChatColor.RED;

            String location = " @(" + x + ", " + y + ", " + z + ")";
            String creatorString = (warp.isPublic() ? "(+)" : "(-)") + " by "
                    + creator;
            player.sendMessage(color + "'" + warpName + "'" + ChatColor.WHITE
                    + creatorString + location);
        }
    }
}
