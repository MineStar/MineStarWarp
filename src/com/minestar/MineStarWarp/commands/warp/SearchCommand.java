package com.minestar.MineStarWarp.commands.warp;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.commands.Command;

public class SearchCommand extends Command {

    public SearchCommand(String syntax, String arguments,  Server server) {
        super(syntax, arguments, server);
    }

    @Override
    public void execute(String[] args, Player player) {
//        int pageNumber = 0;
//        // when player forgets to add a page number show the first site
//        if (split.length == 1)
//            pageNumber = 1;
//        else if (split[1].matches("\\d*")) {
//            try {
//                pageNumber = Integer.parseInt(split[1]);
//            }
//            catch (NumberFormatException e) {
//                Main.writeToLog("User " + player.getDisplayName()
//                        + " used a wrong number for /warp list " + split[1]);
//
//            }
//
//            // TODO show warps of the pageNumber
//        }
//        else if (split[1].equals("my")) {
//            // TODO show all warps the player has created
//        }
        
    }

    @Override
    public boolean hasRights(Player player) {
        return UtilPermissions.playerCanUseCommand(player, "minestarwarp.command.search");
    }
}
