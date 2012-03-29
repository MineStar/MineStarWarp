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

package de.minestar.MineStarWarp.commands.teleport;

import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class TeleportHereCommand extends AbstractExtendedCommand {

    public TeleportHereCommand(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);

        this.description = "Teleportiert den Spieler zu deiner Position";
    }

    @Override
    /**
     * Representing the command <br>
     * /tphere PLAYERNAME <br>
     * This teleports the target to the command player (the player it self)
     * 
     * @param player
     *            Called the command
     * @param args
     *            split is the targets name
     */
    public void execute(String[] args, Player player) {

        for (String playerName : args) {

            Player target = PlayerUtils.getOnlinePlayer(playerName);
            if (target == null) {
                PlayerUtils.sendError(player, pluginName, "Spieler '" + playerName + "' nicht gefunden? Vielleicht ist er offline?");
                return;
            }
            PlayerUtils.sendSuccess(player, pluginName, "'" + target.getName() + "' wurde zu dir teleportiert!");
            PlayerUtils.sendSuccess(target, pluginName, "Du wurdest zu '" + player.getName() + "' teleportiert!");
            target.teleport(player.getLocation());
        }
    }
}
