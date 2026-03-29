package com.vdtt.events;

import com.vdtt.data.CMDMenu;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.Zone;
import com.vdtt.mixer.*;
import com.vdtt.mob.Dracubin;
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
TetNguyenDan extends AbsEvent {

    public int[][] listItemMakeCake = new int[][]{{949, 947, 946}, {948, 945}};
    public int[][] quantityItemMakeCake = new int[][]{{1, 5, 10}, {5, 20}};
    public int[] coinMakeCake = {75000, 25000};
    public int[] finalMakeCake = {950, 951};

    public static int mapThoDaiCa = -1;
    public static int[] locationThoDaiCa = new int[]{-1, -1};
    
    public static int[] hopBanhTopDame = {20, 15, 10, 10};

    @Override
    protected void onPrepare() {
        setRewardToSendWhenRaceEnds(false);
        //addItem vao shop
        ItemStore item1 = ItemStore.builder()
                .id(9997)
                .itemID(948)
                .sys((byte) 0)
                .ballz(75000)
                .isLock(false)
                .expire(-1)
                .options("")
                .build();
        item1.setType(StoreManager.TYPE_TIEN_LOI);
        StoreManager.getInstance().addGoods(StoreManager.TYPE_TIEN_LOI, item1);

        ItemStore item2 = ItemStore.builder()
                .id(9998)
                .itemID(952)
                .sys((byte) 0)
                .expire(-1)
                .zeni(10)
                .isLock(true)
                .options("")
                .build();
        item2.setType(StoreManager.TYPE_KHU_KHOA);
        StoreManager.getInstance().addGoods(StoreManager.TYPE_KHU_KHOA, item2);

        ItemStore item3 = ItemStore.builder()
                .id(9999)
                .itemID(949)
                .sys((byte) 0)
                .expire(-1)
                .zeni(15)
                .isLock(true)
                .options("")
                .build();
        item3.setType(StoreManager.TYPE_KHU_KHOA);
        StoreManager.getInstance().addGoods(StoreManager.TYPE_KHU_KHOA, item3);

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(this::spawnThoDaiCa, 0, 30, TimeUnit.MINUTES);
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
        Dracubin thoDaiCa = (Dracubin) zone.createBoss((short) 245, 50_000_000, (short) 10, listLocation[index][0], listLocation[index][1], 50_000);
        thoDaiCa.zone = zone;
        String text = "c#redBoss " + thoDaiCa.getTemplate().name + " c#whiteđã xuất hiện tại " + map.mapTemplate.name + " khu " + zone.id;
        thoDaiCa.setNotifyText(text);
        zone.addMob(thoDaiCa);
        System.out.println(text);
    }

    @Override
    protected void createRewardForUseItemEvent() {
//        RandomItem.ITEM_EVENT_NORMAL.add(40, 1131);
//        RandomItem.ITEM_EVENT_NORMAL.add(40, 1132);
//        RandomItem.ITEM_EVENT_NORMAL.add(40, 1133);
//        RandomItem.ITEM_EVENT_NORMAL.add(40, 1134);
//        RandomItem.ITEM_EVENT_NORMAL.add(40, 1135);
//        RandomItem.ITEM_EVENT_NORMAL.add(40, 1136);
        RandomItem.ITEM_EVENT_NORMAL.add(20, 4);
        RandomItem.ITEM_EVENT_NORMAL.add(15, 5);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 6);
        RandomItem.ITEM_EVENT_NORMAL.add(2.5, 7);
        RandomItem.ITEM_EVENT_NORMAL.add(0.5, 8);
        RandomItem.ITEM_EVENT_NORMAL.add(0.025, 9);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 277);
        RandomItem.ITEM_EVENT_NORMAL.add(2.5, 161);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 186);
        RandomItem.ITEM_EVENT_NORMAL.add(0.25, 823);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 895);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 158);
        RandomItem.ITEM_EVENT_NORMAL.add(0.25, 827);
        RandomItem.ITEM_EVENT_NORMAL.add(0.25, 828);
        RandomItem.ITEM_EVENT_NORMAL.add(1.5, 177);
        RandomItem.ITEM_EVENT_NORMAL.add(0.5, 682);
        RandomItem.ITEM_EVENT_NORMAL.add(1.5, 712);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 822);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 562);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 564);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 566);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 354);
        RandomItem.ITEM_EVENT_NORMAL.add(0.001, 936);
        RandomItem.ITEM_EVENT_NORMAL.add(0.01, 891);
        RandomItem.ITEM_EVENT_NORMAL.add(0.05, 555);
        RandomItem.ITEM_EVENT_NORMAL.add(0.05, 298);
        RandomItem.ITEM_EVENT_NORMAL.add(0.05, 431);
        RandomItem.ITEM_EVENT_NORMAL.add(0.001, 519);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 284);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 309);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 848);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 972);
        RandomItem.ITEM_EVENT_NORMAL.add(1, 989);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 706);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 705);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 704);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 701);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 702);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 703);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 358);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 359);
        RandomItem.ITEM_EVENT_NORMAL.add(10, 360);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 938);
        RandomItem.ITEM_EVENT_NORMAL.add(0.2, 1114);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1115);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1116);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1117);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1118);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1119);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1120);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1121);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1122);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1123);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1124);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1125);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1126);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1127);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1128);
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 1120);


