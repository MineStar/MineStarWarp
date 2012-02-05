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

package de.minestar.MineStarWarp.dataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.Warp;
import de.minestar.MineStarWarp.database.DatabaseManager;
import de.minestar.minestarlibrary.utils.ChatUtils;

/**
 * This class handels all data changes and data calls for the warps. <br>
 * It is the class between the player commands and the database handeling. <br>
 * Important! <br>
 * This class is Singleton, so only one instance is created! Use getInstance()
 * to get an instance of the class!
 * 
 * @author Meldanor
 */
public class WarpManager {

    // Key = Name of Warp
    private TreeMap<String, Warp> warps;

    // All database handling belongs to this
    private final DatabaseManager dbManager;

    // The indices for all groups
    private final static int DEFAULTS = 0;
    private final static int PROBE = 1;
    private final static int FREE = 2;
    private final static int PAY = 3;

    // Used to store how many warps a player can have
    private final int[] maximumWarps = new int[4];

    /**
     * Creates a WarpManager. Use this for handling all data belongs to warps
     * 
     * @param dbManager
     *            Manages all database communication
     * @param config
     *            The config file for the plugin
     */
    public WarpManager(DatabaseManager dbManager, FileConfiguration config) {
        this.dbManager = dbManager;
        warps = dbManager.loadWarpsFromDatabase();

        maximumWarps[DEFAULTS] = config.getInt("warps.default", 0);
        maximumWarps[PROBE] = config.getInt("warps.probe", 2);
        maximumWarps[FREE] = config.getInt("warps.free", 5);
        maximumWarps[PAY] = config.getInt("warps.pay", 9);
    }

    /**
     * Check if the warp is existing. It is case-sensitive
     * 
     * @param name
     *            The name of warp
     * @return True when there is an warp with the same name
     */
    public boolean isWarpExisting(String name) {
        return warps.containsKey(name);
    }

    /**
     * Stores at first the warp in the database and if no error occurs it is
     * added to the TreeMap. If there is an error while adding the warp to the
     * databse, the warp is not added to the TreeMap! <br>
     * Check at first if the warp is already existing before adding it!
     * 
     * @param creator
     *            The command caller of /warp create
     * @param name
     *            The name of the warp
     * @param warp
     *            The warp itself. You have to create an object of the class for
     *            this.
     */
    public void addWarp(Player creator, String name, Warp warp) {

        if (dbManager.addWarp(creator, name, warp)) {
            warps.put(name, warp);
            ChatUtils.printSuccess(creator, Core.NAME, "Der Warp '" + name + "' wurde erstellt.");
            ChatUtils.printInfo(creator, Core.NAME, ChatColor.GRAY, "Um anderen zum Warp zu inviten, benutze den Befehl '/warp invite <Spieler> " + name + " .");
        } else
            ChatUtils.printError(creator, Core.NAME, "Warp '" + name + "' konnte durch einen internen Fehler nicht erstellt werden! Bitte an einen Admin wenden!");
    }

    /**
     * Delete the entry in database belonging to the warp. If no error occurs it
     * is also deleted from the TreeMap. When an error occurs, the warp is also
     * existing in the TreeMap <br>
     * You have to check if the player has the rights to delete this warp before
     * calling this!
     * 
     * @param player
     *            The command caller of /warp delete
     * @param name
     *            The case-senstive name of the warp
     */
    public void deleteWarp(Player player, String name) {

        if (dbManager.deleteWarp(name)) {
            ChatUtils.printSuccess(player, Core.NAME, "Der Warp '" + name + "' wurde gelöscht!");
            warps.remove(name);
        } else
            ChatUtils.printError(player, Core.NAME, "Warp '" + name + "' konnte durch einen internen Fehler nicht gelöscht werden! Bitte an einen Admin wenden!");
    }

    public void updateWarp(Player player, String name) {

        if (dbManager.updateWarp(name, player.getLocation())) {
            warps.get(name).moveWarp(player.getLocation());
            ChatUtils.printSuccess(player, Core.NAME, "Der Warp '" + name + "' wurde verschoben!");
        } else
            ChatUtils.printError(player, Core.NAME, "Warp '" + name + "' konnte durch einen internen Fehler nicht verschoben werden! Bitte an einen  Admin wenden!");
    }

