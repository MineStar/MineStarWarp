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

public class MoveCommand extends Command {
    public MoveCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization.get(MOVE_DESCRIPTION);
    }

    @Override
    /**
     * Representing the command <br>
     * /warp move <br>
     * Change the Location of a warp.
     */
    public void execute(String[] args, Player player) {
        if (Main.warpManager.isWarpExisting(args[0])) {
            Warp warp = Main.warpManager.getWarp(args[0]);
            if (warp.canEdit(player)) {
                Main.warpManager.updateWarp(player, args[0]);
            }
            else {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get(MOVE_NOT_OWNER));
            }
        }
        else {
            player.sendMessage(ChatColor.RED
                    + Main.localization.get(MOVE_NOT_EXIST, args[0]));
        }
    }

}
