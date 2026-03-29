package com.vdtt.handler;

import com.vdtt.ability.AbilityFromEquip;
import com.vdtt.data.DataCenter;
import com.vdtt.data.StringMenu;
import com.vdtt.effect.Effect;
import com.vdtt.effect.EffectTemplateManager;
import com.vdtt.handler.item.*;
import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemOption;
import com.vdtt.item.ItemTemplate;
import com.vdtt.model.Char;
import com.vdtt.model.DanhHieu;
import com.vdtt.model.Pet;
import com.vdtt.network.Message;
import com.vdtt.server.RuntimeServer;
import com.vdtt.skill.Skill;
import com.vdtt.skill.SkillFactory;
import com.vdtt.util.Log;
import com.vdtt.util.TimeUtils;
import com.vdtt.util.Util;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UseItemHandler {

    private final static Map<Integer, UseItem> handlers = new HashMap<>();

    private UseItemHandler() {
    }

    private static void addHandler(int itemId, UseItem useItem) {
        handlers.put(itemId, useItem);
    }

    public static void init() {
        EatCream cream = new EatCream();
        addHandler(964, cream);
        addHandler(965, cream);
        addHandler(938, new KICloud());
        addHandler(437, new RenameCard());

        UseCakeTrungThu cake = new UseCakeTrungThu();
        addHandler(1137, cake);
        addHandler(1138, cake);
        addHandler(1139, cake);
        addHandler(1140, cake);
        addHandler(1141, cake);
        addHandler(1142, cake);
        addHandler(1143, cake);
        addHandler(1146, cake);

        UseItemTetNguyenDan tet = new UseItemTetNguyenDan();
        addHandler(950, tet);
        addHandler(951, tet);
        addHandler(328, tet);
        addHandler(952, tet);
    }

    public static void useItem(Char player, Item item) {
        Log.debug("use item: " + item.id + " type: " + item.getTemplate().type);
        if (player.level() < item.getTemplate().levelNeed) {
            return;
        }
        if (item.isExpired()) {
            player.removeItem(item.index, 1, true);
            return;
        }
        if ((item.getTemplate().gioiTinh == 0 || item.getTemplate().gioiTinh == 1) && item.getTemplate().gioiTinh != player.gender) {
            return;
        }
        if(!player.isActiveAction()) {
            player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
            return;
        }
        UseItem useItem = handlers.get(item.id);
        if (useItem != null) {
            useItem.doUse(player, item);
            return;
        }
        if (item.isTrangBi()) {
            useEquipment(player, item);
        } else if (item.getTemplate().type == 34) {
            useItemTitle(player, item);
        } else if (item.getTemplate().type == 99) {

        } else if (item.getTemplate().type == 100) {
            useItemType100(player, item);
        } else if (item.isTauBay()) {
            menuItem(player, item);
        } else if (item.getTemplate().type == 24) {
            useItemEffect(player, item);
        } else if (item.getTemplate().type == 29) {
            useItemType29(player, item);
        } else if (item.getTemplate().type == 28) {
            useItemType28(player, item);
        } else if (item.getTemplate().type == 1) {
            useItemType1(player, item);
        } else if (item.getTemplate().type == 37) {
            useItemType37(player, item);
        }
        if (item.id == 724) {
            if (player.timeUseBean > System.currentTimeMillis()) {
                return;
            }
            if (player.taskId == 0 && player.taskMain != null && player.taskMain.index == 2) {
                player.updateTaskCount(1);
            }
            if (player.getEm().findByID(160) != null) {
                player.serverDialog("Đang bị nội thương không thể sử dụng dậu");
                return;
            }
            player.timeUseBean = System.currentTimeMillis() + 30000;
            player.removeItem(item.index, 1, true);
            player.addHp(player.maxHP);
            player.addMp(player.maxMP);
            if (player.pet != null && !player.pet.isDead && (player.pet.typePet == 1 || player.pet.typePet == 2)) {
                player.pet.addHp(player.pet.maxHP);
                player.pet.addMp(player.pet.maxMP);
                player.zone.getService().updatePet(player.id, (byte) 1, player.pet.hp, player.pet.maxHP, player.pet.mp, player.pet.maxMP);
            }
            player.getService().sendTimeItem((short) 142);
        }
        player.getService().updateIndexBag();
    }

    private static void useItemType37(Char player, Item item) {
        Item tbKichHoat = player.body[13];
        if (tbKichHoat == null) {
            player.serverDialog("Chưa mặc Trang bị kích hoạt");
            return;
        }
        int optionId = getOptionIdForItemKichHoat(item);
        if (optionId == -1) {
            return;
        }

        int optionType = getOptionTypeForItemKichHoat(item);
        ItemOption option = tbKichHoat.getOption(optionType);
        if (option != null && option.getParam2() != optionId) {
            player.serverDialog("Chỉ được khảm một loại chỉ số");
            return;
        }
        boolean isHp = optionId == 469;
        int max = isHp ? 600 : 50;
        if (option != null && option.getParam() >= max) {
            player.serverDialog("Chỉ số đã đạt tối đa");
            return;
        }
        if (option != null) {
            option.setParam(option.getParam() + (isHp ? 12 : 1));
            tbKichHoat.replaceOptionById(option);
        } else {
            tbKichHoat.addOptionKichHoat(optionType, (isHp ? 12 : 1), optionId);
        }
        player.removeItem(item.index, 1, true);
        player.setAbility();
        player.getService().updateMPFull();
        player.getService().updateHPFull();
        player.getService().showInfo2(player);
        player.getService().updateItemBodyChange();
    }

    public static int getOptionIdForItemKichHoat(Item item) {
        int optionId = -1;
        if (item.is(1058, 1058 + 5, 1058 + 10, 1058 + 15)) {
            optionId = 469;
        } else if (item.is(1059, 1059 + 5, 1059 + 10, 1059 + 15)) {
            optionId = 470;
        } else if (item.is(1060, 1060 + 5, 1060 + 10, 1060 + 15)) {
            optionId = 471;
        } else if (item.is(1061, 1061 + 5, 1061 + 10, 1061 + 15)) {
            optionId = 472;
        } else if (item.is(1062, 1062 + 5, 1062 + 10, 1062 + 15)) {
            optionId = 473;
        }
        return optionId;
    }

    private static int getOptionTypeForItemKichHoat(Item item) {
        if (item.isKichHoatAo()) {
            return 465;
        }
        if (item.isKichHoatBaoTay()) {
            return 466;
        }
        if (item.isKichHoatQuan()) {
            return 467;
        }
        if (item.isKichHoatGiay()) {
            return 468;
        }
        return -1;
    }

    private static void useItemType1(Char player, Item item) {
        try {
            switch (item.id) {
                case 799:
                    player.getService().showMenuItem(item.index, StringMenu.ONE_STAR, "");
                    break;
                case 800:
                    player.getService().showMenuItem(item.index, StringMenu.TWO_STAR, "");
                    break;
                case 801:
                    player.getService().showMenuItem(item.index, StringMenu.THREE_STAR, "");
                    break;
                case 802:
                    player.getService().showMenuItem(item.index, StringMenu.FOUR_STAR, "");
                    break;
                case 803:
                    player.getService().showMenuItem(item.index, StringMenu.FIVE_STAR, "");
                    break;
                case 804:
                    player.getService().showMenuItem(item.index, StringMenu.SIX_STAR, "");
                    break;
                case 805:
                    player.getService().showMenuItem(item.index, StringMenu.SEVEN_STAR, "");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void useItemType28(Char player, Item item) {
        try {
            switch (item.id) {
                case 185:
                    if (player.countTui1 >= 36) {
                        player.serverDialog("Đã tới giới hạn sử dụng túi");
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.countTui1++;
                    player.numberCellBag += 1;
                    player.updateAddSlotBag();
                    break;
                case 186:
                    if (player.countTui2 >= 18) {
                        player.serverDialog("Đã tới giới hạn sử dụng túi");
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.countTui2++;
                    player.numberCellBag += 2;
                    player.updateAddSlotBag();
                    break;
                case 187:
                    if (player.countTui3 >= 9) {
                        player.serverDialog("Đã tới giới hạn sử dụng túi");
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.countTui3++;
                    player.numberCellBag += 3;
                    player.updateAddSlotBag();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void useItemType29(Char player, Item item) {
        try {
            int effId = -1;
            long duration = TimeUnit.HOURS.toMillis(5);
            int param = 0;
            switch (item.id) {
                case 159:
                    effId = 42;
                    param = 50;
                    break;
                case 281:
                    effId = 205;
                    param = 75;
                    break;
                case 347:
                    effId = 206;
                    param = 100;
                    break;
                case 712:
                    effId = 207;
                    param = 200;
                    break;
                case 171:
                    effId = 39;
                    param = 1000;
                    break;
                case 355:
                    effId = 39;
                    param = 2000;
                    break;
                case 358:
                    effId = 39;
                    param = 3000;
                    break;
                case 701:
                    effId = 39;
                    param = 4000;
                    break;
                case 704:
                    effId = 39;
                    param = 5000;
                    break;

                case 173:
                    effId = 41;
                    param = 300;
                    break;
                case 357:
                    effId = 41;
                    param = 600;
                    break;
                case 360:
                    effId = 41;
                    param = 900;
                    break;
                case 703:
                    effId = 41;
                    param = 1200;
                    break;
                case 706:
                    effId = 41;
                    param = 1500;
                    break;

                case 172:
                    effId = 40;
                    param = 100;
                    break;
                case 356:
                    effId = 40;
                    param = 200;
                    break;
                case 359:
                    effId = 40;
                    param = 300;
                    break;
                case 702:
                    effId = 40;
                    param = 400;
                    break;
                case 705:
                    effId = 40;
                    param = 500;
                    break;

                case 462:
                    effId = 41;
                    param = 350;
                    break;

            }
            if (effId != -1) {
                if (effId >= 39 && effId <= 41) {
                    duration = TimeUnit.MINUTES.toMillis(5);
                }
                Effect effect = player.getEm().findByID(effId);
                if (effect != null && effId >= 39 && effId <= 41 && effect.param != param) {
                    player.getEm().remove(effect);
                    effect = null;
                }
                
                
                if (effect != null) {
                    effect.addTime(duration);
                    player.getEm().setEffect(effect);
                } else {
                    Effect eff = new Effect(effId, duration, param);
                    player.getEm().setEffect(eff);
                }


                if(effId == 42 || effId == 205 || effId == 206 || effId == 207) {
                    Integer[] array = {42, 205, 206, 207};
                    Effect effect1 = player.getEm().findIdExcept(array,effId);
                    if (effect1 != null) {
                        player.getEm().remove(effect1);
                        effect1.addTime(-999999999);
                        player.getEm().setEffect(effect1);
                    }
                }
                
                if (effId >= 39 && effId <= 41) {
                    player.setAbility();
                }
                player.removeItem(item.index, 1, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void useItemEffect(Char player, Item item) {
        try {
            player.removeItem(item.index, 1, true);
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(item.getTemplate().detail);

            // Lưu các số tìm được vào mảng
            int[] numbers = new int[2];
            int index = 0;

            while (matcher.find()) {
                // Chuyển đổi chuỗi số thành số nguyên và lưu vào mảng
                numbers[index] = Integer.parseInt(matcher.group());
                index++;
            }
            int idEffect = EffectTemplateManager.getInstance().getList().stream().filter(e -> e != null && e.name.toLowerCase().equals(item.getTemplate().name.toLowerCase())).findFirst().get().id;
            long time = TimeUnit.MINUTES.toMillis(numbers[1]);
            Effect eff = player.getEm().findByID(idEffect);
            if (eff != null) {
                eff.addTime(time);
                player.zone.getService().playerAddEffect(player, eff);
            } else {
                Effect effect = new Effect(idEffect, time, numbers[0]);
                player.getEm().setEffect(effect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void menuItem(Char player, Item item) {
        switch (item.id) {
            case 168:
                player.getService().showMenuItem(item.index, StringMenu.ITEM_168, "");
                break;
            case 569:
            case 716:
            case 717:
            case 718:
            case 719:
            case 720:
                player.getService().showMenuItem(item.index, StringMenu.MENU_TAU_VIP, "");
                break;
        }
    }

    private static void useItemTitle(Char player, Item item) {
        try {
            String strOption = item.getStrOption();
            player.removeItem(item.index, 1, true);
            DanhHieu dh = new DanhHieu();
            dh.cs = strOption;
            dh.title = item.getTemplate().name.split("Danh hiệu ")[1];
            if (item.expire > 0) {
                dh.setTime((int) TimeUnit.MILLISECONDS.toDays(item.expire - System.currentTimeMillis()));
            }
            dh.a();
            player.danhHieu.add(dh);
            player.getService().updateTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isWearingItems(Item[] items, int[] indexes, int[] ids) {
        int count = 0;
        for (int i = 0; i < indexes.length; i++) {
            Item item = items[indexes[i]];
            if (item != null && item.getId() == ids[i]) {
                count++;
            }
        }
        return count == indexes.length;
    }

    private static void useEquipment(Char player, Item item) {
        try {
            int indexUI = item.index;
            byte index = item.getTemplate().type;
            if (item.getTemplate().typeChar == player.sys || item.getTemplate().typeChar == 0) {
                if (item.getTemplate().type == 3 || item.getTemplate().type == 5 || item.getTemplate().type == 7) {
                    if (player.body[0] == null) {
                        player.getService().serverDialog("Không thể mặc đồ khi chưa có đệ tử");
                        return;
                    }
                }
                if (player.body[index] != null) {
                    Item it = player.body[index].clone();
                    it.index = indexUI;
                    it.isLock = true;
                    item.isLock = true;
                    player.body[index] = item;
                    player.bag[indexUI] = it;
                } else {
                    item.setLock(true);
                    player.body[index] = item;
                    player.bag[indexUI] = null;
                }

                if (index == 0) {
                    player.pet = new Pet(player.id, player.body[0], player.body, player.body2, player.getService(), player.getEm());
                    player.pet.zone = player.zone;
                    player.pet.setAbility();
                }
                if (item.getTemplate().type == 3 || item.getTemplate().type == 5 || item.getTemplate().type == 7 || item.getTemplate().type == 11) {
                    if (player.pet == null) {
                        return;
                    }
                    player.pet.setAbility();
                }
                if (item.getTemplate().type == 12) {
                    if(player.bag[indexUI] != null) {
                        if (player.bag[indexUI].id == 345) {
                            Effect efft = new Effect(150, player.timeGiapLuyenTap, 0);
                            player.getEm().setEffect(efft);
                            player.timeGiapLuyenTap = 0;
                        } else if (player.bag[indexUI].id == 1090) {
                            Effect efft = new Effect(188, player.timeGiapLuyenTap, 0);
                            player.getEm().setEffect(efft);
                            player.timeGiapLuyenTap = 0;
                        }
                    }
                    
                    Effect eff = player.getEm().findByID(item.getTemplate().id == 345 ? 150 : 188);
                    if (eff != null) {
                        player.timeGiapLuyenTap = eff.getTimeRemaining();
                        player.getEm().removeEffect(eff);
                        player.zone.getService().playerRemoveEffect(player, eff);
                    }
                }
            } else {
                player.getService().serverMessage("Trang bị không phù hợp với nhân vật");
                return;
            }
            Log.debug("use equipment: " + item.id + " to body: " + index);
            player.getService().useItem(item);
            player.setAbility();
            if (item.getTemplate().type == 14) {
                if (player.options[327] > 0) {
                    int point = 1;
//                    if (player.options[327] == 93) {
//                        point = 3;
//                    }
//                    if (player.options[327] == 62) {
//                        point = 2;
//                    }
                    Skill skill = SkillFactory.getInstance().getSkill(player.options[327], point);
                    if (skill != null) {
                        int indexSkillWithCT = player.findIndexSkillWithCT();
                        if (indexSkillWithCT != -1) {
                            player.skills.remove(indexSkillWithCT);
                        }
                        skill.isSkillCaiTrang = true;
                        skill.timeCoolDown = player.timeCoolDownSkillCaiTrang;
                        player.addSkill(skill);
                        player.getService().updateSkillCaiTrang();
                        player.getService().updateSkill();
                    }
                } else {
                    int indexSkillWithCT = player.findIndexSkillWithCT();
                    if (indexSkillWithCT != -1) {
                        player.skills.remove(indexSkillWithCT);
                        player.getService().updateSkill();
                    }

                }
            }
            player.getService().updateMPFull();
            player.getService().updateHPFull();
            player.getService().showInfo2(player);
            player.zone.getService().updateItemBody(player);
            if (item.isDoThanLinh()) {
                if (isWearingItems(player.body, new int[]{2, 4, 6, 8}, new int[]{923, 924, 925, 926}) && !player.isContainsTitle("Kẻ Diệt Thần")) {
                    useItemTitle(player, new Item(453, "dh"));
                    player.serverDialog("Bạn nhận được danh hiệu Kẻ Diệt Thần!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void UseHopSacMau(Char player, Item item) throws IOException {
        int timeCurrent = (int)(System.currentTimeMillis() / 1000);
        if (player.TimeChatColor > timeCurrent)
            timeCurrent = player.TimeChatColor;
        player.TimeChatColor = (int)(timeCurrent + (item.id == 892 ? 172800 : 604800));
        Message m = new Message((byte)-123);
        m.writer().writeByte(-36);
        m.writer().writeInt(player.TimeChatColor);
        player.getService().sendMessage(m);
        player.getService().serverMessage("Bạn đã gia hạn chat màu thành công");
        player.removeItem(item.index, 1, true);
    }
    
    private static int getEffectMount(int id) {
        switch (id) {
            case 1086:
                return 185;
            case 1104:
                return 148;
            case 909:
                return 0;
            case 953:
                return 153;
            default:
                return 0;
        }
    }
    
    private static void useItemType100(Char player, Item item) {
        try {
            if (item.isTauBay()) {
                menuItem(player, item);
                return;
            }
            switch (item.id) {
                case 1155:
                    if(TimeUtils.canDoWithTime(player.timeSendZeni,300000)) {
                        player.getService().openPopupIdGame((byte) 1);
                    }
                    break;
                case 1048:
                case 1049:
                    short[] ids = new short[]{168, 169};
                    short[] idcs = new short[]{169, 168};
                    short eid = ids[item.id - 1048];
                    short eidc = idcs[item.id - 1048];
                    Effect ec = player.getEm().findByID(eidc);
                    if (ec != null) {
                        player.getEm().removeEffect(ec);
                        ec.addTime(-999999999);
                        player.getEm().setEffect(ec);
                    }
                    Effect rsm = player.getEm().findByID(eid);
                    if(rsm == null) {
                        rsm = new Effect(eid,86_400_000,100);
                        player.getEm().setEffect(rsm);
                    } else {
                        rsm.addTime(86_400_000);
                        player.getEm().setEffect(rsm);
                    }
                    player.removeItem(item.index, 1, true);
                    break;
                case 266:
                    player.lvPK -= 5;
                    if(player.lvPK < 0)
                        player.lvPK = 0;
                    player.removeItem(item.index, 1, true);
                    player.getService().serverMessage("Bạn đã giảm 5 cấp PK");
                    break;
                case 715:
                case 892:
                    UseHopSacMau(player, item);
                    break;
                case 1086:
                case 1104:
                case 909:
                case 953:
                    Effect efft = player.getEm().findByType((short) 129);
                    if(efft == null) {
                        efft = player.getEm().findByType((short)161);
                        if(efft == null) {
                            efft = player.getEm().findByType((short)134);
                        }
                    }
                    long timec = 0;
                    switch (item.id) {
                        case 1086:
                        case 1104:
                            timec = 86400000;
                            break;
                        case 909:
                        case 953:
                            timec = 18000000;
                            break;
                    }
                    int effI = getEffectMount(item.id);
                    if(efft != null && efft.template != null) {
                        if(efft.template.id != effI) {
                            player.serverDialog("Bạn đang sử dụng một thời trang khác, không thể sử dụng thêm!");
                            return;
                        }

                        efft.addTime(timec);
                        player.getEm().setEffect(efft);
                    } else {
                        efft = new Effect(effI,timec,100);
                        player.getEm().setEffect(efft);
                    }
                    player.removeItem(item.index, 1, true);
                    break;
                case 1102: {
                    Item ct = new Item(Util.nextInt(1114, 1130), "item");
                    ct.addOption(436, ct.id);
                    ct.addOption(433, Util.nextInt(0, 100));
                    player.addItemToBag(ct);
                    player.removeItem(item.index, 1, true);
                    break;
                }
                case 942:
                    Item ct = new Item(Util.nextInt(1114, 1130), "item");
                    ct.addOption(436, ct.id);
                    ct.addOption(433, Util.nextInt(0, 100));
                    ct.expire = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7);
                    player.addItemToBag(ct);
                    player.removeItem(item.index, 1, true);
                    break;
                case 937:
                    player.packOnline = false;
                    player.packTinhAnh = false;
                    player.packThuLinh = false;
                    player.packSieuQuai = false;
                    player.totalTimeOnline = 0;
                    player.totalTA = 0;
                    player.totalTL = 0;
                    player.totalSQ = 0;
                    player.rewardList.removeIf(r -> r.type == 3);
                    player.rewardList.removeIf(r -> r.type == 15);
                    player.rewardList.removeIf(r -> r.type == 16);
                    player.rewardList.removeIf(r -> r.type == 17);
                    player.removeItem(item.index, 1, true);
                    break;
                case 404: {
                    int effId = 66;
                    long duration = TimeUnit.HOURS.toMillis(1);
                    Effect effect = player.getEm().findByID(effId);
                    if (effect != null) {
                        if (effect.getTimeRemaining() >= TimeUnit.HOURS.toMillis(7)) {
                            player.serverDialog("Không thể dùng thêm!");
                            return;
                        }
                        effect.addTime(duration);
                        player.getEm().setEffect(effect);
                    } else {
                        Effect eff = new Effect(effId, duration, 0);
                        player.getEm().setEffect(eff);
                    }
                    player.removeItem(item.index, 1, true);
                }
                break;
                case 1078:
                    if (player.countUseBarrackCard <= 0) {
                        player.serverDialog("Đã hết lượt sử dụng trong ngày hôm nay.");
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.countUseBarrackCard--;
                    player.countBarrack++;
                    player.serverMessage("Số lượt tham gia doanh trại còn lại là " + player.countBarrack);
                    break;
                case 808:
                    player.getService().showMenuItem(item.index, StringMenu.CAPSULE_PHI_THUYEN, "");
                    break;
                case 819:
                    if (player.countUseItemNgucTu >= 2) {
                        player.serverDialog("Đã tới giới hạn sử dụng");
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.countUseItemNgucTu++;
                    player.countNgucTu++;
                    player.serverMessage("Số lượt ngục tù còn lại là " + player.countNgucTu);
                    break;
                case 834:
                    if (player.countTheHell >= 2) {
                        player.serverDialog("Đã tới giới hạn sử dụng");
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.countTheHell++;
                    player.countHell++;
                    player.serverMessage("Số lượt địa ngục còn lại là " + player.countHell);
                    break;
                case 697:
                    if (player.getSlotNull() == 0) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    int[] percents = {40, 40, 19, 1};
                    byte[] types = {4, 5, 6, 7};
                    int index = Util.randomWithRate(percents, 100);
                    byte type = types[index];
                    Item da = new Item(type, "item");
                    da.setLock(true);
                    player.addItemToBag(da);
                    player.getService().addItem(da);
                    break;
                case 839:
                    if (player.getSlotNull() == 0) {
                        player.warningBagFull();
                        return;
                    }
//                    if (RuntimeServer.gI().timeStart > 0) {
//                        player.getService().serverDialog("Rồng thần đang bận rồi ông cháu tý gọi lại sau nhé");
//                        return;
//                    }
                    for (int i = 1; i < 7; i++) {
                        index = player.getIndexItemByIdInBag(839 + i, true);
                        if (index == -1) {
                            player.serverDialog("Cần ngọc rồng " + (i + 1) + " sao");
                            return;
                        }
                    }
                    RuntimeServer.gI().uocRongALL(player, (byte) 1);
                    break;
                case 840:
                case 841:
                case 842:
                case 843:
                case 844:
                case 845:
                    break;
                case 901:
                    if (player.getSlotNull() == 0) {
                        player.warningBagFull();
                        return;
                    }
//                    if (RuntimeServer.gI().timeStart > 0) {
//                        player.getService().serverDialog("Rồng thần đang bận rồi ông cháu tý gọi lại sau nhé");
//                        return;
//                    }
                    for (int i = 1; i < 7; i++) {
                        index = player.getIndexItemByIdInBag(901 + i, true);
                        if (index == -1) {
                            player.serverDialog("Cần ngọc rồng " + (i + 1) + " sao");
                            return;
                        }
                    }
                    RuntimeServer.gI().uocRongALL(player, (byte) 3);
                    break;
                case 902:
                case 903:
                case 904:
                case 905:
                case 906:
                case 907:
                    break;
                case 997:
                    player.removeItem(item.index, 1, true);
                    int idEffect = 144;
                    long time = TimeUnit.MINUTES.toMillis(30);
                    Effect eff = player.getEm().findByID(idEffect);
                    if (eff != null) {
                        eff.addTime(time);
                        player.getEm().setEffect(eff);
                    } else {
                        Effect effect = new Effect(idEffect, time, 0);
                        player.getEm().setEffect(effect);
                    }
                    break;
                case 895:
                    player.removeItem(item.index, 1, true);
                    idEffect = 142;
                    time = TimeUnit.HOURS.toMillis(5);
                    eff = player.getEm().findByID(idEffect);
                    if (eff != null) {
                        eff.addTime(time);
                        player.getEm().setEffect(eff);
                    } else {
                        Effect effect = new Effect(idEffect, time, 80);
                        player.getEm().setEffect(effect);
                    }
                    break;
                case 234:
                    player.removeItem(item.index, 1, true);
                    idEffect = 167;
                    time = TimeUnit.MINUTES.toMillis(30);
                    eff = player.getEm().findByID(idEffect);
                    if (eff != null) {
                        eff.addTime(time);
                        player.getEm().setEffect(eff);
                    } else {
                        Effect effect = new Effect(idEffect, time, 15);
                        player.getEm().setEffect(effect);
                    }
                    break;
                case 428:
                    player.removeItem(item.index, 1, true);
                    Item phale = new Item(Util.nextInt(406, 413), "item");
                    phale.setLock(true);
                    phale.setQuantity(20);
                    player.addItemToBag(phale);
                    player.getService().addItem(phale);
                    break;
                case 891:
                    player.removeItem(item.index, 1, true);
                    int itemid = 0;
                    switch (player.sys) {
                        case 1:
                            itemid = 871;
                            break;
                        case 2:
                            itemid = 872;
                            break;
                        case 3:
                            itemid = 873;
                            break;
                        case 4:
                            itemid = 874;
                            break;
                        case 5:
                            itemid = 1003;
                            break;
                    }
                    Item detu = new Item(itemid, "item");
                    detu.setLock(true);
                    Vector<ItemOption> options = new Vector<>();
                    options.add(new ItemOption(285, 100, 40));
                    options.add(new ItemOption(407, 0, 1));
                    options.add(new ItemOption(0, Util.nextInt(100, 400)));
                    options.add(new ItemOption(152, Util.nextInt(50, 200)));
                    options.add(new ItemOption(327, Util.nextInt(50, 52), 1));
                    options.add(new ItemOption(327, 53, 1));
                    options.add(new ItemOption(327, 54, 1));
                    options.add(new ItemOption(327, 30, 1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    options.add(new ItemOption(327, -1, -1));
                    detu.strOption = Item.creatOption(options);
                    player.addItemToBag(detu);
                    player.getService().addItem(detu);
                    break;
                case 799:
                    player.getService().showMenuItem(item.index, StringMenu.ONE_STAR, "");
                    break;
                case 800:
                    player.getService().showMenuItem(item.index, StringMenu.TWO_STAR, "");
                    break;
                case 801:
                    player.getService().showMenuItem(item.index, StringMenu.THREE_STAR, "");
                    break;
                case 802:
                    player.getService().showMenuItem(item.index, StringMenu.FOUR_STAR, "");
                    break;
                case 803:
                    player.getService().showMenuItem(item.index, StringMenu.FIVE_STAR, "");
                    break;
                case 804:
                    player.getService().showMenuItem(item.index, StringMenu.SIX_STAR, "");
                    break;
                case 805:
                    player.getService().showMenuItem(item.index, StringMenu.SEVEN_STAR, "");
                    break;
                case 833:
                    player.getService().showMenuItem(item.index, StringMenu.CAPSULE_TRANG, "");
                    break;
                case 1106:
                    player.getService().showMenuItem(item.index, StringMenu.ONE_STAR_VIP, "");
                    break;
                case 1107:
                    player.getService().showMenuItem(item.index, StringMenu.TWO_STAR_VIP, "");
                    break;
                case 1108:
                    player.getService().showMenuItem(item.index, StringMenu.THREE_STAR_VIP, "");
                    break;
                case 1109:
                    player.getService().showMenuItem(item.index, StringMenu.FOUR_STAR_VIP, "");
                    break;
                case 1110:
                    player.getService().showMenuItem(item.index, StringMenu.FIVE_STAR_VIP, "");
                    break;
                case 1111:
                    player.getService().showMenuItem(item.index, StringMenu.SIX_STAR_VIP, "");
                    break;
                case 1112:
                    player.getService().showMenuItem(item.index, StringMenu.SEVEN_STAR_VIP, "");
                    break;
                case 1113:
                    player.getService().showMenuItem(item.index, StringMenu.CAPSULE_TRANG_VIP, "");
                    break;
                case 1150:
                    player.getService().showMenuItem(item.index, StringMenu.CAPSULE_TRANG_MAX, "");
                    break;

                case 1045:
                    if (player.getSlotNull() == 0) {
                        return;
                    }
//                    if (RuntimeServer.gI().timeStart > 0) {
//                        player.getService().serverDialog("Rồng thần đang bận rồi ông cháu tý gọi lại sau nhé");
//                        return;
//                    }
                    int nro2 = player.getIndexItemByIdInBag(1046, true);
                    if (nro2 == -1) {
                        player.getService().serverDialog("Cần ngọc rồng Cerealian 2 sao");
                        return;
                    }
                    if (RuntimeServer.gI().callDragon) {
                        player.getService().serverDialog("Có người đang gọi rồng, vui lòng thử lại sau.");
                        return;
                    }
                    RuntimeServer.gI().uocRongALL(player, (byte) 0);
                    break;
                case 1046:
                    break;
                case 301:
                    player.getService().openGUI(53);
                    break;
                case 164:
                    if (player.getSlotNull() < 1) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    if (player.countTuiDo < 5) {
                        player.countTuiDo++;
                        int[] itemId = new int[]{160, 163, 191};
                        int[] percent = new int[]{20, 70, 10};
                        int idItem = itemId[Util.randomWithRate(percent, 100)];
                        if (idItem == 160) {
                            int quantity = Util.nextInt(1, 100);
                            Item it = new Item(idItem, "item");
                            it.setQuantity(quantity);
                            it.setLock(true);
                            player.addItemToBag(it);
                            player.getService().addItem(it);
                        } else if (idItem == 163) {
                            player.addCoin(Util.nextInt(10000, 500000), true);
                        } else if (idItem == 191) {
                            player.addBallz(Util.nextInt(100, 50000), true);
                        }
                    } else {
                        player.addBallz(50, true);
                    }
                    break;
                case 295: {
                    if (player.getSlotNull() < 1) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    List<ItemTemplate> setTamThu = ItemManager.getInstance().createSet(1, player.sys);
                    ItemTemplate itemTemplate = setTamThu.get(Util.nextInt(0, setTamThu.size() - 1));
                    Item item1 = new Item(itemTemplate.id, "item");
                    item1.createOptionRandom(4, 7);
                    item1.setQuantity(1);
                    item1.upgradeItem((byte) 6);
                    item1.isLock = true;
                    player.addItemToBag(item1);
                    player.getService().addItem(item1);
                    break;
                }
                case 296: {
                    if (player.getSlotNull() < 1) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    List<ItemTemplate> setTamThu = ItemManager.getInstance().createSet(1, player.sys);
                    ItemTemplate itemTemplate = setTamThu.get(Util.nextInt(0, setTamThu.size() - 1));
                    Item item1 = new Item(itemTemplate.id, "item");
                    item1.createOptionRandom(4, 7);
                    item1.setQuantity(1);
                    item1.isLock = true;
                    item1.upgradeItem((byte) 8);
                    player.addItemToBag(item1);
                    player.getService().addItem(item1);
                    break;
                }
                case 297: {
                    if (player.getSlotNull() < 1) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    List<ItemTemplate> setTamThu = ItemManager.getInstance().createSet(1, player.sys);
                    ItemTemplate itemTemplate = setTamThu.get(Util.nextInt(0, setTamThu.size() - 1));
                    Item item1 = new Item(itemTemplate.id, "item");
                    item1.createOptionRandom(4, 7);
                    item1.setQuantity(1);
                    item1.isLock = true;
                    item1.upgradeItem((byte) 14);
                    player.addItemToBag(item1);
                    player.getService().addItem(item1);
                    break;
                }
                case 958:
                    player.serverDialog("Tự sử dụng khi hợp nhất lần 2 trang bị");
                    break;
                case 1100:
                    if (player.sachChienDau < 14) {
                        player.getService().serverMessage("Chưa đủ cấp để sử dụng");
                        return;
                    }
                    if (player.sachChienDau >= 15) {
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.sachChienDau++;
                    player.sach.upgradeItem(15);
                    player.sach.strOption += ";486,5,-1";
                    player.getService().sendSachChienDau();
                    player.setAbility();
                    player.getService().updateHPFull();
                    player.getService().updateMPFull();
                    break;
                case 434:
                    if(player.ballZ < 200000) {
                        player.getService().serverDialog("Thiếu ballz");
                        return;
                    }
                    
                    if(item.getQuantity() < 1000) {
                        player.getService().serverDialog("Không đủ 1000 mảnh để ghép");
                        return;
                    }
                    player.addBallz(-200000,true);
                    player.removeItem(item.index, 1000, true);
                    Item scd = new Item(435, "mkn");
                    scd.setLock(true);
                    player.addItemToBag(scd);
                    player.getService().addItem(scd);
                    break;
                case 817:
                    if (player.countQuaSinhLuc >= 10) {
                        return;
                    }
                    if (player.lockEXP) {
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.countQuaSinhLuc++;
                    long expAdd = DataCenter.gI().getLevel(player.level()) / 1000;
                    player.exp += expAdd;
                    player.KI += expAdd / 10;
                    player.getService().showKI();
                    player.getService().showKI(player);
                    break;
                case 813:
                case 814:
                case 815:
                case 816:
                    player.removeItem(item.index, 1, true);
                    player.pointKame += (item.id - 812);
                    player.serverMessage("Bạn nhận được " + (item.id - 812) + " điểm  Kame");
                    break;
                case 818:
                    player.removeItem(item.index, 1, true);
                    player.pointKame += 5;
                    player.serverMessage("Bạn nhận được 5 điểm  Kame");
                    break;
                case 170: {
                    player.removeItem(item.index, 1, true);
                    List<Skill> skills1 = new ArrayList<>();
                    synchronized (player.skills) {
                        for (Skill skill : player.skills) {
                            if (skill.getSkillTemplate().i == 0) {
                                Skill skill1 = SkillFactory.getInstance().getSkill(skill.isSkillTemplate, 0);
                                if (skill.isSkillTemplate == 1 || skill.isSkillTemplate == 7 || skill.isSkillTemplate == 13 || skill.isSkillTemplate == 19 || skill.isSkillTemplate == 25) {
                                    skill1 = SkillFactory.getInstance().getSkill(skill.isSkillTemplate, 1);
                                }
                                skills1.add(skill1);
                            }
                        }
                    }
                    player.skills.clear();
                    player.skills.addAll(skills1);
                    player.KI = player.exp / 10;
                    player.getService().updateSkill();
                    player.getService().showKI();
                }
                break;
                case 1149: {
                    player.removeItem(item.index, 1, true);
                    for (int i = 0; i < player.pet.skillPet.length; i++) {
                        if (player.pet.skillPet[i] != null) {
                            player.pet.skillPet[i].level = 1;
                        }
                    }
                    player.pet.KI = player.pet.exp;
                    player.pet.updateSkillPet();
                    player.pet.setAbility();
                    player.getService().updateItemBody(player.body[0]);
                    break;
                }
                case 1020:
                case 1021:
                case 1024:
                case 1023:
                case 1022:
                    if (item.getTemplate().typeChar != player.sys) {
                        player.getService().serverDialog("Không thể học kỹ năng của hành tinh khác");
                        return;
                    }
                    String[] str = item.getTemplate().name.split("Kỹ năng ");
                    String kyNang = str[1];
                    int id = SkillFactory.getInstance().getIdSkillByName(kyNang);
                    if (id != -1) {
                        Skill old = player.findSkillWithIdSkillTemplate((short) id);
                        if (old != null) {
                            if (old.level == 0) {
                                id = SkillFactory.getInstance().getIdSkillByName(kyNang, player.sys);
                                Skill skill = SkillFactory.getInstance().getSkill(id, 1);
                                player.removeItem(item.index, 1, true);
                                if (skill != null) {
                                    player.totalKiNang++;
                                    int indexSkill = player.skills.indexOf(old);
                                    player.skills.set(indexSkill, skill);
                                    player.getService().showKI();
                                    player.getService().updateSkill();
                                    player.setAbility();
                                    return;
                                }
                            } else {
                                player.serverDialog("Kỹ năng đã học");
                                return;
                            }
                        }

                    }
                    id = SkillFactory.getInstance().getIdSkillByName(kyNang, player.sys);
                    if (id != -1) {
                        Skill old = player.findSkillWithIdSkillTemplate((short) id);
                        if (old != null) {
                            player.serverDialog("Kỹ năng đã học");
                            return;
                        }
                        player.removeItem(item.index, 1, true);
                        int indexSkillWithCt = player.findIndexSkillWithCT();
                        Skill skillCaiTrang = null;
                        if (indexSkillWithCt != -1) {
                            skillCaiTrang = player.skills.get(indexSkillWithCt).clone();
                            player.skills.remove(indexSkillWithCt);
                        }
                        Skill skill = SkillFactory.getInstance().getSkill(id, 1, player.sys);
                        player.addSkill(skill);
                        if (skillCaiTrang != null) {
                            player.addSkill(skillCaiTrang);
                        }
                        player.getService().updateSkill();
                        player.totalKiNang++;
                    }
                    break;
                case 728:
                case 729:
                case 735:
                case 730:
                case 736:
                case 731:
                case 737:
                case 732:
                case 1009:
                case 1010:
                case 1016:
                case 1011:
                case 1017:
                case 1012:
                case 1018:
                case 1013:
                case 750:
                case 751:
                case 757:
                case 752:
                case 758:
                case 753:
                case 759:
                case 754:
                case 739:
                case 740:
                case 746:
                case 741:
                case 747:
                case 742:
                case 748:
                case 743:
                case 761:
                case 762:
                case 768:
                case 763:
                case 769:
                case 764:
                case 770:
                case 765:
                    if (item.getTemplate().typeChar != player.sys) {
                        player.getService().serverDialog("Không thể học kỹ năng của hành tinh khác");
                        return;
                    }
                    str = item.getTemplate().name.split("Kỹ năng ");
                    kyNang = str[1];
                    id = SkillFactory.getInstance().getIdSkillByName(kyNang);
                    if (id != -1) {
                        Skill old = player.findSkillWithIdSkillTemplate((short) id);
                        if (old != null) {
                            player.serverDialog("Kỹ năng đã học");
                            return;
                        }

                    }
                    player.removeItem(item.index, 1, true);
                    if (id != -1) {
                        int indexSkillWithCt = player.findIndexSkillWithCT();
                        Skill skillCaiTrang = null;
                        if (indexSkillWithCt != -1) {
                            skillCaiTrang = player.skills.get(indexSkillWithCt).clone();
                            player.skills.remove(indexSkillWithCt);
                        }
                        Skill skill = SkillFactory.getInstance().getSkill(id, 0);
                        player.addSkill(skill);
                        if (skillCaiTrang != null) {
                            player.addSkill(skillCaiTrang);
                        }
                        player.getService().updateSkill();
                        player.totalKiNang++;
                    }
                    break;
                case 710:
                    if (player.timeTienIch == -1) {
                        player.serverMessage("Tiện ích đã được kích hoạt vĩnh viễn không cần kích hoạt thêm");
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.achievements.increaseAchievementCount(21, 1);
                    player.rank = 10;
                    if (player.timeTienIch < System.currentTimeMillis() / 1000) {
                        player.timeTienIch = (int) (System.currentTimeMillis() / 1000 + (int) (TimeUnit.DAYS.toMillis(7) / 1000));
                    } else {
                        player.timeTienIch += (int) (TimeUnit.DAYS.toMillis(7) / 1000);
                    }
                    player.getService().updateTienIch();
                    player.getService().serverMessage("Tiện ích đã được kích hoạt. Hạn sử dụng tới " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date((long) player.timeTienIch * 1000L)));
                    break;
                case 827:
                    player.achievements.updateAchievementCount(21, 20);
                    player.removeItem(item.index, 1, true);
                    player.rank = 10;
                    player.timeTienIch = -1;
                    player.getService().updateTienIch();
                    player.getService().serverMessage("Tiện ích đã được kích hoạt vĩnh viễn");
                    break;
                case 277:
                    if (player.getSlotNull() < 3) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    for (int i = 0; i < 3; i++) {
                        Item item1 = new Item(4 + i, "rd");
                        item1.isLock = true;
                        player.addItemToBag(item1);
                        player.getService().addItem(item1);
                    }
                    break;
                case 161:
                    if (player.getSlotNull() < 1) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    Item diamond = new Item(160, "item");
                    diamond.setQuantity(100);
                    diamond.isLock = true;
                    player.addItemToBag(diamond);
                    player.getService().addItem(diamond);
                    break;
                case 177:
                    if (player.getSlotNull() < 1) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    int percent = Util.nextInt(1, 1000);
                    if (percent <= 10) {
                        Item item1 = new Item(9, "item");
                        item1.setLock(true);
                        player.addItemToBag(item1);
                        player.getService().addItem(item1);
                    } else if (percent <= 100) {
                        Item item1 = new Item(8, "item");
                        item1.setLock(true);
                        player.addItemToBag(item1);
                        player.getService().addItem(item1);
                    } else {
                        Item item1 = new Item(7, "item");
                        item1.setLock(true);
                        player.addItemToBag(item1);
                        player.getService().addItem(item1);
                    }
                    break;
                case 822:
                    player.removeItem(item.index, 1, true);
                    player.addCoin(50000, true);
                    break;
                case 1156:
                    player.removeItem(item.index, 1, true);
                    player.addCoin(1000000, true);
                    break;     
                case 1157:
                    player.removeItem(item.index, 1, true);
                    player.addBallz(10000, true);
                    break;          
                 case 1158:
    player.removeItem(item.index, 1, true);
    int randomZeni = ThreadLocalRandom.current().nextInt(1, 300); // Random từ 1 đến 1000
    player.addZeni(randomZeni, true);
    break; 
                    
                    
                case 771:
                case 781:
                case 786:
                case 776:
                case 1002: {
                    if (player.getSlotNull() < 4) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    if (player.taskId == 1 && player.taskMain != null && player.taskMain.index == 1) {
                        player.updateTaskCount(1);
                    }
                    List<ItemTemplate> setTamThu = ItemManager.getInstance().createSet(1, player.sys);
                    for (ItemTemplate itemTemplate : setTamThu) {
                        Item item1 = new Item(itemTemplate.id, "item");
                        item1.createOptionRandom(4, 7);
                        item1.setQuantity(1);
                        item1.isLock = true;
                        player.addItemToBag(item1);
                        player.getService().addItem(item1);
                    }
                    break;
                }
                case 824:
                    if (player.getSlotNull() < 4) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    if (player.taskId == 1 && player.taskMain != null && player.taskMain.index == 1) {
                        player.updateTaskCount(1);
                    }
                    List<ItemTemplate> setTamThu = ItemManager.getInstance().createSet(1, player.sys);
                    for (ItemTemplate itemTemplate : setTamThu) {
                        Item item1 = new Item(itemTemplate.id, "item");
                        item1.strOption = "335,150;336,150;341,50;342,50;350,1;347,10;51,35;359,75;30,75;353,30";
//                        item1.createOptionRandom(0, 7);
                        item1.setQuantity(1);
                        item1.isLock = true;
                        player.addItemToBag(item1);
                        player.getService().addItem(item1);
                    }
                    break;
                case 435:
                    if (player.sachChienDau >= 14) {
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.sachChienDau++;
                    player.sach = new Item(435, "item");
                    player.sach.strOption = "207,0,-1;208,0,-1";
                    player.sach.upgradeItem(player.sachChienDau);
                    player.getService().sendSachChienDau();
                    player.setAbility();
                    player.getService().updateHPFull();
                    player.getService().updateMPFull();
                    break;
                case 821:
                    player.removeItem(item.index, 1, true);
                    player.addBallz(5000, true);
                    break;
                case 1019:
                    player.removeItem(item.index, 1, true);
                    player.addZeni(10, true);
                    break;
                case 996:
                    if (player.maxBean > 54) {
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    player.maxBean++;
                    break;
                case 828:
                    player.achievements.updateAchievementCount(22, 20);
                    player.getService().showMenuItem(item.index, StringMenu.CAPSULE_PHI_THUYEN, "");
                    break;
                case 682:
                    if (player.getSlotNull() == 0) {
                        player.warningBagFull();
                        return;
                    }
                    player.removeItem(item.index, 1, true);
                    int rand = Util.nextInt(1, 1000);
                    int itemId = 0;
                    if (rand <= 25) {
                        itemId = 9;
                    } else {
                        itemId = 8;
                    }
                    Item item1 = new Item(itemId, "item");
                    item1.setLock(true);
                    player.addItemToBag(item1);
                    player.getService().addItem(item1);
                    break;
                case 1144:
                    if (player.diemBTTBangHoa >= 10) {
                        player.getService().serverDialog("Mỗi nhân vật sử dụng tối đa 10 bánh Trung Thu Băng Hỏa");
                        return;
                    }
                    player.diemBTTBangHoa += 1;
                    player.removeItem(item.index, 1, true);
                    player.setAbility();
                    break;
                default:
                    player.getService().serverDialog("Item chưa được cập nhật");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
