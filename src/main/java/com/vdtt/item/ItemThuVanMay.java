package com.vdtt.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemThuVanMay {
    private int id;
    private Item item;
    private int rate;
}
