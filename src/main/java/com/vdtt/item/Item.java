package com.vdtt.item;

import com.vdtt.data.DataCenter;
import com.vdtt.handler.UpgradeHandler;
import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.util.ParseData;
import com.vdtt.util.Util;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Getter
@Setter
public class Item implements Cloneable {
    public int id;
    public int index;
    protected int quantity;
    public long expire = -1;
    public byte sys;
    public boolean isLock;
    public byte upgrade;
    public String strOption;
    public String nameCreate;
    public ItemTemplate template;
    protected long updatedAt;
    protected long createdAt;
    public String optionGhepCaiTrang = "";
    public boolean isBug;
    public String src = "";

    public Item(int id, String src) {
        this.id = id;
        this.init();
        this.quantity = 1;
        this.upgrade = 0;
        this.sys = 0;
        this.strOption = "";
        this.isLock = false;
        this.expire = -1;
        this.nameCreate = "";
        this.src = src;
    }

    public Item() {
    }

    public Item(int id, boolean locked, String src) {
        this.id = (short) id;
        this.init();
        this.isLock = locked;
        this.src = src;
    }

    public Item(int id, boolean locked, int quantity, String src) {
        this.id = (short) id;
        this.init();
        this.isLock = locked;
        this.quantity = quantity;
        this.src = src;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("index", this.index);
        obj.put("quantity", this.quantity);
        obj.put("expire", this.expire);
        obj.put("sys", this.sys);
        obj.put("isLock", this.isLock);
        obj.put("upgrade", this.upgrade);
        obj.put("strOption", this.strOption);
        obj.put("nameCreate", this.nameCreate);
        obj.put("optionGhepCaiTrang", this.optionGhepCaiTrang);
        return obj;
    }

    public Item(JSONObject obj) {
        ParseData parse = new ParseData(obj);
        this.id = parse.getInt("id");
        this.index = parse.getInt("index");
        this.quantity = parse.getInt("quantity");
        this.expire = parse.getLong("expire");
        this.sys = parse.getByte("sys");
        this.isLock = parse.getBoolean("isLock");
        this.upgrade = parse.getByte("upgrade");
        this.strOption = parse.getString("strOption");
        this.nameCreate = parse.getString("nameCreate");
        if (parse.containsKey("optionGhepCaiTrang")) {
            this.optionGhepCaiTrang = parse.getString("optionGhepCaiTrang");
        }
        init();
        if (id == 177) {
            this.setLock(true);
        }
        if (id >= 1025 && id <= 1036) {
            this.setLock(true);
        }
        if ((id == 833 || (id >= 799 && id <= 805)) && quantity > 1) {
            isBug = true;
        }
        checkBugOption();
    }

    public static void a(Vector var0, ItemOption var1) {
        for (int var2 = 0; var2 < var0.size(); ++var2) {
            if (((ItemOption) var0.get(var2)).option[0] == var1.option[0]) {
                ((ItemOption) var0.get(var2)).d(var1.option[1]);
                return;
            }
        }

        var0.add(var1);
    }

    private void checkBugOption() {
        if (strOption == null) {
            return;
        }
        if (getTemplate().type == 2 || getTemplate().type == 4 || getTemplate().type == 6 || getTemplate().type == 8) {
            int upgrade = this.getUpgrade();
            upgradeItem(0);
            ItemOption[] itemOptions = getItemOption();
            if (itemOptions != null) {
                Vector<ItemOption> newOption = new Vector<>();
                for (ItemOption itemOption : itemOptions) {
                    if (itemOption.getId() == 0) {
                        itemOption.option[0] = 335;
                        itemOption.option[1] = 150;
                    } else if (itemOption.getId() == 1) {
                        itemOption.option[0] = 336;
                        itemOption.option[1] = 150;
                    } else if (itemOption.getId() == 2) {
                        itemOption.option[0] = 341;
                        itemOption.option[1] = 50;
                    } else if (itemOption.getId() == 3) {
                        itemOption.option[0] = 342;
                        itemOption.option[1] = 50;
                    }
                    newOption.add(itemOption);
                }
                strOption = creatOption(newOption);
                upgradeItem(upgrade);
            }
        }
    }

