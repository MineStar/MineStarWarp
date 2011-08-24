package com.minestar.MineStarWarp.commands.warp;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.commands.Command;

public class InviteCommand extends Command {

    public InviteCommand(String syntax, String arguments,  Server server) {
        super(syntax, arguments, server);
    }

    @Override
    public void execute(String[] args, Player player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean hasRights(Player player) {
        return UtilPermissions.playerCanUseCommand(player, "minestarwarp.command.invite");
    }
}
