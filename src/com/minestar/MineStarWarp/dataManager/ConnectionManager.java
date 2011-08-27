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

import java.sql.Connection;
import java.sql.DriverManager;

import com.minestar.MineStarWarp.Main;

public class ConnectionManager {

    private static Connection con;

    private ConnectionManager() {

    }

    public static Connection getConnection() {
        if (con == null)
            createConnection();
        return con;
    }
    
    public static boolean initialize() {
        if (con != null) {
            Main.writeToLog("Connection is already initialized! Check for double initiations!");
            return false;
        }
        return createConnection();
    }
    
    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            }
            catch(Exception e) {
                Main.writeToLog(e.getMessage());
            }
        }
    }

    private static boolean createConnection() {
        
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:plugins/MineStarWarp/warps.db");
            con.setAutoCommit(false);
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
            return false;
        }
        return true;
    }
}
