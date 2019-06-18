package me.ohvalsgod.bukkitlib.database;

import lombok.Getter;
import me.ohvalsgod.bukkitlib.database.type.DatabaseType;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Database<K, V> implements IDatabase<K, V> {

    private String name;
    private DatabaseCredentials credentials;
    private DatabaseType type;
    private Map<K, V> cached;

    protected Database(String name, DatabaseCredentials credentials, DatabaseType type) {
        this.name = name;
        this.credentials = credentials;
        this.type = type;
        this.cached = new HashMap<>();
    }

    @Override
    public Map<K, V> getCachedObjects() {
        return cached;
    }

    @Override
    public V getCachedObject(K k) {
        return cached.get(k);
    }

    @Override
    public void removedCachedObject(K k) {
        cached.remove(k);
    }
}
