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

package com.minestar.MineStarWarp.commands.back;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.commands.Command;

public class BackCommand extends Command {
    public BackCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    /**
     * Representing the command <br>
     * /back <br>
     * This teleports a player back to the location befor the last teleport.
     * 
     * @param player
     *            Called the command
     * @param args
     *            Hasn't any effect on the Command
     */
    public void execute(String[] args, Player player) {
        Location loc = Main.backManager.getBack(player);
        if (loc != null)
            player.teleport(loc);
        else
            player.sendMessage(ChatColor.RED
                    + Main.localization.get("backCommand.backNotExist"));
    }
}
