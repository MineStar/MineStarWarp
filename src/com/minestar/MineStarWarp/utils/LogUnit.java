/*
 * Copyright (C) 2011 MineStar.de
 *
 * This file is part of 'AdminStuff'.
 *
 * 'AdminStuff' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * 'AdminStuff' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 'AdminStuff'. If not, see <http://www.gnu.org/licenses/>.
 *
 * AUTHOR: GeMoschen
 *
 */

package com.minestar.MineStarWarp.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUnit {
    private static LogUnit instance = null;
    private static Logger log;
    private String pluginName = "";

    /**
     * 
     * CONSTRUCTOR
     * 
     */
    private LogUnit() {
    }

    /**
     * CREATE OR GET INSTANCE
     * 
     * @return Instance of LogUnit
     */
    public static LogUnit getInstance(String pluginName) {
        if (instance == null) {
            instance = new LogUnit();
            LogUnit.log = Logger.getLogger("Minecraft");
            instance.pluginName = pluginName;
        }
        return instance;
    }

    /**
     * PRINT INFO IN CONSOLE
     */
    public void printInfo(String info) {
        log.log(Level.INFO, "[ " + pluginName + " ] : " + info);
    }

    /**
     * PRINT WARNING IN CONSOLE
     */
    public void printWarning(String info) {
        log.log(Level.WARNING, "[ " + pluginName + " ] : " + info);
    }

    /**
     * PRINT ERROR IN CONSOLE
     */
    public void printError(String info, Exception e) {
        log.log(Level.SEVERE, "[ " + pluginName + " ] : " + info);
        e.printStackTrace();
    }
}