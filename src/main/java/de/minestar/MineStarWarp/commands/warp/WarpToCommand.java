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

import java.util.Map.Entry;

import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.Warp;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.minestarlibrary.commands.Command;
import de.minestar.minestarlibrary.commands.SuperCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class WarpToCommand extends SuperCommand {

    private WarpManager wManager;

    public WarpToCommand(String syntax, String arguments, String node, WarpManager wManager, Command... subCommands) {
        super(Core.NAME, syntax, arguments, node, true, subCommands);
        this.description = "Teleportiert dich zu einem Warp";
        this.wManager = wManager;
    }

    @Override
    /**
     * Representing the command <br>
     * /warp <br>
     * This teleports the player to the warp with the same name or the first found with a similiar name
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the warp name
     */
    public void execute(String[] args, Player player) {

        String warpName = args[0];
        Entry<String, Warp> entry = wManager.getSimiliarWarp(warpName, player);
        if (entry == null) {
            ChatUtils.printError(player, pluginName, "Der Warp '" + warpName + "' wurde nicht gefunden!");
            return;
        }

        Warp warp = entry.getValue();
        if (warp.canUse(player)) {
            player.teleport(warp.getLoc());
            ChatUtils.printSuccess(player, pluginName, "Willkommen beim Warp '" + entry.getKey() + "'!");
        } else
            ChatUtils.printError(player, pluginName, "Du darfst den Warp '" + entry.getKey() + "' nicht nutzen!");
    }
}