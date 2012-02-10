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

public class PrivateCommand extends AbstractCommand {

    private WarpManager wManager;

    public PrivateCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Wandelt einen öffentlichen in  einen privaten Warp um";
        this.wManager = wManager;
    }

    @Override
    /**
     * Representing the command <br>
     * /warp private <br>
     * This converts a warp to a private warp, so only the creator or an admin can use it
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the warp name
     */
    public void execute(String[] args, Player player) {

        String warpName = args[0];
        Warp warp = wManager.getWarp(warpName);

        if (warp == null) {
            PlayerUtils.sendError(player, pluginName, "Der Warp '" + warpName + "' existiert nicht!");
            if (wManager.getWarp(warpName.toLowerCase()) != null)
                PlayerUtils.sendInfo(player, pluginName, "Vielleicht meintest du den Warp '" + warpName.toLowerCase() + "'?");
            return;
        }

        if (!warp.canEdit(player)) {
            PlayerUtils.sendError(player, pluginName, "Du kannst diesen Warp nicht privat machen!");
            return;
        }

        if (!warp.isPublic()) {
            PlayerUtils.sendError(player, pluginName, "Der Warp '" + warpName + "' ist bereits privat!");
            return;
        }

        wManager.changeAccess(player, false, warpName);
    }
}
