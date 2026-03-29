package com.vdtt.map.world.stadiums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BTH Cute phô mai que
 */
public class StadiumManager {
    public List<Match> matchs = new ArrayList<>();

    public static StadiumManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        public static final StadiumManager INSTANCE = new StadiumManager();
    }
}
