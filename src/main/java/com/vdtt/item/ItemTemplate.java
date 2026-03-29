package com.vdtt.item;

public class ItemTemplate {
    public short id;
    public String name;
    public String detail;
    public boolean isXepChong;
    public byte gioiTinh;
    public byte type;
    public byte typeChar;
    public short idIcon;
    public short levelNeed;
    public int phamChatNeed;
    public short width;
    public short height;

    public int getType() {
        return this.type;
    }

    public int getLevelNeed() {
        return this.levelNeed;
    }

    public boolean isBlackListItem() {
        return id == 833 || id == 1003 || (id >= 799 && id <= 805) || id >= 871 && id <= 874;
    }

    public boolean isBlackListItemTrade() {
        return id == 833 || (id >= 799 && id <= 805);
    }
}
