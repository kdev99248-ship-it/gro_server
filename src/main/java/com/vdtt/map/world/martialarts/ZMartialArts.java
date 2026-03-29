package com.vdtt.map.world.martialarts;

import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.zones.ZWorld;
import com.vdtt.model.Char;

import java.util.List;

public class ZMartialArts extends ZWorld {
    public ZMartialArts(int id, MapTemplate tilemap, Map map) {
        super(id, tilemap, map);
    }

    @Override
    public void updateChar() {
        super.updateChar();
        List<Char> mChars = getChars();
        if (mChars.isEmpty()) {
            return;
        }
        if (mChars.size() > 1) {
            if (mChars.get(0) == null) {
                if (mChars.get(1) != null) {
                    mChars.get(1).martialArtsPoint = 1;
                }
            } else if (mChars.get(1) == null) {
                if (mChars.get(0) != null) {
                    mChars.get(0).martialArtsPoint = 1;
                }
            }
        }
    }
}
