package com.vdtt.util;

import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.ObjectLive;
import com.vdtt.model.Position;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Util {
    private static final Random rand = new Random();
    private static final NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi"));

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static void logText(String path, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(text);
            writer.newLine(); // Thêm dòng mới
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writing(String path, String text) {
        PrintWriter printWriter = null;
        File file = new File(path);
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists())
                file.createNewFile();
            printWriter = new PrintWriter(new FileOutputStream(path, true));
            printWriter.write(text);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    public static long sum(long... numbers) {
        long sum = 0;
        for (long number : numbers) {
            sum += number;
        }
        return sum;
    }

    public static byte[] inflateByteArray(byte[] var0) {
        Inflater var2;
        (var2 = new Inflater()).setInput(var0);
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        byte[] var3 = new byte[4096];

        try {
            while (!var2.finished()) {
                int var4 = var2.inflate(var3);
                var1.write(var3, 0, var4);
            }

            byte[] var13 = var1.toByteArray();
            return var13;
        } catch (Exception var11) {
        } finally {
            try {
                var1.close();
            } catch (Exception var10) {
            }

        }

        return var0;
    }

    public static void sleep(long paramLong) {
        try {
            Thread.sleep(paramLong);
        } catch (Exception exception) {
        }
    }

    public static int[] getXYFake(int var1, int var2, int J) {
        int var3 = J / 255;
        if (J % 255 != 0) {
            ++var3;
        }
        int byte1 = var2 / 255 * var3 + var1 / 255;
        int byte2 = var1 % 255;
        int byte3 = var2 % 255;
        return new int[]{byte1, byte2, byte3};
    }

    public static int[] getXYReal(int byte1, int byte2, int byte3, int var3) {
        //System.out.println("getXYReal() = ["+byte1+","+byte2+","+byte3+","+var3+"]");
        int var3z = var3 / 255;
        if (var3z % 255 != 0) {
            ++var3z;
        }
        return new int[]{byte2 + byte1 % var3z * 255, byte3 + byte1 / var3z * 255};
//        for (int i = 0; i <= var3; i++) {
//            if (i % 255 == byte2) {
//                for (int j = 0; j <= var3; j++) {
//                    if (j % 255 == byte3) {
//                        if ((j / 255) * var3z + (i / 255) == byte1) {
//                            return new int[]{i, j};
//                        }
//                    }
//                }
//
//            }
//        }
    }

    public static byte[] deflateByteArray(byte[] var0) {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        Deflater var4 = new Deflater();
        var4.setLevel(9);
        var4.setInput(var0);
        var4.finish();
        byte[] var5 = new byte[4096];

        while (!var4.finished()) {
            var2.write(var5, 0, var4.deflate(var5));
        }

        var4.end();
        return var2.toByteArray();
    }

    public static byte[] c(byte[] var0) {
        Inflater var2;
        (var2 = new Inflater()).setInput(var0);
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        byte[] var3 = new byte[4096];

        try {
            while (!var2.finished()) {
                int var4 = var2.inflate(var3);
                var1.write(var3, 0, var4);
            }

            byte[] var13 = var1.toByteArray();
            return var13;
        } catch (Exception var11) {
        } finally {
            try {
                var1.close();
            } catch (Exception var10) {
            }

        }

        return var0;
    }

    public static byte[] getFile(String url) {
        try {
            FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir"), url));
            byte[] ab = new byte[fis.available()];
            fis.read(ab, 0, ab.length);
            fis.close();
            return ab;
        } catch (IOException ex) {

        }
        return null;
    }

    public static byte[] read(String paramString) {
        FileInputStream fileInputStream = null;
        try {
            File file;
            if (!(file = new File(paramString)).exists()) {
                return null;
            }
            fileInputStream = new FileInputStream(file);
            byte[] arrayOfByte = new byte[(int) file.length()];
            fileInputStream.read(arrayOfByte, 0, arrayOfByte.length);
            return arrayOfByte;
        } catch (Exception exception) {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception exception1) {
            }
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception exception) {
            }
        }
        return null;
    }

    public static String timeAgo(int seconds) {
        int minutes = seconds / 60;
        if (minutes > 0) {
            return minutes + " phút";
        } else {
            return seconds + " giây";
        }
    }

    public static int getRange(ObjectLive objectLive1, ObjectLive objectLive2) {
        return getRange(objectLive1.x, objectLive1.y, objectLive2.x, objectLive2.y);
    }

    public static int getRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        paramInt1 = getRange(paramInt1 - paramInt3);
        paramInt2 = getRange(paramInt2 - paramInt4);
        return Math.max(paramInt1, paramInt2);
    }

    public static int getRange(int paramInt) {
        return (paramInt > 0) ? paramInt : -paramInt;
    }

    public static byte randomZoneId(int mapId) {
        Map map = MapManager.getInstance().find(mapId);
        List<Zone> zones = map.getZones();
        if (zones.isEmpty()) {
            return -1;
        }
        byte zoneId = 0;
        for (Zone zone : zones) {
            if (zone.getNumberChar() >= 24) {
                zoneId++;
            } else {
                break;
            }
        }
        int size = zones.size();
        if (zoneId < 0 || zoneId >= size) {
            zoneId = (byte) Util.nextInt(size);
        }
        return zoneId;
    }

    public static String[] split(String paramString1, String paramString2) {
        Vector<String> vector = new Vector();
        for (int i = paramString1.indexOf(paramString2); i >= 0; i = (paramString1 = paramString1.substring(i + paramString2.length())).indexOf(paramString2)) {
            vector.addElement(paramString1.substring(0, i));
        }
        vector.addElement(paramString1);
        String[] arrayOfString = new String[vector.size()];
        vector.copyInto(arrayOfString);
        return arrayOfString;
    }

    public static String[] a(String var0, String var1) {
        Vector var2 = new Vector();

        for (int var3 = var0.indexOf(var1); var3 >= 0; var3 = (var0 = var0.substring(var3 + var1.length())).indexOf(var1)) {
            var2.addElement(var0.substring(0, var3));
        }

        var2.addElement(var0);
        String[] var4 = new String[var2.size()];
        var2.copyInto(var4);
        return var4;
    }

    public static Position a(Position var0, Position var1, Position var2, Position var3) {
        double var9 = var0.x;
        double var11 = var1.x;
        double var13 = var2.x;
        double var15 = var3.x;
        double var17 = var0.y;
        double var19 = var1.y;
        double var21 = var2.y;
        double var23 = var3.y;
        Position var10000;
        double var25 = (var9 - var11) * (var21 - var23) - (var17 - var19) * (var13 - var15);
        if (var25 == 0.0D) {
            var10000 = null;
        } else {
            double var27 = ((var13 - var15) * (var9 * var19 - var17 * var11) - (var9 - var11) * (var13 * var23 - var21 * var15)) / var25;
            double var29 = ((var21 - var23) * (var9 * var19 - var17 * var11) - (var17 - var19) * (var13 * var23 - var21 * var15)) / var25;
            var10000 = Position.create((int) var27, (int) var29);
        }

        Position var4 = var10000;
        return var10000 != null && a(var4.x, var4.y, var0, var1) && a(var4.x, var4.y, var2, var3) ? var4 : null;
    }

    private static boolean a(int var0, int var1, Position var2, Position var3) {
        return (var3.x > var2.x && var2.x <= var0 && var0 <= var3.x || var2.x >= var3.x && var2.x >= var0 && var0 >= var3.x) && (var3.y > var2.y && var2.y <= var1 && var1 <= var3.y || var2.y >= var3.y && var2.y >= var1 && var1 >= var3.y);
    }


    public static int b(int var0) {
        return var0 > 0 ? var0 : -var0;
    }

    public static Random c = new Random();


    public static int nextInt(int min, int max) {
        int min2 = Math.min(min, max);
        int max2 = Math.max(min, max);
        return (max2 == min2 && max2 == 0) ? 0 : (min2 + nextInt(max2 - min2 + 1));
    }

    public static boolean nextInt() {
        return !(c.nextInt(2) == 0);
    }

    public static int nextInt(int[] paramArrayOfint) {
        return paramArrayOfint[nextInt(3)];
    }


    public static ObjectLive a(ObjectLive var0, ObjectLive var1, ObjectLive var2, ObjectLive var3) {
        ObjectLive var4;
        return (var4 = b(var0, var1, var2, var3)) != null && a(var4.x, var4.y, var0, var1) && a(var4.x, var4.y, var2, var3) ? var4 : null;
    }

    private static boolean a(int var0, int var1, ObjectLive var2, ObjectLive var3) {
        return (var3.x > var2.x && var2.x <= var0 && var0 <= var3.x || var2.x >= var3.x && var2.x >= var0 && var0 >= var3.x) && (var3.y > var2.y && var2.y <= var1 && var1 <= var3.y || var2.y >= var3.y && var2.y >= var1 && var1 >= var3.y);
    }

    private static ObjectLive b(ObjectLive var0, ObjectLive var1, ObjectLive var2, ObjectLive var3) {
        double var4 = var0.x;
        double var6 = var1.x;
        double var8 = var2.x;
        double var10 = var3.x;
        double var12 = var0.y;
        double var14 = var1.y;
        double var16 = var2.y;
        double var18 = var3.y;
        double var20 = (var4 - var6) * (var16 - var18) - (var12 - var14) * (var8 - var10);
        if (var20 == 0.0D) {
            return null;
        } else {
            double var22 = ((var8 - var10) * (var4 * var14 - var12 * var6) - (var4 - var6) * (var8 * var18 - var16 * var10)) / var20;
            double var24 = ((var16 - var18) * (var4 * var14 - var12 * var6) - (var12 - var14) * (var8 * var18 - var16 * var10)) / var20;
            return ObjectLive.create((int) var22, (int) var24);
        }
    }

    public static int getRange(int paramInt1, int paramInt2) {
        return getRange(paramInt1 - paramInt2);
    }


    private static double getRange(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        return Math.sqrt(Math.abs((paramDouble1 - paramDouble3) * (paramDouble1 - paramDouble3)) + Math.abs((paramDouble2 - paramDouble4) * (paramDouble2 - paramDouble4)));
    }

    public static String formatMoney(String paramString) {
        try {
            Long.parseLong(paramString);
        } catch (Exception exception) {
            return paramString;
        }
        String str1 = "";
        String str2 = "";
        if (paramString.charAt(0) == '-') {
            str2 = "-";
            paramString = paramString.substring(1);
        }
        for (int i = paramString.length() - 1; i >= 0; i--) {
            if ((paramString.length() - 1 - i) % 3 == 0 && paramString.length() - 1 - i > 0) {
                str1 = paramString.charAt(i) + "." + str1;
            } else {
                str1 = paramString.charAt(i) + str1;
            }
        }
        return str2 + str1;
    }

    public static String formatMoney(long paramLong) {
        return formatMoney(String.valueOf(paramLong));
    }

    public static String formatMoney(int paramInt) {
        return formatMoney(String.valueOf(paramInt));
    }


    public static String replace(String paramString1, String paramString2) {
        return paramString1.replaceAll("#", paramString2);
    }

    public static String replace(String paramString, long paramLong) {
        return paramString.replaceAll("#", String.valueOf(paramLong));
    }

    public static String nextUTF(int paramInt) {
        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder stringBuilder = new StringBuilder(paramInt);
        for (byte b = 0; b < paramInt; b++) {
            stringBuilder.append(str.charAt(nextInt(str.length())));
        }
        return stringBuilder.toString();
    }

    public static byte[] inflateByteArray2(byte[] var0) {
        Inflater var2;
        (var2 = new Inflater()).setInput(var0);
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        byte[] var3 = new byte[4096];

        try {
            while (!var2.finished()) {
                int var4 = var2.inflate(var3);
                var1.write(var3, 0, var4);
            }

            byte[] var13 = var1.toByteArray();
            return var13;
        } catch (Exception var11) {
        } finally {
            try {
                var1.close();
            } catch (Exception var10) {
            }

        }

        return var0;
    }


    public static void write(String paramString, byte[] paramArrayOfbyte) {
        try {
            FileOutputStream fileOutputStream;
            (fileOutputStream = new FileOutputStream(paramString)).write(paramArrayOfbyte);
            fileOutputStream.close();
            return;
        } catch (Exception exception) {
            return;
        } finally {
        }
    }

    public static int readLength(String paramString) {
        byte[] arrayOfByte;
        return ((arrayOfByte = read(paramString)) != null) ? arrayOfByte.length : 0;
    }


    public static String j(int var0) {
        return var0 % 1000 == 0 ? formatMoney(String.valueOf(var0 /= 1000)) : "" + (float) var0 / 1000.0F;
    }


    public static String valueOf(String[] arrayText, String string) {
        String str = "";
        for (int i = 0; i < arrayText.length; i++) {
            str += arrayText[i];
            if (i < arrayText.length - 1) {
                str += string;
            }
        }
        return str;
    }

