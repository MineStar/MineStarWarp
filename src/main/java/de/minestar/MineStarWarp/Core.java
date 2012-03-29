/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of MineStarWarp.
 * 
 * MineStarWarp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MineStarWarp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MineStarWarp.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.MineStarWarp;

import java.io.File;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import de.minestar.MineStarWarp.commands.back.BackCommand;
import de.minestar.MineStarWarp.commands.bank.BankCommand;
import de.minestar.MineStarWarp.commands.bank.BankListCommand;
import de.minestar.MineStarWarp.commands.bank.SetBankCommand;
import de.minestar.MineStarWarp.commands.home.HomeCommand;
import de.minestar.MineStarWarp.commands.home.SetHomeCommand;
import de.minestar.MineStarWarp.commands.teleport.TeleportHereCommand;
import de.minestar.MineStarWarp.commands.teleport.TeleportToCommand;
import de.minestar.MineStarWarp.commands.warp.CreateCommand;
import de.minestar.MineStarWarp.commands.warp.DeleteCommand;
import de.minestar.MineStarWarp.commands.warp.GuestListCommand;
import de.minestar.MineStarWarp.commands.warp.InviteCommand;
import de.minestar.MineStarWarp.commands.warp.ListCommand;
import de.minestar.MineStarWarp.commands.warp.MoveCommand;
import de.minestar.MineStarWarp.commands.warp.PrivateCommand;
import de.minestar.MineStarWarp.commands.warp.PublicCommand;
import de.minestar.MineStarWarp.commands.warp.RandomCommand;
import de.minestar.MineStarWarp.commands.warp.RenameCommand;
import de.minestar.MineStarWarp.commands.warp.SearchCommand;
import de.minestar.MineStarWarp.commands.warp.UninviteCommand;
import de.minestar.MineStarWarp.commands.warp.WarpToCommand;
import de.minestar.MineStarWarp.dataManager.BackManager;
import de.minestar.MineStarWarp.dataManager.BankManager;
import de.minestar.MineStarWarp.dataManager.HomeManager;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.MineStarWarp.database.DatabaseManager;
import de.minestar.MineStarWarp.listeners.TeleportListener;
import de.minestar.MineStarWarp.listeners.SignListener;
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class Core extends AbstractCore {

    public static final String NAME = "MineStarWarp";

    /** Manager */
    private WarpManager warpManager;
    private HomeManager homeManager;
    private BankManager bankManager;
    private BackManager backManager;
    private DatabaseManager dbManager;

    /** Listener */
    private Listener teleportListener, signListener;

    @Override
    protected boolean createManager() {

        MinestarConfig config = loadConfig();
        if (config == null)
            return false;

        dbManager = new DatabaseManager(Core.NAME, getDataFolder());
        if (!dbManager.hasConnection())
            return false;

        warpManager = new WarpManager(dbManager, config);
        homeManager = new HomeManager(dbManager);
        bankManager = new BankManager(dbManager, config);
        backManager = new BackManager();

        return true;
    }

    @Override
    protected boolean createListener() {

        teleportListener = new TeleportListener(backManager);
        signListener = new SignListener(warpManager);

        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {

        pm.registerEvents(teleportListener, this);
        pm.registerEvents(signListener, this);

        return true;
    }

    @Override
    protected boolean createCommands() {

        //@formatter:off
        cmdList = new CommandList(
                
                new TeleportHereCommand(    "/tphere",          "<Player>",                 "minestarwarp.command.tphere"),
                new TeleportToCommand(      "/tp",              "<Player>",                 "minestarwarp.command.tpTo"),

                new SetHomeCommand(         "/sethome",         "",                         "minestarwarp.command.sethome", homeManager),
                new HomeCommand(            "/home",            "",                         "minestarwarp.command.home", homeManager),

                new BackCommand(            "/back",            "",                         "minestarwarp.command.back", backManager),

                new WarpToCommand       (   "/warp",            "<Name>",                   "minestarwarp.command.warpTo", warpManager,

                        new RandomCommand(      "random",       "",                         "minestarwarp.command.random", warpManager),
                        new CreateCommand(      "create",       "<Name>",                   "minestarwarp.command.create", warpManager),
                        new CreateCommand(      "pcreate",      "<Name>",                   "minestarwarp.command.create", warpManager),
                        new DeleteCommand(      "delete",       "<Name>",                   "minestarwarp.command.delete", warpManager),
                        new MoveCommand(        "move",         "<Name>",                   "minestarwarp.command.move", warpManager),
                        new RenameCommand(      "rename",       "<Oldname> <Newname>",      "minestarwarp.command.rename", warpManager),

                        new ListCommand(        "list",         "",                         "minestarwarp.command.list", warpManager),
                        new SearchCommand(      "search",       "<Name>",                   "minestarwarp.command.search", warpManager),

                        new PrivateCommand(     "private",      "<Name>",                   "minestarwarp.command.private", warpManager),
                        new PublicCommand(      "public",       "<Name>",                   "minestarwarp.command.public", warpManager),

                        new InviteCommand(      "invite",       "<PlayerName> <Warpname>",  "minestarwarp.command.invite", warpManager),
                        new UninviteCommand(    "uninvite",     "<PlayerName> <Warpname>",  "minestarwarp.command.uninvite", warpManager),
                        new GuestListCommand(   "guestlist",    "<WarpName>",               "minestarwarp.command.guestlist", warpManager)
                ),

                new BankCommand(            "/bank",            "",                         "minestarwarp.command.bank", bankManager,
                        new BankListCommand(    "list",         "",                         "minestarwarp.command.bankList", bankManager)
                ),

                new SetBankCommand(         "/setbank",         "<Player>",                 "minestarwarp.command.setBank", bankManager)
        );
        //@formatter:on

        return true;
    }

    @Override
    protected boolean commonDisable() {

        dbManager.closeConnection();
        if (dbManager.hasConnection())
            return false;

        dbManager = null;
        warpManager = null;
        homeManager = null;
        bankManager = null;
        cmdList = null;
        backManager = null;

        return true;
    }

    public MinestarConfig loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            // copy default one from .jar
            if (!configFile.exists()) {
                ConsoleUtils.printWarning(NAME, "Can't find " + configFile + ", creating a default configuration");
                MinestarConfig.copyDefault(this.getClass().getResourceAsStream("/config.yml"), configFile);
            }
            // load config and check version tag
            return new MinestarConfig(configFile, NAME, getDescription().getVersion());
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Can't load configuration file!");
            return null;
        }
    }
}
