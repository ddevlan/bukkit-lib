package me.ohvalsgod.bukkitlib.database;

import me.ohvalsgod.bukkitlib.database.dao.DatabaseObjectDAO;

import java.util.Map;

public interface IDatabase<K, V> {

    IDatabaseConnectionManager getConnectionManager();
    DatabaseObjectDAO<K, V> getObjectDAO();

    Map<K, V> getCachedObjects();
    V getCachedObject(K k);
    void removedCachedObject(K k);
    void cacheObject(V v);

}
