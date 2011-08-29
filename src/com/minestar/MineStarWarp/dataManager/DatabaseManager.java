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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.Warp;

public class DatabaseManager {

    private final Connection con = ConnectionManager.getConnection();

    private final Server server;

    private PreparedStatement addWarp = null;
    private PreparedStatement deleteWarp = null;
    private PreparedStatement changeGuestList = null;
    private PreparedStatement addHome = null;
    private PreparedStatement updateHome = null;
    private PreparedStatement convertToPublic = null;

    public DatabaseManager(Server server) {
        this.server = server;
        try {
            initiate();
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
        }
    }

    private void initiate() throws Exception {
        // check the database structure
        createTables();
        addWarp = con
                .prepareStatement("INSERT INTO warps (name, creator, world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?,?,?);");
        deleteWarp = con.prepareStatement("DELETE FROM warps WHERE name = ?;");
        changeGuestList = con
                .prepareStatement("UPDATE warps SET permissions = ? WHERE name = ?;");
        addHome = con
                .prepareStatement("INSERT INTO homes (player,world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?,?);");
        updateHome = con
                .prepareStatement("UPDATE homes SET world = ? , x = ? , y = ? , z = ? , yaw = ? , pitch = ? WHERE player = ?;");
        convertToPublic = con
                .prepareStatement("UPDATE warps SET permissions = null WHERE name = ?");

    }

    private void createTables() {
        // create the table for storing the warps
        try {
            con.createStatement()
                    .executeUpdate(
                            "CREATE TABLE IF NOT EXISTS `warps` ("
                                    + "`id` INTEGER PRIMARY KEY,"
                                    + "`name` varchar(32) NOT NULL DEFAULT 'warp',"
                                    + "`creator` varchar(32) NOT NULL DEFAULT 'Player',"
                                    + "`world` varchar(32) NOT NULL DEFAULT '0',"
                                    + "`x` DOUBLE NOT NULL DEFAULT '0',"
                                    + "`y` tinyint NOT NULL DEFAULT '0',"
                                    + "`z` DOUBLE NOT NULL DEFAULT '0',"
                                    + "`yaw` smallint NOT NULL DEFAULT '0',"
                                    + "`pitch` smallint NOT NULL DEFAULT '0',"
                                    + "`publicAll` boolean NOT NULL DEFAULT '1',"
                                    + "`permissions` text DEFAULT null);");
            con.commit();
        }
        catch (SQLException e) {
            Main.writeToLog(e.getMessage());
        }

        // create the table for storing the homes
        try {
            con.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS `homes` ("
                            + "`player` varchar(32) PRIMARY KEY,"
                            + "`world` varchar(32) NOT NULL DEFAULT '0',"
                            + "`x` DOUBLE NOT NULL DEFAULT '0',"
                            + "`y` tinyint NOT NULL DEFAULT '0',"
                            + "`z` DOUBLE NOT NULL DEFAULT '0',"
                            + "`yaw` smallint NOT NULL DEFAULT '0',"
                            + "`pitch` smallint NOT NULL DEFAULT '0');");
            con.commit();
        }
        catch (SQLException e) {
            Main.writeToLog(e.getMessage());
        }

    }

    public TreeMap<String, Warp> loadWarpsFromDatabase() {

        TreeMap<String, Warp> warps = new TreeMap<String, Warp>();
        try {
            ResultSet rs = con
                    .createStatement()
                    .executeQuery(
                            "SELECT name,creator,world,x,y,z,yaw,pitch,permissions FROM warps");
            while (rs.next()) {

                String name = rs.getString(1);
                String creator = rs.getString(2);
                String world = rs.getString(3);
                Location loc = new Location(server.getWorld(world),
                        rs.getDouble(4), rs.getInt(5), rs.getDouble(6),
                        rs.getInt(7), rs.getInt(8));
                String guestsList = rs.getString(9);
                Warp warp = new Warp(creator, loc,
                        this.convertsGuestsToList(guestsList));
                warps.put(name, warp);
            }
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
        }
        Main.writeToLog("Loaded sucessfully " + warps.size() + " Warps");
        return warps;
    }