    public void renameWarp(Player player, String oldName, String newName) {

        if (dbManager.renameWarp(oldName, newName)) {
            ChatUtils.printSuccess(player, Core.NAME, "Der Warp '" + oldName + "' heißt nun '" + newName + "'!");
            warps.remove(oldName);
            warps.put(newName, warps.get(oldName));
        }
    }

    /**
     * The invited player is giving the right to also use the warp. <br>
     * The changed guest list will stored in the database. If no error occurs it
     * is also changed in the TreeMap. When an error occurs, the guest list is
     * unchanged! <br>
     * You have to check if the player has the rights to change the guest list
     * before calling this!
     * 
     * @param player
     *            The player who have the rights to change the guestlist
     * @param warpName
     *            The case-senstive name of the warp
     * @param guest
     *            The player who shall also use the warp
     */
    public boolean addGuest(Player player, String warpName, String guest) {

        Warp warp = warps.get(warpName);
        warp.invitePlayer(guest);
        if (dbManager.changeGuestList(warp.getGuestsAsString(), warpName)) {
            ChatUtils.printSuccess(player, Core.NAME, "Spieler '" + guest + "' wurde zu dem Warp '" + warpName + "' eingeladen!");
            return true;
        } else {
            warp.uninvitePlayer(guest);
            ChatUtils.printError(player, Core.NAME, "Der Spieler '" + guest + "' konnte nicht zum Warp '" + warpName + "' eingeladen werden! Bitte an einen Admin wenden!");
            return false;
        }
    }

    /**
     * The uninvited player loosing the right to use the warp. <br>
     * The changed guest list will stored in the database. If no error occurs it
     * is also changed in the TreeMap. When an error occurs, the guest list is
     * unchanged! <br>
     * You have to check if the player has the rights to change the guest list
     * before calling this!
     * 
     * @param player
     *            The player who have the rights to change the guestlist
     * @param warpName
     *            The case-senstive name of the warp
     * @param guest
     *            The player who cannot use the warp anymore
     */
    public boolean removeGuest(Player player, String warpName, String guest) {

        Warp warp = warps.get(warpName);
        warp.uninvitePlayer(guest);
        if (dbManager.changeGuestList(warp.getGuestsAsString(), warpName))
            ChatUtils.printSuccess(player, Core.NAME, "Spieler '" + guest + "' wurde aus dem Warp '" + warpName + "' ausgeladen!");

        else {
            warp.invitePlayer(guest);
            ChatUtils.printError(player, Core.NAME, "Der Spieler '" + guest + "' konnte nicht aus Warp '" + warpName + "' ausgeladen werden! Bitte an einen Admin wenden!");
            return false;
        }

        return true;
    }

    /**
     * Count all warps the given player has created. Public warps doesn't not
     * count!
     * 
     * @param player
     *            The player who is the owner of the warp
     * @return How many warps the player has created and are not public
     */
    public int countWarpsCreatedBy(Player player) {

        int counter = 0;
        for (Warp warp : warps.values()) {
            if (!warp.isPublic() && warp.getOwner().equals(player.getName()))
                ++counter;
        }
        return counter;
    }

    public int countWarpsCreatedBy(String playerName) {
        int counter = 0;
        for (Warp warp : warps.values()) {
            if (!warp.isPublic() && warp.getOwner().equals(playerName))
                ++counter;
        }
        return counter;
    }

    /**
     * Count all warps the given player can use(warp is public or player is
     * owner or player is on the guest list) <br>
     * It returns warps.size() when the player is an admin
     * 
     * @param player
     *            The who can uses the warps
     * @return How many warps the player can use
     */
    public int countWarpsCanUse(Player player) {

        if (player.isOp())
            return warps.size();
        int counter = 0;
        for (Warp warp : warps.values()) {
            if (warp.canUse(player))
                ++counter;
        }
        return counter;
    }

    /**
     * Compares the count of private warps the player has created and the
     * maximum number the player can create. If there is space, it returns true. <br>
     * Example: <br>
     * Player is Free User (can have 5 private warps) and have 7 warps created,
     * but 3 are public. So he can create one more private warp and the method
     * returns true
     * 
     * @param player
     *            The player which warp count is checked
     * @return True when the player can at least create one private warp
     */
    public boolean hasFreeWarps(Player player) {

        if (player.isOp())
            return true;
        int warpCount = countWarpsCreatedBy(player);
        int maximumWarps = getMaximumWarp(player);
        // when the value is set to -1 the player group can create infinite
        // warps
        return maximumWarps == -1 || maximumWarps > warpCount;
    }

