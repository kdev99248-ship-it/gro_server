package com.vdtt.handler;

import com.vdtt.model.Char;
import com.vdtt.network.Message;

import java.io.IOException;

public class PvPHandler {
    public static void TyVo(Char _myChar, Message msg) throws IOException {
        String Name = msg.readUTF();
        Char _cTyVo = _myChar.zone.findCharName(Name);
        if (_cTyVo != null)
        {
            _myChar.getService().serverDialog("Đã gửi lời mời tỷ võ đến " + Name);
            _cTyVo.getService().sendMessage(MsgMoiTyVo(_myChar.name));
        }
    }

    private static Message MsgMoiTyVo(String Name) throws IOException {
        Message m = new Message((byte) 32);
        m.writeUTF(Name);
        return m;
    }

    private static Message MsgStartTyVo(int IdChar, int IdTyVo) throws IOException {
        Message m = new Message((byte) 31);
        m.writer().writeInt(IdChar);
        m.writer().writeByte(1);
        m.writer().writeInt(IdTyVo);
        m.writer().writeByte(1);
        return m;
    }

    public static Message MsgCloseTyVo(byte Type, int Id1, int Id2) throws IOException {
        Message m = new Message((byte)29);
        m.writer().writeByte(Type);
        m.writer().writeInt(Id1);
        m.writer().writeInt(Id2);
        return m;
    }

    public static void StartTyVo(Char _myChar, Message msg) throws IOException {
        if(_myChar.isDead) {
            _myChar.getService().serverMessage("Không thể thực hiện.");
            return;
        }
        String Name = msg.readUTF();
        Char _cTyVo = _myChar.zone.findCharName(Name);
        if (_cTyVo != null)
        {
            if(_cTyVo.isDead) {
                _myChar.getService().serverMessage("Không thể thực hiện.");
                return;
            }
            for (Char c : _myChar.zone.getChars()) {
                if(c != null && c.user != null && c.user.session.isConnected()) {
                    c.getService().sendMessage(MsgStartTyVo(_myChar.id, _cTyVo.id));
                }
            }

            _cTyVo.IsTyVo = true;
            _cTyVo.IdTyVo = _myChar.id;
            _myChar.IsTyVo = true;
            _myChar.IdTyVo = _cTyVo.id;
        }
    }

    public static void TuChoiTyVo(Char _myChar, Message msg) throws IOException {
        String Name = msg.readUTF();
        Char _cTyVo = _myChar.zone.findCharName(Name);
        if (_cTyVo != null)
        {
            _cTyVo.getService().serverDialog(_myChar.name + " đã từ chối tỷ võ");
        }
    }

    public static void CuuSat(Char _myChar, Message msg) throws IOException {
        if(_myChar.isDead) {
            _myChar.getService().serverMessage("Không thể thực hiện.");
            return;
        }
        String Name = msg.readUTF();
        Char _cCuuSat = _myChar.zone.findCharName(Name);
        if (_cCuuSat != null)
        {
            if(_cCuuSat.isDead) {
                _myChar.getService().serverMessage("Không thể thực hiện.");
                return;
            }

            _cCuuSat.getService().sendMessage(MsgCuuSat(_myChar.id, _cCuuSat.id));
            _cCuuSat.IsAnCuuSat = true;
            _cCuuSat.IdCharMoiCuuSat = _myChar.id;
            _myChar.getService().sendMessage(MsgCuuSat(_myChar.id, _cCuuSat.id));
            _myChar.IsCuuSat = true;
            _myChar.IdCuuSat = _cCuuSat.id;
        }
    }

    private static Message MsgCuuSat(int IdMyChar, int IdCuuSat) throws IOException {
        Message m = new Message((byte) 19);
        m.writer().writeInt(IdMyChar);
        m.writer().writeInt(IdCuuSat);
        return m;
    }

    public static Message MsgCloseCuuSat(int Id, boolean IsCloseCuuSat) throws IOException {
        Message m = new Message((byte) 18);
        m.writer().writeInt(Id);
        m.writer().writeBoolean(IsCloseCuuSat);
        return m;
    }
}
