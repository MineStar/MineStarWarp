package com.minestar.MineStarWarp.commands.teleport;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.commands.Command;

public class TeleportToCommand extends Command {

    public TeleportToCommand(String syntax, String arguments,  Server server) {
        super(syntax, arguments, server);
    }

    @Override
    /**
     * Representing the command <br>
     * /tp PLAYERNAME <br>
     * This teleports the command player to the target
     * 
     * @param player
     *            Called the command
     * @param args
     *            args[0] is the targets name
     */
    public void execute(String[] args, Player player) {
        
        Player target = server.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("Can't find player named " + args[0]
                    + ". Maybe he is offline?");
            return;
        }
        player.teleport(target.getLocation());
    }

    @Override
    public boolean hasRights(Player player) {
        return UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command.tpTo");
    }
}