    public TreeMap<String, Location> loadHomesFromDatabase() {

        TreeMap<String, Location> homes = new TreeMap<String, Location>();
        try {
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT player,world,x,y,z,yaw,pitch FROM homes");
            while (rs.next()) {

                String name = rs.getString(1);
                String world = rs.getString(2);
                Location loc = new Location(server.getWorld(world),
                        rs.getDouble(3), rs.getInt(4), rs.getDouble(5),
                        rs.getInt(6), rs.getInt(7));
                homes.put(name, loc);
            }
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
        }
        Main.writeToLog("Loaded sucessfully " + homes.size() + " Homes");
        return homes;
    }

    public boolean addWarp(Player creator, String name, Warp warp) {
        try {
            Location loc = warp.getLoc();
            // INSERT INTO warps (name, creator, world, x, y, z, yaw, pitch)
            // VALUES (?,?,?,?,?,?,?,?);
            addWarp.setString(1, name);
            addWarp.setString(2, creator.getName());
            addWarp.setString(3, creator.getWorld().getName());
            addWarp.setDouble(4, loc.getX());
            addWarp.setInt(5, loc.getBlockY());
            addWarp.setDouble(6, loc.getZ());
            addWarp.setInt(7, Math.round(loc.getYaw()) % 360);
            addWarp.setInt(8, Math.round(loc.getPitch()) % 360);
            addWarp.executeUpdate();
            con.commit();
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean setHome(Player creator) {
        try {
            Location loc = creator.getLocation();
            // INSERT INTO homes (player,world, x, y, z, yaw, pitch) VALUES
            // (?,?,?,?,?,?,?);
            addHome.setString(1, creator.getName());
            addHome.setString(2, creator.getWorld().getName());
            addHome.setDouble(3, loc.getX());
            addHome.setInt(4, loc.getBlockY());
            addHome.setDouble(5, loc.getZ());
            addHome.setInt(6, Math.round(loc.getYaw()) % 360);
            addHome.setInt(7, Math.round(loc.getPitch()) % 360);
            addHome.executeUpdate();
            con.commit();
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateHome(Player player) {
        try {
            Location loc = player.getLocation();
            // UPDATE homes SET world = ? , x = ? , y = ? , z = ? , yaw = ? ,
            // pitch = ? WHERE name = ?;
            updateHome.setString(1, player.getWorld().getName());
            updateHome.setDouble(2, loc.getX());
            updateHome.setInt(3, loc.getBlockY());
            updateHome.setDouble(4, loc.getZ());
            updateHome.setInt(5, Math.round(loc.getYaw()) % 360);
            updateHome.setInt(6, Math.round(loc.getPitch()) % 360);
            updateHome.setString(7, player.getName());
            updateHome.executeUpdate();
            con.commit();
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteWarp(String name) {
        try {
            // DELETE FROM warps WHERE name = ?;
            deleteWarp.setString(1, name);
            deleteWarp.executeUpdate();
            con.commit();
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean changeGuestList(String guestList, String name) {
        try {
            // UPDATE warps SET permissions = ? WHERE name = ?;
            changeGuestList.setString(1, guestList);
            changeGuestList.setString(2, name);
            changeGuestList.executeUpdate();
            con.commit();
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
            return false;
        }
        return true;
    }

    private ArrayList<String> convertsGuestsToList(String guestList) {
        if (guestList == null)
            return null;
        ArrayList<String> guests = new ArrayList<String>();
        String[] split = guestList.split(";");
        guests.addAll(Arrays.asList(split));

        return guests;
    }

    public boolean removeGuestsList(String name) {
        try {
            // UPDATE warps SET permissions = null WHERE name = ?
            convertToPublic.setString(1, name);
            convertToPublic.executeUpdate();
            con.commit();
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
            return false;
        }
        return true;
    }
}
