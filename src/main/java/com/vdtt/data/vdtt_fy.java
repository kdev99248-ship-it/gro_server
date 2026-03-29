package com.vdtt.data;

import java.util.Comparator;

final class vdtt_fy implements Comparator {
    vdtt_fy() {
    }

    @Override
    public final int compare(Object obj, Object obj2) {
        return ((vdtt_fw) obj).d[0].x - ((vdtt_fw) obj2).d[0].x;
    }
}
