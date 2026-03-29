package com.vdtt.events;

import com.vdtt.data.CMDMenu;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.Zone;
import com.vdtt.mixer.*;
import com.vdtt.mob.ThoDaiCa;
import com.vdtt.model.Char;
import com.vdtt.model.Menu;
import com.vdtt.model.RandomItem;
import com.vdtt.store.ItemStore;
import com.vdtt.store.StoreManager;
import com.vdtt.util.Util;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class
TrungThu2024 extends AbsEvent {

    public int[][] listItemMakeCake = new int[][]{{1131, 1132, 1134, 1133, 1136}, {1131, 1134, 1133, 1136}, {1131, 1132, 1133, 1135}, {1131, 1132, 1133, 1136}, {1139, 1140, 1141, 1142, 1147}, {1139, 1140, 1141, 1142, 1148}};
    public int[][] quantityItemMakeCake = new int[][]{{10, 5, 5, 5, 5}, {10, 5, 5, 5}, {10, 5, 5, 5}, {10, 5, 5, 5}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};
    public int[] coinMakeCake = {5000, 5000, 5000, 5000, -1, -1};
    public int[] finalMakeCake = {1139, 1140, 1141, 1142, 1137, 1138};

    public static int mapThoDaiCa = -1;
    public static int[] locationThoDaiCa = new int[]{-1, -1};
    
    public static int[] hopBanhTopDame = {20, 15, 10, 10};

    @Override
    protected void onPrepare() {
        setRewardToSendWhenRaceEnds(false);
        //addItem vao shop
        ItemStore item1 = ItemStore.builder()
                .id(9997)
                .itemID(1146)
                .sys((byte) 0)
                .ballz(75000)
                .isLock(false)
                .options("")
                .build();
        item1.setType(StoreManager.TYPE_TIEN_LOI);
        StoreManager.getInstance().addGoods(StoreManager.TYPE_TIEN_LOI, item1);

        ItemStore item2 = ItemStore.builder()
                .id(9998)
                .itemID(1147)
                .sys((byte) 0)
                .ballz(50000)
                .isLock(true)
                .options("")
                .build();
        item2.setType(StoreManager.TYPE_KHU_KHOA);
        StoreManager.getInstance().addGoods(StoreManager.TYPE_KHU_KHOA, item2);

        ItemStore item3 = ItemStore.builder()
                .id(9999)
                .itemID(1148)
                .sys((byte) 0)
                .zeni(15)
                .isLock(true)
                .options("")
                .build();
        item3.setType(StoreManager.TYPE_KHU_KHOA);
        StoreManager.getInstance().addGoods(StoreManager.TYPE_KHU_KHOA, item3);

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(this::spawnThoDaiCa, 30, 30, TimeUnit.MINUTES);
    }

    public void spawnThoDaiCa() {
        int[] listMap = {75, 55, 57, 77, 88, 81};
        short[][] listLocation = {{900, 389}, {1560, 429}, {1560, 309}, {1512, 366}, {2000, 416}, {715, 415}};
        int index = Util.nextInt(listMap.length);
        int mapId = listMap[index];
        Map map = MapManager.getInstance().find(mapId);
        Zone zone = map.rand();
        mapThoDaiCa = mapId;
        locationThoDaiCa[0] = listLocation[index][0];
        locationThoDaiCa[1] = listLocation[index][1];
        ThoDaiCa thoDaiCa = (ThoDaiCa) zone.createBoss((short) 134, 450_000_000, (short) 10, listLocation[index][0], listLocation[index][1], 2000000);
        thoDaiCa.zone = zone;
        String text = "c#redBoss " + thoDaiCa.getTemplate().name + " c#whiteđã xuất hiện tại " + map.mapTemplate.name + " khu " + zone.id;
        thoDaiCa.setNotifyText(text);
        zone.addMob(thoDaiCa);
        System.out.println(text);
    }

    @Override
    protected void createRewardForUseItemEvent() {
        RandomItem.ITEM_EVENT_NORMAL.add(40, 1131);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 1132);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 1133);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 1134);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 1135);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 1136);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 4);
        RandomItem.ITEM_EVENT_NORMAL.add(30, 5);
        RandomItem.ITEM_EVENT_NORMAL.add(20, 6);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 7);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 8);
        RandomItem.ITEM_EVENT_NORMAL.add(0.05, 9);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 277);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 161);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 186);
        RandomItem.ITEM_EVENT_NORMAL.add(0.5, 823);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 895);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 158);
        RandomItem.ITEM_EVENT_NORMAL.add(0.5, 827);
        RandomItem.ITEM_EVENT_NORMAL.add(0.5, 828);
        RandomItem.ITEM_EVENT_NORMAL.add(3, 177);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 682);
        RandomItem.ITEM_EVENT_NORMAL.add(3, 712);
        RandomItem.ITEM_EVENT_NORMAL.add(20, 822);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 562);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 564);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 566);
        RandomItem.ITEM_EVENT_NORMAL.add(40, 354);
        RandomItem.ITEM_EVENT_NORMAL.add(0.001, 936);
        RandomItem.ITEM_EVENT_NORMAL.add(0.01, 891);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 555);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 298);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 431);
        RandomItem.ITEM_EVENT_NORMAL.add(0.005, 519);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 284);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 309);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 848);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 972);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 989);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 706);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 705);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 704);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 701);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 702);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 703);
        RandomItem.ITEM_EVENT_NORMAL.add(20, 358);
        RandomItem.ITEM_EVENT_NORMAL.add(20, 359);
        RandomItem.ITEM_EVENT_NORMAL.add(20, 360);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 938);
        RandomItem.ITEM_EVENT_NORMAL.add(0.5, 1114);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1115);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1116);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1117);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1118);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1119);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1120);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1121);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1122);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1123);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1124);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1125);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1126);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1127);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1128);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1120);


        RandomItem.ITEM_EVENT_VIP.add(40, 1131);
        RandomItem.ITEM_EVENT_VIP.add(40, 1132);
        RandomItem.ITEM_EVENT_VIP.add(40, 1133);
        RandomItem.ITEM_EVENT_VIP.add(40, 1134);
        RandomItem.ITEM_EVENT_VIP.add(40, 1135);
        RandomItem.ITEM_EVENT_VIP.add(40, 1136);
        RandomItem.ITEM_EVENT_VIP.add(40, 4);
        RandomItem.ITEM_EVENT_VIP.add(30, 5);
        RandomItem.ITEM_EVENT_VIP.add(20, 6);
        RandomItem.ITEM_EVENT_VIP.add(5, 7);
        RandomItem.ITEM_EVENT_VIP.add(1, 8);
        RandomItem.ITEM_EVENT_VIP.add(0.05, 9);
        RandomItem.ITEM_EVENT_VIP.add(40, 277);
        RandomItem.ITEM_EVENT_VIP.add(5, 161);
        RandomItem.ITEM_EVENT_VIP.add(10, 186);
        RandomItem.ITEM_EVENT_VIP.add(0.5, 823);
        RandomItem.ITEM_EVENT_VIP.add(10, 895);
        RandomItem.ITEM_EVENT_VIP.add(2, 158);
        RandomItem.ITEM_EVENT_VIP.add(0.5, 827);
        RandomItem.ITEM_EVENT_VIP.add(0.5, 828);
        RandomItem.ITEM_EVENT_VIP.add(3, 177);
        RandomItem.ITEM_EVENT_VIP.add(1, 682);
        RandomItem.ITEM_EVENT_VIP.add(3, 712);
        RandomItem.ITEM_EVENT_VIP.add(20, 822);
        RandomItem.ITEM_EVENT_VIP.add(40, 562);
        RandomItem.ITEM_EVENT_VIP.add(40, 564);
        RandomItem.ITEM_EVENT_VIP.add(40, 566);
        RandomItem.ITEM_EVENT_VIP.add(40, 354);
        RandomItem.ITEM_EVENT_VIP.add(0.01, 936);
        RandomItem.ITEM_EVENT_VIP.add(0.005, 891);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 555);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 298);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 431);
        RandomItem.ITEM_EVENT_VIP.add(0.005, 519);
        RandomItem.ITEM_EVENT_VIP.add(1, 284);
        RandomItem.ITEM_EVENT_VIP.add(1, 309);
        RandomItem.ITEM_EVENT_VIP.add(1, 848);
        RandomItem.ITEM_EVENT_VIP.add(1, 972);
        RandomItem.ITEM_EVENT_VIP.add(1, 989);
        RandomItem.ITEM_EVENT_VIP.add(2, 706);
        RandomItem.ITEM_EVENT_VIP.add(2, 705);
        RandomItem.ITEM_EVENT_VIP.add(0.005, 460);
        RandomItem.ITEM_EVENT_VIP.add(0.01, 555);
        RandomItem.ITEM_EVENT_VIP.add(0.01, 525);
        RandomItem.ITEM_EVENT_VIP.add(0.06, 896);
        RandomItem.ITEM_EVENT_VIP.add(0.06, 897);
        RandomItem.ITEM_EVENT_VIP.add(0.06, 898);
        RandomItem.ITEM_EVENT_VIP.add(0.01, 918);
        RandomItem.ITEM_EVENT_VIP.add(0.01, 919);
        RandomItem.ITEM_EVENT_VIP.add(0.01, 920);
        RandomItem.ITEM_EVENT_VIP.add(2, 704);
        RandomItem.ITEM_EVENT_VIP.add(10, 701);
        RandomItem.ITEM_EVENT_VIP.add(10, 702);
        RandomItem.ITEM_EVENT_VIP.add(10, 703);
        RandomItem.ITEM_EVENT_VIP.add(20, 358);
        RandomItem.ITEM_EVENT_VIP.add(0.005, 1144);
        RandomItem.ITEM_EVENT_VIP.add(20, 359);
        RandomItem.ITEM_EVENT_VIP.add(20, 360);
        RandomItem.ITEM_EVENT_VIP.add(5, 938);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1114);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1115);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1116);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1117);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1118);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1119);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1120);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1121);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1122);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1123);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1124);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1125);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1126);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1127);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1128);
        RandomItem.ITEM_EVENT_VIP.add(0.2, 1120);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 1050);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 1051);
    }

    @Override
    protected void doCreateRace() {
        EventRace eatCreamRace = new EventRace(KeyPoint.MAKE_CAKE, """
                - Trong thời gian diễn ra sự kiện, farm quái theo sức mạnh tương ứng có tỉ lệ rớt nguyên liệu sự kiện: trứng, bột, mì, hạt sen, đường, đậu xanh, mứt (Không khóa)
                
                - Làm Bánh trung thu tại Sứ giả sự kiện tại Nhà Bulma
                
                - Bánh Thập Cẩm = 10 Bột + 5 Trứng + 5 Hạt sen + 5 Đường + 5 Mứt + 75.000 Ballz
                
                - Bánh Dẻo = 10 Bột + 5 Hạt sen + 5 Đường + 5 Mứt + 5.000 Xu
                
                - Bánh Đậu xanh = 10 Bột + 5 Trứng + 5 Đường + 5 Đậu xanh + 5.000 Xu
                
                - Bánh Pía = 10 Bột + 5 Trứng + 5 Đường + 5 Mứt + 5.000 Xu
                
                - Hộp bánh thường = 4 loại bánh + 1 giấy gói thường
                
                - Hộp bánh thượng hạng = 4 loại bánh + 1 giấy gói cao cấp
                
                - Khi sử dụng hộp bánh thượng hạng sẽ nhận được 2 điểm đua top làm bánh và 1 phần quà ngẫu nhiên
                
                - Khi sử dụng hộp bánh thường sẽ nhận được 1 điểm đua top làm bánh và 1 phần quà ngẫu nhiên
                """, type.getStartDate(), type.getEndDate());


        eatCreamRace.addRewardTop(1, 1,
                List.of(new Item(5, "sk"),
                        new Item(0, "sk"),
                        new Item(1, "sk"),
                        new Item(2, "sk"),
                        new Item(3, "sk"))
                , item -> {
                    // Thêm các thuộc tính cho item
                });

        eatCreamRace.addRewardTop(2, 2, List.of(new Item(5, "sk"),
                new Item(4, "sk"),
                new Item(5, "sk"),
                new Item(6, "sk"),
                new Item(7, "sk")), item -> {

        });

        eatCreamRace.addRewardTop(3, 3, List.of(new Item(5, "sk"),
                new Item(8, "sk"),
                new Item(9, "sk"),
                new Item(10, "sk"),
                new Item(11, "sk")), item -> {

        });

        eatCreamRace.addRewardTop(4, 4, List.of(new Item(5, "sk"),
                new Item(12, "sk"),
                new Item(13, "sk"),
                new Item(14, "sk"),
                new Item(15, "sk")), item -> {

        });

        eatCreamRace.addRewardTop(5, 5, List.of(new Item(5, "sk"),
                new Item(16, "sk"),
                new Item(17, "sk"),
                new Item(18, "sk"),
                new Item(19, "sk")), item -> {

        });

        eatCreamRace.addRewardTop(6, 10, List.of(new Item(5, "sk"),
                new Item(20, "sk"),
                new Item(21, "sk"),
                new Item(22, "sk"),
                new Item(23, "sk")), item -> {

        });
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onEnd() {

    }

    @Override
    protected void setKeys() {
        addKey(KeyPoint.MAKE_CAKE);
        addKey(KeyPoint.THA_LONG_DEN);
    }

    @Override
    protected void onCreateMenu(Char player, int npcId, List<Menu> menuList) {
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm bánh thập cẩm", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            for (int i = 0; i < listItemMakeCake[0].length; i++) {
                ingredientContainer.addIngredient(new Ingredient(listItemMakeCake[0][i], quantityItemMakeCake[0][i]));
            }
            ingredientContainer.addIngredient(new Coin(coinMakeCake[0]));
            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(finalMakeCake[0], 1, true));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm bánh dẻo", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            for (int i = 0; i < listItemMakeCake[1].length; i++) {
                ingredientContainer.addIngredient(new Ingredient(listItemMakeCake[1][i], quantityItemMakeCake[1][i]));
            }
            ingredientContainer.addIngredient(new Coin(coinMakeCake[1]));
            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(finalMakeCake[1], 1, true));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm bánh đậu xanh", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            for (int i = 0; i < listItemMakeCake[2].length; i++) {
                ingredientContainer.addIngredient(new Ingredient(listItemMakeCake[2][i], quantityItemMakeCake[2][i]));
            }
            ingredientContainer.addIngredient(new Coin(coinMakeCake[2]));
            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(finalMakeCake[2], 1, true));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm bánh pía", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            for (int i = 0; i < listItemMakeCake[3].length; i++) {
                ingredientContainer.addIngredient(new Ingredient(listItemMakeCake[3][i], quantityItemMakeCake[3][i]));
            }
            ingredientContainer.addIngredient(new Coin(coinMakeCake[3]));
            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(finalMakeCake[3], 1, true));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm hộp bánh thường", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            for (int i = 0; i < listItemMakeCake[4].length; i++) {
                ingredientContainer.addIngredient(new Ingredient(listItemMakeCake[4][i], quantityItemMakeCake[4][i]));
            }
            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(finalMakeCake[4], 1, true));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm hộp bánh thượng hạng", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            for (int i = 0; i < listItemMakeCake[5].length; i++) {
                ingredientContainer.addIngredient(new Ingredient(listItemMakeCake[5][i], quantityItemMakeCake[5][i]));
            }
            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(finalMakeCake[5], 1, true));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        if (ThoDaiCa.current != null) {
            menuList.add(new Menu(CMDMenu.EXECUTE, "Di chuyển tới Thỏ đại ca", () -> {
                if (ThoDaiCa.current == null || mapThoDaiCa == -1) {
                    return;
                }
                player.setXY(locationThoDaiCa[0], locationThoDaiCa[1]);
                MapManager.getInstance().joinZone(player, mapThoDaiCa, 0, 1);
            }));
        }
    }

    @Override
    protected void createDropList() {
        setDropRate(50);
        addDrop(1, 1131);
        addDrop(1, 1132);
        addDrop(1, 1133);
        addDrop(1, 1134);
        addDrop(1, 1135);
        addDrop(1, 1136);
    }
}
