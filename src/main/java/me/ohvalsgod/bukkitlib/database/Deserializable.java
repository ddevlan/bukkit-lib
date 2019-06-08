package me.ohvalsgod.bukkitlib.database;

import com.google.gson.JsonObject;

import java.util.Map;

public interface Deserializable<T> {

    /**
     * Deserialize an object that was serialized from {@link me.ohvalsgod.bukkitlib.database.Serializable#serializeToString(Object)}
     *
     * @param source serialized string
     * @return deserialized object
     */
    T deserializeFromString(String source);
    
    /**
     * Deserialize an object that was serialized from {@link me.ohvalsgod.bukkitlib.database.Serializable#serializeToJson(Object)} (Object)}
     *
     * @param source serialized string
     * @return deserialized object
     */
    T deserializeFromJson(JsonObject source);

    /**
     * Deserialize an object that was serialized from {@link me.ohvalsgod.bukkitlib.database.Serializable#serializeToMap(Object)} (Object)}
     *
     * @param source serialized string
     * @return deserialized object
     */
    T deserializeFromMap(Map<String, Object> source);

}
