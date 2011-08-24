package com.minestar.MineStarWarp;

import java.util.ArrayList;

public class SaveSystem {

    private ArrayList<Warp> warps;

    private void save() {

    }

    private void load() {

    }
    
    private boolean addWarp(Warp warp) {
        if (warps.contains(warp))
            return false;
        return warps.add(warp);
    }
    
    private boolean deleteWarp(Warp warp) {
        return warps.remove(warp);
    }
    
   

}
