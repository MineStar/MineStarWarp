/*
 * Copyright (C) 2012 MineStar.de 
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

package de.minestar.MineStarWarp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SignListener implements Listener {

    private WarpManager wManager;

    public SignListener(WarpManager wManager) {
        this.wManager = wManager;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.isCancelled() || !event.getBlock().getType().equals(Material.SIGN_POST) || !event.getBlock().getType().equals(Material.WALL_SIGN))
            return;

        String[] lines = event.getLines();
        if (lines[1] != null && lines[1].equalsIgnoreCase("[warp]") && lines[2] != null) {
            Player player = event.getPlayer();
            if (wManager.isWarpExisting(lines[2]))
                PlayerUtils.sendSuccess(player, Core.NAME, "Du kannst das Schild nun mit Rechtsklick benutzen!");
            else {
                PlayerUtils.sendError(player, Core.NAME, "Der Warp '" + lines[2] + "' existiert nicht!");
                event.setCancelled(true);
                event.getBlock().breakNaturally();
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled() || !event.hasBlock() || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        Block block = event.getClickedBlock();
        if (block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {
            Sign sign = (Sign) block.getState();
            String[] lines = sign.getLines();
            if (lines[1] != null && lines[1].equalsIgnoreCase("[warp]") && lines[2] != null && lines[2].length() >= 1)
                Bukkit.dispatchCommand(event.getPlayer(), "warp " + lines[2]);
        }
    }
}
