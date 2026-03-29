package com.vdtt.map.world.stadiums;

import com.vdtt.data.CMDInputDialog;
import com.vdtt.mixer.Ballz;
import com.vdtt.model.Char;
import com.vdtt.model.InputDialog;
import com.vdtt.network.Message;
import lombok.Getter;

import java.io.IOException;

/**
 * @author BTH Cute phô mai que
 */
public class Stadium {
    @Getter
    private Char player1;
    public Match match;
    @Getter
    private int ballz;
    
    private void datCuoc(Char player) {
        player.getService().openPopupSoLuong((byte) 0);
    }

    public void register(Char player) {
        player.getService().openPopupIdGame((byte) 0);
//        InputDialog inputDialog = new InputDialog(CMDInputDialog.EXECUTE, "Nhập tên nhân vật muốn lôi đài");
//        inputDialog.setRunnable(() -> handlePlayerInvitation(player, inputDialog.getText(), ballz));
//        player.setInput(inputDialog);
//        player.getService().showInputDialog();
    }

    public void handlePlayerInvitation(Char inviter, String inviteeName) {
        Char invitee = inviter.zone.findCharName(inviteeName);
        if (invitee == null) {
            inviter.serverDialog("Người chơi bạn mời hiện không ở gần đây");
            return;
        }
        if (invitee.user.activated == 1) {
            inviter.serverDialog("Đối thủ chưa mở thành viên");
            return;
        }
        if(!inviter.isActiveAction()) {
            inviter.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
            return;
        }
        if(!invitee.isActiveAction()) {
            inviter.serverDialog("Đối thủ chưa mở mã bảo vệ");
            return;
        }
//        if (invitee.ballZ < ballz) {
//            inviter.serverDialog("Đối thủ không đủ tiền cược");
//            return;
//        }

        invitee.stadium = this;
        this.player1 = invitee;
        
        datCuoc(inviter);

//        invitee.serverDialog("Bạn nhận được lời mời tham gia lôi đài mức cược " + ballz + "ballz của " + inviter.name + " để tham gia hãy tới uron");
//        startMatch(inviter, ballz);
    }

    public void startMatch(Char player, int ballz) {
        if(player.ballZ < ballz) {
            player.getService().serverMessage("Bạn không đủ ballz để lôi đài");
            return;
        }
        if(player1.ballZ < ballz) {
            player.getService().serverMessage("Đối thủ không đủ ballz để lôi đài");
            return;
        }
        
        player1.serverDialog("Bạn nhận được lời mời tham gia lôi đài mức cược " + ballz + "ballz của " + player.name + " để tham gia hãy tới uron");
        player.addBallz(-ballz,true);
        match = new Match(this);
        match.createArea();
        match.addMember(player);
        StadiumManager.getInstance().matchs.add(match);

        player.setXY(497, 594);
        match.join(player);
        this.ballz = ballz;
        player.serverDialog("Mời thành công đối thủ, vui lòng chờ đối thủ vào map");
    }

    public void joinMap(Char player) {
        if (isPlayer1(player)) {
            if(ballz == 0) {
                player1.getService().serverMessage("Vui lòng đợi người mời đặt cược");
                return;
            }
            if(player1.ballZ < ballz) {
                player1.getService().serverMessage("Bạn không đủ ballZ để lôi đài");
                return;
            }
            player.addBallz(-ballz,true);
            player.setXY(497, 594);
            match.join(player);
            match.addMember(player);
        } else {
            player.serverDialog("Bạn không có lời mời nào lôi đài");
        }
    }

    private boolean isPlayer1(Char player) {
        return player.name.equals(player1.name);
    }

    public void close() {
        StadiumManager.getInstance().matchs.remove(match);
        clearReferences();
    }

    private void clearReferences() {
        match = null;
    }
}