    private void init() {
        this.template = ItemManager.getInstance().getItemTemplate(id);
    }

    public Item clone() {
        try {
            Item var1;
            var1 = (Item) super.clone();
            return var1;
        } catch (Exception var2) {
            return null;
        }
    }

    public void write(Message m) {
        try {
            m.writer().writeShort(id);
            if (this.id >= 0) {
                m.writer().writeBoolean(isLock);
                m.writer().writeLong(expire);
                if (!this.isTrangBi() && !this.ai() && !this.getTemplate().name.startsWith("Danh hiệu")) {
                    m.writer().writeInt(quantity);
                } else {
                    m.writer().writeByte(sys);
                    m.writer().writeByte(upgrade);
                    m.writeUTF(strOption);
                    m.writeUTF(nameCreate);
                }
                if (this.aA()) {
                    m.writeUTF(strOption);
                }
                m.writer().writeShort(index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean aA() {
        return this.getTemplate().type == 99 || this.getTemplate().type == 35 || this.getTemplate().type == 36 || this.getTemplate().type == 37;
    }

    public boolean isTrangBi() {
        if (this.ai()) {
            return false;
        } else {
            for (int var1 = 0; var1 < DataCenter.gI().dataTypeBody.length; ++var1) {
                if (DataCenter.gI().dataTypeBody[var1] == this.getTemplate().type) {
                    return true;
                }
            }

            return false;
        }
    }

    public int getPrice() {
        ItemOption[] var1 = this.options();
        int var2 = 0;
        if (this.isTrangBi() && var1 != null) {
            var2 = 0 + var1.length;
        }

        if (this.getTemplate().type == 34) {
            var2 = 100000;
        }

        if (this.id == 185) {
            var2 = 50000;
        }

        if (this.id == 186) {
            var2 = 100000;
        } else if (this.id == 187) {
            var2 = 200000;
        }
        return var2;
    }

    public boolean ai() {
        return this.id >= 799 && this.id <= 806;
    }

    public boolean hasExpire() {
        return !isForever();
    }

    public boolean isForever() {
        return this.expire == -1;
    }

    public boolean has() {
        return has(1);
    }

    public void add(int amount) {
        this.quantity += amount;
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean has(int amount) {
        return this.quantity >= amount;
    }

    public void reduce(int amount) {
        this.quantity -= amount;
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return this.expire != -1 && this.expire < System.currentTimeMillis();
    }

    public final int M() {
        if (this.id == 190) {
            return this.quantity / 10;
        } else if (this.id == 826) {
            return this.quantity <= 0 ? 0 : this.quantity - 1;
        } else {
            return this.quantity <= 0 ? 1 : this.quantity;
        }
    }

    public void replaceOption(ItemOption op) {
        ItemOption[] itemOptions = getItemOption();
        for (int i = 0; i < itemOptions.length; i++) {
            if (itemOptions[i].getItemOptionTemplate().type == op.getItemOptionTemplate().type) {
                itemOptions[i] = op;
                break;
            }
        }
        strOption = creatOption(itemOptions);
    }

    public void creatItemOptionTrangBi() {
        List<ItemOptionTemplate> list = ItemManager.getInstance().optionTemplates;
        int id_ChiSoGoc = 349;
        List<ItemOptionTemplate> listChiSoGoc = list.stream().filter(item -> item.type == 17).collect(Collectors.toList());
        int[][] arrayChiSo = new int[][]{
                new int[]{50, 400},
                new int[]{50, 400},
                new int[]{5, 100},
                new int[]{5, 100},
                new int[]{5, 100},
                new int[]{5, 100},
                new int[]{5, 50},
                new int[]{5, 100},
                new int[]{5, 10},
                new int[]{5, 10},
                new int[]{20, 50},};
        int id_KichEpSao = 350;
        List<ItemOptionTemplate> list1sao = list.stream().filter(item -> item.type == 19).collect(Collectors.toList());
        List<ItemOptionTemplate> list2sao = list.stream().filter(item -> item.type == 3).collect(Collectors.toList());
        List<ItemOptionTemplate> list3sao = list.stream().filter(item -> item.type == 11).collect(Collectors.toList());
        List<ItemOptionTemplate> list4sao = list.stream().filter(item -> item.type == 4).collect(Collectors.toList());
        List<ItemOptionTemplate> list5sao = list.stream().filter(item -> item.type == 12).collect(Collectors.toList());
        List<ItemOptionTemplate> list6sao = list.stream().filter(item -> item.type == 5).collect(Collectors.toList());
        List<ItemOptionTemplate> list7sao = list.stream().filter(item -> item.type == 6 && item.id > 39).collect(Collectors.toList());
        List<ItemOptionTemplate> list8sao = list.stream().filter(item -> item.type == 7).collect(Collectors.toList());
        List<ItemOption> itemOptions = new ArrayList<>();
        itemOptions.add(new ItemOption(349, 0));
        for (int i = 0; i < 4; i++) {
            int rand = Util.nextInt(0, listChiSoGoc.size() - 1);
            int id = listChiSoGoc.get(rand).id;
            int value = Util.nextInt(arrayChiSo[rand][0], arrayChiSo[rand][1]);
            itemOptions.add(new ItemOption(id, value));
        }
        itemOptions.add(new ItemOption(350, 0));
        int[][][] arrayEpSao_ = new int[][][]{
                new int[][]{
                        new int[]{50, 100},
                        new int[]{10, 25},
                        new int[]{10, 25},},
                new int[][]{
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},},
                new int[][]{
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},},
                new int[][]{
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},},
                new int[][]{
                        new int[]{20, 40},
                        new int[]{20, 40},
                        new int[]{20, 40},
                        new int[]{20, 40}
                },
                new int[][]{
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10}
                }, new int[][]{
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100}
        },
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{}

        };
        List[] listArray = new List[]{
                list1sao,
                list2sao,
                list3sao,
                list4sao,
                list5sao,
                list6sao,
                list7sao,};
        ItemOptionTemplate[] arrayEpSao = new ItemOptionTemplate[]{
                list1sao.get(Util.nextInt(0, list1sao.size() - 1)),
                list2sao.get(Util.nextInt(0, list2sao.size() - 1)),
                list3sao.get(Util.nextInt(0, list3sao.size() - 1)),
                list4sao.get(Util.nextInt(0, list4sao.size() - 1)),
                list5sao.get(Util.nextInt(0, list5sao.size() - 1)),
                list6sao.get(Util.nextInt(0, list6sao.size() - 1)),
                list7sao.get(Util.nextInt(0, list7sao.size() - 1))
        };
        for (int i = 0; i < template.levelNeed; i++) {
            int[] array = arrayEpSao_[i][listArray[i].indexOf(arrayEpSao[i])];
            itemOptions.add(new ItemOption(arrayEpSao[i].id, Util.nextInt(array[0], array[1])));
        }
        this.strOption = creatOption(itemOptions.toArray(new ItemOption[0]));
    }

