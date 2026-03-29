package com.vdtt.task;

import lombok.Getter;

import java.util.List;
@Getter
public class TaskTemplate {
    public int id;
    public String name;
    public int level_need;
    public int npcid;
    public int mapid;
    public int x;
    public int y;
    public String str1;
    public String str2;
    public String str3;
    public long ext;
    public int ballz;
    public int zeni;
    public int xu;
    public String strItem;
    public List<Step>steps;
}
