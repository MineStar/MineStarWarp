/*
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

package com.minestar.MineStarWarp.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.commands.Command;
import com.minestar.MineStarWarp.utils.PlayerUtil;

public class TeleportHereCommand extends Command {

    public TeleportHereCommand(String syntax, String arguments, String node, Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization.get(TELEPORT_HERE_DESCRIPTION);
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
     *            split[0] is the targets name
     */
    public void execute(String[] args, Player player) {

        Player target = PlayerUtil.getPlayer(server, args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + Main.localization.get(TELEPORT_HERE_PLAYER_NOT_FOUND, args[0]));
            return;
        }
        player.sendMessage(ChatColor.AQUA + Main.localization.get(TELEPORT_HERE_TARGET_MESSAGE, target.getName()));
        target.sendMessage(ChatColor.AQUA + Main.localization.get(TELEPORT_HERE_TARGET_TELEPORTED, player.getName()));
        target.teleport(player.getLocation());
    }
}
