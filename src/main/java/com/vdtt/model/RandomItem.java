package com.vdtt.model;

import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemTemplate;
import com.vdtt.util.Log;
import com.vdtt.util.RandomCollection;

import java.util.ArrayList;
import java.util.List;

public class RandomItem {
    public static final RandomCollection<Integer> VONG_QUAY_PHUC_LOI = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_EQUIP = new RandomCollection<>();
    public static final RandomCollection<Integer> SAO_PHA_LE = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_NGUC_TU = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_THU_LINH = new RandomCollection<>();
    public static final RandomCollection<Integer> BOSS = new RandomCollection<>();
    public static final RandomCollection<Integer> BOSS_BARRACK = new RandomCollection<>();
    public static final RandomCollection<Integer> BOSS_HELL = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_EVENT_VIP = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_EVENT_NORMAL = new RandomCollection<>();

    public static final RandomCollection<Integer> STAR_DOWN200 = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_THU_LINH_DOWN200 = new RandomCollection<>();
    
    public static void init() {
        VONG_QUAY_PHUC_LOI.add(10, 0);
        VONG_QUAY_PHUC_LOI.add(30, 1);
        VONG_QUAY_PHUC_LOI.add(3, 2);
        VONG_QUAY_PHUC_LOI.add(3, 3);
        VONG_QUAY_PHUC_LOI.add(3, 4);
        VONG_QUAY_PHUC_LOI.add(3, 5);
        VONG_QUAY_PHUC_LOI.add(2, 6);
        VONG_QUAY_PHUC_LOI.add(1, 7);
        VONG_QUAY_PHUC_LOI.add(1, 8);
        List<ItemTemplate> listEquip = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                listEquip.addAll(ItemManager.getInstance().createSet(j, i));
            }
        }
        for (ItemTemplate itemTemplate : listEquip) {
            if (!itemTemplate.name.startsWith("Kích hoạt")
                    && !itemTemplate.name.contains("Cẩm Tú")
                    && !itemTemplate.name.contains("Khâm La")
                    && !itemTemplate.name.contains("Hiệp Nghĩa")
                    && !itemTemplate.name.contains("Hạo Thiên"))
                ITEM_EQUIP.add(10, (int) itemTemplate.id);
        }
        SAO_PHA_LE.add(4, 406);
        SAO_PHA_LE.add(10, 407);
        SAO_PHA_LE.add(10, 408);
        SAO_PHA_LE.add(10, 409);
        SAO_PHA_LE.add(10, 410);
        SAO_PHA_LE.add(10, 411);
        SAO_PHA_LE.add(10, 412);
        SAO_PHA_LE.add(10, 413);
        SAO_PHA_LE.add(40, 2);
        SAO_PHA_LE.add(30, 3);
        SAO_PHA_LE.add(20, 4);
        SAO_PHA_LE.add(40, 2);
        SAO_PHA_LE.add(30, 3);
        SAO_PHA_LE.add(20, 4);
        SAO_PHA_LE.add(40, 2);
        SAO_PHA_LE.add(30, 3);
        SAO_PHA_LE.add(20, 4);
        SAO_PHA_LE.add(40, 2);
        SAO_PHA_LE.add(30, 3);
        SAO_PHA_LE.add(20, 4);
        SAO_PHA_LE.add(40, 2);
        SAO_PHA_LE.add(30, 3);
        SAO_PHA_LE.add(20, 4);
        SAO_PHA_LE.add(5, 164);
        
        STAR_DOWN200.add(1, 406);
        STAR_DOWN200.add(1, 407);
        STAR_DOWN200.add(1, 408);
        STAR_DOWN200.add(1, 409);
        STAR_DOWN200.add(1, 410);
        STAR_DOWN200.add(1, 411);
        STAR_DOWN200.add(1, 412);
        STAR_DOWN200.add(1, 413);
        STAR_DOWN200.add(1, 2);
        STAR_DOWN200.add(1, 3);
        STAR_DOWN200.add(1, 4);
        STAR_DOWN200.add(1, 164);
        
        ITEM_THU_LINH.add(4, 406);
        ITEM_THU_LINH.add(10, 407);
        ITEM_THU_LINH.add(10, 408);
        ITEM_THU_LINH.add(10, 409);
        ITEM_THU_LINH.add(10, 410);
        ITEM_THU_LINH.add(10, 411);
        ITEM_THU_LINH.add(10, 412);
        ITEM_THU_LINH.add(10, 413);
        ITEM_THU_LINH.add(40, 1);
        ITEM_THU_LINH.add(30, 2);
        ITEM_THU_LINH.add(20, 3);
        ITEM_THU_LINH.add(40, 1);
        ITEM_THU_LINH.add(30, 2);
        ITEM_THU_LINH.add(20, 3);
        ITEM_THU_LINH.add(40, 1);
        ITEM_THU_LINH.add(30, 2);
        ITEM_THU_LINH.add(20, 3);
        ITEM_THU_LINH.add(40, 1);
        ITEM_THU_LINH.add(30, 2);
        ITEM_THU_LINH.add(20, 3);
        ITEM_THU_LINH.add(40, 1);
        ITEM_THU_LINH.add(30, 2);
        ITEM_THU_LINH.add(20, 3);
        ITEM_THU_LINH.add(40, 1);
        ITEM_THU_LINH.add(30, 2);
        ITEM_THU_LINH.add(20, 3);
        ITEM_THU_LINH.add(4, 838);
        ITEM_THU_LINH.add(15, 160);
        
        ITEM_THU_LINH_DOWN200.add(1, 406);
        ITEM_THU_LINH_DOWN200.add(1, 407);
        ITEM_THU_LINH_DOWN200.add(1, 408);
        ITEM_THU_LINH_DOWN200.add(1, 409);
        ITEM_THU_LINH_DOWN200.add(1, 410);
        ITEM_THU_LINH_DOWN200.add(1, 411);
        ITEM_THU_LINH_DOWN200.add(1, 412);
        ITEM_THU_LINH_DOWN200.add(1, 413);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 1);
        ITEM_THU_LINH_DOWN200.add(1, 2);
        ITEM_THU_LINH_DOWN200.add(1, 3);
        ITEM_THU_LINH_DOWN200.add(1, 838);
        ITEM_THU_LINH_DOWN200.add(1, 160);

        BOSS.add(10, 406);
        BOSS.add(10, 407);
        BOSS.add(10, 408);
        BOSS.add(10, 409);
        BOSS.add(10, 410);
        BOSS.add(10, 411);
        BOSS.add(10, 412);
        BOSS.add(10, 413);
