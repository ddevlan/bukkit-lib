package me.ohvalsgod.bukkitlib.database;

import com.google.gson.JsonObject;

import java.util.Map;

public interface Serializable<T> {

    /**
     * Basic string serialization.
     *
     * @param t object to be serialize
     * @return serialized string
     */
    String serializeToString(T t);

    /**
     *  JsonObject serialization.
     *
     * @param t object to be serialized
     * @return serialized json object
     */
    JsonObject serializeToJson(T t);

    /**
     * Map serialization.
     *
     * @param t object to be serialized
     * @return serialized map
     */
    Map<String, Object> serializeToMap(T t);

}
