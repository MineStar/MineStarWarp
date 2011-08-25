package com.minestar.MineStarWarp;

import java.util.ArrayList;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp {

    /** Is able to edit , use the warp and invite other player to it */
    private final String owner;
    /** The position to warp to */
    private final Location loc;
    /** When the warp is a public warp, the value is null */
    private ArrayList<String> guests = null;

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
    public Warp(String owner, Location loc, ArrayList<String> guests) {
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
        this(creator.getName(),creator.getLocation(),null);
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
    public ArrayList<String> getGuests() {
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
    public boolean canUse(String name) {
        return isPublic() || owner.equals(name)
                || guests.contains(name);

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
            guests = new ArrayList<String>();
    }

    /**
     * Add
     * 
     * @param player
     *            The new guest
     * @return False when the Warp is a public warp (prevents a NPE)
     */
    public boolean invitePlayer(String name) {
        if (isPublic())
            return false;
        guests.add(name);
        return true;
    }

    /**
     * Removes a player from the guests's list so the player cannot use the warp
     * anymore
     * 
     * @param player
     *            The uninvited player
     * @return False when the Warp is a public warp (prevents a NPE)
     */
    public boolean uninvitePlayer(Player player) {
        if (isPublic())
            return false;
        guests.remove(player);
        return true;
    }

    /**
     * @param possibleOwner
     *            The checked player
     * @return True when the possibleOwner is the owner
     */
    public boolean isTheOwner(String possibleOwner) {
        return owner.equals(possibleOwner);
    }
    
    public String getGuestsAsString() {
        StringBuilder result = new StringBuilder("");
        for (String player : guests) {
            result.append(player);
            result.append(";");
        }
                
        return result.substring(0, result.length()-1);
    }
}
