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

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.minestar.MineStarWarp.Main;
import com.minestar.MineStarWarp.localization.LocalizationConstants;

public class BankManager implements LocalizationConstants {

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
                player.sendMessage(ChatColor.AQUA
                        + Main.localization.get(BANKM_UPDATED_BANK, playerName));
                banks.put(playerName, bankLocation);
                return;
            }
            else {
                player.sendMessage(ChatColor.RED
                        + "An error occurs updating database! Check the log for further information!");
            }
        }
        else {
            if (dbManager.setBank(playerName, bankLocation)) {
                player.sendMessage(ChatColor.AQUA
                        + Main.localization.get(BANKM_UPDATED_BANK, playerName));
                banks.put(playerName, bankLocation);
                return;
            }
            else
                player.sendMessage(ChatColor.RED
                        + "An error occurs while updating database! Check the log for further information!");
        }
    }
}