//    public static int getIntItemOption(ItemOption[] arrayItemOption, int id) {
//        if (arrayItemOption == null) {
//            return 0;
//        }
//        for (int i = 0; i < arrayItemOption.length; i++) {
//            if (arrayItemOption[i].b[0] == id) {
//                return arrayItemOption[i].b[1];
//            }
//        }
//        return 0;
//
//    }

    public static long a() {
        return System.currentTimeMillis();
    }

    public static ArrayList<Item> cleanArrayItem(Item[] array) {
        ArrayList<Item> item = new ArrayList<Item>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                item.add(array[i]);
            }
        }

        return item;
    }

    public static String b(String var0, String var1) {
        return var0.replaceAll("#", var1);
    }

    public static String c(int var0) {
        return c(String.valueOf(var0));
    }

    public static int a(int var0) {
        return var0 <= 0 ? 0 : rand.nextInt(var0);
    }

    public static String c(long var0) {
        return c(String.valueOf(var0));
    }

    public static String c(String var0) {
        try {
            Long.parseLong(var0);
        } catch (Exception var4) {
            return var0;
        }

        String var1 = "";
        String var2 = "";
        if (var0.charAt(0) == '-') {
            var2 = "-";
            var0 = var0.substring(1);
        }

        for (int var3 = var0.length() - 1; var3 >= 0; --var3) {
            if ((var0.length() - 1 - var3) % 3 == 0 && var0.length() - 1 - var3 > 0) {
                var1 = var0.charAt(var3) + "." + var1;
            } else {
                var1 = var0.charAt(var3) + var1;
            }
        }

        return var2 + var1;
    }

    public static String getSTR(String[] string, String string0) {
        String str = "";

        for (int i = 0; i < string.length; i++) {
            str += string[i];
            if (i < string.length - 1) {
                str += string0;
            }
        }

        return str;
    }

    public static float nextFloat(float percent, int i) {
        return rand.nextFloat(percent, i) + 1;
    }

    public static Date getDate(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.getTime();
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    public static void setTimeoutSchedule(Runnable runnable, int delay) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }

    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat DateFor = new SimpleDateFormat(dateFormat);
        return DateFor.format(date);
    }

    public static boolean isSameWeek(Date date1, Date date2) {
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        int year1 = c.get(Calendar.YEAR);
        int week1 = c.get(Calendar.WEEK_OF_YEAR);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        int year2 = c2.get(Calendar.YEAR);
        int week2 = c2.get(Calendar.WEEK_OF_YEAR);
        if (year1 == year2) {
            if (week1 == week2) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrency(long number) {
        return numberFormat.format(number);
    }

    public static int randomWithRate(int[] percent, int max) {
        int next = nextInt(max), i = 0;

        for (i = 0; i < percent.length; i++) {
            if (next < percent[i]) {
                return i;
            }
            next -= percent[i];
        }
        return i;

    }

    public static double calculateCriticalHitPercentage(int currentRate) {
        double maxRate = 3000.0;
        double maxPercentage = 75.0;
        return (currentRate / maxRate) * maxPercentage;
    }

    public static int randomWithRate(int[] percent) {
        int sum = (int) sum(percent);
        int next = nextInt(sum), i;
        for (i = 0; i < percent.length; i++) {
            if (next < percent[i]) {
                return i;
            }
            next -= percent[i];
        }
        return i;
    }

    public static long sum(int... numbers) {
        long sum = 0;
        for (long number : numbers) {
            sum += number;
        }
        return sum;
    }

    public static boolean randomCriticalHit(int currentRate) {
        double critHitPercentage = calculateCriticalHitPercentage(currentRate);
        double randomValue = rand.nextDouble() * 100; // Random value between 0 and 100
        return randomValue <= critHitPercentage;
    }

    public static boolean isNewDay(Timestamp lastTimeStamp) {
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime lastZonedDateTime = lastTimeStamp.toInstant().atZone(currentZone);
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now(currentZone);

        return lastZonedDateTime.toLocalDate().isBefore(currentZonedDateTime.toLocalDate());
    }

    public static boolean isNewWeek(Timestamp lastTimeStamp) {
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");

        ZonedDateTime lastZonedDateTime = lastTimeStamp.toInstant().atZone(currentZone);

        ZonedDateTime currentZonedDateTime = ZonedDateTime.now(currentZone);

        int lastWeekOfYear = lastZonedDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int currentWeekOfYear = currentZonedDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return lastWeekOfYear != currentWeekOfYear;  
    }

    public static void schedule(Runnable runnable, int hours, int minutes, int seconds) {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNext5 = zonedNow.withHour(hours).withMinute(minutes).withSecond(seconds);
        if (zonedNow.compareTo(zonedNext5) > 0) {
            zonedNext5 = zonedNext5.plusDays(1);
        }

        Duration duration = Duration.between(zonedNow, zonedNext5);
        long initalDelay = duration.getSeconds();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(runnable, initalDelay, 1 * 24 * 60 * 60, TimeUnit.SECONDS);
    }

    public static boolean checkExist(ArrayList<Integer> array, int number) {
        for (int i : array) {
            if (i == number) {
                return true;
            }
        }
        return false;
    }

    public static Item parseItem(String data) {
        String[] array = data.split("@");
        int itemId = Short.parseShort(array[0]);
        Item item = new Item(itemId, "parse");
        item.isLock = Boolean.parseBoolean(array[1]);
        item.expire = Long.parseLong(array[2]);
        item.setQuantity(Integer.parseInt(array[3]));
        item.sys = Byte.parseByte(array[4]);
        item.upgrade = Byte.parseByte(array[5]);
        if (array.length > 6) {
            item.strOption = array[6];
        }
        return item;
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(localDateTime);
        return localDateTime.toInstant(zoneOffset).toEpochMilli();
    }

    public static boolean isSameWeek(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        ZonedDateTime zonedDateTime1 = dateTime1.atZone(zoneId);
        ZonedDateTime zonedDateTime2 = dateTime2.atZone(zoneId);
        WeekFields weekFields = WeekFields.of(Locale.forLanguageTag("vi-VN"));

        int weekOfYear1 = zonedDateTime1.get(weekFields.weekOfYear());
        int weekOfYear2 = zonedDateTime2.get(weekFields.weekOfYear());

        int year1 = zonedDateTime1.get(weekFields.weekBasedYear());
        int year2 = zonedDateTime2.get(weekFields.weekBasedYear());

        return weekOfYear1 == weekOfYear2 && year1 == year2;
    }

    public static long byteArrayToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip(); // Chuẩn bị buffer để đọc
        return buffer.getLong();
    }

    public static byte[] longToByteArray(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        return buffer.array();
    }

    public static String getFormatTime(long time) {
        if (time >= 86400000) {
            return (time / 86400000) + " ngày nữa";
        } else if (time >= 3600000) {
            return (time / 3600000) + " giờ nữa";
        } else if (time >= 60000) {
            return (time / 60000) + " phút nữa";
        } else if (time >= 1000) {
            return (time / 1000) + " giây nữa";
        } else {
            return "0 giây nữa";
        }
    }

    public synchronized static void log(String s, String filename) {
        String time = (new SimpleDateFormat("dd_MM_yyyy")).format(Date.from(Instant.now()));
        File f = new File("logs/" + filename + "_" + time + ".txt");
        FileWriter fr = null;
        BufferedWriter bfr = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fr = new FileWriter(f, StandardCharsets.UTF_8, true);
            bfr = new BufferedWriter(fr);
            SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            String txt = "[" + fm.format(Date.from(Instant.now())) + "]\n" + s.replace("\\n", System.lineSeparator());
            bfr.write(txt);
            bfr.newLine();
            bfr.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bfr != null) {
                    bfr.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception ex2) {
            }
        }
    }
}
