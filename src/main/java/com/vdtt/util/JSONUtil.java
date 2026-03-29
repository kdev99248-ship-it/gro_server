package com.vdtt.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.List;

@Getter
public class JSONUtil {
    private final Gson gson;

    public JSONUtil() {
        gson = new Gson();
    }

    public <T> T fromJson(String src, Class<?> type) {
        return (T) gson.fromJson(src, type);
    }

    public <T> T fromJsonList(String src, Class<?> type) {
        Type listType = TypeToken.getParameterized(List.class, type).getType();
        return gson.fromJson(src, listType);
    }

    public String toJson(Object type) {
        return gson.toJson(type, type.getClass());
    }

    public static JSONUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        public static final JSONUtil INSTANCE = new JSONUtil();
    }
}