//        RandomItem.ITEM_EVENT_VIP.add(40, 1131);
//        RandomItem.ITEM_EVENT_VIP.add(40, 1132);
//        RandomItem.ITEM_EVENT_VIP.add(40, 1133);
//        RandomItem.ITEM_EVENT_VIP.add(40, 1134);
//        RandomItem.ITEM_EVENT_VIP.add(40, 1135);
//        RandomItem.ITEM_EVENT_VIP.add(40, 1136);
        RandomItem.ITEM_EVENT_VIP.add(20, 1086);
        RandomItem.ITEM_EVENT_VIP.add(20, 953);
        RandomItem.ITEM_EVENT_VIP.add(40, 4);
        RandomItem.ITEM_EVENT_VIP.add(30, 5);
        RandomItem.ITEM_EVENT_VIP.add(20, 6);
        RandomItem.ITEM_EVENT_VIP.add(5, 7);
        RandomItem.ITEM_EVENT_VIP.add(1, 8);
        RandomItem.ITEM_EVENT_VIP.add(0.05, 9);
        RandomItem.ITEM_EVENT_VIP.add(25, 277);
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
        RandomItem.ITEM_EVENT_VIP.add(25, 822);
        RandomItem.ITEM_EVENT_VIP.add(25, 562);
        RandomItem.ITEM_EVENT_VIP.add(25, 564);
        RandomItem.ITEM_EVENT_VIP.add(25, 566);
        RandomItem.ITEM_EVENT_VIP.add(25, 354);
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
        RandomItem.ITEM_EVENT_VIP.add(0.1, 1053);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 1054);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 1055);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 1056);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 1057);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 829);
        RandomItem.ITEM_EVENT_VIP.add(0.1, 958);
    }

    @Override
    protected void doCreateRace() {
        EventRace eatCreamRace = new EventRace(KeyPoint.BANH_CHUNG, """
                - Trong thời gian diễn ra sự kiện, farm quái theo sức mạnh tương ứng có tỉ lệ rớt nguyên liệu sự kiện: bột gạo, bột nếp, thịt heo.
                
                - Trong cửa hàng có các vật phẩm sự kiện hỗ trợ khác như: lá dong, lá chuối, thiệp chúc tết.
                
                - Làm Bánh tại Sứ giả sự kiện tại Nhà Bulma
                
                - Bánh Chưng = 1 Lá dong + 5 thịt heo + 10 bột nếp + 75.000 Ballz
                
                - Bánh Giầy = 5 Lá chuối + 10 bột gạo + 25.000 Xu
                
                - Khi sử dụng hộp bánh chưng sẽ nhận được 1 điểm đua top bánh chưng và 1 phần quà ngẫu nhiên
                
                - Khi sử dụng hộp bánh giầy sẽ nhận được 1 điểm đua top bánh giầy và 1 phần quà ngẫu nhiên
                
                - Khi sử dụng thiệp chúc tết sẽ tặng được món quà bất kỳ của cả 2 bánh cho người được tặng và người tặng sẽ được tính 1 điểm đua vào top random cho phần quà nhận được
                
                - Trong thời gian diễn ra sự kiện, sau mỗi 30 phút sẽ xuất hiện boss dracubin, săn boss sẽ rơi item, quà và vật phẩm.
                """, type.getStartDate(), type.getEndDate());

        eatCreamRace = new EventRace(KeyPoint.BANH_GIAY, """
                - Trong thời gian diễn ra sự kiện, farm quái theo sức mạnh tương ứng có tỉ lệ rớt nguyên liệu sự kiện: bột gạo, bột nếp, thịt heo.
                
                - Trong cửa hàng có các vật phẩm sự kiện hỗ trợ khác như: lá dong, lá chuối, thiệp chúc tết.
                
                - Làm Bánh tại Sứ giả sự kiện tại Nhà Bulma
                
                - Bánh Chưng = 1 Lá dong + 5 thịt heo + 10 bột nếp + 75.000 Ballz
                
                - Bánh Giầy = 5 Lá chuối + 10 bột gạo + 25.000 Xu
                
                - Khi sử dụng hộp bánh chưng sẽ nhận được 1 điểm đua top bánh chưng và 1 phần quà ngẫu nhiên
                
                - Khi sử dụng hộp bánh giầy sẽ nhận được 1 điểm đua top bánh giầy và 1 phần quà ngẫu nhiên
                
                - Khi sử dụng hộp thiệp chúc tết sẽ tặng được món quà bất kỳ của cả 2 bánh cho người được tặng và người tặng sẽ được tính 1 điểm đua vào top random cho phần quà nhận được
                
                - Trong thời gian diễn ra sự kiện, sau mỗi 30 phút sẽ xuất hiện boss dracubin, săn boss sẽ rơi item, quà và vật phẩm.
                """, type.getStartDate(), type.getEndDate());
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onEnd() {

    }

    @Override
    protected void setKeys() {
        addKey(KeyPoint.BANH_CHUNG);
        addKey(KeyPoint.BANH_GIAY);
    }

    @Override
    protected void onCreateMenu(Char player, int npcId, List<Menu> menuList) {
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm bánh chưng", () -> {
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
            ingredientContainer.addIngredient(new Ballz(coinMakeCake[0]));
            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(finalMakeCake[0], 1, false));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm bánh giầy", () -> {
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
            productContainer.addProduct(new Product(finalMakeCake[1], 1, false));
            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        
        if (Dracubin.current != null) {
            menuList.add(new Menu(CMDMenu.EXECUTE, "Di chuyển tới Dracubin", () -> {
                if (Dracubin.current == null || mapThoDaiCa == -1) {
                    return;
                }
                player.setXY(locationThoDaiCa[0], locationThoDaiCa[1]);
                MapManager.getInstance().joinZone(player, mapThoDaiCa, 0, 1);
            }));
        }
    }

    @Override
    protected void createDropList() {
        setDropRate(20);
        addDrop(1, 945);
        addDrop(1, 946);
        addDrop(1, 947);
    }
}
