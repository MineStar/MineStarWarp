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

package com.minestar.MineStarWarp.dataManager;

import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import com.gemo.utils.UtilPermissions;
import com.minestar.MineStarWarp.Warp;

public class WarpManager {

    // Key = Name of Warp
    private TreeMap<String, Warp> warps;

    private final DatabaseManager dbManager;

    private static WarpManager instance;

    private final static int DEFAULTS = 0;
    private final static int PROBE = 1;
    private final static int FREE = 2;
    private final static int PAY = 3;

    private final int[] maximumWarps = new int[4];

    private WarpManager(DatabaseManager dbManager, Configuration config) {
        this.dbManager = dbManager;
        warps = dbManager.loadWarpsFromDatabase();

        maximumWarps[DEFAULTS] = config.getInt("warps.default", 0);
        maximumWarps[PROBE] = config.getInt("warps.probe", 2);
        maximumWarps[FREE] = config.getInt("warps.default", 5);
        maximumWarps[PAY] = config.getInt("warps.default", 9);
    }

    public static WarpManager getInstance(DatabaseManager dbManager,
            Configuration config) {
        if (instance == null)
            instance = new WarpManager(dbManager, config);
        return instance;
    }

    public boolean isWarpExisting(String name) {
        return warps.containsKey(name);
    }

    public void addWarp(Player creator, String name, Warp warp) {
        warps.put(name, warp);
        if (dbManager.addWarp(creator, name, warp)) {
            creator.sendMessage(ChatColor.AQUA + name
                    + "was sucessfully created!");
        }
        else {
            warps.remove(name);
            creator.sendMessage(ChatColor.RED
                    + "ERROR! Can't save the warp in the database! The warp was not created! Contact an admin!");
        }
    }

    public void deleteWarp(Player player, String name) {
        Warp warp = warps.remove(name);
        if (dbManager.deleteWarp(name))
            player.sendMessage(ChatColor.AQUA + name
                    + " was sucessfully deleted!");
        else {
            warps.put(name, warp);
            player.sendMessage(ChatColor.RED
                    + "ERROR! Can't delete the warp from the database! Warp was not deleted! Contact an admin!");
        }

    }

    public void addGuest(Player player, String warpName, String guest) {
        Warp warp = warps.get(warpName);
        warp.invitePlayer(guest);
        if (dbManager.changeGuestList(warp.getGuestsAsString()))
            player.sendMessage(ChatColor.AQUA + guest
                    + " was sucessfully invited into " + warpName);
        else {
            warp.uninvitePlayer(guest);
            player.sendMessage(ChatColor.RED
                    + "ERROR! Can't add "
                    + guest
                    + " as an guest to the database! He was not invited! Contact an admin!");
        }
    }

    public void removeGuest(Player player, String warpName, String guest) {
        Warp warp = warps.get(warpName);
        warp.uninvitePlayer(guest);
        if (dbManager.changeGuestList(warp.getGuestsAsString()))
            player.sendMessage(ChatColor.AQUA + guest
                    + " was sucessfully uninvited from " + warpName);
        else {
            warp.invitePlayer(guest);
            player.sendMessage(ChatColor.RED
                    + "ERROR! Can't remove "
                    + guest
                    + " as an guest to the database! He was not uninvited! Contact an admin!");
        }
    }

    public int getWarpCount(Player player) {
        int counter = 0;
        for (Warp warp : warps.values()) {
            if (warp.getOwner().equals(player.getName()))
                ++counter;
        }

        return counter;
    }

    public boolean hasFreeWarps(Player player) {
        if (player.isOp())
            return true;
        int warpCount = getWarpCount(player);
        String groupName = UtilPermissions.getGroupName(player);

        if (groupName.equals("default"))
            return warpCount < this.maximumWarps[DEFAULTS];
        else if (groupName.equals("probe"))
            return warpCount < this.maximumWarps[PROBE];
        else if (groupName.equals("probe"))
            return warpCount < this.maximumWarps[FREE];
        else if (groupName.equals("probe"))
            return warpCount < this.maximumWarps[PAY];

        return false;
    }

    public Warp getWarp(String name) {
        return warps.get(name);
    }
}
