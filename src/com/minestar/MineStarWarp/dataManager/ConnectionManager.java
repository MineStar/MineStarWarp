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

    private static Connection instance;

    /**
     * The default constructor is private to avoid calling
     * <code>new Connectionmanager() </code> from outside, because only one
     * object can exists
     */
    private ConnectionManager() {

    }

    /**
     * Returns a connection to the sqllite database. When the instance is not
     * created yet, a new connection is creating
     * 
     * @return A connection to the sqllite database
     */
    public static Connection getConnection() {

        if (instance == null)
            createConnection();
        return instance;
    }

    /**
     * Create a connection to the database. Calling this methode twice before
     * closing an error will send to the log
     * 
     * @return True when no error occurs
     */
    public static boolean initialize() {

        if (instance != null) {
            Main.log.printWarning("Connection is already initialized! Check for double initiations!");
            return false;
        }
        return createConnection();
    }

    /**
     * Closes the connection to the database and set the value to null. Call
     * this when you want to establish a new connection
     */
    public static void closeConnection() {

        if (instance != null) {
            try {
                instance.close();
                instance = null;
            } catch (Exception e) {
                Main.log.printError("Error while closing the database connection!", e);
            }
        }
    }

    /**
     * Creates a connection to the database using the JDBC driver
     * 
     * @return True when no error occurs
     */
    private static boolean createConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
            instance = DriverManager.getConnection("jdbc:sqlite:plugins/MineStarWarp/warps.db");
            instance.setAutoCommit(false);
        } catch (Exception e) {
            Main.log.printError("Error while creating database connection!", e);
            return false;
        }
        return true;
    }
}
