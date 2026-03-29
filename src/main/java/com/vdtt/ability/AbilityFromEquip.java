package com.vdtt.ability;

import com.vdtt.clan.Member;
import com.vdtt.effect.Effect;
import com.vdtt.effect.EffectManager;
import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemOption;
import com.vdtt.map.world.planetblackstar.EnumBuffBlackBall;
import com.vdtt.map.world.planetblackstar.RewardDataBlackBall;
import com.vdtt.model.Char;
import com.vdtt.model.DanhHieu;
import com.vdtt.skill.Skill;
import com.vdtt.skill.SkillClan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class AbilityFromEquip {
    public void setAbility(Char owner) {
        try {
            int length = ItemManager.getInstance().getOptionSize();
            owner.pointPhamChat = 0;
            owner.options = new int[length];
            owner.haveOptions = new boolean[length];
            owner.optionsSupportSkill = new int[length];
            
            if(owner.selectDanhhieu < owner.danhHieu.size() && owner.selectDanhhieu != -1) {
                DanhHieu danhHieu = owner.danhHieu.get(owner.selectDanhhieu);
                ItemOption[] options = danhHieu.getItemOption();
                if(options != null && options.length > 0) {
                    for (ItemOption o : options) {
                        int optionID = o.getItemOptionTemplate().id;
                        owner.options[optionID] += o.getParam();
                        owner.haveOptions[optionID] = true;
                        if(o.getId() != 413)
                            owner.pointPhamChat += o.getParam();
                    }
                }
            }
            
            for (Item equip : owner.body) {
                if (equip != null && equip.getTemplate().type != 0) {
                    if (!equip.isExpired() && equip.strOption != null && !equip.strOption.isEmpty()) {
                        for (ItemOption o : equip.getItemOption()) {
                            int optionID = o.getItemOptionTemplate().id;
                            if ((o.getItemOptionTemplate().type >= 3 && o.getItemOptionTemplate().type <= 7) ||
                                    (o.getItemOptionTemplate().type == 19 || o.getItemOptionTemplate().type == 11 || o.getItemOptionTemplate().type == 12)) {
                                if ((o.getItemOptionTemplate().type == 19 && equip.upgrade >= 2)
                                        || (o.getItemOptionTemplate().type == 3 && equip.upgrade >= 4)
                                        || (o.getItemOptionTemplate().type == 11 && equip.upgrade >= 6)
                                        || (o.getItemOptionTemplate().type == 4 && equip.upgrade >= 8)
                                        || (o.getItemOptionTemplate().type == 12 && equip.upgrade >= 10)
                                        || (o.getItemOptionTemplate().type == 5 && equip.upgrade >= 12)
                                        || (o.getItemOptionTemplate().type == 6 && equip.upgrade >= 14)
                                        || (o.getItemOptionTemplate().type == 7 && equip.upgrade >= 16)) {
                                    owner.options[optionID] += o.getParam();
                                    owner.haveOptions[optionID] = true;
                                }
                            } else if (optionID >= 465 && optionID <= 468) {
                                owner.options[o.getParam2()] += o.getParam();
                                owner.haveOptions[o.getParam2()] = true;
                            } else {
                                owner.options[optionID] += o.getParam();
                                owner.haveOptions[optionID] = true;
                            }
                            if(o.getId() != 413)
                                owner.pointPhamChat += o.getParam();
                        }
                    }
                }
            }
            for (Item equip : owner.body2) {
                if (equip != null) {
                    if (!equip.isExpired() && equip.strOption != null && !equip.strOption.isEmpty()) {
                        for (ItemOption o : equip.getItemOption()) {
                            int optionID = o.getItemOptionTemplate().id;
                            if ((o.getItemOptionTemplate().type >= 3 && o.getItemOptionTemplate().type <= 7) ||
                                    (o.getItemOptionTemplate().type == 19 || o.getItemOptionTemplate().type == 11 || o.getItemOptionTemplate().type == 12)) {
                                if ((o.getItemOptionTemplate().type == 19 && equip.upgrade >= 2)
                                        || (o.getItemOptionTemplate().type == 3 && equip.upgrade >= 4)
                                        || (o.getItemOptionTemplate().type == 11 && equip.upgrade >= 6)
                                        || (o.getItemOptionTemplate().type == 4 && equip.upgrade >= 8)
                                        || (o.getItemOptionTemplate().type == 12 && equip.upgrade >= 10)
                                        || (o.getItemOptionTemplate().type == 5 && equip.upgrade >= 12)
                                        || (o.getItemOptionTemplate().type == 6 && equip.upgrade >= 14)
                                        || (o.getItemOptionTemplate().type == 7 && equip.upgrade >= 16)) {
                                    owner.options[optionID] += (o.getParam() * owner.increaseExtra / 100);
                                    owner.haveOptions[optionID] = true;
                                }
                            } else {
                                if (o.getId() == 327) {
                                    continue;
                                }
                                owner.options[optionID] += (o.getParam() * owner.increaseExtra / 100);
                                owner.haveOptions[optionID] = true;
                            }
                            if(o.getId() != 413)
                                owner.pointPhamChat += o.getParam();
                        }
                    }
                }
            }
            for (Skill skill : owner.skills) {
                if (skill != null && (skill.getSkillTemplate().type == 5 || skill.getSkillTemplate().id == 30) && skill.level > 0) {
                    if (skill.options != null && !skill.options.isEmpty()) {
                        for (ItemOption o : skill.getOptions()) {
                            int optionID = o.getItemOptionTemplate().id;
                            owner.options[optionID] += o.getParam();
                        }
                    }
                }
                if (skill != null && owner.isSuperSayan) {
                    switch (skill.isSkillTemplate) {
                        case 3:
                        case 8:
                        case 15:
                        case 21:
                        case 24:
                            if (skill.options != null && !skill.options.isEmpty()) {
                                for (ItemOption o : skill.getOptions()) {
                                    int optionID = o.getItemOptionTemplate().id;
                                    owner.options[optionID] += o.getParam();
                                }
                            }
                            break;
                    }
                }
                if (skill != null) {
                    if (owner.getEm().findByID(85) != null && skill.isSkillTemplate == 2) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(113) != null && skill.isSkillTemplate == 17) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(111) != null && skill.isSkillTemplate == 10) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(99) != null && skill.isSkillTemplate == 39) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(100) != null && skill.isSkillTemplate == 40) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(101) != null && skill.isSkillTemplate == 41) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(102) != null && skill.isSkillTemplate == 55) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(106) != null && skill.isSkillTemplate == 46) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(107) != null && skill.isSkillTemplate == 47) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(108) != null && skill.isSkillTemplate == 48) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(136) != null && skill.isSkillTemplate == 62) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (owner.getEm().findByID(59) != null && skill.isSkillTemplate == 82) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                    if (skill.isSkillTemplate == 93 && owner.getEm().findByID(171 + skill.level) != null) {
                        if (skill.options != null && !skill.options.isEmpty()) {
                            for (ItemOption o : skill.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                }
            }
            if (owner.isLuongLongNhatThe) {
                if (owner.pet != null) {
                    for (Skill skill : owner.pet.skillPet) {
                        if (skill != null && (skill.getSkillTemplate().type == 5 || skill.getSkillTemplate().id == 30) && skill.level > 0) {
                            if (skill.options != null && !skill.options.isEmpty()) {
                                for (ItemOption o : skill.getOptions()) {
                                    int optionID = o.getItemOptionTemplate().id;
                                    owner.options[optionID] += o.getParam();
                                }
                            }
                        }
                    }
                }
            }
            if (owner.clan != null) {
                if (!owner.clan.skillClans.isEmpty()) {
                    for (SkillClan skillClan : owner.clan.skillClans) {
                        if (skillClan != null && !skillClan.strOptions.isEmpty() && skillClan.getOptions() != null) {
                            for (ItemOption o : skillClan.getOptions()) {
                                int optionID = o.getItemOptionTemplate().id;
                                owner.options[optionID] += o.getParam();
                            }
                        }
                    }
                }
            }
            if (owner.selectSkill != null && owner.selectSkill.isSkillTemplate != 30) {
                Skill skill = owner.selectSkill;
                if (skill.getOptions() != null && skill.getOptions().length > 0) {
                    for (ItemOption o : skill.getOptions()) {
                        int optionID = o.getItemOptionTemplate().id;
                        owner.options[optionID] += o.getParam();
                    }
                }
            }
            if (owner.sach != null) {
                for (ItemOption o : owner.sach.getItemOption()) {
                    int optionID = o.getItemOptionTemplate().id;
                    owner.options[optionID] += o.getParam();
                    owner.haveOptions[optionID] = true;
                }
            }
            int[] options = owner.options;
            int dameDefaut = owner.options[314] + owner.options[380];
            int hpDefaut = owner.options[319] + owner.options[376];
            int mpDefaut = owner.options[318] + owner.options[381];
            short exactlyDefaut = (short) ((short) owner.options[316] + owner.options[377]);
            short missDefaut = (short) ((short) owner.options[315] + owner.options[378]);
            short criticalDefaut = (short) ((short) owner.options[317] + owner.options[379]);
            hpDefaut += hpDefaut * (owner.options[32] + owner.options[354] + owner.options[79] + owner.options[119]) / 100;
            owner.maxHP = owner.options[335] + owner.options[360] + owner.options[29] + owner.options[202] + owner.options[175] + owner.options[192] + owner.options[0] + owner.options[106] + owner.options[18] + (hpDefaut * 32) + options[469];
            mpDefaut += mpDefaut * (owner.options[33] + owner.options[355] + owner.options[80] + owner.options[120]) / 100;
            owner.maxMP = owner.options[336] + owner.options[361] + owner.options[419] + owner.options[1] + owner.options[107] + owner.options[19] + owner.options[30] + (mpDefaut * 30);
            dameDefaut += dameDefaut * (owner.options[414] + owner.options[425] + owner.options[34] + owner.options[358] + owner.options[47] + owner.options[122] + owner.options[254]) / 100;
            if (System.currentTimeMillis() <= owner.getLastTimeQuaCauDen() + 14000) {
                dameDefaut += (int) (dameDefaut * 1.9);
            }
            owner.damage = (int) (owner.options[2] + owner.options[31] + owner.options[78] + owner.options[146] + owner.options[199] + owner.options[208] + owner.options[276] + owner.options[277] + owner.options[341] + owner.options[366] + (dameDefaut * 2.6));
            owner.exactly = (short) (owner.options[20] + owner.options[65] + owner.options[167] + owner.options[205] + owner.options[287] + owner.options[339] + owner.options[352] + owner.options[364] + (exactlyDefaut * 3) + options[471]);
            owner.miss = (short) (owner.options[64] + owner.options[151] + owner.options[161] + owner.options[204] + owner.options[278] + owner.options[337] + owner.options[351] + owner.options[362] + owner.options[4] + (missDefaut * 3) + options[472]);
            owner.crit = (short) (owner.options[5] + owner.options[15] + owner.options[28] + owner.options[63] + owner.options[144] + owner.options[166] + owner.options[203] + owner.options[338] + owner.options[363] + (criticalDefaut * 2.5) + options[470]);
            switch (owner.sys) {
                case 1://goku
                    owner.damage += 33;
                    owner.maxHP += 520;
                    owner.maxMP += 300;
                    owner.exactly += 32;
                    owner.miss += 30;
                    owner.crit += 24;
                    break;
                case 2://cadic
                    owner.damage += 36;
                    owner.maxHP += 480;
                    owner.maxMP += 250;
                    owner.exactly += 24;
                    owner.miss += 24;
                    owner.crit += 30;
                    break;
                case 3://gohan
                    owner.damage += 33;
                    owner.maxHP += 560;
                    owner.maxMP += 350;
                    owner.exactly += 36;
                    owner.miss += 27;
                    owner.crit += 21;
                    break;
                case 4://picolo
                    owner.damage += 28;
                    owner.maxHP += 600;
                    owner.maxMP += 400;
                    owner.exactly += 28;
                    owner.miss += 21;
                    owner.crit += 27;
                    break;
                case 5://Frieza
                    owner.damage += 33;
                    owner.maxHP += 520;
                    owner.maxMP += 300;
                    owner.exactly += 32;
                    owner.miss += 30;
                    owner.crit += 24;
                    break;
            }
            owner.armor = (short) (owner.options[12] + owner.options[40] + owner.options[81] + owner.options[121] + owner.options[152] + owner.options[201] + owner.options[258] + owner.options[273] + owner.options[340] + owner.options[365] + options[473]);
            owner.reduceDame = (short) (IntStream.of(13, 173, 206, 274, 279, 322, 359).map(i -> owner.options[i]).sum());
            owner.reduceCrit = (short) (IntStream.of(345, 404).map(i -> owner.options[i]).sum());
            owner.reduceSuyGiam = (short) (IntStream.of(288).map(i -> owner.options[i]).sum());
            owner.reduceChiuDon = (short) (IntStream.of(291).map(i -> owner.options[i]).sum());
            owner.reduceStun = (short) (IntStream.of(292).map(i -> owner.options[i]).sum());
            owner.suyGiam = (short) (IntStream.of(48, 68, 123, 168, 259).map(i -> owner.options[i]).sum());
            owner.chiuDon = (short) (IntStream.of(51, 71, 126, 171, 262).map(i -> owner.options[i]).sum());
            owner.stun = (short) (IntStream.of(52, 72, 127, 172, 263).map(i -> owner.options[i]).sum());
            owner.randomDame = (short) (IntStream.of(297).map(i -> owner.options[i]).sum());
            owner.speed = (short) (800 + (IntStream.of(17, 91, 118, 150, 346).map(i -> owner.options[i]).sum()));
            owner.counterDame = (short) (IntStream.of(16).map(i -> owner.options[i]).sum());
            owner.counterDame += (short) (owner.options[162] + owner.options[424] + owner.options[67]);
            owner.skipMiss = (short) (IntStream.of(4, 147, 160, 356, 422).map(i -> owner.options[i]).sum());
            owner.armorPenetration = (short) (IntStream.of(145, 149, 197, 275, 357, 421).map(i -> owner.options[i]).sum());
            owner.criticalHitPower = (short) (IntStream.of(41, 95, 398).map(i -> owner.options[i]).sum());
            owner.reduceCritHitPower = (short) (IntStream.of(42, 43, 44, 45, 46, 174, 353).map(i -> owner.options[i]).sum());
            owner.increaseXu = (short) (IntStream.of(344, 369, 375).map(i -> owner.options[i]).sum());
            owner.increaseKI = (short) (IntStream.of(66, 267, 343, 368).map(i -> owner.options[i]).sum());
            owner.damegeMonster = (short) (IntStream.of(207, 342, 367, 3, 423).map(i -> owner.options[i]).sum());
            owner.speed -= (short) owner.options[429];
            owner.damage -= owner.options[430];
            owner.exactly -= (short) owner.options[431];
            owner.increaseKI -= (short) (owner.options[432] * 10);

            if (owner.crit > 7000) {
                owner.damage += (owner.crit - 7000) * 1;
                owner.crit = 7000;
            }
            
            if (owner.hp > owner.maxHP) {
                owner.hp = owner.maxHP;
            }
            if (owner.mp > owner.maxMP) {
                owner.mp = owner.maxMP;
            }
            EffectManager em = owner.getEm();
            Effect eff = em.findByID(150);
            if (eff != null) {
                owner.increaseKI += 2000;
                owner.damage += 200;
                owner.skipMiss += 2;
                owner.armorPenetration += 2;
            }

            eff = em.findByID(188);
            if (eff != null) {
                owner.increaseKI += 2500;
                owner.damage += 300;
                owner.increaseXu += 100;
                owner.skipMiss += 3;
                owner.armorPenetration += 3;
                owner.miss += 100;
            }
            
            eff = em.findByID(39);
            if (eff != null) {
                owner.maxHP += eff.param;
                owner.maxMP += eff.param;
            }
//            thú cưỡi 
            eff = em.findByID(145);
            if (eff != null) {
                owner.maxHP += eff.param;
                owner.speed += 50;
            }
            eff = em.findByID(185);
            if (eff != null) {
                owner.maxHP += 1000;
                owner.damage += 100;
                owner.reduceDame += 100;
                owner.reduceCrit += 100;
                owner.speed += 50;
            }
            eff = em.findByID(148);
            if (eff != null) {
                owner.maxHP += 2000;
                owner.armor += 200;
                owner.crit += 150;
                owner.speed += 50;
            }
            eff = em.findByID(194);
            if (eff != null) {
                owner.maxHP += 2000;
                owner.speed += 50;
                owner.increaseKI += 150;
            }
            eff = em.findByID(195);
            if (eff != null) {
                owner.maxHP += 2000;
                owner.speed += 50;
                owner.increaseKI += 150;
            }
            eff = em.findByID(196);
            if (eff != null) {
                owner.maxHP += 2000;
                owner.speed += 50;
                owner.increaseKI += 80;
                owner.increaseXu += 80;
            }

            eff = em.findByID(168);
            if (eff != null) {
                owner.maxMP += 750;
                owner.crit += (short) eff.param;
                owner.exactly += (short) eff.param;
            }

            eff = em.findByID(169);
            if (eff != null) {
                owner.maxMP += 750;
                owner.miss += (short) eff.param;
                owner.armor += (short) eff.param;
                owner.damage += eff.param;
            }
//            end
            eff = em.findByID(40);
            if (eff != null) {
                owner.armor += (short) eff.param;
            }
            eff = em.findByID(41);
            if (eff != null) {
                owner.damage += eff.param;
            }
            Effect buffHPMP = owner.getEm().findByID(39);
            if (buffHPMP != null) {
                owner.maxHP += buffHPMP.getParam();
                owner.maxMP += buffHPMP.getParam();
            }
            if (owner.getEm().findByID(137) != null) {
                owner.damage += owner.getEm().findByID(137).getParam();
            }
            if (owner.getEm().findByID(139) != null) {
                owner.maxHP += owner.getEm().findByID(139).getParam();
            }
            if (owner.getEm().findByID(202) != null) {
                owner.maxHP += 1000;
                owner.damage += 100;
                owner.exactly += 100;
                owner.skipMiss += 5;
            }
            if (owner.getEm().findByID(158) != null) {
                owner.crit += 100;
                owner.damage += 200;
                owner.maxHP += 1500;
            }
            if (owner.getEm().findByID(205) != null) {
                owner.damage += 1500;
            }
            if (owner.getEm().statusWithID(147)) {
                owner.maxHP += 1000;
                owner.damage += 500;
                owner.armor += 500;
                owner.miss += 750;
            }
            if (owner.getEm().statusWithID(151)) {
                owner.maxHP += 1000;
                owner.damage += 1000;
                owner.armor += 1000;
                owner.miss += 1500;
            }
            if (owner.getEm().statusWithID(207)) {
                owner.damage += owner.getEm().findByID(207).getParam();
            }
            if (owner.getEm().statusWithID(208)) {
                owner.damage -= owner.getEm().findByID(208).getParam();
            }
            if (owner.getEm().statusWithID(206)) {
                int param = owner.getEm().findByID(206).getParam();
                owner.damage += param;
                owner.crit += param;
            }
            if (owner.getEm().statusWithID(9)) {
                owner.speed -= 300;
            }
            if (owner.getEm().statusWithID(10)) {
                owner.speed -= 400;
            }
            if (owner.getEm().statusWithID(178)) {
                owner.maxHP += 2000;
                owner.skipMiss += 1;
                owner.armorPenetration += 1;
                owner.criticalHitPower += 10;
            }
            
            owner.damage += owner.diemBTTBangHoa * 100;
            if (owner.clan != null) {
                Member member = owner.clan.getMemberByName(owner.name);
                if (member != null && member.getRewardDataBlackBall() != null) {
                    List<RewardDataBlackBall> toRemove = new ArrayList<>();
                    for (RewardDataBlackBall rewardDataBlackBall : member.getRewardDataBlackBall()) {
                        boolean removeRewardData = true;
                        HashMap<EnumBuffBlackBall, Long> buff = rewardDataBlackBall.getBuff();
                        if (buff != null) {
                            for (Map.Entry<EnumBuffBlackBall, Long> entry : buff.entrySet()) {
                                EnumBuffBlackBall key = entry.getKey();
                                Long value = entry.getValue();
                                if (value > 0 && System.currentTimeMillis() <= value + 604800016) {
                                    removeRewardData = false;
                                    switch (key) {
                                        case CRIT:
                                            owner.crit += (short) key.param;
                                            break;
                                        case DAME:
                                            owner.damage += key.param;
                                            break;
                                        case HP:
                                            owner.maxHP += key.param;
                                            break;
                                    }
                                } else {
                                    buff.remove(key);
                                }
                            }
                            if (removeRewardData) {
                                toRemove.add(rewardDataBlackBall);
                            }
                        }
                    }
                    member.getRewardDataBlackBall().removeAll(toRemove);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