    public void createOptionRandom(int num, int num2) {
        List<ItemOptionTemplate> list = ItemManager.getInstance().optionTemplates;
        List<ItemOptionTemplate> listChiSoGoc = list.stream().filter(item -> item.type == 17).collect(Collectors.toList());
        int[][] arrayChiSo = new int[][]{
                new int[]{50, 400},
                new int[]{50, 400},
                new int[]{5, 100},
                new int[]{5, 100},
                new int[]{5, 100},
                new int[]{5, 100},
                new int[]{5, 50},
                new int[]{5, 100},
                new int[]{5, 10},
                new int[]{5, 10},
                new int[]{20, 50},};
        List<ItemOptionTemplate> list1sao = list.stream().filter(item -> item.type == 19).collect(Collectors.toList());
        List<ItemOptionTemplate> list2sao = list.stream().filter(item -> item.type == 3).collect(Collectors.toList());
        List<ItemOptionTemplate> list3sao = list.stream().filter(item -> item.type == 11).collect(Collectors.toList());
        List<ItemOptionTemplate> list4sao = list.stream().filter(item -> item.type == 4).collect(Collectors.toList());
        List<ItemOptionTemplate> list5sao = list.stream().filter(item -> item.type == 12).collect(Collectors.toList());
        List<ItemOptionTemplate> list6sao = list.stream().filter(item -> item.type == 5).collect(Collectors.toList());
        List<ItemOptionTemplate> list7sao = list.stream().filter(item -> item.type == 6 && item.id > 39).collect(Collectors.toList());
        List<ItemOptionTemplate> list8sao = list.stream().filter(item -> item.type == 7).collect(Collectors.toList());
        List<ItemOption> itemOptions = new ArrayList<>();
        itemOptions.add(new ItemOption(349, 0));
        for (int i = 0; i < num; i++) {
            int rand = Util.nextInt(0, listChiSoGoc.size() - 1);
            int id = listChiSoGoc.get(rand).id;
            int value = Util.nextInt(arrayChiSo[rand][0], arrayChiSo[rand][1]);
            itemOptions.add(new ItemOption(id, value));
        }
        itemOptions.add(new ItemOption(350, 0));
        int[][][] arrayEpSao_ = new int[][][]{
                new int[][]{
                        new int[]{50, 100},
                        new int[]{10, 25},
                        new int[]{10, 25},
                        new int[]{10, 25},},
                new int[][]{
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},
                        new int[]{10, 50},},
                new int[][]{
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},},
                new int[][]{
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},
                        new int[]{50, 200},},
                new int[][]{
                        new int[]{20, 40},
                        new int[]{20, 40},
                        new int[]{20, 40},
                        new int[]{20, 40}
                },
                new int[][]{
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10},
                        new int[]{5, 10}
                }, new int[][]{
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100},
                new int[]{50, 100}
        },
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{},
                new int[][]{}

        };
        List[] listArray = new List[]{
                list1sao,
                list2sao,
                list3sao,
                list4sao,
                list5sao,
                list6sao,
                list7sao,};
        ItemOptionTemplate[] arrayEpSao = new ItemOptionTemplate[]{
                list1sao.get(Util.nextInt(0, list1sao.size() - 1)),
                list2sao.get(Util.nextInt(0, list2sao.size() - 1)),
                list3sao.get(Util.nextInt(0, list3sao.size() - 1)),
                list4sao.get(Util.nextInt(0, list4sao.size() - 1)),
                list5sao.get(Util.nextInt(0, list5sao.size() - 1)),
                list6sao.get(Util.nextInt(0, list6sao.size() - 1)),
                list7sao.get(Util.nextInt(0, list7sao.size() - 1))
        };
        for (int i = 0; i < num2; i++) {
            int[] array = arrayEpSao_[i][listArray[i].indexOf(arrayEpSao[i])];
            itemOptions.add(new ItemOption(arrayEpSao[i].id, Util.nextInt(array[0], array[1])));
        }
        this.strOption = creatOption(itemOptions.toArray(new ItemOption[0]));
    }

    public final boolean isTauBay() {
        return this.id == 168 || d(this.id);
    }
