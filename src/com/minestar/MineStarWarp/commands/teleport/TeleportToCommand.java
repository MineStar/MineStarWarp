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

package com.minestar.MineStarWarp.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.commands.ExtendedCommand;
import com.minestar.MineStarWarp.utils.PlayerUtil;

public class TeleportToCommand extends ExtendedCommand {

    public TeleportToCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
        this.description = Main.localization
                .get("teleportToCommand.description");
    }

    @Override
    /**
     * Representing the command <br>
     * /tphere PLAYERNAME (TARGETSNAME) <br>
     * This teleports first player to the other player
     * 
     * @param player
     *            Called the command
     * @param args
     *            args[0] is the player to teleport OR addionatiol <br>
     *            args[1] is the targets name
     * 
     */
    public void execute(String[] args, Player player) {
        if (args.length == 1)
            teleportToPlayer(args, player);
        else if (args.length == 2)
            teleportPlayerToPlayer(args, player);
        else if (args.length == 3 || args.length == 4)
            teleportToCoords(args, player);
        else
            player.sendMessage(getHelpMessage());
    }

    private void teleportToCoords(String[] args, Player player) {

        if (!UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command.tptocoords")) {
            player.sendMessage(NO_RIGHT);
            return;
        }

        double[] coords = new double[3];
        try {
            coords[0] = Double.parseDouble(args[0]);
            coords[1] = Double.parseDouble(args[1]);
            coords[2] = Double.parseDouble(args[2]);
        }
        catch (Exception e) {
            player.sendMessage(ChatColor.BLUE + "/tp X Y Z ");
            return;
        }
        String worldName;

        if (args.length == 4)
            worldName = args[4];
        else
            worldName = player.getWorld().getName();

        player.teleport(new Location(server.getWorld(worldName), coords[0],
                coords[1], coords[2]));
        player.sendMessage(ChatColor.AQUA
                + Main.localization.get("teleportToCoords.description"));
    }

    private void teleportPlayerToPlayer(String[] args, Player player) {

        if (!UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command.tpPlayerTo")) {
            player.sendMessage(NO_RIGHT);
            return;
        }

        Player playerToTeleport = PlayerUtil.getPlayer(server, args[0]);
        if (playerToTeleport == null) {
            player.sendMessage(Main.localization.get(
                    "teleportToCommand.playerNotFound", args[0]));
            return;
        }
        Player target = PlayerUtil.getPlayer(server, args[1]);
        if (target == null) {
            player.sendMessage(Main.localization.get(
                    "teleportToCommand.playerNotFound", args[1]));
            return;
        }
        playerToTeleport.teleport(target.getLocation());
        playerToTeleport.sendMessage(ChatColor.AQUA
                + Main.localization.get("teleportToCommand.targetInformation",
                        target.getName()));
        target.sendMessage(ChatColor.AQUA
                + Main.localization.get("teleportToCommand.targetTeleported",
                        player.getName()));
        player.sendMessage(ChatColor.AQUA
                + Main.localization.get("teleportToCommand.teleportSucess",
                        playerToTeleport.getName(), target.getName()));

    }

    private void teleportToPlayer(String[] args, Player player) {

        if (!UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command.tpTo")) {
            player.sendMessage(NO_RIGHT);
            return;
        }

        Player target = PlayerUtil.getPlayer(server, args[0]);
        if (target == null) {
            player.sendMessage(Main.localization.get(
                    "teleportToCommand.playerNotFound", args[0]));
            return;
        }
        player.teleport(target.getLocation());
        player.sendMessage(ChatColor.AQUA
                + Main.localization.get("teleportToCommand.targetInformation",
                        target.getName()));
    }

    @Override
    protected boolean hasRights(Player player) {
        return UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command.tpPlayerTo")
                || UtilPermissions.playerCanUseCommand(player,
                        "minestarwarp.command.tpTo");
    }
}
