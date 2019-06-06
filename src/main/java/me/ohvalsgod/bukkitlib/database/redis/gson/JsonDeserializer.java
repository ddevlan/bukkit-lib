package me.ohvalsgod.bukkitlib.database.redis.gson;

import com.google.gson.JsonObject;

public interface JsonDeserializer<T> {

    T deserialize(JsonObject object);

}