//

    public static boolean d(int var0) {
        if (var0 == 569 || var0 >= 716 && var0 <= 720) {
            return true;
        } else {
            return false;
        }
    }

    public void createOption() {
        // Lấy các ItemOption từ phương thức getItemOption()
        ItemOption[] itemOptions = getItemOption();

        // Kiểm tra null để đảm bảo itemOptions không phải là null
        if (itemOptions == null) {
            // Xử lý trường hợp itemOptions là null, có thể là log hoặc ném ngoại lệ
            return;
        }

        // Tạo một danh sách để lưu trữ các ItemOption đã được xử lý
        List<ItemOption> list = new ArrayList<>();

        // Duyệt qua từng ItemOption trong mảng
        for (ItemOption op : itemOptions) {
            // Nếu chiều dài của mảng option lớn hơn 2
            if (op.option.length > 2) {
                // Lấy giá trị min và max từ mảng option
                int min = op.option[1];
                int max = op.option[2];

                // Tạo một ItemOption mới với giá trị ngẫu nhiên giữa min và max
                ItemOption _op = new ItemOption(op.option[0], Util.nextInt(min, max));

                // Thêm ItemOption mới vào danh sách
                list.add(_op);
            } else {
                // Nếu không, thêm ItemOption gốc vào danh sách
                list.add(op);
            }
        }

        // Chuyển đổi danh sách thành mảng và gán cho strOption
        this.strOption = creatOption(list.toArray(new ItemOption[0]));
    }

    public void upgradeItem(int upgrade) {
        ItemOption[] options = this.options();
        boolean isGodEquipment = UpgradeHandler.isGodEquipment(this);
        if (options != null) {
            if (upgrade >= this.upgrade) {
                for (int i = this.upgrade + 1; i <= upgrade; ++i) {
                    for (int j = 0; j < options.length; ++j) {
                        if (j < 5 && isGodEquipment) {
                            continue;
                        }
                        if (options[j].getItemOptionTemplate().type != 8) {
                            int[] increases = options[j].getItemOptionTemplate().getOption();
                            if (i <= increases.length) {
                                options[j].addValue(increases[i - 1]);
                            }
                        }
                    }
                }
            } else {
                for (int i = this.upgrade; i > upgrade; --i) {
                    for (int j = 0; j < options.length; ++j) {
                        if (j < 5 && isGodEquipment) {
                            continue;
                        }
                        if (options[j].getItemOptionTemplate().type != 8) {
                            int[] increase = options[j].getItemOptionTemplate().getOption();
                            if (i <= increase.length) {
                                options[j].addValue(-increase[i - 1]);
                            }
                        }
                    }
                }
            }
        }

        this.upgrade = (byte) upgrade;
        this.strOption = creatOption(options);
    }

    public final ItemOption[] options() {
        if (this.strOption != null && !this.strOption.isEmpty()) {
            String[] data = Util.a(this.strOption, ";");
            ItemOption[] options = new ItemOption[data.length];
            for (int i = 0; i < data.length; ++i) {
                options[i] = new ItemOption(data[i]);
            }

            return options;
        } else {
            return null;
        }
    }

    public static String creatOption(ItemOption[] itemOptions) {
        String string = "";
        if (itemOptions != null) {
            for (int i = 0; i < itemOptions.length; ++i) {
                string = string + itemOptions[i].g();
                if (i < itemOptions.length - 1) {
                    string = string + ";";
                }
            }
        }

        return string;
    }

    public static String creatOption(Vector<ItemOption> itemOptions) {
        ItemOption[] itemOptions1 = new ItemOption[itemOptions.size()];

        for (int i = 0; i < itemOptions1.length; ++i) {
            itemOptions1[i] = itemOptions.get(i);
        }

        return creatOption(itemOptions1);
    }

    public final ItemOption a(Vector<ItemOption> var1, int var2) {
        var2 = ItemOption.f(var2);
        ItemOption[] var3 = this.options();
        ItemOption var4 = null;
        if (var3 != null) {
            for (ItemOption itemOption : var3) {
                if (itemOption.getItemOptionTemplate().type == 8) {
                    var1.add(itemOption);
                    if (itemOption.option[0] == var2) {
                        var4 = itemOption;
                    }
                }
            }
        }

        return var4;
    }

    public final boolean u() {
        ItemOption[] var1;
        if (this.getTemplate().levelNeed >= 50 && (var1 = this.options()) != null) {
            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2].option[0] == 159 || var1[var2].option[0] == 163 || var1[var2].option[0] == 164 || var1[var2].option[0] == 165) {
                    return true;
                }
            }
        }

        return false;
    }

    public final boolean U() {
        ItemOption[] var1;
        if ((var1 = this.options()) != null) {
            for (ItemOption itemOption : var1) {
                if (itemOption.option[0] == 165 || itemOption.option[0] == 164 || itemOption.option[0] == 163 || itemOption.option[0] == 159) {
                    return true;
                }
            }
        }

        return false;
    }

    public final int[] a(int var1, int var2) {
        ItemOption[] var3;
        if ((var3 = this.options()) == null) {
            return null;
        } else {
            int var4 = ItemOption.f(var2);
            int[] var5 = null;
            boolean var6 = this.U();

            for (int var7 = 0; var7 < var3.length; ++var7) {
                if (var3[var7].getItemOptionTemplate().type == 8 && var3[var7].option[0] == var4) {
                    int var8 = var3[var7].i();
                    (var5 = new int[4])[0] = var2;
                    var5[1] = var8;
                    var5[2] = var8;
                    int[] var9 = var3[var7].getItemOptionTemplate().getOption();

                    for (int var10 = var8 + 1; var10 <= var9.length; ++var10) {
                        if (var1 >= DataCenter.gI().ar[var10]) {
                            if (var6) {
                                if (var10 > 17) {
                                    continue;
                                }
                            } else if (var10 > 16) {
                                continue;
                            }

                            var5[1] = var8;
                            var5[2] = var10;
                            var5[3] += DataCenter.gI().ar[var10];
                            var1 -= DataCenter.gI().ar[var10];
                            var3[var7].addValue(var9[var10 - 1]);
                            var3[var7].option[3] = var10;
                        }
                    }
                }
            }

            this.strOption = creatOption(var3);
            return var5;
        }
    }

    public boolean isTypeWeapon() {
        return this.getTemplate().type == 1 && !this.ai();
    }

    public boolean isTypeClothe() {
        return this.getTemplate().type == 0 || this.getTemplate().type == 2 || this.getTemplate().type == 4 || this.getTemplate().type == 6 || this.getTemplate().type == 8;
    }

    public boolean isTypePet() {
        return this.getTemplate().type == 3 || this.getTemplate().type == 5 || this.getTemplate().type == 7 || this.getTemplate().type == 9;
    }


    public ItemOption[] getItemOption() {
        if (strOption != null && !strOption.isEmpty()) {
            String[] data = Util.split(strOption, ";");
            ItemOption[] itemOption = new ItemOption[data.length];
            for (int i = 0; i < data.length; ++i) {
                try {
                    itemOption[i] = new ItemOption(data[i]);
                } catch (Exception ex) {

                }
            }
            return itemOption;
        } else {
            return null;
        }
    }

    public boolean checkOptionByType(int i) {
        if (strOption.isEmpty() || strOption == null) {
            return false;
        }
        ItemOption[] itemOptions = getItemOption();
        if (itemOptions != null) {
            for (ItemOption itemOption : itemOptions) {
                if (itemOption.getItemOptionTemplate().type == i) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addSM(long ki) {
        if (strOption.isEmpty() || strOption == null) {
            return;
        }
        ItemOption[] itemOptions = getItemOption();
        if (itemOptions != null) {
            for (ItemOption itemOption : itemOptions) {
                if (itemOption.getId() == 285) {
                    itemOption.option[1] += ki;
                }
            }
            strOption = creatOption(itemOptions);
        }
    }


    public void addKI(long ki) {
        if (strOption.isEmpty() || strOption == null) {
            return;
        }
        ItemOption[] itemOptions = getItemOption();
        if (itemOptions != null) {
            for (ItemOption itemOption : itemOptions) {
                if (itemOption.getId() == 407) {
                    itemOption.option[1] += ki;
                }
            }
            strOption = creatOption(itemOptions);
        }
    }

    public void updateGioiHan(int var) {
        if (strOption.isEmpty() || strOption == null) {
            return;
        }
        ItemOption[] itemOptions = getItemOption();
        if (itemOptions != null) {
            for (ItemOption itemOption : itemOptions) {
                if (itemOption.getId() == 285) {
                    itemOption.option[2] += var;
                }
            }
            strOption = creatOption(itemOptions);
        }
    }

    public boolean replaceOption(Char player, ItemOption gemOption, int index) {
        if (strOption.isEmpty()) {
            return false;
        }
        int upgradeItem = getUpgrade();
        upgradeItem(0);
        ItemOption[] itemOptions = getItemOption();
        if (itemOptions != null) {
            int var = 0;
            for (int i = 0; i < itemOptions.length; i++) {
                if (itemOptions[i].getItemOptionTemplate().type == gemOption.getItemOptionTemplate().type) {
                    if (var == index) {
                        if (UpgradeHandler.isGodEquipment(this) && i < 3) {
                            player.serverDialog("Dòng này không thể ghi đè!");
                            upgradeItem(upgradeItem);
                            return false;
                        }
                        itemOptions[i] = gemOption;
                        strOption = creatOption(itemOptions);
                        upgradeItem(upgradeItem);
                        return true;
                    }
                    ++var;
                }
            }
        }
        upgradeItem(upgradeItem);
        return false;
    }

    public void addOptionRandom(int id, int min, int max) {
        if (!strOption.isEmpty()) {
            strOption += ";";
        }
        strOption += id + "," + Util.nextInt(min, max);
    }

    public void addOption(int id, int value) {
        if (!strOption.isEmpty()) {
            strOption += ";";
        }
        strOption += id + "," + value;
    }

    public void addOptionNew(int id, int value) {
        if (getOption(id) == null) {
            if (!strOption.isEmpty()) {
                strOption += ";";
            }
            strOption += id + "," + value;
        } else {
            ItemOption option = getOption(id);
            option.setParam(option.getParam() + value);
            replaceOptionById(option);
        }
    }

    public boolean isKichHoatAo() {
        return is(1058, 1059, 1060, 1061, 1062);
    }

    public boolean isKichHoatBaoTay() {
        return is(1063, 1064, 1065, 1066, 1067);
    }

    public boolean isKichHoatQuan() {
        return is(1068, 1069, 1070, 1071, 1072);
    }

    public boolean isKichHoatGiay() {
        return is(1073, 1074, 1075, 1076, 1077);
    }

    public boolean is(int... ids) {
        for (int id : ids) {
            if (this.template.id == id) {
                return true;
            }
        }
        return false;
    }

    public ItemOption getOption(int id) {
        if (strOption == null || strOption.isEmpty()) return null;
        return Arrays.stream(Util.split(strOption, ";"))
                .map(ItemOption::new)
                .filter(option -> option.getItemOptionTemplate().id == id)
                .findFirst()
                .orElse(null);
    }


    public void addOptionKichHoat(int id, int param, int optionId) {
        if (!strOption.isEmpty()) {
            strOption += ";";
        }
        strOption += id + "," + param + "," + optionId;
    }

    public void replaceOptionById(ItemOption op) {
        ItemOption[] itemOptions = getItemOption();
        for (int i = 0; i < itemOptions.length; i++) {
            if (itemOptions[i].getId() == op.getId()) {
                itemOptions[i] = op;
                break;
            }
        }
        strOption = creatOption(itemOptions);
    }

    public boolean isDoThanLinh() {
        return id >= 923 && id <= 926 ||  id >= 1159 && id <= 1162;
    }
     
    

    public boolean isCloud() {
        return is(189, 276, 306, 476, 477, 478, 479, 480, 481, 482, 483, 484);
    }

    public boolean isHopNhat() {
        if (strOption == null || strOption.isEmpty()) {
            return false;
        }
        ItemOption[] itemOptions = getItemOption();
        if (itemOptions != null) {
            for (ItemOption itemOption : itemOptions) {
                if (itemOption.getId() == 370 && itemOption.option[1] >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isKham() {
        if (this.strOption.isEmpty() || this.strOption == null) {
            return false;
        }
        ItemOption[] itemOptions = getItemOption();
        if (itemOptions != null) {
            for (ItemOption itemOption : itemOptions) {
                if (itemOption.getId() == 298 && itemOption.option[1] > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