    private int getMaximumWarp(Player player) {

        String groupName = UtilPermissions.getGroupName(player);
        if (groupName.equals("default"))
            return maximumWarps[DEFAULTS];
        else if (groupName.equals("probe"))
            return maximumWarps[PROBE];
        else if (groupName.equals("vip"))
            return maximumWarps[FREE];
        else if (groupName.equals("pay"))
            return maximumWarps[PAY];
        else if (groupName.equals("admins") || groupName.equals("mods"))
            return Integer.MAX_VALUE;
        return 0;
    }

    public int getMaximumWarp(String playerName) {

        String groupName = UtilPermissions.getGroupName(playerName, Bukkit.getServer().getWorlds().get(0).getName());
        if (groupName.equals("default"))
            return maximumWarps[DEFAULTS];
        else if (groupName.equals("probe"))
            return maximumWarps[PROBE];
        else if (groupName.equals("vip"))
            return maximumWarps[FREE];
        else if (groupName.equals("pay"))
            return maximumWarps[PAY];
        else if (groupName.equals("admins") || groupName.equals("mods"))
            return Integer.MAX_VALUE;
        return 0;
    }

    /**
     * @param name
     *            The case-sensitive name
     * @return Warp have the same, case-sensitive name as given. Null if no warp
     *         exists
     */
    public Warp getWarp(String name) {
        return warps.get(name);
    }

    /**
     * Return a warp that have the same name. If no warp exists with the same
     * name, the first warp, that starts with the name will returned.
     * 
     * @param name
     *            Same name or similiar name
     * @param player
     *            Ignore warps this player cannot use
     * @return Warp matching name
     */
    public Entry<String, Warp> getSimiliarWarp(String name, Player player) {

        if (warps.containsKey(name))
            return warps.ceilingEntry(name);

        String lowerName = name.toLowerCase();
        if (warps.containsKey(lowerName))
            return warps.ceilingEntry(lowerName);

        Entry<String, Warp> found = null;
        int delta = Integer.MAX_VALUE;

        for (Entry<String, Warp> entry : warps.entrySet()) {
            if (!entry.getValue().canUse(player))
                continue;
            String tempName = entry.getKey().toLowerCase();
            if (tempName.startsWith(lowerName)) {
                int curDelta = tempName.length() - lowerName.length();
                if (curDelta < delta) {
                    found = entry;
                    delta = curDelta;
                }
                if (curDelta == 0)
                    break;
            }
        }

        return found;
    }

    /**
     * Search for all warps that contains the query. <br>
     * Example: <br>
     * The warps "probe1,probe2,probe3,probe4 and probe42" are returned when the
     * query is probe
     * 
     * @param query
     *            The warp name must contain this phrase to match
     * @return HashMap of all warps that contains the phrase. Returns null if no
     *         warp is find
     */
    public HashMap<String, Warp> getSimiliarWarps(String query, Player player) {

        query = query.toLowerCase();

        HashMap<String, Warp> warpList = new HashMap<String, Warp>();
        for (Entry<String, Warp> entry : warps.entrySet()) {
            Warp warp = entry.getValue();
            String warpName = entry.getKey();
            if (warp.canUse(player) && warpName.toLowerCase().contains(query))
                warpList.put(warpName, warp);
        }

        return warpList.size() != 0 ? warpList : null;
    }

    /**
     * Stores all warps into a HashMap the player is owning. That also concern
     * public warps
     * 
     * @param playerName
     *            The owner of the warps
     * @return HashMap concerning warps player is owning. Returns null if no
     *         matching warp is find
     */
    public TreeMap<String, Warp> getWarpsPlayerIsOwner(String playerName) {

        TreeMap<String, Warp> warpList = new TreeMap<String, Warp>();

        for (Entry<String, Warp> entry : warps.entrySet()) {
            Warp tempWarp = entry.getValue();
            if (tempWarp.isOwner(playerName))
                warpList.put(entry.getKey(), tempWarp);
        }

        return warpList.size() != 0 ? warpList : null;
    }

