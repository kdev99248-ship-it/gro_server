package com.vdtt.map;

import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MapManager {

    private static final MapManager instance = new MapManager();

    public static MapManager getInstance() {
        return instance;
    }
    @Getter
    public final List<MapTemplate> mapTemplates = new ArrayList<>();
    @Getter
    private final ArrayList<Map> maps = new ArrayList<>();

    public void add(Map map) {
        maps.add(map);
    }

    public void remove(Map map) {
        maps.remove(map);
    }

    public Map find(int id) {
        for (Map map : maps) {
            if (map.id == id) {
                return map;
            }
        }
        return null;
    }
    public void init() {
        for (MapTemplate tile : mapTemplates) {
            try {
                add(new Map(tile.id));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void joinZone(Char pl, int mapId, int zoneId,int typeTau) {
        Map m = find(mapId);
        if (m != null) {
            m.joinZone(pl, zoneId,typeTau);
        }
    }

    public void outZone(Char pl) {
        if (pl != null && pl.zone != null) {
            Zone z = pl.zone;
            z.out(pl);
        }
    }
}
