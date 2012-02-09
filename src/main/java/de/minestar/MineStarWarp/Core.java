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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
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
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class Core extends JavaPlugin {

    public static final String NAME = "MineStarWarp";

    private WarpManager warpManager;
    private HomeManager homeManager;
    private BankManager bankManager;
    private BackManager backManager;
    private DatabaseManager dbManager;

    private CommandList commandList;

    public void onDisable() {
        dbManager.closeConnection();
        dbManager = null;
        warpManager = null;
        homeManager = null;
        bankManager = null;
        commandList = null;
        backManager = null;
        ChatUtils.printConsoleInfo("Disabled!", NAME);
    }

    public void onEnable() {

        YamlConfiguration config = checkConfig();

        dbManager = new DatabaseManager(Core.NAME, getDataFolder());
        warpManager = new WarpManager(dbManager, config);
        homeManager = new HomeManager(dbManager);
        bankManager = new BankManager(dbManager, config);
        backManager = new BackManager();

        initCommands();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerTeleportListener(backManager), this);

        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled", NAME);
    }

    private void initCommands() {
        //@formatter:off
        commandList = new CommandList(
                
                // Teleport Commands
                new TeleportHereCommand("/tphere", "<Player>", "minestarwarp.command.tphere"),
                new TeleportToCommand("/tp", "<Player>", "minestarwarp.command.tpTo"),

                // Home
                new SetHomeCommand("/sethome", "", "minestarwarp.command.sethome", homeManager),
                new HomeCommand("/home", "", "minestarwarp.command.home", homeManager),

                // Bank
                new BankCommand("/bank", "", "minestarwarp.command.bank", bankManager,
                        new BankListCommand("list", "", "minestarwarp.command.bankList", bankManager)
                ),

                new SetBankCommand("/setbank", "<Player>", "minestarwarp.command.setBank", bankManager),

                // Back
                new BackCommand("/back", "", "minestarwarp.command.back", backManager),

                // Warp Command
                new WarpToCommand("/warp", "<Name>", "minestarwarp.command.warpTo", warpManager,
                        
                        // Create,Delete,Move,Rename
                        new CreateCommand("create", "<Name>", "minestarwarp.command.create", warpManager),
                        new CreateCommand("pcreate", "<Name>", "minestarwarp.command.create", warpManager),
                        new DeleteCommand("delete", "<Name>", "minestarwarp.command.delete", warpManager),
                        new MoveCommand("move", "<Name>", "minestarwarp.command.move", warpManager),
                        new RenameCommand("rename", "<Oldname> <Newname>", "minestarwarp.command.rename", warpManager),

                        // Searching Warps
                        new ListCommand("list", "", "minestarwarp.command.list", warpManager),
                        new SearchCommand("search", "<Name>", "minestarwarp.command.search", warpManager),

                        // Modifiers
                        new PrivateCommand("private", "<Name>", "minestarwarp.command.private", warpManager),
                        new PublicCommand("public", "<Name>", "minestarwarp.command.public", warpManager),

                        // Guests
                        new InviteCommand("invite", "<PlayerName> <Warpname>", "minestarwarp.command.invite", warpManager),
                        new UninviteCommand("uninvite", "<PlayerName> <Warpname>", "minestarwarp.command.uninvite", warpManager),
                        new GuestListCommand("guestlist", "<WarpName>", "minestarwarp.command.guestlist", warpManager)
                )
        );
        //@formatter:on
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    public YamlConfiguration checkConfig() {
        YamlConfiguration config = null;
        try {
            File configFile = new File(getDataFolder(), "config.yml");
            config = new YamlConfiguration();
            if (!configFile.exists()) {
                configFile.createNewFile();
                ChatUtils.printConsoleWarning("Can't find config.yml. Plugin creates a default configuration and uses the default values.", NAME);
                config.load(configFile);
                config.set("warps.default", 0);
                config.set("warps.probe", 2);
                config.set("warps.free", 5);
                config.set("warps.pay", 9);
                config.set("warps.warrpsPerPage", 15);
                config.set("banks.banksPerPage", 15);
                config.save(configFile);
            }
            config.load(configFile);
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load configuration file!", NAME);
        }

        return config;
    }
}
