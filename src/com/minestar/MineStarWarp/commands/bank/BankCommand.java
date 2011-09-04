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

package com.minestar.MineStarWarp.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.commands.ExtendedCommand;

public class BankCommand extends ExtendedCommand {

    public BankCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    /**
     * Representing the command <br>
     * /bank (<PlayerName>) <br>
     * This teleports a player to his bank or a player to the bank of the player
     * 
     * @param player
     *            Called the command
     * @param args
     *            (args[0] is the players name(not command caller!) who can use the bank)
     */
    public void execute(String[] args, Player player) {

        Location bank = null;

        if (args.length == 0) {
            bank = Main.bankManager.getBank(player.getName());
            if (bank != null) {
                player.teleport(bank);
                player.sendMessage("Welcome to your bank!");
            }
            else {
                player.sendMessage(ChatColor.RED
                        + "Sorry, you don't have a bank!");
            }
        }
        else {
            if (!UtilPermissions.playerCanUseCommand(player,
                    "minestarwarp.commands.bankSpecific")) {
                player.sendMessage(NO_RIGHT);
                return;
            }

            bank = Main.bankManager.getBank(args[0]);
            if (bank != null) {
                player.teleport(bank);
                player.sendMessage("You teleported to bank of '" + args[0]
                        + "'");
            }
            else {
                player.sendMessage(ChatColor.RED
                        + "Sorry, no bank was found for '" + args[0] + "'");
            }
        }
    }
}
