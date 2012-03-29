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

package de.minestar.MineStarWarp.commands.warp;

import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class CreateCommand extends AbstractCommand {

    private WarpManager wManager;

    public CreateCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.wManager = wManager;

        this.description = "Erstellt einen privaten Warp";
    }

    @Override
    /**
     * Representing the command <br>
     * /warp create <br>
     * This creates a new warp at the location the player is at the moment.
     * Every warp is created private and must convert to public manuelly.
     * The player can only create a new warp when he haven't hit the maximum warp number (public warps does not count)
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the warp name
     */
    public void execute(String[] args, Player player) {

        String warpName = args[0];
        if (wManager.isKeyWord(warpName.toLowerCase())) {
            PlayerUtils.sendError(player, pluginName, "Der Name '" + warpName + "' kann nicht verwendet werden. Bitte nutz einen anderen!");
            return;
        }

        int freeWarp = wManager.getFreeWarpCount(player);
        // player has no free warps
        if (freeWarp == 0) {
            PlayerUtils.sendError(player, pluginName, "Du kannst keinen privaten Warp mehr erstellen! Lösche bitte einen alten Warp!");
            return;
        }
        if (wManager.isWarpExisting(warpName)) {
            PlayerUtils.sendError(player, pluginName, "Es existiert bereits ein Warp names '" + warpName + "'!");
            return;
        }
        wManager.addWarp(player, warpName, freeWarp);
    }
}
