package com.vdtt.data;

import com.vdtt.item.ItemOption;

import java.util.Comparator;

public final class vdtt_gx implements Comparator {

    public final int compare(Object var1, Object var2) {
        ItemOption var10000 = (ItemOption)var1;
        ItemOption var4 = (ItemOption)var2;
        ItemOption var3 = var10000;
        return var4.getItemOptionTemplate().text.compareTo(var3.getItemOptionTemplate().text);
    }
}

