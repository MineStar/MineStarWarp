package com.minestar.MineStarWarp.commands.teleport;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.commands.Command;

public class TeleportPlayerToCommand extends Command {

    public TeleportPlayerToCommand(String syntax, String arguments,  Server server) {
        super(syntax, arguments, server);
    }

    @Override
    /**
     * Representing the command <br>
     * /tphere PLAYERNAME TARGETSNAME <br>
     * This teleports first player to the other player
     * 
     * @param player
     *            Called the command
     * @param args
     *            args[0] is the player to teleport name <br>
     *            args[1] is the targets name
     * 
     */
    public void execute(String[] args, Player player) {
        
        Player playerToTeleport = server.getPlayer(args[0]);
        if (playerToTeleport == null) {
            player.sendMessage("Can't find player named " + args[0]
                    + ". Maybe he is offline?");
            return;
        }
        Player target = server.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("Can't find player named " + args[1]
                    + ". Maybe he is offline?");
            return;
        }
        playerToTeleport.teleport(target.getLocation());
    }

    @Override
    public boolean hasRights(Player player) {
        return UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command.tpPlayerTo");
    }
}
