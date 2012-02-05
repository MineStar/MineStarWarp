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
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.database.DatabaseManager;

public class BankManager {

    private HashMap<String, Location> banks;

    private final DatabaseManager dbManager;

    public BankManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        banks = dbManager.loadBanksFromDatabase();
    }

    public Location getBank(String playerName) {

        playerName = playerName.toLowerCase();
        Location loc = banks.get(playerName);

        if (loc != null)
            return loc;

        for (Entry<String, Location> bank : banks.entrySet()) {
            String owner = bank.getKey();
            if (owner.contains(playerName))
                return bank.getValue();
        }

        return null;
    }

    public void setBank(Player player, String playerName, Location bankLocation) {

        if (banks.containsKey(playerName)) {
            if (dbManager.updateBank(playerName, bankLocation)) {
                ChatUtils.printSuccess(player, Core.NAME, "Eine Bank f�r Spieler '" + playerName + "' erstellt!");
                banks.put(playerName, bankLocation);
                return;
            } else
                player.sendMessage(ChatColor.RED + "An error occurs updating database! Check the log for further information!");

        } else {
            if (dbManager.setBank(playerName, bankLocation)) {
                ChatUtils.printSuccess(player, Core.NAME, "Bank f�r Spieler '" + playerName + "' aktualisiert!");
                banks.put(playerName, bankLocation);
                return;
            } else
                player.sendMessage(ChatColor.RED + "An error occurs while updating database! Check the log for further information!");
        }
    }

    /**
     * This is used for the command /bank list (#) . <br>
     * It iterates through the TreeMaps' keys and adding an intervall to a new
     * HashMap that is returned by this method. <br>
     * <code>(pageNumber-1)*banksPerPage</code> <br>
     * indicates the start of the intervall and adds as much warps as high
     * banksPerPage is.
     * 
     * @param pageNumber
     *            Indicates the start of the intervall
     * @param banksPerPage
     *            How many banks are returned
     * @return HashMap banks concerning the intervall. Returns null if the list
     *         is empty
     */
    public TreeMap<String, Location> getBanksForList(int pageNumber, int banksPerPage) {

        TreeMap<String, Location> bankList = new TreeMap<String, Location>();

        String[] keys = new String[banks.size()];
        keys = banks.keySet().toArray(keys);

        for (int i = 0; i < banksPerPage && (((pageNumber - 1) * banksPerPage) + i) < keys.length; ++i) {
            String key = keys[((pageNumber - 1) * banksPerPage) + i];
            bankList.put(key, banks.get(key));
        }

        return bankList.size() > 0 ? bankList : null;
    }

    /**
     * @return Size of banks
     */
    public int countBanks() {
        return banks.size();
    }
}
