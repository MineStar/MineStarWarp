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
import de.minestar.MineStarWarp.Warp;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.minestarlibrary.commands.Command;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class CreateCommand extends Command {

    private WarpManager wManager;

    public CreateCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Erstellt einen privaten Warp";
        this.wManager = wManager;
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
            ChatUtils.printError(player, pluginName, "Der Name '" + warpName + "' kann nicht verwendet werden. Bitte nutz einen anderen!");
            return;
        }
        // TODO: Anzeigen wieviele Warps ein user noch frei hat
        if (wManager.hasFreeWarps(player)) {
            if (!wManager.isWarpExisting(warpName))
                wManager.addWarp(player, warpName, new Warp(player));
            else
                ChatUtils.printError(player, pluginName, "Es existiert bereits ein Warp names '" + warpName + "'!");
        } else
            ChatUtils.printError(player, warpName, "Du hast zu viele private Warps erstellt, um einen neuen erstellen zu k�nnen. L�sche bitte einen alten.");
    }
}
