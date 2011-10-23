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

package com.minestar.MineStarWarp.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.commands.ExtendedCommand;

public class TeleportToCoords extends ExtendedCommand {

    public TeleportToCoords(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization
                .get("teleportToCoords.description");
    }

    @Override
    public void execute(String[] args, Player player) {

        if (!UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command.tptocoords")) {
            player.sendMessage(NO_RIGHT);
            return;
        }

        double[] coords = new double[3];
        try {
            coords[0] = Double.parseDouble(args[0]);
            coords[1] = Double.parseDouble(args[1]);
            coords[2] = Double.parseDouble(args[2]);
        }
        catch (Exception e) {
            player.sendMessage(ChatColor.BLUE + "/tp X Y Z ");
            return;
        }
        String worldName;

        if (args.length == 4)
            worldName = args[4];
        else
            worldName = player.getWorld().getName();

        player.teleport(new Location(server.getWorld(worldName), coords[0],
                coords[1], coords[2]));
        player.sendMessage(ChatColor.AQUA + Main.localization
                .get("teleportToCoords.description"));
    }
}
