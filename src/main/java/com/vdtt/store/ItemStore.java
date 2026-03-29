package com.vdtt.store;

import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemTemplate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemStore {
    private int id;
    private int itemID;
    public byte sys;
    private int coin;
    private int zeni;
    private int ballz;
    private int diamond;
    private int coinClan;
    private boolean isLock;
    private long expire;
    private String strOptions;
    private ItemTemplate template;
    private int require;
    private byte type;
    public List<Item> items = new ArrayList<>();

    @Builder
    public ItemStore(int id, int itemID, byte sys, int coin, int zeni, int ballz, int diamond, boolean isLock, long expire,
                     String options, int require) {
        this.id = id;
        this.itemID = itemID;
        this.sys = sys;
        this.coin = coin;
        this.zeni = zeni;
        this.diamond = diamond;
        this.ballz = ballz;
        this.isLock = isLock;
        this.expire = expire;
        this.strOptions = options;
        this.require = require;
        this.template = ItemManager.getInstance().getItemTemplate(itemID);
    }
}
