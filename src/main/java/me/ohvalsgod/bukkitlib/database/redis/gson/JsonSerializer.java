package me.ohvalsgod.bukkitlib.database.redis.gson;

import com.google.gson.JsonObject;

public interface JsonSerializer<T> {

    JsonObject serialize(T t);

}

