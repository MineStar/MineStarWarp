package com.minestar.MineStarWarp.data;

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
    
    public static void initialize() {
        if (con != null) {
            Main.writeToLog("Connection is already initialized! Check for double initiations!");
            return;
        }
        createConnection();
        
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

    private static void createConnection() {
        
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:plugins/MineStarWarp/warps.db");
            con.setAutoCommit(false);
        }
        catch (Exception e) {
            Main.writeToLog(e.getMessage());
        }
    }
}
