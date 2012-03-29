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

public class InviteCommand extends AbstractCommand {

    private WarpManager wManager;

    public InviteCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.wManager = wManager;

        this.description = "Einem anderen Spieler erlauben, den Warp mitzubenutzen";
    }

    @Override
    /**
     * Representing the command <br>
     * /warp invite <br>
     * This allows the player to also use the private warp.
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the player name
     *            args[1] is the warp name
     */
    public void execute(String[] args, Player player) {

        String guestName = args[0];
        String warpName = args[1];
        Warp warp = wManager.getWarp(warpName);

        if (guestName.equalsIgnoreCase(player.getName())) {
            PlayerUtils.sendError(player, pluginName, "Du kannst dich nicht selber einladen!");
            return;
        }
        if (warp == null) {
            PlayerUtils.sendError(player, pluginName, "Warp '" + warpName + "' existiert nicht!");
            return;
        }
        if (warp.isPublic()) {
            PlayerUtils.sendError(player, pluginName, "Du kannst niemanden einladen, da der Warp öffentlich ist!");
            return;
        }
        if (!warp.canEdit(player)) {
            PlayerUtils.sendError(player, pluginName, "Du darfst zu dem Warp '" + warpName + "' niemanden einladen!");
            return;
        }
        Player guest = PlayerUtils.getOnlinePlayer(guestName);
        if (guest != null)
            guestName = guest.getName();
        else {
            guestName = PlayerUtils.getOfflinePlayerName(guestName);

            // PLAYER WAS NEVER ON THE SERVER -> DOES NOT EXIST!
            if (guestName == null) {
                player.sendMessage(ChatColor.RED + " Spieler '" + args[0] + "' existiert nicht!");
                return;
            }
        }
        if (wManager.addGuest(player, warpName, guestName) && guest != null)
            PlayerUtils.sendSuccess(guest, pluginName, "Du wurdest zu dem Warp '" + warpName + "' eingeladen!");
    }
}
