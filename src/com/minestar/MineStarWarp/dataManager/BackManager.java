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

import java.util.TreeMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackManager {
	private TreeMap<String, Location> backs;

	public BackManager() {
		backs = new TreeMap<String, Location>();
	}

	public void setBack(Player player) {
		String playername = player.getName().toLowerCase();
		Location loc = player.getLocation();
		backs.put(playername, loc);
	}

	public Location getBack(Player player) {
		String playername = player.getName().toLowerCase();
		if (backs.containsKey(playername))
			return backs.get(playername);
		return null;
	}
}
