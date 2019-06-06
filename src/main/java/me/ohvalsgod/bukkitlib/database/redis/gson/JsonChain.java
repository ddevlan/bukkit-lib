package me.ohvalsgod.bukkitlib.database.redis.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonChain {

    private JsonObject jsonObject;

    public JsonChain() {
        this.jsonObject = new JsonObject();
    }

    public JsonChain addProperty(String property, String value) {
        this.jsonObject.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Number value) {
        this.jsonObject.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Boolean value) {
        this.jsonObject.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Character value) {
        this.jsonObject.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, JsonElement value) {
        this.jsonObject.add(property, value);
        return this;
    }

    public JsonObject link() {
        return jsonObject;
    }

}
