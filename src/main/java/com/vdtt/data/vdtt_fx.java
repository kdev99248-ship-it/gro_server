package com.vdtt.data;

import java.util.Comparator;

final class vdtt_fx implements Comparator {
    vdtt_fx() {
    }

    @Override
    public final int compare(Object obj, Object obj2) {
        return ((vdtt_fw) obj).d[0].y - ((vdtt_fw) obj2).d[0].y;
    }
}
