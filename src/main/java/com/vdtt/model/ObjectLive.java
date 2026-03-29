package com.vdtt.model;

import com.vdtt.util.Util;

public class ObjectLive {
    public int idEntity;
    public short x = 250;
    public short y = 250;

    public static ObjectLive create(int x, int y) {
        ObjectLive objectLive = new ObjectLive();
        objectLive.x = (short) x;
        objectLive.y = (short) y;
        return objectLive;
    }

    public void updateXY(int[] array) {
        this.x = (short) array[0];
        this.y = (short) array[1];

    }
    public void setXY(int x, int y) {
        this.x = (short)x;
        this.y = (short)y;
    }

    public void updateXY2(int[] array) {
        if (array[0] != 0 && array[1] != 0) {
            this.x = (short) array[0];
            this.y = (short) array[1];
        }
    }

    public int getRange(ObjectLive objectLive) {
        return Util.getRange(objectLive, this);
    }
}
