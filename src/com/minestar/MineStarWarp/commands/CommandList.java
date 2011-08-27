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

package com.minestar.MineStarWarp.commands;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.commands.home.*;
import com.minestar.MineStarWarp.commands.teleport.*;
import com.minestar.MineStarWarp.commands.warp.*;

public class CommandList {

    private static Command[] commands;

    public CommandList(Server server) {
        commands = new Command[] {
                // Teleport Commands
                new TeleportHereCommand("/tphere", "<Player>", "tphere", server),
                new TeleportPlayerToCommand("/tp", "<Player> <Player>",
                        "tpPlayerTo", server),
                new TeleportToCommand("/tp", "<Player>", "tpTo", server),

                // Warp Command
                new WarpToCommand("/warp pcreate", "<Name>", "warpTo", server),

                // Warp Creation and Removing
                new CreateCommand("/warp create", "<Name>", "create", server),
                new CreateCommand("/warp pcreate", "<Name>", "create", server),
                new DeleteCommand("/warp pcreate", "<Name>", "delete", server),

                // Searching Warps
                new ListCommand("/warp pcreate", "<Name>", "list", server),
                new SearchCommand("/warp search", "<Name>", "search", server),

                // Modifiers
                new PrivateCommand("/warp pcreate", "<Name>", "private", server),
                new PublicCommand("/warp pcreate", "<Name>", "public", server),

                // Guests
                new InviteCommand("/warp pcreate", "<Name>", "invite", server),
                new UninviteCommand("/warp pcreate", "<Name>", "uninvite",
                        server),
                // Home
                new SetHomeCommand("/setHome", "<Name>", "setHome", server),
                new HomeCommand("/home", "", "home", server) };

    }

    public static void handleCommand(CommandSender sender, String label,
            String[] args) {
        if (!label.startsWith("/"))
            label = "/" + label;

        if (sender instanceof Player) {
            for (Command cmd : commands) {
                if (cmd.getSyntax().equalsIgnoreCase(label)
                        && cmd.hasCorrectSyntax(args)) {
                    cmd.run(args, (Player) sender);
                    return;
                }
            }
            ((Player) sender).sendMessage("command not found!");
        }
    }
}
