package com.minestar.MineStarWarp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

import com.minestar.MineStarWarp.Main;

public class ChatCommandListener extends PlayerListener {

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        String message = event.getMessage();
        // not ours
        if (!message.startsWith("/warp))"))
            return;

        final Player player = event.getPlayer();
        String[] split = message.substring(6).split(" ");
        // /warp create <name> -> creates a new public warp
        if (split[0].equals("create")) {
            handleCreate(player, split);
        }

        // /warp pcreacte <name> -> creates a new private warp
        else if (split[0].equals("pcreate")) {
            handlePCreate(player, split);
        }
        // /warp delete <name> -> deletes a warp but only the owner can do this!
        else if (split[0].equals("delete")) {
            handleDelete(player, split);
        }
        // / warp list # -> shows the player warps of the given page number
        else if (split[0].equals("list")) {
            handleList(player, split);
        }
        // /warp invite <player> <warpname> -> add the mentioned player to the
        // guests's list
        else if (split[0].equals("invite")) {
            handleInvite(player, split);
        }
        // /warp uninvite <player> <warpname> -> removes the mentioned player
        // from the guests's list
        else if (split[0].equals("uninvite")) {
            handleUninvite(player, split);
        }
        // /warp public <warpname> -> converts the Warp to public access
        else if (split[0].equals("public")) {
            handlePublic(player, split);
        }
        // /warp private <warpname> -> converts the Warp to private access
        else if (split[0].equals("private")) {
            handlePrivate(player, split);
        }
        // /warp <warpname> -> warps the player to the warp
        else if (split.length == 2) {
            handleWarp(player, split);
        }
        // /warp -> show help message
        else {
            handleShowHelp(player);
        }
    }

    /**
     * Representing the command <br>
     * /warp create WARPNAME <br>
     * This creates a public warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the WARPNAME
     */
    private void handleCreate(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp pcreate WARPNAME <br>
     * This creates a private warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the WARPNAME
     */
    private void handlePCreate(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp delete WARPNAME <br>
     * This deletes a Warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the WARPNAME
     */
    private void handleDelete(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp list NUMBER <br>
     * This sends the player a list of warps of the pagenumber
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the NUMBER
     */
    private void handleList(final Player player, String[] split) {
        int pageNumber = 0;
        // when player forgets to add a page number show the first site
        if (split.length == 1)
            pageNumber = 1;
        else if (split[1].matches("\\d*")) {
            try {
                pageNumber = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException e) {
                Main.writeToLog("User " + player.getDisplayName()
                        + " used a wrong number for /warp list " + split[1]);

            }

            // TODO show warps of the pageNumber
        }
        else if (split[1].equals("my")) {
            // TODO show all warps the player has created
        }
    }

    /**
     * Representing the command <br>
     * /warp invite PLAYERNAME WARPNAME <br>
     * This allows the mentioned player to use the warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the PLAYERNAME <br>
     *            split[2] is the WARPNAME
     */
    private void handleInvite(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp uninvite PLAYERNAME WARPNAME <br>
     * The disallows the mentioned player to use the warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the PLAYERNAME <br>
     *            split[2] is the WARPNAME
     */
    private void handleUninvite(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp public NAME <br>
     * This converts the mentioned Warp to a public warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the WARPNAME
     */
    private void handlePublic(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp private NAME <br>
     * This converts the mentioned Warp to a private warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the WARPNAME
     */
    private void handlePrivate(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp WARPNAME <br>
     * This warps the player to the mentioned Warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            split[1] is the WARPNAME
     */
    private void handleWarp(Player player, String[] split) {
        // TODO Auto-generated method stub

    }

    /**
     * Representing the command <br>
     * /warp <br>
     * This sends the player a help message
     * 
     * @param player
     *            Called the command
     */
    private void handleShowHelp(Player player) {
        // TODO Auto-generated method stub

    }
}
