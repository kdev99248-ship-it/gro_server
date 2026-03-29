package com.vdtt.stall;

import com.vdtt.db.DbManager;
import com.vdtt.item.Item;
import com.vdtt.mail.Mail;
import com.vdtt.model.Char;
import com.vdtt.server.Config;
import com.vdtt.server.ServerManager;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class StallManager implements Runnable {
    public static final byte STATUS_ON_SALE = 0;
    public static final byte TYPE_ALL = 0;
    public static final byte TYPE_ALL_DA = 1;

    public static final byte TYPE_DA_1_2 = 2;
    public static final byte TYPE_DA_3 = 3;
    public static final byte TYPE_DA_4 = 4;
    public static final byte TYPE_DA_5 = 5;
    public static final byte TYPE_DA_6 = 6;
    public static final byte TYPE_DA_7 = 7;
    public static final byte TYPE_DA_8_9_10 = 8;
    public static final byte TYPE_PHA_LE = 9;
    public static final byte TYPE_DA_THAN_LINH = 10;
    public static final byte TYPE_DO_KICH_HOAT = 11;
    public static final byte TYPE_TRANG_BI = 12;
    public static final byte TYPE_TRANG_BI_VIP = 13;
    public static final byte TYPE_THE_DIEM = 14;
    public static final byte TYPE_SU_KIEN = 15;
    public static final byte TYPE_KHAC = 16;

    @Getter
    private static final StallManager instance = new StallManager();

    private List<Stall> stalls;
    private boolean running;
    private long lastUpdate;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();


    public StallManager() {
        this.running = true;
        this.stalls = new ArrayList<>();
        load();
    }

    public void load() {
        if (Config.getInstance().getServerID() == 1) {
            try (Connection conn = DbManager.getInstance().getConnection();
                 PreparedStatement ps = conn
                         .prepareStatement("SELECT * FROM `stall` WHERE `status` = ?");) {
                ps.setInt(1, STATUS_ON_SALE);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String seller = rs.getString("seller");
                    int price = rs.getInt("price");
                    byte status = rs.getByte("status");
                    int time = rs.getInt("time");
                    JSONObject obj = (JSONObject) JSONValue.parse(rs.getString("item"));
                    Stall stall = new Stall();
                    stall.setId(id);
                    stall.setName(seller);
                    stall.setPrice(price);
                    stall.setStatus(status);
                    stall.setTime(time);
                    stall.setItem(new Item(obj));
                    stalls.add(stall);
                }
                Log.info("Stall loaded size [" + stalls.size() + "].");
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (Connection conn = DbManager.getInstance().getConnection();
                 PreparedStatement ps = conn
                         .prepareStatement("SELECT * FROM `stall_sv2` WHERE `status` = ?");) {
                ps.setInt(1, STATUS_ON_SALE);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String seller = rs.getString("seller");
                    int price = rs.getInt("price");
                    byte status = rs.getByte("status");
                    int time = rs.getInt("time");
                    JSONObject obj = (JSONObject) JSONValue.parse(rs.getString("item"));
                    Stall stall = new Stall();
                    stall.setId(id);
                    stall.setName(seller);
                    stall.setPrice(price);
                    stall.setStatus(status);
                    stall.setTime(time);
                    stall.setItem(new Item(obj));
                    stalls.add(stall);
                }
                Log.info("Stall loaded size: " + stalls.size());
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void themItem(Char p, Item item, int price, int time) {
        try {
            Stall stall = new Stall();
            stall.setName(p.name);
            stall.setPrice(price);
            stall.setStatus(STATUS_ON_SALE);
            stall.setTime((int) ((time + System.currentTimeMillis()) / 1000));
            stall.setItem(item);
            insertItemToStall(stall);
            add(stall);
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
            String strDate = formatter.format(date);
            Util.writing("loggame/stall/sell/" + p.name + ".txt", "Gửi bán item " + item.getTemplate().name + " giá " + price + " vào lúc " + strDate + "\n");
        } catch (Exception e) {
            Log.error("Loi them item stall ", e);
        }
    }

    public void add(Stall item) {
        lock.writeLock().lock();
        try {
            stalls.add(item);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(Stall item) {
        lock.writeLock().lock();
        try {
            stalls.remove(item);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void insertItemToStall(Stall item) {
        if (Config.getInstance().getServerID() == 1) {
            item.setId(DbManager.getInstance().executeInsertAndReturnID("INSERT INTO `stall`(`seller`, `item`, `price`, `status`, `time`) VALUES (?,?,?,?,?)", stmt -> {
                try {
                    stmt.setString(1, item.getName());
                    stmt.setString(2, item.getItem().toJSONObject().toJSONString());
                    stmt.setInt(3, item.getPrice());
                    stmt.setInt(4, item.getStatus());
                    stmt.setInt(5, item.getTime());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }));
        } else {
            item.setId(DbManager.getInstance().executeInsertAndReturnID("INSERT INTO `stall_sv2`(`seller`, `item`, `price`, `status`, `time`) VALUES (?,?,?,?,?)", stmt -> {
                try {
                    stmt.setString(1, item.getName());
                    stmt.setString(2, item.getItem().toJSONObject().toJSONString());
                    stmt.setInt(3, item.getPrice());
                    stmt.setInt(4, item.getStatus());
                    stmt.setInt(5, item.getTime());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public void deleteItemFromStall(long id) {
        if (Config.getInstance().getServerID() == 1) {
            try (Connection conn = DbManager.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM `stall` WHERE `id` = ?");) {
                stmt.setLong(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected < 0) {
                    Log.info("No item found with id " + id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (Connection conn = DbManager.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM `stall_sv2` WHERE `id` = ?");) {
                stmt.setLong(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected < 0) {
                    Log.info("No item found with id " + id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void openUI(Char pl, byte type, byte option, short index) {
        List<Stall> stallList;
        lock.readLock().lock();
        try {
            stallList = new ArrayList<>(stalls);
        } finally {
            lock.readLock().unlock();
        }
        switch (type) {
            case TYPE_ALL:
                break;
            case TYPE_ALL_DA:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id < 10)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_1_2:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id < 2)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_3:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id == 2)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_4:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id == 3)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_5:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id == 4)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_6:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id == 5)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_7:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id == 6)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_8_9_10:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id >= 7 && item.getItem().id <= 9)
                        .collect(Collectors.toList());
                break;

            case TYPE_PHA_LE:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id >= 406 && item.getItem().id <= 413)
                        .collect(Collectors.toList());
                break;

            case TYPE_DA_THAN_LINH:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id >= 562 && item.getItem().id <= 566)
                        .collect(Collectors.toList());
                break;

            case TYPE_DO_KICH_HOAT:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().id >= 1053 && item.getItem().id <= 1057)
                        .collect(Collectors.toList());
                break;

            case TYPE_TRANG_BI, TYPE_TRANG_BI_VIP:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().isTypeWeapon() || item.getItem().isTypePet() || item.getItem().isTypeClothe())
                        .collect(Collectors.toList());
                break;

            case TYPE_THE_DIEM:
                break;

            case TYPE_SU_KIEN:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().getTemplate().name.startsWith("Thẻ điểm"))
                        .collect(Collectors.toList());
                break;

            case TYPE_KHAC:
                stallList = stallList.stream()
                        .filter(item -> item.getItem().getTemplate().type == 100 || item.getItem().getTemplate().type == 99)
                        .collect(Collectors.toList());
                break;
        }
        switch (option) {
            case 0:
                // Sắp xếp theo thời gian giảm dần
                stallList.sort((o1, o2) -> Long.compare(o2.getTime(), o1.getTime()));
                break;
            case 1:
                // Sắp xếp theo giá tăng dần
                stallList.sort((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()));
                break;
            case 2:
                // Sắp xếp theo type của item
                stallList.sort(Comparator.comparingInt(o -> o.getItem().getTemplate().getType()));
                break;
            case 3:
                // Sắp xếp theo thời gian tăng dần
                stallList.sort((o1, o2) -> Long.compare(o1.getTime(), o2.getTime()));
                break;
            case 4:
                // Sắp xếp theo tên
                stallList.sort(Comparator.comparing(Stall::getName));
                break;
            case 5:
                // Sắp xếp theo levelNeed của template
                stallList.sort((o1, o2) -> Integer.compare(o1.getItem().getTemplate().getLevelNeed(), o2.getItem().getTemplate().getLevelNeed()));
                break;
        }
        List<List<Stall>> split = splitList(stallList, 30);
        List<Stall> stallList1 = new ArrayList<>();
        if (index < split.size() && index >= 0) {
            stallList1 = split.get(index);
        }
        pl.getService().openUIStall(stallList1, index);

    }

    public static <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> subLists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            subLists.add(new ArrayList<>(list.subList(i, Math.min(list.size(), i + size))));
        }
        return subLists;
    }

    public void openUIMeSell(Char pl) {
        lock.readLock().lock();
        try {
            List<Stall> itemSell = new ArrayList<>();
            for (Stall market : stalls) {
                if (market.getItem().isLock) {
                    String seller = market.getName();
                    Char pl_return = ServerManager.findCharByName(seller);
                    if (pl_return != null) {
                        try {
                            Mail mail = pl_return.createMail("Hệ thống", "Thư từ hệ thống", "Vật phẩm bạn treo bán bị trả lại do là vật phẩm khóa", 0, 0, 0, market.getItem());
                            pl_return.mailManager.addMail(mail);
                            itemSell.remove(market);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            List<Mail> mailList = DbManager.getInstance().getMail(seller);
                            if (mailList.size() >= 120) {
                                continue;
                            }
                            Mail mail = new Mail();
                            if (mailList.isEmpty()) {
                                mail.id = 0;
                            } else {
                                mail.id = mailList.get(mailList.size() - 1).id + 1;
                            }
                            mail.setSender("Hệ thống");
                            mail.setTitle("Thư từ hệ thống");
                            mail.setContent("Vật phẩm bạn treo bán bị trả lại do là vật phẩm khóa");
                            mail.setBallz(0);
                            mail.setCoin(0);
                            mail.setZeni(0);
                            mail.setItem(market.getItem());
                            mail.setTime((int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000));
                            mailList.add(mail);
                            DbManager.getInstance().updateMail(seller, mailList);
                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
                            String strDate = formatter.format(date);
                            Util.writing("loggame/stall/return/" + seller + ".txt", "return item " + market.getItem().getTemplate().name + " lúc: " + strDate + "\n");
                            itemSell.remove(market);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    continue;
                }
                if (market.getName().equals(pl.name)) {
                    itemSell.add(market);
                }
            }
            pl.getService().openUIMeSell(itemSell);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void update() {
        List<Stall> expiredProductList = new ArrayList<>();
        lock.writeLock().lock();
        try {
            stalls.removeIf(t -> {
                if (t.isExpired()) {
                    expiredProductList.add(t);
                    return true;
                }
                return false;
            });
        } finally {
            lock.writeLock().unlock();
        }

        try {
            for (Stall itemMarket : expiredProductList) {
                deleteItemFromStall(itemMarket.getId());
                String seller = itemMarket.getName();
                Char pl = ServerManager.findCharByName(seller);
                if (pl != null) {
                    try {
                        Mail mail = pl.createMail("Hệ thống", "Thư từ hệ thống", "Vật phẩm bạn treo bán đã hết hạn", 0, 0, 0, itemMarket.getItem());
                        pl.mailManager.addMail(mail);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        List<Mail> mailList = DbManager.getInstance().getMail(seller);
                        if (mailList.size() >= 120) {
                            continue;
                        }
                        Mail mail = new Mail();
                        if (mailList.isEmpty()) {
                            mail.id = 0;
                        } else {
                            mail.id = mailList.get(mailList.size() - 1).id + 1;
                        }
                        mail.setSender("Hệ thống");
                        mail.setTitle("Thư từ hệ thống");
                        mail.setContent("Vật phẩm bạn treo bán đã hết hạn");
                        mail.setBallz(0);
                        mail.setCoin(0);
                        mail.setZeni(0);
                        mail.setItem(itemMarket.getItem());
                        mail.setTime((int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000));
                        mailList.add(mail);
                        DbManager.getInstance().updateMail(seller, mailList);
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
                        String strDate = formatter.format(date);
                        Util.writing("loggame/stall/return/" + seller + ".txt", "return item " + itemMarket.getItem().getTemplate().name + " lúc: " + strDate + "\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            Log.error("Loi clear item market het han ", e);
        }
//        long l = System.currentTimeMillis();
//        if (l - lastUpdate > 900000) {
//            lastUpdate = l;
//            save();
//        }
    }

    public Stall find(long id) {
        lock.readLock().lock();
        try {
            for (Stall item : stalls) {
                if (item.getStatus() == StallManager.STATUS_ON_SALE && item.getId() == id) {
                    return item;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public synchronized void buyItem(Char p, long id) {
        try {
            Stall itemMarket = find(id);
            if (itemMarket == null) {
                p.getService().serverMessage("Vật phẩm đã bị mua!");
                return;
            }
            if (p.getSlotNull() == 0) {
                p.serverMessage("Túi đồ của bạn đã đầy!");
                return;
            }
            if (itemMarket.getItem().template.isBlackListItem()) {
                p.serverDialog("Không thể mua vật phẩm này.");
                return;
            }
            int price = itemMarket.getPrice();
            if (p.ballZ < price) {
                p.serverDialog("Bạn không đủ BallZ để mua vật phẩm này!");
                return;
            }
            String seller = itemMarket.getName();
            remove(itemMarket);
            deleteItemFromStall(id);
            p.addBallz(-price, true);
            p.addItemToBag(itemMarket.getItem());
            p.getService().addItem(itemMarket.getItem());
            p.getService().buyStall();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
            String strDate = formatter.format(date);
            Util.writing("loggame/stall/buy/" + p.name + ".txt", "Mua item " + itemMarket.getItem().getTemplate().name + " giá " + price + " từ " + seller + " lúc: " + strDate + "\n");
            Char pl = ServerManager.findCharByName(seller);
            price -= price / 100;
            if (pl != null && !pl.isCleaned) {
                Mail mail = pl.createMail("Hệ thống", "Thư từ hệ thống", "Vật phẩm bạn treo bán đã được mua bởi " + p.name, price, 0, 0, null);
                pl.mailManager.addMail(mail);
            } else {
                List<Mail> mailList = DbManager.getInstance().getMail(seller);
                if (mailList.size() >= 120) {
                    return;
                }
                Mail mail = new Mail();
                if (mailList.isEmpty()) {
                    mail.id = 0;
                } else {
                    mail.id = mailList.get(mailList.size() - 1).id + 1;
                }
                mail.setSender("Hệ thống");
                mail.setTitle("Thư từ hệ thống");
                mail.setContent("Vật phẩm bạn treo bán đã được mua bởi " + p.name);
                mail.setBallz(price);
                mail.setCoin(0);
                mail.setZeni(0);
                mail.setTime((int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000));
                mail.setItem(null);
                mailList.add(mail);
                DbManager.getInstance().updateMail(seller, mailList);
            }
        } catch (Exception e) {
            Log.error("Loi mua item stall ", e);
        }
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (running) {
            long l1 = System.currentTimeMillis();
            update();
            long l2 = System.currentTimeMillis();
            if (l2 - l1 < 1000) {
                try {
                    Thread.sleep(1000 - (l2 - l1));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        this.running = false;
    }

    public boolean isSeller(String name) {
        return stalls.stream().anyMatch(e -> e.getName().equals(name));
    }
}
