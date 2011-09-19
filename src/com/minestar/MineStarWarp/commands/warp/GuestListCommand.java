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

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.Warp;
import com.minestar.MineStarWarp.commands.Command;

public class GuestListCommand extends Command {
    public GuestListCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization.get("guestList.description");
    }

    @Override
    /**
     * Representing the command <br>
     * /warp guestlist <br>
     * Show the guests of a warp.
     */
    public void execute(String[] args, Player player) {
        String warpName = args[0];
        if (Main.warpManager.isWarpExisting(warpName)) {
            Warp warp = Main.warpManager.getWarp(warpName);
            if (warp.canEdit(player)) {
                ArrayList<String> guests = warp.getGuests();
                if (guests != null) {
                    if (!guests.isEmpty()) {
                        StringBuilder result = new StringBuilder("");
                        for (String temp : guests) {
                            result.append(temp);
                            result.append(", ");
                        }
                        player.sendMessage(ChatColor.AQUA
                                + Main.localization.get("guestList.list",
                                        warpName) + ChatColor.GREEN
                                + result.substring(0, result.length() - 2));
                    }
                    else {
                        player.sendMessage(ChatColor.AQUA
                                + Main.localization.get("guestList.noGuests",
                                        warpName));
                    }
                }
                else {
                    player.sendMessage(ChatColor.AQUA
                            + Main.localization.get("guestList.public",
                                    warpName));
                }
            }
            else {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get("guestList.notOwner", warpName));
            }
        }
        else {
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("guestList.notExist", warpName));
        }
    }
}
