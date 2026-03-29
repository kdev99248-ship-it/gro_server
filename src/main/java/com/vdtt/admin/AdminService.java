package com.vdtt.admin;

import com.vdtt.data.CMDMenu;
import com.vdtt.handler.UseItemHandler;
import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemTemplate;
import com.vdtt.model.Char;
import com.vdtt.model.Menu;
import com.vdtt.server.Server;
import com.vdtt.server.ServerManager;
import com.vdtt.util.Util;
import lombok.Getter;

import java.util.List;

public class AdminService {

    @Getter
    private static final AdminService instance = new AdminService();


    public void openUIAdmin(Char p, int npcid) {
        p.menus.clear();
//        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đi tới", () -> {
//
//        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Thông tin", () -> {
            showServerInfo(p);
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảo trì", () -> {
            Util.setTimeoutSchedule(() -> Server.maintenance((byte) 1), 0);
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Lưu dữ liệu", () -> {
            Util.setTimeoutSchedule(() -> {
                Server.saveAll();
                p.serverDialog("Đã lưu dữ liệu!");
            }, 0);
        }));
//        p.menus.add(new Menu(CMDMenu.EXECUTE, "Tìm người chơi", () -> {
//
//        }));
//        p.menus.add(new Menu(CMDMenu.EXECUTE, "Nhập lệnh", () -> {
//
//        }));
//        p.menus.add(new Menu(CMDMenu.EXECUTE, "Add Item", () -> {
//            List<ItemTemplate> setTamThu = ItemManager.getInstance().createSet(1, p.sys);
//            ItemTemplate itemTemplate = setTamThu.get(Util.nextInt(0, setTamThu.size() - 1));
//            Item item1 = new Item(itemTemplate.id, "item");
//            item1.createOptionRandom(4, 7);
//            item1.addOption(2,100000000 );
//            item1.setQuantity(1);
//            item1.isLock = true;
//            item1.upgradeItem((byte) 8);
//            p.addItemToBag(item1);
//            p.getService().addItem(item1);
//        }));
        p.getService().openUIMenu(npcid);
    }

    private void showServerInfo(Char p) {
        long total, free, used;
        double mb = 1024 * 1024;
        Runtime runtime = Runtime.getRuntime();
        total = runtime.totalMemory();
        free = runtime.freeMemory();
        used = total - free;
        StringBuilder sb = new StringBuilder();
        sb.append("- Số người đang online: ").append(ServerManager.getNumberOnline()).append("\n");
        sb.append("- Memory usage (JVM): ")
                .append(String.format("%.1f/%.1f MB (%d%%)", used / mb, total / mb, (used * 100 / total))).append("\n");
        p.getService().serverDialog(sb.toString());
    }
}
