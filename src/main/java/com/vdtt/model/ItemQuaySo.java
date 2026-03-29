package com.vdtt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class ItemQuaySo {
    private int id;
    private int idItem;
    @Builder.Default
    private int quantity = 1;
    @Builder.Default
    private byte type = 0;
    private double rate;
}
