package com.vdtt.model;

import com.vdtt.db.DbManager;
import com.vdtt.item.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Trade {
    public Trader[] traders = new Trader[2];
    public boolean isFinish = false;
    public void openUITrade() {
        try {
            String name_new_0 = traders[0].player.name;
            String name_new_1 = traders[1].player.name;
            traders[0].player.getService().openUITrade(name_new_1);
            traders[1].player.getService().openUITrade(name_new_0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clearTrade() {
        try {
            Char _char1 = traders[0].player;
            Char _char2 = traders[1].player;
            _char1.cleanTrade();
            _char2.cleanTrade();
            isFinish = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void tradeItemLock(Trader trader) {
        try {
            (trader == this.traders[0] ? this.traders[1] : this.traders[0]).player.getService().tradeItemLock(trader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            if (!this.isFinish && traders[0].accept && traders[1].accept) {
                Char _char1 = traders[0].player;
                Char _char2 = traders[1].player;
                boolean isError = false;
                String error1 = "";
                String error2 = "";
                try {
                    int num = _char1.getSlotNull();
                    if (traders[1].itemTradeOrder.size() > num) {
                        isError = true;
                        error1 = "Hành trang của bạn không đủ chỗ trống";
                        error2 = "Hành trang của đối phương không đủ chỗ trống";
                        return;
                    }
                    num = _char2.getSlotNull();
                    if (traders[0].itemTradeOrder.size() > num) {
                        isError = true;
                        error2 = "Hành trang của bạn không đủ chỗ trống";
                        error1 = "Hành trang của đối phương không đủ chỗ trống";
                        return;
                    }
                    if (traders[0].coinTradeOrder > _char1.ballZ) {
                        isError = true;
                        error1 = "Bạn không đủ xu để giao dịch";
                        error2 = "Đối phương không đủ xu để giao dịch";
                        return;
                    }
                    if (traders[1].coinTradeOrder > _char2.ballZ) {
                        isError = true;
                        error2 = "Bạn không đủ ballz để giao dịch";
                        error1 = "Đối phương không đủ ballz để giao dịch";
                        return;
                    }
                    if (traders[0].coinTradeOrder > 500000000) {
                        isError = true;
                        error1 = "Số ballz tối đa có thể giao dịch là 500.000.000 ballz.";
                        error2 = "Đối phương đã giao dịch quá giới hạn 500.000.000 ballz.";
                        return;
                    }
                    if (traders[1].coinTradeOrder > 500000000) {
                        isError = true;
                        error2 = "Số ballz tối đa có thể giao dịch là 500.000.000 ballz.";
                        error1 = "Đối phương đã giao dịch quá giới hạn 500.000.000 ballz.";
                        return;
                    }
               
                    int numberItem = traders[1].itemTradeOrder.size();
                    if (numberItem > 0) {
                        for (Item item : traders[1].itemTradeOrder) {
                            int id = item.id;
                            int index = item.index;
                            int quantity = item.getQuantity();
                            if (_char2.bag[index] == null || _char2.bag[index].id != id
                                    || _char2.bag[index].getQuantity() != quantity || _char2.bag[index].isLock) {
                                isError = true;
                                error2 = "Vật phẩm ở ô " + (index + 1) + " không hợp lệ.";
                                error1 = "Đối phương giao dịch vật phẩm không hợp lệ.";
                                return;
                            }
                        }
                    }
                    numberItem = traders[0].itemTradeOrder.size();
                    if (numberItem > 0) {
                        for (Item item : traders[0].itemTradeOrder) {
                            int id = item.id;
                            int index = item.index;
                            int quantity = item.getQuantity();
                            if (_char1.bag[index] == null || _char1.bag[index].id != id
                                    || _char1.bag[index].getQuantity() != quantity || _char1.bag[index].isLock) {
                                isError = true;
                                error1 = "Vật phẩm ở ô " + (index + 1) + " không hợp lệ.";
                                error2 = "Đối phương giao dịch vật phẩm không hợp lệ.";
                                return;
                            }
                        }
                    }
                    StringBuilder item_2 = new StringBuilder();
                    numberItem = traders[1].itemTradeOrder.size();
                    if (numberItem > 0) {
                        for (Item item : traders[1].itemTradeOrder) {
                            int index = item.index;
                            int quantity = item.getQuantity();
                            if (_char2.bag[index] != null && _char2.bag[index].has(quantity)) {
                                _char1.addItemToBag(item);
                                _char2.bag[index] = null;
                                item_2.append(item.getTemplate().name).append("với số lượng ").append(quantity).append("\n");
                            }
                        }
                    }
                    StringBuilder item_1 = new StringBuilder();
                    numberItem = traders[0].itemTradeOrder.size();
                    if (numberItem > 0) {
                        for (Item item : traders[0].itemTradeOrder) {
                            int index = item.index;
                            int quantity = item.getQuantity();
                            if (_char1.bag[index] != null && _char1.bag[index].has(quantity)) {
                                _char2.addItemToBag(item);
                                _char1.bag[index] = null;
                                item_1.append(item.getTemplate().name).append("với số lượng ").append(quantity).append("\n");
                            }
                        }
                    }
                    _char1.ballZ += traders[1].coinTradeOrder - traders[0].coinTradeOrder;
                    _char2.ballZ += traders[0].coinTradeOrder - traders[1].coinTradeOrder;
                    insertLogToDataBase(_char1.name, _char2.name, item_1.toString(), item_2.toString(), traders[0].coinTradeOrder, traders[1].coinTradeOrder);
                    long now = System.currentTimeMillis();
                    _char1.getService().tradeOk();
                    _char2.getService().tradeOk();
                 
                    this.isFinish = true;
                } finally {
                    if (isError) {
                        closeUITrade();
                        _char1.serverMessage(error1);
                        _char2.serverMessage(error2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeUITrade() {
        try {
            Char _char1 = traders[0].player;
            Char _char2 = traders[1].player;
            _char1.getService().tradeCancel();
            _char2.getService().tradeCancel();
            _char1.cleanTrade();
            _char2.cleanTrade();
            isFinish = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertLogToDataBase(String name1, String name2, String item1, String item2, int coin1, int coin2) {
            try (Connection conn = DbManager.getInstance().getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO historytrans (player1, player2, itemtrans1, itemtrans2, ballzs1, ballzs2) VALUES (?, ?, ?, ?, ?, ?)");
            try {
                stmt.setString(1, name1);
                stmt.setString(2, name2);
                stmt.setString(3, item1);
                stmt.setString(4, item2);
                stmt.setInt(5, coin1);
                stmt.setInt(6, coin2);
                stmt.executeUpdate();
            } finally {
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
