package com.minestar.MineStarWarp.commands;

import org.bukkit.Server;

import com.minestar.MineStarWarp.commands.teleport.*;
import com.minestar.MineStarWarp.commands.warp.*;

public class CommandList {
    
    private final Command[] commands;
    
    public CommandList(Server server) {
        commands = new Command[]{
                // Teleport Commands
                new TeleportHereCommand("/tphere","<Player>", server),
                new TeleportPlayerToCommand("/tp","<Player> <Player>", server),
                new TeleportToCommand("/tp", "<Player>", server),
                
                // Warp Command
                new WarpToCommand("/warp pcreate","<Name>", server),
                
                // Warp Creation and Removing 
                new CreateCommand("/warp create","<Name>", server),
                new CreateCommand("/warp pcreate","<Name>", server),
                new DeleteCommand("/warp pcreate","<Name>", server),
                
                // Searching Warps
                new ListCommand("/warp pcreate","<Name>", server),
                new SearchCommand("/warp search","<Name>", server),
                
                // Modifiers
                new PrivateCommand("/warp pcreate","<Name>", server),
                new PublicCommand("/warp pcreate","<Name>", server),                
                
                // Guests
                new InviteCommand("/warp pcreate","<Name>", server),
                new UninviteCommand("/warp pcreate","<Name>", server)
        };
    }
    
}
