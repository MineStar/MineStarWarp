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

public class PublicCommand extends Command {

    public PublicCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization.get("publicCommand.description");
    }

    @Override
    /**
     * Representing the command <br>
     * /warp public <br>
     * This converts a warp to a public warp, so everybody can use it
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the warp name
     */
    public void execute(String[] args, Player player) {

        String warpName = args[0];
        Warp warp = Main.warpManager.getWarp(warpName);

        if (warp == null) {
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("publicCommand.notExisting",
                            warpName));
            if (Main.warpManager.getWarp(warpName.toLowerCase()) != null)
                player.sendMessage(ChatColor.GRAY
                        + Main.localization.get("publicCommand.possibleWarp",
                                warpName.toLowerCase()));
            return;
        }

        if (warp.canEdit(player)) {
            if (!warp.isPublic())
                Main.warpManager.changeAccess(player, true, warpName);
            else
                player.sendMessage(ChatColor.RED
                        + Main.localization.get("publicCommand.isPublic",
                                warpName));
        }
        else
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("publicCommand.notOwner", warpName));
    }
}
