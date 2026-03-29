package com.vdtt.store;

import com.vdtt.util.Log;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoreManager {
    public static final byte TYPE_TIEN_LOI = 6;
    public static final byte TYPE_KHU_KHOA = 7;
    public static final byte TYPE_COMBO = 39;
    public static final byte TYPE_KY_NANG = 37;
    public static final byte TYPE_DO_XIN = 38;
    public static final byte TYPE_TRANG_PHUC = 19;
    public static final byte TYPE_CAI_TRANG = 30;
    public static final byte TYPE_THOI_TRANG = 35;
    public static int id;

    @Getter
    private static final StoreManager instance = new StoreManager();

    private final List<Store> stores = new ArrayList<>();

    public void init() {
        Store store = new Store(TYPE_TIEN_LOI, "Tiện Lợi");
        store.load();
        add(store);
        Store store2 = new Store(TYPE_KHU_KHOA, "Khu Khoá");
        store2.load();
        add(store2);
        Store store4 = new Store(TYPE_KY_NANG, "Kỹ Năng");
        store4.load();
        add(store4);
        Store store5 = new Store(TYPE_DO_XIN, "Đồ Xịn");
        store5.load();
        add(store5);
        Store store6 = new Store(TYPE_TRANG_PHUC, "Trang Phục");
        store6.load();
        add(store6);
        Store store7 = new Store(TYPE_CAI_TRANG, "Cải Trang");
        store7.load();
        add(store7);
        Store store8 = new Store(TYPE_THOI_TRANG, "Thời Trang");
        store8.load();
        add(store8);
        Store store9 = new Store(TYPE_COMBO, "Combo");
        store9.load();
        add(store9);
        Log.info("Loaded [" + stores.size() + "] stores.");
    }

    public void add(Store store) {
        stores.add(store);
    }

    public void addGoods(byte storeType, ItemStore item) {
        Store store = find(storeType);
        if (store != null) {
            store.add(item);
        }
    }

    public void remove(Store store) {
        stores.remove(store);
    }

    public Store find(byte type) {
        for (Store store : stores) {
            if (store.getType() == type) {
                return store;
            }
        }
        return null;
    }

    public ItemStore findItemStore(int itemID) {
        for (Store store : stores) {
            for (ItemStore item : store.getItems()) {
                if (item.getId() == itemID) {
                    return item;
                }
            }
        }
        return null;
    }

}
