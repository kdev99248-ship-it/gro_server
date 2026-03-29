package com.vdtt.model;

import com.vdtt.network.Message;
import com.vdtt.server.GlobalService;
import com.vdtt.util.ProfanityFilter;
import com.vdtt.util.Util;

import java.io.IOException;

public class ChatGlobal {

    private static ProfanityFilter profanityFilter;

    public static ProfanityFilter getFilter() {
        if (profanityFilter == null) {
            synchronized (ProfanityFilter.class) {
                if (profanityFilter == null) {
                    profanityFilter = new ProfanityFilter();
                    profanityFilter.addBadWord("lồn");
                    profanityFilter.addBadWord("buồi");
                    profanityFilter.addBadWord("địt");
                    profanityFilter.addBadWord("súc vật");
                    profanityFilter.addBadWord("lon");
                    profanityFilter.addBadWord("buoi");
                    profanityFilter.addBadWord("dit");
                    profanityFilter.addBadWord("suc vat");
                    profanityFilter.addBadWord("mẹ mày");
                    profanityFilter.addBadWord("me may");
                    profanityFilter.addBadWord("đm");
                    profanityFilter.addBadWord("dm");
                    profanityFilter.addBadWord(".com");
                    profanityFilter.addBadWord(".tk");
                    profanityFilter.addBadWord(".ga");
                    profanityFilter.addBadWord(".cf");
                    profanityFilter.addBadWord(".net");
                    profanityFilter.addBadWord(".xyz");
                    profanityFilter.addBadWord(".mobi");
                    profanityFilter.addBadWord(".ml");
                    profanityFilter.addBadWord(".onine");
                    profanityFilter.addBadWord("như cc");
                    profanityFilter.addBadWord("nhu cc");
                    profanityFilter.addBadWord("game rác");
                    profanityFilter.addBadWord("game rac");
                }
            }
        }
        return profanityFilter;
    }

    private Char player;
    private long lastTimeChat;
    private long delay;
    private String text;
    private byte type;
    private boolean isLoa;

    public ChatGlobal(Char player) {
        this.player = player;
        this.delay = 30000;
    }

    public void read(Message ms) {

        try {
            isLoa = ms.reader().readBoolean();
            text = ms.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void wordFilter() {
        text = getFilter().filterBadWords(text);
    }

    public void send() {
        if (player.user.activated == 1) {
            player.serverDialog("Tính năng này cần mtv ở bulma để sử dụng");
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastTimeChat < delay) {
            long mili = (int) (now - lastTimeChat);
            player.getService().serverMessage(String.format("Chỉ có thể chat sau %s giây.",
                    Util.timeAgo((int) ((delay - mili) / 1000))));
            return;
        }
        lastTimeChat = now;
        if (isLoa) {
            if(!player.isActiveAction()) {
                player.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            if (player.zeni < 5) {
                player.getService().serverMessage("Bạn không đủ tiền để sử dụng loa");
                return;
            }
            player.addZeni(-5, true);
            GlobalService.getInstance().chat(player.name, text, (byte) 1);
            player.achievements.increaseAchievementCount(8, 1);
        } else {
            GlobalService.getInstance().chat(player.name, text, (byte) 0);
            player.achievements.increaseAchievementCount(9, 1);
        }
    }
}
