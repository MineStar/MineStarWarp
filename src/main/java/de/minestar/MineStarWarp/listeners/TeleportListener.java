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

package de.minestar.MineStarWarp.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.MineStarWarp.dataManager.BackManager;

public class TeleportListener implements Listener {

    private BackManager backManager;

    public TeleportListener(BackManager backManager) {
        this.backManager = backManager;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        loadChunk(player, event.getTo());
        if (UtilPermissions.playerCanUseCommand(player, "minestarwarp.command.back"))
            backManager.addBack(player);
    }

    private void loadChunk(Player player, Location target) {
        target.getBlock().getChunk().load();
    }
}
