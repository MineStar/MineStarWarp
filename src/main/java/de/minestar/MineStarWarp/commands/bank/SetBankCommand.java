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

package de.minestar.MineStarWarp.commands.bank;

import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.dataManager.BankManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;

public class SetBankCommand extends AbstractCommand {

    private BankManager bankManager;

    public SetBankCommand(String syntax, String arguments, String node, BankManager bankManager) {
        super(Core.NAME, syntax, arguments, node);
        this.bankManager = bankManager;
    }

    @Override
    /**
     * Representing the command <br>
     * /setBank <br>
     * This set the bank location for the player
     * 
     * @param player
     *            Called the command
     * @param args
     *            args[0] is the players name(not command caller!) who can use the bank
     */
    public void execute(String[] args, Player player) {

        bankManager.setBank(player, args[0].toLowerCase(), player.getLocation());
    }

}
