package com.vdtt.map.world.planetblackstar;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.planetblackstar.dragon.AbsDragon;
import com.vdtt.server.GlobalService;
import com.vdtt.util.Util;

public class PlanetBlackStar {

    public static int TIME_WIN = 300000;

    public void createArea() {
        GlobalService.getInstance().chat("Hệ thống", "c#redHành tinh sao đen đã mở tới phi thuyền trái đất để tham gia", (byte) 1);
        Map map = MapManager.getInstance().find(39);
        map.getZones().clear();
        for (int i = 0; i < 30; i++) {
            ZPlanetBlackStar area = new ZPlanetBlackStar(i, map.mapTemplate, map);
            area.timeInMap = false;
            AbsDragon absDragon = (AbsDragon) area.createBoss((short) Util.nextInt(0, 6), 150000000, (short) 10, (short) 542, (short) 505, 1000);
            absDragon.zone = area;
            area.addMob(absDragon);
        }
    }

    public static PlanetBlackStar getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        public static final PlanetBlackStar INSTANCE = new PlanetBlackStar();
    }
}