//        BOSS.add(15,813);
//        BOSS.add(15,814);
        BOSS.add(5, 838);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 5);
        BOSS.add(10, 6);
        BOSS.add(10, 160);
        BOSS.add(10, 160);
        BOSS.add(10, 160);
        BOSS.add(10, 160);
        BOSS.add(10, 160);
        BOSS.add(10, 160);
        BOSS.add(10, 160);
        BOSS.add(10, 4);
        BOSS.add(10, 5);
        BOSS.add(10, 4);
        BOSS.add(10, 5);
        BOSS.add(10, 4);
        BOSS.add(10, 5);
        BOSS.add(10, 4);
        BOSS.add(10, 5);
        BOSS.add(10, 4);
        BOSS.add(10, 5);
        BOSS.add(10, 4);
        BOSS.add(10, 5);
        BOSS.add(10, 4);
        BOSS.add(10, 5);
        BOSS.add(10, 164);
        BOSS.add(3, 822);


        BOSS_BARRACK.add(10, 4);
        BOSS_BARRACK.add(10, 5);
        BOSS_BARRACK.add(10, 6);


        BOSS_HELL.add(5, 838);
        BOSS_HELL.add(5, 6);
        BOSS_HELL.add(5, 6);
        BOSS_HELL.add(5, 6);
        BOSS_HELL.add(5, 6);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(5, 164);
        BOSS_HELL.add(4, 822);


        ITEM_NGUC_TU.add(10, 160);
        ITEM_NGUC_TU.add(5, 838);
        ITEM_NGUC_TU.add(10, 817);
        ITEM_NGUC_TU.add(10, 5);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 3);
        ITEM_NGUC_TU.add(10, 5);
        ITEM_NGUC_TU.add(10, 5);
        ITEM_NGUC_TU.add(10, 5);
        ITEM_NGUC_TU.add(10, 5);
        ITEM_NGUC_TU.add(10, 5);
        ITEM_NGUC_TU.add(10, 5);
        ITEM_NGUC_TU.add(5, 164);
        ITEM_NGUC_TU.add(3, 822);

    }
}
