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

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.commands.Command;

public class SearchCommand extends Command {

    public SearchCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    public void execute(String[] args, Player player) {
        // int pageNumber = 0;
        // // when player forgets to add a page number show the first site
        // if (split.length == 1)
        // pageNumber = 1;
        // else if (split[1].matches("\\d*")) {
        // try {
        // pageNumber = Integer.parseInt(split[1]);
        // }
        // catch (NumberFormatException e) {
        // Main.writeToLog("User " + player.getDisplayName()
        // + " used a wrong number for /warp list " + split[1]);
        //
        // }
        //
        // // TODO show warps of the pageNumber
        // }
        // else if (split[1].equals("my")) {
        // // TODO show all warps the player has created
        // }

    }
}
