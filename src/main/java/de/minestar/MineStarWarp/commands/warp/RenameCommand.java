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
import de.minestar.minestarlibrary.utils.ChatUtils;

public class RenameCommand extends AbstractCommand {

    private WarpManager wManager;

    public RenameCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Benennt einen Warp um";
        this.wManager = wManager;
    }

    @Override
    /**
     * Representing the command <br>
     * /warp rename<br>
     * Chance the name of a warp.
     */
    public void execute(String[] args, Player player) {

        String oldName = args[0];

        if (!wManager.isWarpExisting(oldName)) {
            ChatUtils.printError(player, pluginName, "Der Warp '" + oldName + "' existiert nicht!");
            return;
        }

        String newName = args[1];
        if (wManager.isWarpExisting(newName)) {
            ChatUtils.printError(player, pluginName, "Der Warp '" + newName + "' existiert bereits!");
            return;
        }

        if (wManager.isKeyWord(newName)) {
            ChatUtils.printError(player, pluginName, "Der Name '" + newName + "' kann nicht verwendet werden! Bitte benutze einen anderen!");
            return;
        }

        if (wManager.getWarp(oldName).canEdit(player))
            wManager.renameWarp(player, oldName, newName);
        else
            ChatUtils.printError(player, pluginName, "Du darfst den Warp '" + oldName + "' nicht umbennen!");
    }
}
