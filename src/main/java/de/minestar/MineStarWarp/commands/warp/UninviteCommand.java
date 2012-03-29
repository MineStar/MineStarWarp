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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.Warp;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class UninviteCommand extends AbstractCommand {

    private WarpManager wManager;

    public UninviteCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.wManager = wManager;
        

        this.description = "Entfernt einen Spieler aus der Gaesteliste des Warps";
    }

    @Override
    /**
     * Representing the command <br>
     * /warp uninvite <br>
     * This disallows the player to use the private warp anymore
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the player name
     *            args[1] is the warps name
     */
    public void execute(String[] args, Player player) {

        String guestName = args[0];
        String warpName = args[1];
        Warp warp = wManager.getWarp(warpName);
        if (warp == null) {
            PlayerUtils.sendError(player, pluginName, "Der Warp '" + warpName + "' existiert nicht!");
            return;
        }

        if (warp.isPublic()) {
            player.sendMessage(ChatColor.RED + "Warp ist public und für alle Spieler zugänglich!");
            return;
        }

        if (!warp.canEdit(player)) {
            PlayerUtils.sendError(player, pluginName, "Du kannst bei diesem Warp niemanden aus der Gästeliste entfernen!");
            return;
        }

        Player guest = PlayerUtils.getOnlinePlayer(guestName);
        if (guest != null)
            guestName = guest.getName();
        else {
            guestName = PlayerUtils.getOfflinePlayerName(guestName);

            if (guestName == null) {
                PlayerUtils.sendError(player, pluginName, "Der Spieler '" + args[0] + "' existiert nicht!");
                return;
            }
        }
        if (wManager.removeGuest(player, warpName, guestName) && guest != null)
            PlayerUtils.sendError(guest, pluginName, "Du wurdest aus dem Warp '" + warpName + "' ausgeladen!");
    }
}
