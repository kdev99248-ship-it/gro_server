package com.vdtt.network;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

public class Message {
    @Setter
    @Getter
    private byte command;
    private ByteArrayOutputStream os;
    private DataOutputStream dos;
    private ByteArrayInputStream is;
    public DataInputStream dis;
    public boolean inflate;
    public static String m = " 0123456789+-*='\"\\/_?.,ˋˊ~ˀ:;|<>[]{}!@#$%^&()aáàảãạâấầẩẫậăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòỏõọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵzwAÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶBCDĐEÉÈẺẼẸÊẾỀỂỄỆFGHIÍÌỈĨỊJKLMNOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢPQRSTUÚÙỦŨỤƯỨỪỬỮỰVXYÝỲỶỸỴZW";

    public Message(byte var1) {
        this.command = var1;
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.os);
    }

    public Message(byte var1, byte[] var2) {
        this.command = var1;
        is = new ByteArrayInputStream(var2);
        dis = new DataInputStream(is);
    }

    public byte[] getData() {
        return os.toByteArray();
    }

    public DataInputStream reader() {
        return dis;

    }

    public DataOutputStream writer() {
        return dos;
    }

    public void cleanup() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
        } catch (IOException e) {
        }
    }

    public String readUTF() throws java.io.IOException {
        short var1;
        if ((var1 = (short) this.dis.readUnsignedByte()) == 0) {
            return this.dis.readUTF();
        } else {
            String var2 = "";

            for (int var3 = 0; var3 < var1; ++var3) {
                var2 = var2 + m.charAt(this.dis.readUnsignedByte());
            }

            return var2;
        }
    }

    public void writeUTF(String var1) throws java.io.IOException {
        if (!var1.isEmpty() && var1.length() <= 255) {
            this.dos.writeByte(var1.length());

            for (int var3 = 0; var3 < var1.length(); ++var3) {
                int var2;
                if ((var2 = m.indexOf(var1.charAt(var3))) < 0) {
                    var2 = 0;
                }

                this.dos.writeByte(var2);
            }

        } else {
            this.dos.writeByte(0);
            this.dos.writeUTF(var1);
        }
    }
}
