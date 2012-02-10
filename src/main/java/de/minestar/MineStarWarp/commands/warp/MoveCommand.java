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
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class MoveCommand extends AbstractCommand {

    private WarpManager wManager;

    public MoveCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Verschiebt den Warp an deine Position";
        this.wManager = wManager;
    }

    @Override
    /**
     * Representing the command <br>
     * /warp move <br>
     * Change the Location of a warp.
     */
    public void execute(String[] args, Player player) {

        String warpName = args[0];
        if (!wManager.isWarpExisting(warpName)) {
            PlayerUtils.sendError(player, pluginName, "Der Warp '" + warpName + "' existiert nicht!");
            return;
        }
        Warp warp = wManager.getWarp(args[0]);
        if (!warp.canEdit(player)) {
            PlayerUtils.sendError(player, pluginName, "Du darfst diesen Warp nicht verschieben!");
            return;
        }

        wManager.updateWarp(player, args[0]);
    }
}
