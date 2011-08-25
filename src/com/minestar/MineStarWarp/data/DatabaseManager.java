package com.minestar.MineStarWarp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        addWarp = con
                .prepareStatement("INSERT INTO warpTable (name, creator, world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?,?,?)");
        deleteWarp = con.prepareStatement("DELETE FROM warps WHERE name = ?)");
        changeGuestList = con.prepareStatement("UPDATE warpTable SET permissions = ?");

    }

    public TreeMap<String, Warp> loadWarpsFromDatabase() {
        TreeMap<String, Warp> warps = new TreeMap<String, Warp>();
        try {
            ResultSet rs = con
                    .createStatement()
                    .executeQuery(
                            "SELECT name,creator,world,x,y,z,yaw,pitch,permissions FROM warpTable");
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

    public boolean addWarp(Player creator, Warp warp, String name) {
        try {
            addWarp.setString(1, name);
            addWarp.setString(2, creator.getName());
            addWarp.setString(3, creator.getWorld().getName());
            addWarp.setDouble(4, warp.getLoc().getX());
            addWarp.setInt(5, warp.getLoc().getBlockY());
            addWarp.setDouble(6, warp.getLoc().getZ());
            addWarp.setInt(7, Math.round(warp.getLoc().getYaw()) % 360);
            addWarp.setInt(8, Math.round(warp.getLoc().getPitch()) % 360);
            addWarp.executeUpdate();
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

    public boolean changeGuestList(String name) {
        try {
            changeGuestList.setString(1, name);
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
}
