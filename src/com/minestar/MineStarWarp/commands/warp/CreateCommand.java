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

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.Warp;
import com.minestar.MineStarWarp.commands.Command;

public class CreateCommand extends Command {

    public CreateCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    public void execute(String[] args, Player player) {
        String warpName = args[0];
        if (Main.warpManager.hasFreeWarps(player)) {
            if (!Main.warpManager.isWarpExisting(warpName))
                Main.warpManager.addWarp(player, warpName, new Warp(player));
            else
                player.sendMessage(ChatColor.RED
                        + "Sorry, there is already an warp named " + warpName);
        }
        else
            player.sendMessage(ChatColor.RED
                    + "Sorry, you didn't have enough free warp slots to create one more. Please delete one warp of yours!");
    }
}
