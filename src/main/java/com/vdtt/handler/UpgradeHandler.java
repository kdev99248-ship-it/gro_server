package com.vdtt.handler;

import com.vdtt.data.DataCenter;
import com.vdtt.item.Item;
import com.vdtt.item.ItemOption;
import com.vdtt.model.Char;
import com.vdtt.network.Message;

import java.io.IOException;
import java.util.*;

public class UpgradeHandler {

    public static void upgradeMask(Char player, Message ms) {
        player.lockItem.lock();
        try {
            byte size = ms.reader().readByte();
            byte[] typeTemp = new byte[size];
            short[] indexArr = new short[size];
            boolean[][] list = new boolean[4][player.numberCellBag];
            List<Item> items = new ArrayList<>();
            Set<Integer> keys = new HashSet<>();
            for (int i = 0; i < size; i++) {
                byte type = ms.reader().readByte();
                short index = ms.reader().readShort();
                Item[] array = player.checkBag(type);
                if (index >= 0 && index < array.length && !list[type][index]) {
                    Item item = array[index];
                    if (item != null && item.getTemplate().type == 14) {
                        if (i > 0 && item.upgrade > 0) {
                            player.serverDialog("Không thể ghép cải trang đã nâng cấp");
                            return;
                        }
                        if (item.expire != -1) {
                            player.serverDialog("Không thể ghép cải trang có hạn sử dụng");
                            return;
                        }
                        list[type][index] = true;
                        typeTemp[i] = type;
                        indexArr[i] = index;
                        items.add(item);
                    }
                }
            }
            if (items.size() < 2) {
                player.serverDialog("Đặt vào ít nhất 2 cải trang khác loại!");
                return;
            }

            if (items.get(0).getUpgrade() > 15) {
                player.serverDialog("Cải trang đã đạt giới hạn ghép!");
                return;
            }
            List<Item> allMask = getAllMask(items.get(0));
            if (allMask.isEmpty()) {
                allMask.add(items.get(0));
            }
            List<Item> fullMask = new ArrayList<>(allMask);
            for (Item mask : allMask) {
                keys.add(mask.id);
            }
            for (int i = 1; i < items.size(); i++) {
                Item mask = items.get(i);
                if (keys.contains(mask.id)) {
                    player.serverDialog("Không thể ghép 2 " + mask.getTemplate().name + " vào cùng một cải trang!");
                    return;
                }
                keys.add(mask.id);
                fullMask.add(mask);
            }
            if (fullMask.size() > 22) {
                player.serverDialog("Chỉ được ghép tôi đa 22 cải trang!");
                return;
            }
            int fee = (items.size() - 1) * 200_000;
            if (player.ballZ < fee) {
                player.serverDialog("Không đủ Ballz!");
                return;
            }
            player.addBallz(-fee, true);
            for (int j = 1; j < size; j++) {
                player.checkBag(typeTemp[j])[indexArr[j]] = null;
            }
            fusion(fullMask);
            Item dest = fullMask.get(0);
            dest.index = indexArr[0];
            dest.isLock = true;
            player.checkBag(typeTemp[0])[dest.index] = dest;
            player.getService().ghepCaiTrang(size, typeTemp, indexArr, dest);
            player.setAbility();
            player.getService().updateIndexBag();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            player.lockItem.unlock();
        }
    }

    private static void fusion(List<Item> allMask) {
        Item mainMask = allMask.get(0);
        StringBuilder str = new StringBuilder();
        int level = 0;
        for (Item item : allMask) {
            if (item != null) {
                str.append(item.id).append("@").append(item.strOption).append("&");
                ++level;
            }
        }
        str.deleteCharAt(str.length() - 1);
        mainMask.optionGhepCaiTrang = str.toString();
        Vector<ItemOption> fullOptions = new Vector<>();
        Vector<ItemOption> otherOptions = new Vector<>();
        for (Item item : allMask) {
            ItemOption[] options = item.options();
            if (options != null) {
                if (!item.equals(mainMask)) {
                    for (int j = 0; j < DataCenter.gI().itemOptionTemplate.length; ++j) {
                        if (DataCenter.gI().itemOptionTemplate[j].text.trim().equalsIgnoreCase(item.getTemplate().name.trim())) {
                            otherOptions.add(new ItemOption(DataCenter.gI().itemOptionTemplate[j].id + ",0,0"));
                            break;
                        }
                    }
                }
                for (ItemOption itemOption : options) {
                    boolean hasOption = true;

                    for (ItemOption o : fullOptions) {
                        if (o.option[0] == itemOption.option[0] && itemOption.option[0] != 327) {
                            o.setValue(o.option[1] + itemOption.option[1]);
                            hasOption = false;
                            break;
                        }
                    }
                    if (itemOption.option[0] == 327 && !item.equals(mainMask)) {
                        hasOption = false;
                    }

                    if (hasOption) {
                        fullOptions.add(itemOption);
                    }
                }

            }
        }
        fullOptions.addAll(otherOptions);
        mainMask.strOption = Item.creatOption(fullOptions);
        mainMask.isLock = true;
        if (level > 2) {
            level -= 1;
        }
        int upgradePoints = (level / 3) * 2 + (level % 3 == 2 ? 1 : 0);
        mainMask.setUpgrade((byte) (upgradePoints));
    }

