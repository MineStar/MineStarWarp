package com.minestar.MineStarWarp.commands.teleport;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.commands.Command;

public class TeleportHereCommand extends Command {

    public TeleportHereCommand(String syntax, String arguments, String node,
            Server server) {
        super(syntax, arguments, node, server);
    }

    @Override
    /**
     * Representing the command <br>
     * /tphere PLAYERNAME <br>
     * This teleports the target to the command player (the player it self)
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[0] is the targets name
     */
    public void execute(String[] args, Player player) {

        Player target = server.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("Can't find player named " + args[0]
                    + ". Maybe he is offline?");
            return;
        }
        target.teleport(player.getLocation());
    }
}