    /**
     * This is used for the command /warp list (#) . <br>
     * It iterates through the TreeMaps' keys and adding an intervall to a new
     * HashMap that is returned by this method. <br>
     * <code>(pageNumber-1)*warpsPerPage</code> <br>
     * indicates the start of the intervall and adds as much warps as high
     * warpsPerPage is.
     * 
     * @param pageNumber
     *            Indicates the start of the intervall
     * @param warpsPerPage
     *            How many warps are returned
     * @param player
     *            The command caller. Only warps are listed he can use
     * @return HashMap concerning the intervall. Returns null if the list is
     *         empty
     */
    public TreeMap<String, Warp> getWarpsForList(int pageNumber, int warpsPerPage, Player player) {

        TreeMap<String, Warp> warpList = new TreeMap<String, Warp>();
        TreeMap<String, Warp> warpsPlayerCanUse = new TreeMap<String, Warp>();

        for (Entry<String, Warp> entry : warps.entrySet()) {
            if (entry.getValue().canUse(player))
                warpsPlayerCanUse.put(entry.getKey(), entry.getValue());
        }

        String[] keys = new String[warpsPlayerCanUse.size()];
        keys = warpsPlayerCanUse.keySet().toArray(keys);

        for (int i = 0; i < warpsPerPage && (((pageNumber - 1) * warpsPerPage) + i) < keys.length; ++i) {
            String key = keys[((pageNumber - 1) * warpsPerPage) + i];
            warpList.put(key, warpsPlayerCanUse.get(key));
        }

        return warpList.size() != 0 ? warpList : null;
    }

    /**
     * Changing the access from public to private or vice versa. The change is
     * send at first to the database and when no error occurs, it is changed in
     * the TreeMap. <br>
     * You have to check if the player has the rights to change the guest list
     * before calling this!
     * 
     * @param player
     *            Is changing the access
     * @param toPublic
     *            <code>True</code> = The warp is private and shall changed to
     *            public <br>
     *            <code>False</code> = The warp is public and shall changed to
     *            private
     * @param warpName
     *            The name of the warp
     */
    public void changeAccess(Player player, boolean toPublic, String warpName) {

        if (toPublic) {
            if (dbManager.removeGuestsList(warpName)) {
                ChatUtils.printSuccess(player, Core.NAME, "Der Warp '" + warpName + "' ist nun öffentlich!");
                warps.get(warpName).setAccess(toPublic);
            } else
                ChatUtils.printError(player, Core.NAME, "Der Warp '" + warpName + "' konnte durch internen Fehler nicht veröffentlich werden! Bitte an einen Admin wenden!");
        } else {
            if (dbManager.changeGuestList("", warpName)) {
                ChatUtils.printSuccess(player, Core.NAME, "Der Warp '" + warpName + "' ist nun privat!");
                warps.get(warpName).setAccess(toPublic);
            } else
                ChatUtils.printError(player, Core.NAME, "Der Warp '" + warpName + "' konnte durch internen Fehler nicht privat werden! Bitte an einen Admin wenden!");
        }
    }

    // Used for showWarpList
    // Format is:
    // COLOR 'WARPNAME' by CREATOR + (X Y Z world)
    private final static String LIST_MESSAGE = "%s'%s'" + ChatColor.WHITE + " by " + ChatColor.GREEN + "%s" + ChatColor.WHITE + " (" + ChatColor.BLUE + "%d %d %d %s" + ChatColor.WHITE + ")";

    /**
     * This sends all the warps in a good format to the player
     * 
     * @param player
     *            The reciever
     * @param warps
     *            The warps to present
     */
    public void showWarpList(Player player, Map<String, Warp> warps) {

        Warp warp = null;
        boolean isOwner = false;
        String creator = null;
        Location loc = null;
        int x, y, z = 0;
        ChatColor color = null;

        for (Entry<String, Warp> entry : warps.entrySet()) {

            warp = entry.getValue();
            isOwner = warp.isOwner(player.getName());
            creator = isOwner ? "you" : warp.getOwner();
            loc = warp.getLoc();
            x = loc.getBlockX();
            y = loc.getBlockY();
            z = loc.getBlockZ();

            if (isOwner)
                color = ChatColor.AQUA;
            else if (warp.isPublic())
                color = ChatColor.GREEN;
            else
                color = ChatColor.RED;

            player.sendMessage(String.format(LIST_MESSAGE, color, entry.getKey(), creator, x, y, z, loc.getWorld().getName()));
        }
    }
}
