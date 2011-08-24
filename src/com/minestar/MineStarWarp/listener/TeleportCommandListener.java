package com.minestar.MineStarWarp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

import com.minestar.MineStarWarp.Main;

public class TeleportCommandListener extends PlayerListener {

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
            if (split.length == 1)
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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

}
