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

package de.minestar.MineStarWarp.commands.home;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.dataManager.HomeManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class HomeCommand extends AbstractCommand {

    private HomeManager hManager;

    public HomeCommand(String syntax, String arguments, String node, HomeManager hManager) {
        super(Core.NAME, syntax, arguments, node);
        this.hManager = hManager;
    }

    @Override
    /**
     * Representing the command <br>
     * /home <br>
     * This teleports the player to his home(if set)
     * 
     * @param player
     *            Called the command
     * @param args
     *            Must be empty!
     */
    public void execute(String[] args, Player player) {

        // When the player didn't have set a home, it returns null
        Location homeLocation = hManager.getPlayersHome(player);
        if (homeLocation != null) {
            player.teleport(homeLocation);
            ChatUtils.printSuccess(player, pluginName, "Willkommen zu Hause!");
        } else
            ChatUtils.printError(player, pluginName, "Du besitzt kein Zuhause! Benutze /setHome um eines zu erstellen.");
    }
}
