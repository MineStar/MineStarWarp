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

public class WarpToCommand extends Command {

    public WarpToCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    public void execute(String[] args, Player player) {
        String warpName = args[0];
        Warp warp = Main.warpManager.getSimiliarWarp(warpName);
        if (warp != null) {
            if (warp.canUse(player.getName())) {
                player.teleport(warp.getLoc());
                player.sendMessage(ChatColor.AQUA + "Welcome to "+warpName);
            }
            else
                player.sendMessage(ChatColor.RED + "Sorry, you can't use the warp "+warpName+"!");
        }
        else
            player.sendMessage(ChatColor.RED + warpName + " doesn't not exist!");
    }
}
