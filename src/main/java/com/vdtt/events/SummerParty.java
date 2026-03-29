package com.vdtt.events;

import com.vdtt.data.CMDMenu;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.MoroQuest;
import com.vdtt.map.zones.Zone;
import com.vdtt.mixer.*;
import com.vdtt.mob.Boss;
import com.vdtt.mob.CellMax;
import com.vdtt.model.Char;
import com.vdtt.model.Menu;
import com.vdtt.model.RandomItem;
import com.vdtt.party.Group;
import com.vdtt.server.SpawnBossManager;
import com.vdtt.store.ItemStore;
import com.vdtt.store.StoreManager;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class
SummerParty extends AbsEvent {
    private ScheduledExecutorService executorService;
    public int totalCellKilled;

    @Override
    protected void onPrepare() {
        setRewardToSendWhenRaceEnds(false);

        ItemStore goods = ItemStore.builder()
                .id(9999)
                .itemID(990)
                .sys((byte) 0)
                .expire(TimeUnit.HOURS.toMillis(24))
                .ballz(80_000 * 24)
                .isLock(true)
                .options("")
                .build();
        goods.setType(StoreManager.TYPE_KHU_KHOA);
        StoreManager.getInstance().addGoods(StoreManager.TYPE_KHU_KHOA, goods);
        initMap();
        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(this::spawnCellMax, 30, 30, TimeUnit.MINUTES);
    }

    private void initMap() {
        spawnBosses(75, 900, 389);
        spawnBosses(55, 1560, 429);
        spawnBosses(57, 1560, 309);
        spawnBosses(77, 1512, 366);
        spawnBosses(88, 2000, 416);
        spawnBosses(81, 715, 415);
    }

    private void spawnBosses(int mapId, int x, int y) {
        Map map = MapManager.getInstance().find(mapId);
        List<Zone> zones = map.getZones();
        for (Zone zone : zones) {
            Boss cell = zone.createBoss((short) 77, 5_000_000, (short) 10, (short) x, (short) y, 1000);
            cell.zone = zone;
            zone.addMob(cell);
        }
    }

    public void spawnCellMax() {
        int mapId = 44;
        int x = 497;
        int y = 594;
        Map map = MapManager.getInstance().find(mapId);
        Zone zone = map.rand();
        CellMax cellMax = (CellMax) zone.createBoss((short) 296, 200_000_000, (short) 10, (short) x, (short) y, 500000);
        cellMax.zone = zone;
        String text = "c#redBoss " + cellMax.getTemplate().name + " c#whiteđã xuất hiện tại " + map.mapTemplate.name + " khu " + zone.id;
        cellMax.setNotifyText(text);
        zone.addMob(cellMax);
    }

    @Override
    protected void createRewardForUseItemEvent() {
        RandomItem.ITEM_EVENT_NORMAL.add(5, 966);
        RandomItem.ITEM_EVENT_NORMAL.add(5, 967);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 968);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 969);
        RandomItem.ITEM_EVENT_NORMAL.add(2, 970);
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
        RandomItem.ITEM_EVENT_NORMAL.add(0.1, 374);
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


        RandomItem.ITEM_EVENT_VIP.add(5, 966);
        RandomItem.ITEM_EVENT_VIP.add(5, 967);
        RandomItem.ITEM_EVENT_VIP.add(2, 968);
        RandomItem.ITEM_EVENT_VIP.add(2, 969);
        RandomItem.ITEM_EVENT_VIP.add(2, 970);
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
        RandomItem.ITEM_EVENT_VIP.add(0.001, 936);
        RandomItem.ITEM_EVENT_VIP.add(0.01, 891);
        RandomItem.ITEM_EVENT_VIP.add(0.5, 374);
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
        RandomItem.ITEM_EVENT_VIP.add(2, 704);
        RandomItem.ITEM_EVENT_VIP.add(10, 701);
        RandomItem.ITEM_EVENT_VIP.add(10, 702);
        RandomItem.ITEM_EVENT_VIP.add(10, 703);
        RandomItem.ITEM_EVENT_VIP.add(20, 358);
        RandomItem.ITEM_EVENT_VIP.add(20, 359);
        RandomItem.ITEM_EVENT_VIP.add(20, 360);
        RandomItem.ITEM_EVENT_VIP.add(5, 938);
    }

    @Override
    protected void doCreateRace() {
        EventRace eatCreamRace = new EventRace(KeyPoint.EAT_CREAM, """
                - Trong thời gian diễn ra sự kiện, farm quái theo sức mạnh tương ứng có tỉ lệ rớt nguyên liệu sự kiện: Đường, trứng , sữa, dưa hấu, dừa

                - Làm kem tại Sứ giả sự kiện tại Nhà Bulma

                - Kem Dừa : 5 Đường + 5 Trứng + 5 Sữa + 5 Dừa + 15 Zeni

                - Kem Dưa Hấu : 5 Đường + 5 Trứng + 5 Sữa + 5 Dưa hấu + 75.000 Ballz

                - Sử dụng Kem Dừa nhận được 2 điểm Đua Top Ăn Kem + 50k-100k xu + 1 phần quà ngẫu nhiên

                - Sử dụng Kem Dưa Hấu nhận được 1 điểm Đua Top Ăn Kem + 50k-100k xu + 1 phần quà ngẫu nhiên""", type.getStartDate(), type.getEndDate());


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


        EventRace killCellRace = new EventRace(KeyPoint.KILL_CELL, """
                - Cell xuất hiện tại tất cả các khu của các map đầu của các hành tinh (ví dụ hành tinh fide thì xuất hiện ở căn cứ trung tâm). Bị hạ xong 30s sau hồi sinh.

                - Tại shop có bán Thẻ Sự kiện giá 80.000 ballz. Có Thẻ Sự kiện trong hành trang sẽ pem dc boss Cell (sức mạnh nào cũng có thể pem)

                - Mỗi 200 Cell của cả server bị hạ thì Cell Max (boss to) sẽ xuất hiện tại 1 map nào đó được thông báo trên KTG (sm nào cũng có thể pem, ko cần thẻ sk).

                - Hạ Cell cho 1 Điểm Đua Top Săn Cell + 50k-100k xu + 1 phần quà ngẫu nhiên

                - Hạ Cell Max cho 50 EXP vũ trụ và rất nhiều quà ngon.""", type.getStartDate(), type.getEndDate());


        killCellRace.addRewardTop(1, 1, List.of(new Item(5, "sk"),
                new Item(6, "sk"),
                new Item(7, "sk"),
                new Item(8, "sk"),
                new Item(9, "sk")), item -> {

        });
        killCellRace.addRewardTop(2, 2, List.of(new Item(5, "sk"),
                new Item(6, "sk"),
                new Item(7, "sk"),
                new Item(8, "sk"),
                new Item(9, "sk")), item -> {

        });
        killCellRace.addRewardTop(3, 3, List.of(new Item(5, "sk"),
                new Item(6, "sk"),
                new Item(7, "sk"),
                new Item(8, "sk"),
                new Item(9, "sk")), item -> {

        });
        killCellRace.addRewardTop(4, 4, List.of(new Item(5, "sk"),
                new Item(6, "sk"),
                new Item(7, "sk"),
                new Item(8, "sk"),
                new Item(9, "sk")), item -> {

        });
        killCellRace.addRewardTop(5, 5, List.of(new Item(5, "sk"),
                new Item(6, "sk"),
                new Item(7, "sk"),
                new Item(8, "sk"),
                new Item(9, "sk")), item -> {

        });
        killCellRace.addRewardTop(6, 10, List.of(new Item(5, "sk"),
                new Item(6, "sk"),
                new Item(7, "sk"),
                new Item(8, "sk"),
                new Item(9, "sk")), item -> {

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
        addKey(KeyPoint.EAT_CREAM);
        addKey(KeyPoint.KILL_CELL);
    }

    @Override
    protected void onCreateMenu(Char player, int npcId, List<Menu> menuList) {
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm kem dừa", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            ingredientContainer.addIngredient(new Ingredient(966, 5));
            ingredientContainer.addIngredient(new Ingredient(967, 5));
            ingredientContainer.addIngredient(new Ingredient(968, 5));
            ingredientContainer.addIngredient(new Ingredient(970, 5));
            ingredientContainer.addIngredient(new Zeni(15));

            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(965, 1,true));

            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Làm kem dưa hấu", () -> {
            if (player.user.activated == 1) {
                player.serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            IngredientContainer ingredientContainer = new IngredientContainer();
            ingredientContainer.addIngredient(new Ingredient(966, 5));
            ingredientContainer.addIngredient(new Ingredient(967, 5));
            ingredientContainer.addIngredient(new Ingredient(968, 5));
            ingredientContainer.addIngredient(new Ingredient(969, 5));
            ingredientContainer.addIngredient(new Ballz(75_000));

            ProductContainer productContainer = new ProductContainer();
            productContainer.addProduct(new Product(964, 1,true));

            Mixer.mix(player, ingredientContainer, productContainer, true);
        }));
        menuList.add(new Menu(CMDMenu.EXECUTE, "Phong Ấn Phù Thuỷ Ngân Hà (200 Zeni)", () -> {
            int fee = 200;
            if (player.zeni < fee) {
                player.serverDialog("Bạn không đủ " + fee + " Zeni!");
                return;
            }
            Group group = player.getGroup();
            if (group == null) {
                player.addZeni(-200, true);
                MoroQuest moroQuest = new MoroQuest();
                player.addWorld(moroQuest);
                moroQuest.joinZone(player);
            } else {
                if (group.getIndexById(player.id) != 0) {
                    player.serverDialog("Phải là trưởng nhóm mới có thể mở phó bản");
                } else {
                    List<Char> chars = group.getCharsInZone(player.zone.map.id, player.zone.id);
                    if (chars.size() < group.getNumberMember()) {
                        player.serverDialog("Vui lòng tập hợp đủ thành viên trước khi tham gia");
                        return;
                    }
                    for (Char _char : chars) {
                        if (_char != null && !_char.isCleaned && _char != player && !_char.isDead) {
                            if (_char.zeni < fee) {
                                player.serverDialog("Người chơi " + _char.name + " không đủ " + fee + " Zeni!");
                                return;
                            }
                        }
                    }
                    MoroQuest moroQuest = new MoroQuest();
                    for (Char _char : chars) {
                        if (_char != null && !_char.isCleaned && !_char.isDead) {
                            _char.addZeni(-200, true);
                            _char.addWorld(moroQuest);
                            moroQuest.joinZone(_char);
                        }
                    }
                }
            }
        }));
        if (CellMax.current != null) {
            menuList.add(new Menu(CMDMenu.EXECUTE, "Di chuyển tới Cell Max", () -> {
                if (CellMax.current == null) {
                    return;
                }
                player.setXY(345, 594);
//                CellMax.current.zone.join(player, -1);
                MapManager.getInstance().joinZone(player, 44, 0, 1);
            }));
        }
    }

    @Override
    protected void createDropList() {
        setDropRate(50);

        addDrop(1, 966);
        addDrop(1, 967);
        addDrop(1, 968);
        addDrop(1, 969);
        addDrop(1, 970);
    }
}
