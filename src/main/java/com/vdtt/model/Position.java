package com.vdtt.model;

public class Position {
    public short x;
    public short y;

    public static Position create(int var0, int var1) {
        Position position = new Position();
        position.setXY(var0, var1);
        return position;
    }

    public void setXY(int x, int y) {
        this.x = (short)x;
        this.y = (short)y;
    }

    public static Position parse(String var0) {
        String[] data = var0.split("_");
        Position position = new Position();
        position.setXY(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
        return position;
    }

    public Position copy() {
        return create(this.x, this.y);
    }


    public Object clone() {
        return this.copy();
    }
}
