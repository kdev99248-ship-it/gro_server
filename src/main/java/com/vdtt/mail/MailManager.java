package com.vdtt.mail;

import com.vdtt.item.Item;
import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.util.Util;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailManager {
    private List<Mail> mails;
    public Char player;

    public MailManager(Char player) {
        this.mails = new ArrayList<>();
        this.player = player;
    }

    public void add(Mail mail) {
        synchronized (mails) {
            mails.add(mail);
        }
    }

    public int size() {
        return mails.size();
    }

    public void remove(Mail mail) {
        synchronized (mails) {
            mails.remove(mail);
        }
    }

    public Mail findByID(int id) {
        synchronized (mails) {
            for (Mail mail : mails) {
                if (mail.id == id) {
                    return mail;
                }
            }
        }
        return null;
    }

    public void write(Message m) throws IOException {
        synchronized (mails) {
            m.writer().writeShort(mails.size());
            for (Mail mail : mails) {
                mail.write(m);
            }
        }
    }

    public int getID() {
        if (mails.isEmpty()) {
            return 0;
        }
        return mails.get(mails.size() - 1).id + 1;
    }

    public void addMail(Mail mail) {
        if (mails.size() >= 120) {
            return;
        }
        add(mail);
        player.getService().updateMail();
    }

    public void receiveMail(int id) {
        synchronized (mails) {
            for (Mail mail : mails) {
                if (mail.id == id && !mail.isRecived) {
                    if (player.getSlotNull() == 0) {
                        player.getService().serverDialog("Không đủ chỗ trong túi");
                        return;
                    }
                    if (mail.item != null) {
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
                        String strDate = formatter.format(date);
                        String str = mail.item.getTemplate().name + " số lượng: " + mail.item.getQuantity();
                        Util.writing("loggame/nhanthu/", "Người gửi:" + mail.getSender() + " người nhận " + player.name + " " + mail.getContent() + " Gửi đi: " + mail.ballz + " Ballz và Item :" + str + " thời gian: " + strDate + "\n");
                    }
                    mail.isRecived = true;
                    player.addZeni(mail.zeni, true);
                    player.addCoin(mail.coin, true);
                    player.addBallz(mail.ballz, true);
                    mail.zeni = 0;
                    mail.coin = 0;
                    mail.ballz = 0;
                    if (mail.item != null) {
                        Item itemAdd = mail.item.clone();
                        if (itemAdd.id == 163) {
                            player.addCoin(itemAdd.getQuantity(), true);
                        } else {
                            if (itemAdd.strOption != null && !itemAdd.strOption.isEmpty()) {
                                itemAdd.createOption();
                            }
                            if (itemAdd.getQuantity() <= 0) {
                                itemAdd.setQuantity(1);
                            }
                            player.addItemToBag(itemAdd);
                            player.getService().addItem(itemAdd);
                        }
                        mail.item = null;
                    }
                }
            }
            player.getService().updateMail();
        }
    }

    public void removeMail(int id) {
        Mail mail = findByID(id);
        if (mail != null) {
            if (!mail.isRecived) {
                player.getService().serverDialog("Không thể xóa thư chưa nhận quà");
                return;
            }
            remove(mail);
            player.getService().updateMail();
        }
    }

    public void receiveAll() {
        synchronized (mails) {
            for (Mail mail : mails) {
                if (!mail.isRecived) {
                    if (player.getSlotNull() == 0) {
                        player.getService().serverDialog("Không đủ chỗ trong túi");
                        return;
                    }
                    if (mail.item != null) {
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
                        String strDate = formatter.format(date);
                        String str = mail.item.getTemplate().name + " số lượng: " + mail.item.getQuantity();
                        Util.writing("loggame/nhanthu/", "Người gửi:" + mail.getSender() + " người nhận " + player.name + " " + mail.getContent() + " Gửi đi: " + mail.ballz + " Ballz và Item :" + str + " thời gian: " + strDate + "\n");
                    }
                    mail.isRecived = true;
                    player.addZeni(mail.zeni, true);
                    player.addCoin(mail.coin, true);
                    player.addBallz(mail.ballz, true);
                    mail.zeni = 0;
                    mail.coin = 0;
                    mail.ballz = 0;
                    if (mail.item != null) {
                        Item itemAdd = mail.item.clone();
                        if (itemAdd.id == 163) {
                            player.addCoin(itemAdd.getQuantity(), true);
                        } else {
                            if (itemAdd.strOption != null && !itemAdd.strOption.isEmpty()) {
                                itemAdd.createOption();
                            }
                            if (itemAdd.getQuantity() <= 0) {
                                itemAdd.setQuantity(1);
                            }
                            player.addItemToBag(itemAdd);
                            player.getService().addItem(itemAdd);
                        }
                        mail.item = null;
                    }
                }
            }
            player.getService().updateMail();
        }
    }

    public JSONArray toJson() {
        JSONArray array = new JSONArray();
        synchronized (mails) {
            for (Mail mail : mails) {
                array.add(mail.toJson());
            }
        }
        return array;
    }
}
