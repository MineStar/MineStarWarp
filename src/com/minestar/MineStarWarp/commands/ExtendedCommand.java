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
import org.bukkit.entity.Player;

public abstract class ExtendedCommand extends Command {

    public ExtendedCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    public abstract void execute(String[] args, Player player);

    @Override
    public void run(String[] args, Player player) {
        if (!super.hasRights(player)) {
            player.sendMessage(NO_RIGHT);
            return;
        }

        if (!this.hasCorrectSyntax(args)) {
            player.sendMessage(getHelpMessage());
            return;
        }

        execute(args, player);
    }

    @Override
    protected boolean hasCorrectSyntax(String[] args) {

        return args.length >= super.getArgumentCount();
    }

}
