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
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
import de.minestar.MineStarWarp.commands.warp.RenameCommand;
import de.minestar.MineStarWarp.commands.warp.SearchCommand;
import de.minestar.MineStarWarp.commands.warp.UninviteCommand;
import de.minestar.MineStarWarp.commands.warp.WarpToCommand;
import de.minestar.MineStarWarp.dataManager.BackManager;
import de.minestar.MineStarWarp.dataManager.BankManager;
import de.minestar.MineStarWarp.dataManager.HomeManager;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.MineStarWarp.database.DatabaseManager;
import de.minestar.MineStarWarp.listeners.PlayerTeleportListener;
import de.minestar.minestarlibrary.commands.Command;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class Core extends JavaPlugin {

    public static final String NAME = "MineStarWarp";

    public static WarpManager warpManager;
    public static HomeManager homeManager;
    public static BankManager bankManager;
    public static BackManager backManager;
    public static DatabaseManager dbManager;

    public static ArrayList<String> respawn;

    private CommandList commandList;

    public static FileConfiguration config;

    public void onDisable() {
        dbManager.closeConnection();
        dbManager = null;
        warpManager = null;
        homeManager = null;
        bankManager = null;
        commandList = null;
        backManager = null;
        respawn = null;
        ChatUtils.printConsoleInfo("Disabled!", NAME);
    }

    public void onEnable() {

        checkConfig();

        dbManager = new DatabaseManager(Core.NAME, getDataFolder());
        warpManager = new WarpManager(dbManager, config);
        homeManager = new HomeManager(dbManager);
        bankManager = new BankManager(dbManager);
        backManager = new BackManager();
        respawn = new ArrayList<String>();

        initCommands();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerTeleportListener(), this);

        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled", NAME);
    }

    private void initCommands() {
        //@formatter:off
        commandList = new CommandList(new Command[] {

                // Teleport Commands
                new TeleportHereCommand("/tphere", "<Player>", "tphere"),
                new TeleportToCommand("/tp", "<Player>", "tpTo"),

                // Home
                new SetHomeCommand("/sethome", "", "sethome"),
                new HomeCommand("/home", "", "home"),

                // Bank
                new BankCommand("/bank", "", "bank",
                        new BankListCommand("list", "", "bankList")),
                new SetBankCommand("/setbank", "<Player>", "setBank"),

                // Back
                new BackCommand("/back", "", "back"),

                // Warp Command
                new WarpToCommand("/warp", "<Name>", "warpTo",
                        new Command[] {
                                // Warp Creation, Removing, Moving and
                                // Renameing.
                                new CreateCommand("create", "<Name>", "create"),
                                new CreateCommand("pcreate", "<Name>", "create"),
                                new DeleteCommand("delete", "<Name>", "delete"),
                                new MoveCommand("move", "<Name>", "move"),
                                new RenameCommand("rename", "<Oldname> <Newname>", "rename"),
        
                                // Searching Warps
                                new ListCommand("list", "", "list"),
                                new SearchCommand("search", "<Name>", "search"),
        
                                // Modifiers
                                new PrivateCommand("private", "<Name>", "private"),
                                new PublicCommand("public", "<Name>", "public"),
        
                                // Guests
                                new InviteCommand("invite", "<PlayerName> <Warpname>", "invite"),
                                new UninviteCommand("uninvite", "<PlayerName> <Warpname>", "uninvite"),
                                new GuestListCommand("guestlist", "<WarpName>", "guestlist")
                        }
                )
        });
        //@formatter:on
    }
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    /**
     * Load the properties from the configFile. If the configFile not exists it
     * create ones
     */
    private void checkConfig() {

        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        File configFile = new File(dataFolder, "config.yml");

        if (!configFile.exists()) {
            createConfig();
        }

        config = getConfig();
    }

    /**
     * Creates a config and use default values for it. They are stored in a yml
     * format.
     */
    public void createConfig() {

        config = getConfig();
        config.addDefault("warps.default", 0);
        config.addDefault("warps.probe", 2);
        config.addDefault("warps.free", 5);
        config.addDefault("warps.pay", 9);
        config.addDefault("warps.warpsPerPage", 8);

        config.addDefault("banks.banksPerPage", 10);

        config.addDefault("home.setHomeUsingBed", true);

        config.addDefault("language", "de");

        config.options().copyDefaults(true);
        saveConfig();
    }
}
