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

package com.minestar.MineStarWarp.commands.spawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.commands.Command;

public class SpawnCommand extends Command {

    public SpawnCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    /**
     * Representing the command <br>
     * /spawn <br>
     * This teleports the player to the main spawn(world with ID 0) or 
     * the spawn of the world in args[0]
     * 
     * @param player
     *            Called the command
     * @param args
     *            Can be empty or the worlds name
     */
    public void execute(String[] args, Player player) {

        if (args.length == 0) {
            Location loc = Main.spawnManager.getMainSpawn(server);
            if (loc != null) {
                player.teleport(loc);
                player.sendMessage(ChatColor.AQUA
                        + Main.localization.get("spawnCommand.welcomeMain"));
            }
            else
                player.sendMessage(ChatColor.RED
                        + Main.localization.get("spawnCommand.noMainSpawn"));
        }
        else {
            String worldName = args[0].toLowerCase();

            if (!UtilPermissions.playerCanUseCommand(player,
                    "minestarwarp.command.spawnSpecific." + worldName)) {
                player.sendMessage(ChatColor.RED
                        + Main.localization.get("spawnCommand.notAllowed",
                                worldName));
                return;
            }

            Location loc = Main.spawnManager.getSpawn(worldName);
            if (loc != null) {
                player.teleport(loc);
                player.sendMessage(ChatColor.AQUA
                        + Main.localization.get("spawnCommand.welcome", loc
                                .getWorld().getName()));
            }
            else
                player.sendMessage(ChatColor.RED
                        + Main.localization.get("spawnCommand.notFound",
                                worldName));
        }
    }
}
