package com.minestar.MineStarWarp.commands;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.gemo.utils.UtilPermissions;

public abstract class Command {

    public final static String NO_RIGHT = "You aren't allowed to use this command!";
    public final Server server;

    private String description;
    private String syntax;
    private String arguments;
    private String node;

    public Command(String syntax, String arguments, String node, Server server) {
        this.syntax = syntax;
        this.arguments = arguments;
        this.server = server;
        this.node = node;
    }

    public void run(String[] args, Player player) {
        if (!hasRights(player)) {
            player.sendMessage(NO_RIGHT);
            return;
        }

        if (!hasCorrectSyntax(args)) {
            player.sendMessage(getSyntax() + " " + getArguments() + " "
                    + getDescription());
            return;
        }

        execute(args, player);
    }

    public abstract void execute(String[] args, Player player);

    public final boolean hasRights(Player player) {
        return UtilPermissions.playerCanUseCommand(player,
                "minestarwarp.command." + getNode());
    }

    public final boolean hasCorrectSyntax(String[] args) {
        return args.length == countArguments();
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public String getArguments() {
        return arguments;
    }

    public String getNode() {
        return node;
    }

    private int countArguments() {
        int counter = 0;
        for (int i = 0; i < arguments.length(); ++i)
            if (arguments.charAt(i) == '<')
                ++counter;
        return counter;
    }

}
