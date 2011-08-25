package com.minestar.MineStarWarp.commands;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.commands.teleport.*;
import com.minestar.MineStarWarp.commands.warp.*;

public class CommandList {

    private static Command[] commands;

    public CommandList(Server server) {
        commands = new Command[] {
                // Teleport Commands
                new TeleportHereCommand("/tphere", "<Player>", "tphere", server),
                new TeleportPlayerToCommand("/tp", "<Player> <Player>",
                        "tpPlayerTo", server),
                new TeleportToCommand("/tp", "<Player>", "tpTo", server),

                // Warp Command
                new WarpToCommand("/warp pcreate", "<Name>", "warpTo", server),

                // Warp Creation and Removing
                new CreateCommand("/warp create", "<Name>", "create", server),
                new CreateCommand("/warp pcreate", "<Name>", "create", server),
                new DeleteCommand("/warp pcreate", "<Name>", "delete", server),

                // Searching Warps
                new ListCommand("/warp pcreate", "<Name>", "list", server),
                new SearchCommand("/warp search", "<Name>", "search", server),

                // Modifiers
                new PrivateCommand("/warp pcreate", "<Name>", "private", server),
                new PublicCommand("/warp pcreate", "<Name>", "public", server),

                // Guests
                new InviteCommand("/warp pcreate", "<Name>", "invite", server),
                new UninviteCommand("/warp pcreate", "<Name>", "uninvite",
                        server) };
    }

    public static void handleCommand(CommandSender sender, String label,
            String[] args) {
        if (!label.startsWith("/"))
            label = "/" + label;

        if (sender instanceof Player) {
            for (Command cmd : commands) {
                if (cmd.getSyntax().equalsIgnoreCase(label)
                        && cmd.hasCorrectSyntax(args)) {
                    cmd.run(args, (Player) sender);
                    return;
                }
            }
            ((Player) sender).sendMessage("command not found!");
        }
    }

}
