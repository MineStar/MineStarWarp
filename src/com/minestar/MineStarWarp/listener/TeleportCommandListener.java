package com.minestar.MineStarWarp.listener;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

import com.minestar.MineStarWarp.Main;

public class TeleportCommandListener extends PlayerListener {

    private final Server server;

    public TeleportCommandListener(Server server) {
        this.server = server;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        String message = event.getMessage();

        // used for split and to know, which command to use
        int tempNumber = 0;
        if (message.startsWith("/tphere"))
            tempNumber = 8;
        else if (message.startsWith("/tp"))
            tempNumber = 4;
        // not ours
        else
            return;

        final Player player = event.getPlayer();
        String[] split = message.substring(tempNumber).split(" ");

        // /tphere <name> -> teleports the player to the command caller
        if (tempNumber == 8) {
            handleTeleportHere(player, split);
        }
        else {
            // /tp <name> -> teleports the command caller to the player
            if (split.length != 2)
                handleTeleportToTarget(player, split);
            // /tp <name> <name> -> teleports first player to the second player
            else
                handlePlayerTeleportToPlayer(player, split);
        }
    }

    /**
     * Representing the command <br>
     * /tphere PLAYERNAME <br>
     * This teleports the target to the command caller (the player it self)
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[0] is the targets name
     */
    private void handleTeleportHere(Player player, String[] split) {

        // TODO Implement permission!

        // only /tphere without any arguments
        if (split.length == 0) {
            player.sendMessage("/tphere <PlayerName> - Teleports the player to your location");
            return;
        }
        Player target = server.getPlayer(split[0]);
        if (target == null) {
            player.sendMessage("Can't find player named " + split[0]
                    + ". Maybe he is offline?");
            return;
        }
        target.teleport(player.getLocation());
    }

    /**
     * Representing the command <br>
     * /tp PLAYERNAME <br>
     * This teleports the command caller to the target
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[0] is the targets name
     */
    private void handleTeleportToTarget(Player player, String[] split) {

        // TODO Implement permission!

        // only /tphere without any arguments
        if (split.length == 0) {
            player.sendMessage("/tp <PlayerName> - Teleports you to the player");
            return;
        }
        Player target = server.getPlayer(split[0]);
        if (target == null) {
            player.sendMessage("Can't find player named " + split[0]
                    + ". Maybe he is offline?");
            return;
        }
        player.teleport(target.getLocation());
    }

    /**
     * Representing the command <br>
     * /tphere PLAYERNAME TARGETSNAME <br>
     * This teleports first player to the other player
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[0] is the player to teleport name <br>
     *            split[1] is the targets name
     * 
     */
    private void handlePlayerTeleportToPlayer(Player player, String[] split) {
        
        // TODO Implement permission!
        
        Player playerToTeleport = server.getPlayer(split[0]);
        if (playerToTeleport == null) {
            player.sendMessage("Can't find player named "+split[0]+". Maybe he is offline?");
            return;
        }
        Player target           = server.getPlayer(split[1]);
        if (target == null) {
            player.sendMessage("Can't find player named "+split[1]+". Maybe he is offline?");
            return;
        }
        playerToTeleport.teleport(target.getLocation());

    }
}
