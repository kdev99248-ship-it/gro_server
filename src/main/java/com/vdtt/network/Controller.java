package com.vdtt.network;

import com.vdtt.achievement.PlayerAchievementList;
import com.vdtt.clan.Clan;
import com.vdtt.effect.Effect;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.handler.PvPHandler;
import com.vdtt.handler.UpgradeHandler;
import com.vdtt.item.Item;
import com.vdtt.item.ItemOption;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.martialarts.ZMartialArts;
import com.vdtt.map.world.planetblackstar.ZPlanetBlackStar;
import com.vdtt.map.world.powerstation.ZPowerTournament;
import com.vdtt.model.Char;
import com.vdtt.model.Pet;
import com.vdtt.model.RandomItem;
import com.vdtt.model.User;
import com.vdtt.server.ServerManager;
import com.vdtt.server.ShowRanked;
import com.vdtt.skill.Skill;
import com.vdtt.skill.SkillFactory;
import com.vdtt.stall.StallManager;
import com.vdtt.util.Log;
import com.vdtt.util.TimeUtils;
import com.vdtt.util.Util;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Controller implements IMessageHandler {
    private final Session client;
    @Setter
    private Service service;
    @Setter
    private User user;
    private Char _char;

    public Controller(Session client) {
        this.client = client;
    }

    public void setChar(Char _char) {
        this._char = _char;
    }

    @Override
    public void onMessage(Message mss) {
        if (mss != null) {
            try {
                int command = mss.getCommand();
                if (command != -127 && command != -113 && command != -124 && command != -123 && command != -122) {
                    if (user == null || _char == null || user.isCleaned || _char.isCleaned) {
                        return;
                    }
                }
                switch (command) {
                    case -127:
//                        String keyApp = mss.readUTF();
                        break;
                    case -113:
                        client.readKey(mss);
                        break;
                    case -123:
                        messageSub_123(mss);
                        break;
                    case -124:
                        user.createCharacter(mss);
                        break;
                    case -122:
                        messageSub_122(mss);
                        break;
                    case 21:
                        _char.chatPublic(mss);
                        break;
                    case 127:
                        _char.requestChangeMap();
                        break;
                    case 14:
                        _char.learnSkill(mss);
                        break;
                    case 63:
                        _char.showDame(mss);
                        break;
                    case -6:
                        service.openTabZone();
                        break;
                    case 116:
                        _char.useItem(mss);
                        break;
                    case 113:
                        _char.itemBodyToBag(mss);
                        break;
                    case -56:
                        short var = mss.reader().readShort();
                        service.showKI();
                        break;
                    case -62:
                        _char.showKIChar(mss);
                        break;
                    case 13:
                        short idMob = mss.reader().readShort();
                        service.showMob(idMob);
                        break;
                    case -7:
                        _char.changeZone(mss);
                        break;
                    case 54:
                        _char.openMenu(mss);
                        break;
                    case 53:
                        _char.menu(mss);
                        break;
                    case 122:
                        messageGUI(mss);
                        break;
                    case 115:
                        _char.itemBagToBox(mss);
                        break;
                    case 114:
                        _char.itemBoxToBag(mss);
                        break;
                    case 49:
                        _char.wakeUpFromDead(mss);
                        break;
                    case 48:
                        _char.returnFromDead();
                        break;
                    case 121:
                        _char.buyItem(mss);
                        break;
                    case 117:
                        _char.sortItem(mss);
                        break;
                    case 107:
                        _char.upgradeItem(mss);
                        break;
                    case -25:
                        _char.confirmMenuItem(mss);
                        break;
                    case -50:
                        UpgradeHandler.upgradeMask(_char, mss);
                        break;
                    case 36:
                        _char.itemBagToBody2(mss);
                        break;
                    case 37:
                        _char.itemBody2ToBag(mss);
                        break;
                    case 35:
                        _char.itemBodyChange(mss);
                        break;
                    case -52:
                        _char.showCaiTrangTach(mss);
                        break;
                    case -51:
                        UpgradeHandler.downgradeMask(_char, mss);
                        break;
                    case 108:
                        _char.upPearl(mss);
                        break;
                    case -46:
                        _char.khamNgoc(mss);
                        break;
                    case -47:
                        _char.tachKham(mss);
                        break;
                    case 105:
                        _char.splitItem(mss);
                        break;
                    case 95:
                        int idMail = mss.reader().readShort();
                        _char.mailManager.receiveMail(idMail);
                        break;
                    case 88:
                        _char.removeMail(mss);
                        break;
                    case 111:
                        _char.throwItem(mss);
                        break;
                    case 118:
                        _char.inputNumberSplit(mss);
                        break;
                    case 119:
                        _char.saleItem(mss);
                        break;
                    case 59:
                        _char.pickItem(mss);
                        break;
                    case 39:
                        _char.addPartyAccept(mss);
                        break;
                    case 41:
                        if (mss.reader().available() == 0)
                            _char.createGroup();
                        else
                            _char.addParty(mss);
                        break;
                    case 42:
                        _char.lockParty();
                        break;
                    case 43:
                        if (_char.getGroup() != null) {
                            _char.getGroup().getGroupService().playerInParty();
                        }
                        break;
                    case 44:
                        _char.outParty();
                        break;
                    case 45:
                        _char.openFindParty();
                        break;
                    case 46:
                        _char.changeTeamLeader(mss);
                        break;
                    case 47:
                        _char.moveMember(mss);
                        break;
                    case 12:
                        _char.updateTaskCount(1);
                        break;
                    case 34:
                        _char.checkInfo(mss);
                        break;
                    case 26:
                        if (_char.getGroup() != null) {
                            _char.getGroup().getGroupService().chat(_char.name, mss.readUTF());
                        }
                        break;
                    case 22:
                        _char.chatGlobal(mss);
                        break;
                    case 28:
                        _char.chatPrivate(mss);
                        break;
                    case -15:
                        if (_char.zone.map.isHell() || _char.zone.map.isNgucTu() || _char.zone instanceof ZMartialArts || _char.zone instanceof ZPowerTournament || _char.zone instanceof ZPlanetBlackStar) {
                            _char.serverDialog("Không thể đổi cờ ở map này");
                            return;
                        }
                        _char.setTypePk(mss.reader().readByte());
                        break;
                    case 72:
                        byte type = mss.reader().readByte();
                        _char.getQuaySo().spin(_char, type);
                        break;
                    case 74:
                        _char.claimQuaySo(mss);
                        break;
                    case 25:
                        if (_char.clan != null) {
                            _char.clan.getClanService().chat(_char.name, mss.readUTF());
                        }
                        break;
                    case 62:
                        _char.learnSkillPet(mss);
                        break;
                    case 86:
                        _char.tradeInvite(mss);
                        break;
                    case 85:
                        if (_char.trade != null) {
                            _char.trade.openUITrade();
                        }
                        break;
                    case 82:
                        _char.tradeItemLock(mss);
                        break;
                    case 81:
                        _char.tradeAccept();
                        break;
                    case 84:
                        _char.tradeClose();
                        break;
                    case 83:
                        _char.tradeClose();
                        break;
                    case -22:
                        ShowRanked.gI().show(mss, _char);
                        break;
                    case -33:
                        _char.input(mss);
                        break;
                    case 5:
                        _char.submenu(mss);
                        break;
                    case 87:
                        _char.sendMail(mss);
                        break;
                    case 101:
                        _char.openUIStall(mss);
                        break;
                    case 99:
                        _char.saleItemToStall(mss);
                        break;
                    case 100:
                        StallManager.getInstance().openUIMeSell(_char);
                        break;
                    case 98:
                        _char.buyItemStall(mss);
                        break;
                    case -19:
                        _char.raoBan(mss);
                        break;
                    case 79:
                        _char.addFriend(mss);
                        break;
                    case 76:
                        _char.removeFriend(mss);
                        break;
                    case 104:
                        UpgradeHandler.moveOption(_char, mss);
                        break;
                    case 30:
                        PvPHandler.TuChoiTyVo(_char, mss);
                        break;
                    case 31:
                        PvPHandler.StartTyVo(_char, mss);
                        break;
                    case 32:
                        PvPHandler.TyVo(_char, mss);
                        break;
                    case 19:
                        PvPHandler.CuuSat(_char, mss);
                        break;
                    case -92:
                        int ballz = mss.reader().readInt();
                        if(ballz <= 0 || _char == null) {
                            return;
                        }
                        
                        switch (_char.typePopup) {
                            case 0:
                                if(_char.stadium != null) {
                                    if(_char.stadium.getPlayer1() != null) {
                                        _char.stadium.startMatch(_char,ballz);
                                    } else
                                        _char.getService().serverMessage("Không tìm thấy người chơi");
                                }
                                break;
                            case 1:
                                Item dongzeni = _char.getItemInBag(1155);
                                if(dongzeni == null || dongzeni.getQuantity() < ballz) {
                                    _char.serverDialog("Thiếu đồng zeni.");
                                    return;
                                }
                                int minZeni = 10;
                                int zeniSend = minZeni *  ballz;
                                if(_char.zeni < minZeni || _char.zeni < zeniSend) {
                                    _char.serverDialog("Không đủ zeni để chuyển.");
                                    return;
                                }
                                Char charReceive = ServerManager.findCharByName(_char.nameZeni);
                                if(charReceive == null) {
                                    _char.serverDialog("Người chơi không online hoặc không có.");
                                    return;
                                }

                                _char.addZeni(-zeniSend,true);
                                _char.removeItem(dongzeni.index, ballz, true);
                                charReceive.addZeni(zeniSend,true);
                                _char.serverDialog("Bạn đã gửi " + Util.formatMoney(zeniSend) + " zeni cho người chơi " + charReceive.name + " thành công");
                                Util.log("Bạn đã gửi " + Util.formatMoney(zeniSend) + " zeni cho người chơi " + charReceive.name + " thành công","send_zeni_" + _char.name + "_" + charReceive.name);
                                charReceive.serverDialog("Người chơi " + _char.name + " đã gửi bạn " + Util.formatMoney(zeniSend) + " zeni");
                                Util.log("Người chơi " + _char.name + " đã gửi bạn " + Util.formatMoney(zeniSend) + " zeni","send_zeni_" + charReceive.name + "_" + _char.name);
                                break;
                        }
                        
                        break;
                    case -93:
                        String nameTd = mss.readUTF();
                        if(_char == null || nameTd.isEmpty())
                            return;

                        switch (_char.typePopup) {
                            case 0:
                                if(_char.stadium != null)
                                    _char.stadium.handlePlayerInvitation(_char,nameTd);
                                break;
                            case 1:
                                if(_char.getItemInBag(1155) == null) {
                                    _char.serverDialog("Thiếu đồng zeni.");
                                    return;
                                }

                                if(nameTd.equals(_char.name)) {
                                    _char.serverDialog("Không thể tự gửi cho chính mình.");
                                    return;
                                }
                                
                                if(ServerManager.findCharByName(nameTd) == null) {
                                    _char.serverDialog("Người chơi không online hoặc không có.");
                                    return;
                                }
                                
                                _char.nameZeni = nameTd;
                                _char.getService().openPopupSoLuong((byte) 1);
                                break;
                            case 2:
                                Item item = _char.getItemInBag(952);
                                if(item == null) {
                                    _char.serverDialog("Thiếu thiệp chúc tết.");
                                    return;
                                }
                                
                                if(nameTd.equals(_char.name)) {
                                    _char.serverDialog("Không thể tự chúc tết cho chính mình.");
                                    return;
                                }
                                
                                Char pl = ServerManager.findCharByName(nameTd);
                                if(pl == null) {
                                    _char.serverDialog("Người chơi không online hoặc không có.");
                                    return;
                                }
                                if (pl.getSlotNull() == 0) {
                                    _char.serverDialog("Hành trang người chơi chúc tết đã đầy.");
                                    return;
                                }
                                int itemId = -1;
                                boolean isV = Util.nextInt(100) > 20;
                                if (isV) {
                                    itemId = RandomItem.ITEM_EVENT_NORMAL.next();
                                } else {
                                    itemId = RandomItem.ITEM_EVENT_VIP.next();
                                } 

                                Item newItem = new Item(itemId, "sk");
                                newItem.setQuantity(1);
                                //        newItem.expire = -1;
                                if (item.isLock) {
                                    newItem.isLock = true;
                                } else {
                                    newItem.isLock = false;
                                }
                                if (newItem.id >= 1114 && newItem.id <= 1129) {
                                    newItem.setExpire(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
                                    newItem.addOptionRandom(433, 1, 80);
                                } else if (newItem.id >= 918 && newItem.id <= 920) {
                                    newItem.setExpire(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
                                    newItem.strOption = "0,3000;1,3000;2,350;5,350;258,350;151,350;167,350;297,50";
                                } else if (newItem.id >= 896 && newItem.id <= 898) {
                                    newItem.setExpire(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
                                    newItem.strOption = "0,2000;1,2000;2,200;5,200;258,200;151,200;167,200;297,20";
                                } else if (newItem.id == 1050 || newItem.id == 1051) {
                                    newItem.strOption = "0,1000;2,150;5,10";
                                }
                                int newId = newItem.id;
                                if (newId == 989) {
                                    newItem.addOptionRandom(0, 2000, 3500);
                                    newItem.addOptionRandom(2, 150, 315);
                                    newItem.addOptionRandom(5, 150, 315);
                                    newItem.addOptionRandom(167, 150, 214);
                                    newItem.addOptionRandom(297, 15, 32);
                                    newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                                } else if (newId == 298) {
                                    newItem.strOption = "327,58;380,8;2,130;0,1300";
                                } else if (newId == 555) {
                                    newItem.strOption = "327,88;379,8;2,120;0,1200";
                                } else if (newId == 431) {
                                    newItem.strOption = "327,48;380,8;2,140;0,1400";
                                } else if (newId == 519) {
                                    newItem.addOptionRandom(0, 2000, 3500);
                                    newItem.addOptionRandom(2, 150, 315);
                                    newItem.addOptionRandom(5, 150, 315);
                                    newItem.addOptionRandom(167, 150, 214);
                                    newItem.addOptionRandom(297, 15, 32);
                                } else if (newId == 972) {
                                    newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                                    newItem.addOptionRandom(0, 2000, 3500);
                                    newItem.addOptionRandom(2, 150, 315);
                                    newItem.addOptionRandom(5, 150, 315);
                                    newItem.addOptionRandom(167, 150, 214);
                                    newItem.addOptionRandom(297, 15, 32);
                                } else if (newId == 848) {
                                    newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                                    newItem.addOptionRandom(0, 1000, 1500);
                                    newItem.addOptionRandom(2, 150, 250);
                                    newItem.addOptionRandom(5, 150, 250);
                                    newItem.addOptionRandom(167, 100, 214);
                                    newItem.addOptionRandom(297, 10, 25);
                                } else if (newId == 374) {
                                    newItem.addOption(327, 89);
                                    newItem.addOptionRandom(379, 5, 12);
                                    newItem.addOptionRandom(2, 50, 160);
                                    newItem.addOptionRandom(0, 500, 1300);
                                } else if (newId == 284) {
                                    newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                                    newItem.addOptionRandom(0, 1000, 1500);
                                    newItem.addOptionRandom(2, 150, 250);
                                    newItem.addOptionRandom(5, 150, 250);
                                    newItem.addOptionRandom(167, 100, 214);
                                    newItem.addOptionRandom(297, 10, 25);
                                } else if (newId == 309) {
                                    newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                                    newItem.addOptionRandom(0, 1000, 1500);
                                    newItem.addOptionRandom(2, 100, 150);
                                    newItem.addOptionRandom(5, 100, 150);
                                    newItem.addOptionRandom(167, 100, 150);
                                    newItem.addOptionRandom(297, 10, 15);
                                } else if (newId == 460) {
                                    newItem.strOption = "376,12,-1;379,12,-1;377,12,-1;2,500;327,62,62";
                                } else if (newId == 525) {
                                    newItem.strOption = "327,68,68;149,1,-1;376,10,-1;152,100,-1";
                                }
                                if (pl.addItemToBag(newItem)) {
                                    pl.getService().addItem(newItem);
                                }
                                int coin = Util.nextInt(50, 100) * 1000;
                                pl.addCoin(coin, true);
                                if (pl.exp <= 230000000000L) {
                                    if (!pl.lockEXP) {
                                        pl.exp += Util.nextInt(100000, 150000);
                                        pl.KI += Util.nextInt(100000, 150000);
                                        pl.getService().showKI(pl);
                                    }
                                }
                                if (!isV) {
                                    _char.addEventPoint(KeyPoint.BANH_CHUNG, 1);
                                } else {
                                    _char.addEventPoint(KeyPoint.BANH_GIAY, 1);
                                }

                                _char.removeItem(item.index, 1, true);
                                break;
                        }
                        break;
                    default:
                        Log.debug("Command not found: " + command);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void messageGUI(Message mss) {
        try {
            if (user == null || _char == null || user.isCleaned || _char.isCleaned) {
                return;
            }
            int command = mss.reader().readByte();
            Log.debug("GUI ID: " + command);
            switch (command) {
                case 50:
                    service.openBox();
                    break;
                case 54:
                    if (_char.clan == null) {
                        service.showListVuTru();
                    } else {
                        service.sendInfoVuTru();
                    }
                    if (_char.taskId == 17 && _char.taskMain != null && _char.taskMain.index == 0) {
                        _char.updateTaskCount(1);
                    }
                    break;
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                    _char.requestItem((byte) command);
                    break;
                case 82:
                    service.openGUI(82);
                    break;
                case 111:
                    if (_char.taskId == 5 && _char.taskMain != null && _char.taskMain.index == 0) {
                        _char.updateTaskCount(1);
                    }
                    service.thongBao(mss);
                    break;
                case 94:
                    service.openGUI(94);
                    break;
                case 95:
                    service.openGUI(95);
                    break;
                case 108:
                    service.hopNhatItem();
                    break;
                case 89:
                    service.openNapLanDau();
                    break;
                case 88:
                    service.openRewardDaily();
                    break;
                case 92:
                    if (_char.taskId == 3 && _char.taskMain != null && _char.taskMain.index == 0) {
                        _char.updateTaskCount(1);
                    }
                    break;
                case 86:
                    service.openQuaySo();
                    break;
                case 72:
                    service.sendAchievementInfo();
                    break;
                case 97:
                    service.openMaBaoVe(_char);
                    break;
                default:
                    service.openGUI(command);
                    Log.debug("Command not found(122): " + command);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void messageSub_122(Message mss) {
        try {
            int command = mss.reader().readByte();
            switch (command) {
                case -127:
                    selectChar(mss);
                    break;
                default:
                    Log.debug("Command not found(-122): " + command);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectChar(Message mss) {
        try {
            int id = mss.reader().readByte();
            user.sltChar = user.chars.get(0);

            if (!user.sltChar.load()) {
                client.disconnect();
                return;
            }
            user.sltChar.setLoadFinish(true);
            user.chars.clear();
            service.selectChar(user);
            service.sendIntoGame(user.sltChar);
            setChar(user.sltChar);
            if (user.sltChar.mapId == 39) {
                MapManager.getInstance().getMaps().get(56).joinZone(user.sltChar, 0, -1);
            } else {
                MapManager.getInstance().getMaps().get(user.sltChar.mapId).joinZone(user.sltChar, 0, -1);
            }
            if (user.sltChar.zone.map.isWorld()) {
                MapManager.getInstance().getMaps().get(56).joinZone(user.sltChar, 0, -1);
            }
            user.sltChar.setAbility();
            updateSkillCaiTrang();
            user.sltChar.hp = user.sltChar.maxHP;
            user.sltChar.mp = user.sltChar.maxMP;
            service.updateHP();
            service.updateMP();
            service.updateHPFull();
            service.updateMPFull();
            user.sltChar.preLoad();
            ServerManager.addChar(user.sltChar);
            service.updateTienIch();
            service.sendSachChienDau();
            _char.zone.getService().meLive(_char);
            if (_char.body[0] != null) {
                _char.pet = new Pet(_char.id, _char.body[0], _char.body, _char.body2, service, _char.getEm());
                _char.pet.zone = _char.zone;
            }
            long timeReming = _char.timeZoom - System.currentTimeMillis();
            if (timeReming > 0) {
                Effect effect = new Effect(116, timeReming, 0);
                _char.getEm().setEffect(effect);
            }

//            _char.serverDialog("Giftcode hiện có: goirongz, kichhoat, vnvodich, vnchienthang, binnnnnne7s");
        } catch (Exception e) {
            client.disconnect();
        }
    }

    private void updateSkillCaiTrang() {
        try {
            if (user.sltChar.options[327] > 0) {
                int point = 1;
//                if (user.sltChar.options[327] == 93) {
//                    point = 3;
//                }
//                if (user.sltChar.options[327] == 62) {
//                    point = 2;
//                }
                Skill skill = SkillFactory.getInstance().getSkill(user.sltChar.options[327], point);
                if (skill != null) {
                    int index = user.sltChar.findIndexSkillWithCT();
                    if (index != -1) {
                        user.sltChar.skills.remove(index);
                    }
                    skill.timeCoolDown = user.sltChar.timeCoolDownSkillCaiTrang;
                    skill.isSkillCaiTrang = true;
                    user.sltChar.addSkill(skill);
                    user.sltChar.getService().updateSkillCaiTrang();
                    user.sltChar.getService().updateSkill();
                }
            } else {
                int index = user.sltChar.findIndexSkillWithCT();
                if (index != -1) {
                    user.sltChar.skills.remove(index);
                    user.sltChar.getService().updateSkill();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void messageNotMap(Message ms) {

    }

    public void messageSub_123(Message ms) {
        try {
            int command = ms.reader().readByte();
            if (command != -127 && command != -126) {
                if (user == null || _char == null || user.isCleaned || _char.isCleaned) {
                    return;
                }
            }
            switch (command) {
                case -49:
                    byte typeMbv = ms.reader().readByte();
                    String mbv = ms.readUTF();
                    if(_char != null) {
                        if(typeMbv == 1 && _char.MaBaoVe.isEmpty()) {
                            if(mbv.length() >= 6) {
                                _char.MaBaoVe = mbv;
                                _char.getService().serverMessage("Tạo mã bảo vệ thành công");
                                _char.getService().openMaBaoVe(_char);
                            }
                            else 
                                _char.getService().serverMessage("Mã bảo vệ yêu cầu tối thiểu 6 ký tự");
                        } else if(typeMbv == 2 && !_char.isUnlock()) {
                            if(mbv.equals(_char.MaBaoVe)) {
                                _char.IsDaMoKhoa = true;
                                _char.TimeReset = 0;
                                _char.getService().serverMessage("Mở khoá thành công");
                                _char.getService().openMaBaoVe(_char);
                            }
                            else
                                _char.getService().serverMessage("Mã bảo vệ không đúng, vui lòng thử lại.");
                        } else if(typeMbv == 3 && !_char.MaBaoVe.isEmpty()) {
                            if(mbv.equals(_char.MaBaoVe)) {
                                _char.MaBaoVe = "";
                                _char.IsDaMoKhoa = false;
                                _char.TimeReset = 0;
                                _char.getService().serverMessage("Xoá mã bảo vệ thành công");
                                _char.getService().openMaBaoVe(_char);
                            } else
                                _char.getService().serverMessage("Mã bảo vệ không đúng, vui lòng thử lại.");
                        } else if(typeMbv == 0) {
                            if(_char.TimeReset != 0) {
                                _char.getService().serverMessage("Bạn đã kích hoạt huỷ rồi.");
                                return;
                            }
                            _char.TimeReset = (int) (System.currentTimeMillis() / 1000 + 259200);
                            _char.getService().openMaBaoVe(_char);
                            _char.getService().serverMessage("Mã bảo vệ sẽ tự huỷ sau 3 ngày.");
                        }
                    }
                    break;
                case -38:
                    claimReward(ms);
                    break;
                case -127:
                    client.login(ms);
                    break;
                case -126:
                    service.sendTabLogin();
                    break;
                case 6:
                    service.showPet();
                    break;
                case -73:
                    _char.getInfo2(ms);
                    break;
                case -74:
                    _char.selectDanhhieu = ms.reader().readByte();
                    service.updateTitle();
                    _char.setAbility();
                    break;
                case -47:
                    _char.showInfoGhepCT(ms);
                    break;
                case 19:
                    Log.debug("var: " + ms.reader().readByte());// level bien hinh
                    break;
                case -42:
                    _char.ducLoKham(ms);
                    break;
                case 18:
                    _char.convertUpgrade(ms);
                    break;
                case -50:
                    _char.reciveAllMail();
                    break;
                case -70:
                    _char.claimedReward(ms);
                    break;
                case -60:
                    _char.drawRewardDaily();
                    break;
                case -63:
                    _char.buyPackReward(ms);
                    break;
                case -82:
                    _char.extendBox();
                    break;
                case -57:
                    service.sendDuaTop();
                    break;
                case -18:
                    if (_char.zone.map.isNgucTu()) {
                        MapManager.getInstance().joinZone(_char, 54, 0, 1);
                    } else
                        MapManager.getInstance().joinZone(_char, 56, 0, -1);
                    break;
                case -11:
                    if (_char.lastTimeDoneTask - System.currentTimeMillis() > 0) {
                        _char.serverDialog("Hành động quá nhanh vui lòng thử lại sau " + Util.getFormatTime(_char.lastTimeDoneTask - System.currentTimeMillis()));
                        return;
                    }
                    _char.checkDoneTask();
                    _char.lastTimeDoneTask = System.currentTimeMillis() + _char.delayDoneTask;
                    break;
                case -85:
                    _char.thuVanMay(ms);
                    break;
                case -106:
                    _char.createClan(ms);
                    break;
                case -105:
                    _char.clanInvite(ms);
                    break;
                case -100:
                    _char.acceptInviteClan(ms);
                    break;
                case -102:
                    _char.declineIntiveClan(ms);
                    break;
                case -104:
                    _char.requestJoinClan(ms);
                    break;
                case -101:
                    _char.acceptRequestJoinClan(ms);
                    break;
                case -10:
                    service.openSettingClan();
                    break;
                case -99:
                    _char.changeClanType(ms);
                    break;
                case -9:
                    _char.setTimeDunClan(ms);
                    break;
                case -96:
                    _char.outClan();
                    break;
                case -97:
                    _char.moveOutClan(ms);
                    break;
                case -95:
                    _char.inputCoinClan(ms);
                    break;
                case -93:
                    if (_char.clan != null) {
                        String alert = ms.readUTF();
                        int typeClan = _char.clan.getMemberByName(_char.name).getType();
                        if (typeClan == Clan.TYPE_TOCTRUONG || typeClan == Clan.TYPE_TOCPHO) {
                            _char.clan.setAlert(alert);
                            service.resetScreen();
                            service.sendInfoVuTru();
                        }
                    }
                    break;
                case 24:
                    _char.uocRongCerealian(ms);
                    break;
                case -48:
                    short mapId = ms.reader().readShort();
                    short var2 = ms.reader().readShort();
                    short var3 = ms.reader().readShort();
                    short var4 = ms.reader().readShort();
                    short var5 = ms.reader().readShort();
                    if (var2 == -1 && var3 == -1 && var4 == 0 && var5 == 0) {
                        MapManager.getInstance().joinZone(_char, mapId, 0, -1);
                    }
                    break;
                case 12:
                    int pr = 1;
                    if(_char.body[10] == null)
                        return;
                    
                    switch (_char.body[10].id) {
                        case 276:
                            pr = 2;
                            break;
                        case 306:
                            pr = 3;
                            break;
                        case 476:
                            pr = 4;
                            break;
                        case 477:
                            pr = 5;
                            break;
                        case 478:
                            pr = 6;
                            break;
                        case 479:
                            pr = 7;
                            break;
                        case 480:
                            pr = 8;
                            break;
                        case 481:
                            pr = 9;
                            break;
                        case 482:
                            pr = 10;
                            break;
                        case 483:
                            pr = 11;
                            break;
                        case 484:
                            pr = 12;
                            break;
                    }
                    Effect effect = new Effect(145, 60000 * pr, 300 * pr);
                    _char.getEm().setEffect(effect);
                    break;
                case 8:
                    _char.petManager(ms);
                    break;
                case 4:
                    _char.uocRongEath(ms);
                    break;
                case 5:
                    _char.uocRongDen(ms);
                    break;
                case 10:
                    _char.uocRongNamec(ms);
                    break;
                case -58:
                    byte type = ms.reader().readByte();
                    service.updateHinhAnh(type);
                    break;
                case -68:
                    _char.learnSkillClan(ms);
                    break;
                case -24: // thay đổi cải trang
                    _char.changeSkin();
                    break;
                case 11:
                    Item cdv = _char.body[10];
                    if(cdv != null) {
                        if(!cdv.isForever()) {
                            _char.getService().serverMessage("Cân đẩu vân vĩnh viễn mới có thể sử dụng chức năng này");
                            return;
                        }
                        ItemOption powerOption = null;
                        if((powerOption = cdv.getOption(413)) == null) {
                            if(_char.zeni < 250) {
                                _char.getService().serverMessage("Thiếu zeni kích hoạt");
                                return;
                            }
                            _char.addZeni(-250,true);
                            String option = cdv.getStrOption();
                            option = "413,0,30000;" + option;
                            cdv.setStrOption(option);
                            _char.setAbility();
                            _char.getService().updateHP();
                            _char.getService().updateMP();
                            _char.getService().showInfo2(_char);
                            _char.getService().updateItemBody(cdv);
                            _char.getService().serverMessage("Kích hoạt thành công");
                        } else {
                            if(powerOption.getParam() < powerOption.getParam2())
                                return;
                            int zeni = 0;
                            switch(cdv.getId()) {
                                case 189:
                                    zeni = 150;
                                    break;
                                case 276:
                                    zeni = 300;
                                    break;
                                case 306:
                                    zeni = 450;
                                    break;
                                case 476:
                                    zeni = 600;
                                    break;
                                case 477:
                                    zeni = 800;
                                    break;
                                case 478:
                                    zeni = 1000;
                                    break;
                                case 479:
                                    zeni = 1200;
                                    break;
                                case 480:
                                    zeni = 1400;
                                    break;
                                case 481:
                                    zeni = 1600;
                                    break;
                                case 482:
                                    zeni = 1800;
                                    break;
                                case 483:
                                    zeni = 2000;
                                    break;
                            }
                            if(zeni <= 0)
                                return;
                            
                            if(_char.zeni < zeni) {
                                _char.getService().serverMessage("Thiếu zeni kích hoạt");
                                return;
                            }
                            _char.addZeni(-zeni,true);
                            int[] idItem = new int[]{189, 276, 306, 476, 477, 478, 479, 480, 481, 482, 483, 484};
                            ItemOption[] options = cdv.getItemOption();
                            for(ItemOption option : options) {
                                if(option.getId() == 413) {
                                    option.setValue(0);
                                    option.e(30000 * (cdv.getUpgrade() + 2));
                                } else {
                                    switch (option.getId()) {
                                        case 0:
                                        case 1:
                                            option.addValue(Util.nextInt(1,300));
                                            break;
                                        case 2:
                                        case 5:
                                        case 167:
                                        case 258:
                                            option.addValue(Util.nextInt(1,30));
                                            break;
                                        case 414:
                                            option.addValue(1);
                                            break;
                                    }
                                }
                            }
                            String optionsStr = Item.creatOption(options);
                            cdv.setStrOption(optionsStr);
                            int newId = idItem[cdv.getUpgrade() + 1];
                            cdv.upgradeItem(cdv.getUpgrade() + (newId == 484 ? 2 : 1));
                            cdv.setId(newId);
                            _char.setAbility();
                            _char.getService().updateHP();
                            _char.getService().updateMP();
                            _char.getService().showInfo2(_char);
                            _char.getService().updateItemBody(cdv);
                            _char.getService().serverMessage("Nâng cấp thành công");
                        }
                    }
                    break;
                case 13: {
                    short varc = ms.reader().readByte();
                    short var0 = ms.reader().readShort();
                    Item itemChangee = varc == 2 ? _char.body[var0].clone() : _char.body2[var0].clone();
                    if(itemChangee != null) {
                        short idChange = ms.reader().readShort();
                        short idSkillCT = ms.reader().readShort();
                        itemChangee.id = idChange;
                        String[] split = Objects.toString(itemChangee.optionGhepCaiTrang, "").split("&");
                        short idOption = 0;
                        String firstt = "";
                        String lastt = "";
                        String optionGhepCaiTrang = "";
                        for(int i = 0; i < split.length; i++) {
                            if (!split[i].isEmpty()) {
                                String[] data = split[i].split("@");
                                if(data.length == 2 && Short.parseShort(data[0]) == idSkillCT) {
                                    firstt = split[i];
                                    String[] data2 = data[1].split(";");
                                    for(int j = 0; j < data2.length; j++) {
                                        String[] d2 = data2[j].split(",");
                                        if(d2.length >= 2 && Short.parseShort(d2[0]) == 327) {
                                            idOption = Short.parseShort(d2[1]);
                                        }
                                    }
                                } else if(Short.parseShort(data[0]) == idChange) {
                                    lastt = split[i];
                                } else {
                                    optionGhepCaiTrang += (split[i] + "&");
                                }
                            }
                        }

                        optionGhepCaiTrang = firstt + "&" + optionGhepCaiTrang + lastt;
                        if (optionGhepCaiTrang.startsWith("&")) {
                            optionGhepCaiTrang = optionGhepCaiTrang.substring(1);
                        }
                        if (optionGhepCaiTrang.endsWith("&")) {
                            optionGhepCaiTrang = optionGhepCaiTrang.substring(0, optionGhepCaiTrang.length() - 1);
                        }
                        if(idOption != 0) {
                            itemChangee.setOptionGhepCaiTrang(optionGhepCaiTrang);
                            itemChangee.replaceOptionById(new ItemOption(327,idOption));
                        }
                    }
                    Message message = new Message((byte) -123);
                    message.writer().writeByte(-28);
                    itemChangee.write(message);
                    _char.getService().sendMessage(message);
                }
                break;
                case -25: // hợp nhất trang bị
                    _char.hopNhatItem(ms);
                    break;
                case 14:
                    if(_char == null)
                        return;
                    int timeCurrent = (int) (System.currentTimeMillis() / 1000);
                    if(_char.timeWaitChangeSkin > timeCurrent) {
                        _char.getService().serverMessage("Không thể thao tác");
                        return;
                    }
                    byte typeItem = ms.reader().readByte();
                    short indexItem = ms.reader().readShort();
                    Item itemChange = typeItem == 2 ? _char.body[indexItem].clone() : _char.body2[indexItem].clone();
                    if(itemChange != null) {
                        short idChange = ms.reader().readShort();
                        short idSkillCT = ms.reader().readShort();
                        itemChange.id = idChange;
                        String[] split = Objects.toString(itemChange.optionGhepCaiTrang, "").split("&");
                        short idOption = 0;
                        String firstt = "";
                        String lastt = "";
                        String optionGhepCaiTrang = "";
                        for(int i = 0; i < split.length; i++) {
                            if (!split[i].isEmpty()) {
                                String[] data = split[i].split("@");
                                if(data.length == 2 && Short.parseShort(data[0]) == idSkillCT) {
                                    firstt = split[i];
                                    String[] data2 = data[1].split(";");
                                    for(int j = 0; j < data2.length; j++) {
                                        String[] d2 = data2[j].split(",");
                                        if(d2.length >= 2 && Short.parseShort(d2[0]) == 327) {
                                            idOption = Short.parseShort(d2[1]);
                                        }
                                    }
                                } else if(Short.parseShort(data[0]) == idChange) {
                                    lastt = split[i];
                                } else {
                                    optionGhepCaiTrang += (split[i] + "&");
                                }
                            }
                        }
                        
                        optionGhepCaiTrang = firstt + "&" + optionGhepCaiTrang + lastt;
                        if (optionGhepCaiTrang.startsWith("&")) {
                            optionGhepCaiTrang = optionGhepCaiTrang.substring(1);
                        }
                        if (optionGhepCaiTrang.endsWith("&")) {
                            optionGhepCaiTrang = optionGhepCaiTrang.substring(0, optionGhepCaiTrang.length() - 1);
                        }
                        if(idOption != 0) {
                            itemChange.setOptionGhepCaiTrang(optionGhepCaiTrang);
                            itemChange.replaceOptionById(new ItemOption(327,idOption));
                            if(typeItem == 2) {
                                _char.body[indexItem] = itemChange;
                            } else 
                                _char.body2[indexItem] = itemChange;
                            _char.setAbility();
                            if (itemChange.getTemplate().type == 14) {
                                if (_char.options[327] > 0) {
                                    int point = 1;
//                                    if (_char.options[327] == 93) {
//                                        point = 3;
//                                    }
//                                    if (_char.options[327] == 62) {
//                                        point = 2;
//                                    }
                                    Skill skill = SkillFactory.getInstance().getSkill(_char.options[327], point);
                                    if (skill != null) {
                                        int indexSkillWithCT = _char.findIndexSkillWithCT();
                                        if (indexSkillWithCT != -1) {
                                            _char.skills.remove(indexSkillWithCT);
                                        }
                                        skill.isSkillCaiTrang = true;
                                        skill.timeCoolDown = _char.timeCoolDownSkillCaiTrang;
                                        _char.addSkill(skill);
                                        _char.getService().updateSkillCaiTrang();
                                    }
                                } else {
                                    int indexSkillWithCT = _char.findIndexSkillWithCT();
                                    if (indexSkillWithCT != -1) {
                                        _char.skills.remove(indexSkillWithCT);
                                    }
                                }
                            }
                            _char.timeWaitChangeSkin = timeCurrent + 300;
                            Message message = new Message((byte) -123);
                            message.writer().writeByte(-46);
                            message.writer().writeByte(typeItem);
                            message.writer().writeShort(indexItem);
                            itemChange.write(message);
                            message.writer().writeInt(_char.timeWaitChangeSkin);
                            _char.getService().sendMessage(message);

                            _char.getService().updateHP();
                            _char.getService().updateMP();
                            _char.getService().showInfo2(_char);
                            _char.zone.getService().updateItemBody(_char);
                            _char.getService().updateSkill();
                        }
                    }
                    break;
                default:
                    Log.debug("Command not found(-123): " + command);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void claimReward(Message message) throws IOException {
        byte index = message.reader().readByte();
        PlayerAchievementList achievementList = _char.achievements;
        achievementList.claimReward(index);
    }


    @Override
    public void messageNotLogin(Message ms) {

    }
}
