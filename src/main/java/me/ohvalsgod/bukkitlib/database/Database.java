package me.ohvalsgod.bukkitlib.database;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public abstract class Database<K, V extends Serializable & Deserializable> {

    private String name;
    private DatabaseType type;
    private Map<K, V> cached;

    public Database(String name, DatabaseType type) {
        this.name = name;
        this.type = type;
        this.cached = new HashMap<>();
    }

    public abstract boolean connect();
    public abstract void close();

    public abstract void save(V v);
    public abstract V load(K k);

    /**
     *  Save database from cached keys
     */
    public void saveDatabase() {
        for (Map.Entry<K, V> entry : cached.entrySet()) {
            save(entry.getValue());
        }
    }

    /**
     * Load database from a specific key set.
     *
     * @param set to be loaded
     */
    public void loadDatabase(Set<K> set) {
        for (K k : set) {
            load(k);
        }
    }

}