    private static List<Item> getAllMask(Item mask) {
        List<Item> items = new ArrayList<>();
        String[] split = Objects.toString(mask.optionGhepCaiTrang, "").split("&");
        for (String str : split) {
            if (!str.isEmpty()) {
                String[] data = str.split("@");
                Item item = new Item(Integer.parseInt(data[0]), "abc");
                item.setStrOption(data[1]);
                items.add(item);
            }
        }
        return items;
    }

    public static void downgradeMask(Char player, Message ms) {
        player.lockItem.lock();
        try {
            byte type = ms.reader().readByte();
            short index = ms.reader().readShort();
            byte select = ms.reader().readByte();
            Item item = player.checkBag(type)[index];
            if (item == null) {
                return;
            }
            List<Item> allMask = getAllMask(item);
            if (allMask.isEmpty()) {
                return;
            }
            if (select == 0) {
                if (player.getSlotNull() < allMask.size()) {
                    player.warningBagFull();
                    return;
                }
                for (Item mask : allMask) {
                    if (mask.getTemplate().name.endsWith("khóa")) {
                        mask.isLock = true;
                    }
                    if (player.addItemToBag(mask)) {
                        player.getService().addItem(mask);
                    }
                }
                player.getService().tachCaiTrang(type, index, allMask.get(0));
            } else if (select < allMask.size()) {
                int fee = 30;
                if (player.zeni < fee) {
                    player.serverDialog("Không đủ Zeni!");
                    return;
                }
                player.addZeni(-fee, true);
                Item mask = allMask.remove(select);
                fusion(allMask);
                Item dest = allMask.get(0);
                if (player.getSlotNull() < 2) {
                    player.warningBagFull();
                    return;
                }
                if (mask.getTemplate().name.endsWith("khóa")) {
                    mask.isLock = true;
                }
                if (player.addItemToBag(mask)) {
                    player.getService().addItem(mask);
                }
                dest.isLock = true;
                if (player.addItemToBag(dest)) {
                    player.getService().addItem(dest);
                }
                player.getService().tachCaiTrang(type, index, dest);
            }
            player.checkBag(type)[index] = null;
            player.getService().updateIndexBag();
            player.setAbility();
            player.getService().updateMPFull();
            player.getService().updateMPFull();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            player.lockItem.unlock();
        }
    }

    public static void moveOption(Char player, Message ms) {
        try {
            short index = ms.reader().readShort();
            if (index < 0 || index >= player.bag.length) {
                return;
            }
            Item godEquipmemnt = player.bag[index];
            if (!isGodEquipment(godEquipmemnt)) {
                return;
            }
            
            
            
            Item normalEquipment = player.body[godEquipmemnt.getTemplate().type];
            if (normalEquipment == null) {
                player.serverDialog("Không có trang bị để chuyển hoá!");
                return;
            }
            ItemOption[] normalOptions;
            if (isGodEquipment(normalEquipment) || (normalOptions = normalEquipment.options()) == null) {
                player.serverDialog("Trang bị đang mặc không phù hợp!");
                return;
            }
            int fee = 1000;
            if (player.zeni < fee) {
                player.serverDialog("Cần " + fee + " Zeni để chuyển hoá!");
                return;
            }
            ItemOption[] godOptions = godEquipmemnt.options();
            if (godOptions == null) {
                return;
            }
            if (godOptions.length > 6) {
                player.serverDialog("Trang bị này đã chuyển hoá từ trước!");
                return;
            }


            player.addZeni(-fee, true);
            godEquipmemnt.upgrade = normalEquipment.upgrade;
            for (int i = 1; i <= godEquipmemnt.upgrade; ++i) {
                for (int j = 0; j < godOptions.length; ++j) {
                    if (godOptions[j].getItemOptionTemplate().type != 8) {
                        int[] increases = godOptions[j].getItemOptionTemplate().getOption();
                        if (i <= increases.length) {
                            godOptions[j].addValue(increases[i - 1]);
                        }
                    }
                }
            }
            
            ItemOption[] options = new ItemOption[godOptions.length + normalOptions.length];
            System.arraycopy(godOptions, 0, options, 0, godOptions.length);
            System.arraycopy(normalOptions, 0, options, godOptions.length, normalOptions.length);

            godEquipmemnt.strOption = Item.creatOption(options);
            godEquipmemnt.isLock = true;
            
            normalEquipment.upgrade = 0;
            normalEquipment.setStrOption("");

            player.getService().updateItem(godEquipmemnt);
            player.getService().updateItemBody(normalEquipment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  public static boolean isGodEquipment(Item item) {
    return (item.id >= 923 && item.id <= 926) || 
           (item.id >= 1159 && item.id <= 1162);
}

     
}
