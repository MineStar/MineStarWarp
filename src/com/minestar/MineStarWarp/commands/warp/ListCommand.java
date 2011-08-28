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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.Warp;
import com.minestar.MineStarWarp.commands.Command;

public class ListCommand extends Command {

    private final static int WARPS_PER_PAGE = 8;

    public ListCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    public void execute(String[] args, Player player) {

        HashMap<String, Warp> warps;
        boolean showPlayersWarps = args[0].equalsIgnoreCase("my");
        if (showPlayersWarps) {
            warps = Main.warpManager.getWarpsPlayerIsOwner(player.getName());
            if (warps != null)
                showWarpList(player, warps);
            else
                player.sendMessage(ChatColor.RED
                        + "You didn't have created a warp!");
        }
        else {
            int maxPageNumber = (int) Math.ceil(Main.warpManager
                    .countWarpsCanUse(player) / (double) WARPS_PER_PAGE);
            if (maxPageNumber == 0) {
                player.sendMessage(ChatColor.RED
                        + "You do not have access to any warps!");
                return;
            }
            int pageNumber = Integer.parseInt(args[0]);
            if (pageNumber <= maxPageNumber) {
                warps = Main.warpManager.getWarpsForList(pageNumber,
                        WARPS_PER_PAGE);
                String intro = "------------------- Page " + pageNumber + "/"
                        + maxPageNumber + " -------------------";

                showWarpList(player, warps, intro);
            }
            else
                player.sendMessage(ChatColor.RED
                        + "Use a pagenumber between 1 and " + maxPageNumber);

        }
    }

    private void showWarpList(Player player, HashMap<String, Warp> warps) {
        showWarpList(player, warps, "");
    }

    private void showWarpList(Player player, HashMap<String, Warp> warps,
            String intro) {

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

            // Find remaining length left
            int left = getStringWidth(intro)
                    - getStringWidth("''" + creatorString + location);

            int nameLength = getStringWidth(warpName);
            if (left > nameLength) {
                warpName = "'" + warpName + "'" + ChatColor.WHITE
                        + creatorString + whitespace(left - nameLength);
            }
            else if (left < nameLength) {
                warpName = "'" + substring(warpName, left) + "'"
                        + ChatColor.WHITE + creatorString;
            }

            player.sendMessage(color + warpName + location);
        }
    }

    private String charWidthIndexIndex = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»";
    private int[] charWidths = { 4, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6, 7, 6, 6, 6, 6,
            6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            4, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6, 6,
            6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 4, 6, 3, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6, 8, 9, 9,
            6, 6, 6, 8, 8, 6, 8, 8, 8, 8, 8, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 9, 9, 9, 5, 9,
            9, 8, 7, 7, 8, 7, 8, 8, 8, 7, 8, 8, 7, 9, 9, 6, 7, 7, 7, 7, 7, 9,
            6, 7, 8, 7, 6, 6, 9, 7, 6, 7, 1 };

    private int getStringWidth(String s) {
        int i = 0;
        if (s != null)
            for (int j = 0; j < s.length(); j++)
                i += getCharWidth(s.charAt(j));
        return i;
    }

    private int getCharWidth(char c) {
        int k = charWidthIndexIndex.indexOf(c);
        if (c != '\247' && k >= 0)
            return charWidths[k];
        return 0;
    }

    /**
     * Lob shit off that string till it fits.
     */
    private String substring(String name, int left) {
        while (getStringWidth(name) > left) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    public String whitespace(int length) {
        int spaceWidth = getStringWidth(" ");

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < length; i += spaceWidth)
            ret.append(" ");

        return ret.toString();
    }

}
