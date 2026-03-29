package com.vdtt.effect;


import com.vdtt.network.Message;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.io.IOException;

@Getter
public class Effect {

    public final byte EFF_ME = 0;
    public final byte EFF_FRIEND = 1;

    public int param;
    public int param2;
    private long startAt;
    private long endAt;
    private long timeLength;
    public EffectTemplate template;
    public boolean isPet;

    public Effect(int templateId, long startAt, long endAt, int param) {
        this.template = EffectTemplateManager.getInstance().find(templateId);
        this.startAt = startAt;
        this.endAt = endAt;
        this.param = param;
        this.timeLength = endAt - startAt;
    }


    public Effect(int templateId, long length, int param) {
        this.template = EffectTemplateManager.getInstance().find(templateId);
        this.startAt = System.currentTimeMillis();
        this.endAt = startAt + length;
        this.param = param;
        this.timeLength = length;
    }

    public Effect(int templateId, long length, int param, boolean isPet) {
        this.template = EffectTemplateManager.getInstance().find(templateId);
        this.startAt = System.currentTimeMillis();
        this.endAt = startAt + length;
        this.param = param;
        this.timeLength = length;
        this.isPet = isPet;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= endAt;
    }

    public int getTimeStart() {
        return (int) ((System.currentTimeMillis() - startAt) / 1000);
    }

    public int getTimeLength() {
        return (int) (endAt - startAt);
    }

    public long getTimeRemaining() {
        timeLength = endAt - System.currentTimeMillis();
        return endAt - System.currentTimeMillis();
    }

    public void setDuration(long duration) {
        this.startAt = System.currentTimeMillis();
        this.endAt = startAt + duration;
        this.timeLength = duration;
    }

    public void addTime(long time) {
        this.endAt += time;
        this.timeLength += time;
    }

    public JSONObject toJSONObject() {
        JSONObject job = new JSONObject();
        job.put("id", this.template.id);
        job.put("start_at", startAt);
        job.put("end_at", endAt);
        job.put("param", this.param);
        job.put("time_length", endAt - System.currentTimeMillis());
        return job;
    }

    public void write(Message m) {
        try {
            m.writer().writeShort(template.id);
            m.writer().writeInt(param);
            m.writer().writeLong(startAt);
            m.writer().writeInt((int) timeLength);
            if (isPet) {
                m.writer().writeBoolean(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
