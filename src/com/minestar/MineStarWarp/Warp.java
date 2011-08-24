package com.minestar.MineStarWarp;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp {

    /** Is able to edit , use the warp and invite other player to it */
    private final Player owner;
    /** The unique name */
    private final String name;
    /** The position to warp to */
    private final Location loc;
    /** When the warp is a public warp, the value is null */
    private ArrayList<Player> guests = null;

    /**
     * Use this constructor in the save system
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
    public Warp(Player owner, String name, Location loc,
            ArrayList<Player> guests) {
        this.owner = owner;
        this.name = name;
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
    public Warp(Player owner, String name, boolean isPublic) {
        this(owner, name, owner.getLocation(), isPublic ? null
                : new ArrayList<Player>());
        Main.writeToLog("New Warp created: "+owner.getDisplayName()+" "+name+" "+isPublic);
    }

    /**
     * @return The owner of the warp
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @return The unique name of the warp
     */
    public String getName() {
        return name;
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
    public ArrayList<Player> getGuests() {
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
        return isPublic() || arePlayersEqual(owner, player)
                || guests.contains(player);

    }

    /**
     * An implementation of the function Player.Equals(Object o)
     * 
     * @param player1
     * @param player2
     * @return True when both player has the same Entity ID
     */
    private boolean arePlayersEqual(Player player1, Player player2) {
        return player1.getEntityId() == player2.getEntityId();
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
            guests = new ArrayList<Player>();
    }

    /**
     * Add
     * 
     * @param player
     *            The new guest
     * @return False when the Warp is a public warp (prevents a NPE)
     */
    public boolean invitePlayer(Player player) {
        if (isPublic())
            return false;
        guests.add(player);
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
    public boolean isTheOwner(Player possibleOwner) {
        return arePlayersEqual(owner, possibleOwner);
    }
}
