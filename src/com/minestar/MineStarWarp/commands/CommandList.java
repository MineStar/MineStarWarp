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

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.commands.home.HomeCommand;
import com.minestar.MineStarWarp.commands.home.SetHomeCommand;
import com.minestar.MineStarWarp.commands.teleport.TeleportHereCommand;
import com.minestar.MineStarWarp.commands.teleport.TeleportPlayerToCommand;
import com.minestar.MineStarWarp.commands.teleport.TeleportToCommand;
import com.minestar.MineStarWarp.commands.warp.CreateCommand;
import com.minestar.MineStarWarp.commands.warp.DeleteCommand;
import com.minestar.MineStarWarp.commands.warp.InviteCommand;
import com.minestar.MineStarWarp.commands.warp.ListCommand;
import com.minestar.MineStarWarp.commands.warp.PrivateCommand;
import com.minestar.MineStarWarp.commands.warp.PublicCommand;
import com.minestar.MineStarWarp.commands.warp.SearchCommand;
import com.minestar.MineStarWarp.commands.warp.UninviteCommand;
import com.minestar.MineStarWarp.commands.warp.WarpToCommand;

public class CommandList {

    // The commands are stored in this list. The key indicates the
    // commandssyntax and the argument counter
    private HashMap<String, Command> commandList = new HashMap<String, Command>();

    /**
     * Creates an array where the commands are stored in and add them all to the
     * HashMap
     * 
     * @param server
     */
    public CommandList(Server server) {

        // Add an command to this list to register it in the plugin
        Command[] commands = new Command[] {
                // Teleport Commands
                new TeleportHereCommand("/tphere", "<Player>", "tphere", server),
                new TeleportPlayerToCommand("/tp", "<Player> <Player>",
                        "tpPlayerTo", server),
                new TeleportToCommand("/tp", "<Player>", "tpTo", server),

                // Warp Command
                new WarpToCommand("/warp", "<Name>", "warpTo", server),

                // Warp Creation and Removing
                new CreateCommand("/warp create", "<Name>", "create", server),
                new CreateCommand("/warp pcreate", "<Name>", "create", server),
                new DeleteCommand("/warp delete", "<Name>", "delete", server),

                // Searching Warps
                new ListCommand("/warp list", "<Number or my>", "list", server),
                new SearchCommand("/warp search", "<Name>", "search", server),

                // Modifiers
                new PrivateCommand("/warp private", "<Name>", "private", server),
                new PublicCommand("/warp public", "<Name>", "public", server),

                // Guests
                new InviteCommand("/warp invite", "<PlayerName> <Warpname>",
                        "invite", server),
                new UninviteCommand("/warp uninvite",
                        "<PlayerName> <Warpname>", "uninvite", server),
                // Home
                new SetHomeCommand("/sethome", "", "sethome", server),
                new HomeCommand("/home", "", "home", server) };

        // store the commands in the hash map
        initCommandList(commands);
    }

    /**
     * Call this method in onCommand() in the Main. It searchs for the command
     * and runs it. If a command is not found it will recursively add an
     * argument to the label until there are arguments. If no arguments left, it
     * stops the recursive and say, that no command exists
     * 
     * @param sender
     *            The instace should be a player, but not necessary
     * @param label
     *            The first word after the slash ( / )
     * @param args
     *            The words after the label seperated by an space
     */
    public void handleCommand(CommandSender sender, String label, String[] args) {

        if (!label.startsWith("/"))
            label = "/" + label;

        if (sender instanceof Player) {
            Player player = (Player) sender;
            // the keys in the CommandList are build by this:
            // commandssyntax + _ + number of arguments
            String key = label + "_" + (args != null ? args.length : 0);
            Command com = commandList.get(key);
            // a command was found
            if (com != null)
                com.run(args, player);
            // a command was not found, go recursively try finding the command
            else if (com == null && args != null && args.length >= 1) {
                // add the first argument of the command to the syntax
                label += " " + args[0];
                if (args.length == 1)
                    args = null;
                else
                    args = Arrays.copyOfRange(args, 1, args.length);

                handleCommand(sender, label, args);
            }
            else
                player.sendMessage(ChatColor.RED + "Command '" + label
                        + "' not found!");
        }
    }

    /**
     * Stores the commands from the array to a HashMap. The key is generated by
     * the followning: <br>
     * <code>syntax_numberOfArguments</code> <br>
     * Example: /warp create_1 (because create has one argument)
     * 
     * @param cmds The array list for commands
     */
    private void initCommandList(Command[] cmds) {
        for (Command cmd : cmds)
            commandList.put(
                    cmd.getSyntax() + "_"
                            + (cmd.getArguments().split("<").length - 1), cmd);
    }
}
