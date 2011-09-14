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
import com.minestar.MineStarWarp.commands.Command;

public class RenameCommand extends Command {
    public RenameCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization.get(RENAME_DESCRIPTION);
    }

    @Override
    /**
     * Representing the command <br>
     * /warp rename<br>
     * Chance the name of a warp.
     */
    public void execute(String[] args, Player player) {
        if (args.length == 2) {
            if (Main.warpManager.isWarpExisting(args[0])) {
                if (!Main.warpManager.isWarpExisting(args[1])) {
                    if (Main.warpManager.getWarp(args[0]).canEdit(player)) {
                        Main.warpManager.renameWarp(player, args[0], args[1]);
                    }
                    else {
                        player.sendMessage(ChatColor.RED
                                + Main.localization.get(RENAME_NOT_OWNER));
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED
                            + Main.localization.get(RENAME_ALREADY_EXIST,
                                    args[0], args[1]));
                }
            }
            else {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get(RENAME_NOT_EXIST, args[0]));
            }
        }
    }
}
