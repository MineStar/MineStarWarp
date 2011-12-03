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

package com.minestar.MineStarWarp;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

public class Warp {

    /** Is able to edit , use the warp and invite other player to it */
    private final String owner;
    /** The position to warp to */
    private Location loc;
    /** When the warp is a public warp, the value is null */
    private HashSet<String> guests = null;

    /**
     * Use this constructor for loading it from database
     * 
     * @param owner
     *            The creator of the Warp
     * @param name
     *            The unique name of the Warp
     * @param loc
     *            The location for the Warp
     * @param guests
     *            Players are able to use the Warp
     */
    public Warp(String owner, Location loc, HashSet<String> guests) {
        this.owner = owner;
        this.loc = loc;
        this.guests = guests;
    }

    /**
     * Use this constructor ONLY when a Warp is created in the game!
     * 
     * @param owner
     *            The owner of the Warp. The location of the Warp is the
     *            location of owner in the moment of creation!
     * @param name
     *            The unique Name of the Warp
     * @param isPublic
     *            Defines the Warp as a public or private Warp. If it is private
     *            by setting false, a new Object of ArrayList<Player> will be
     *            created. Otherwise the value for guests is null
     */
    public Warp(Player creator) {
        this(creator.getName(), creator.getLocation(), new HashSet<String>());
    }

    public void moveWarp(Location loc) {
        this.loc = loc;
    }

    /**
     * @return The owner of the warp
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return The position where a player will warped to
     */
    public Location getLoc() {
        return loc;
    }

    /**
     * @return All player who can also use the warp. If the value is null, the
     *         warp is public.
     */
    public HashSet<String> getGuests() {
        return guests;
    }

    /**
     * Returns true, when the warp is public. When the warp is private, it
     * returns true, when the player is the owner of the warp or it is a guest.
     * 
     * @param player
     *            The player which is checked
     * @return True if the player is allowed to use it
     */
    public boolean canUse(Player player) {
        return isPublic()
                || canEdit(player)
                || UtilPermissions.playerCanUseCommand(player, "minestarwarp.useAll")
                || guests.contains(player.getName().toLowerCase());

    }

    /**
     * @return True when the guests's list is null
     */
    public boolean isPublic() {
        return guests == null;
    }

    /**
     * Convert a Warp from public to private or vice versa
     * 
     * @param isPublic
     *            True for a public warp (Deletes ALL guests!) or false for
     *            private
     */
    public void setAccess(boolean isPublic) {
        if (isPublic)
            guests = null;
        else if (guests == null)
            guests = new HashSet<String>();
    }

    /**
     * The given player can now use the warp
     * 
     * @param player
     *            The new guest
     */
    public void invitePlayer(String name) {
        if (isPublic())
            return;
        guests.add(name.toLowerCase());
    }

    /**
     * Removes a player from the guests's list so the player cannot use the warp
     * anymore
     * 
     * @param player
     *            The uninvited player
     */
    public void uninvitePlayer(String name) {
        if (isPublic())
            return;
        guests.remove(name.toLowerCase());
    }

    /**
     * @param possibleOwner
     *            The checked player
     * @return True when the possibleOwner is the owner
     */
    public boolean isOwner(String possibleOwner) {
        return owner.equals(possibleOwner);
    }

    /**
     * 
     * @param player
     *            Want to edit(delete,invite,uninvite,private,public) the warp
     * @return True, when the player is the owner or an admin
     */
    public boolean canEdit(Player player) {
        return isOwner(player.getName())
                || player.isOp()
                || UtilPermissions.playerCanUseCommand(player, "minestarwarp.editAll");
    }

    /**
     * Convert the list of guests to a String. The guests are seperated by ;. If
     * the guest list is empty it returns an empty String
     * 
     * @return The guest list as one String
     */
    public String getGuestsAsString() {

        if (guests.size() == 0)
            return "";

        StringBuilder result = new StringBuilder("");
        for (String player : guests) {
            result.append(player);
            result.append(";");
        }
        return result.substring(0, result.length() - 1);
    }
}
