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

package de.minestar.MineStarWarp.commands.back;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.dataManager.BackManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class BackCommand extends AbstractCommand {

    private BackManager backManager;

    public BackCommand(String syntax, String arguments, String node, BackManager backManager) {
        super(Core.NAME, syntax, arguments, node);
        this.backManager = backManager;
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
        Location loc = backManager.getBack(player);
        if (loc != null) {
            backManager.usedBack(player);
            player.teleport(loc);
            PlayerUtils.sendSuccess(player, pluginName, "Du wurdest an die letzte gespeicherte Position teleporiert!");
        } else
            PlayerUtils.sendError(player, pluginName, "Es gibt keinen Punkt zum Zur�ckwarpen!");
    }
}
