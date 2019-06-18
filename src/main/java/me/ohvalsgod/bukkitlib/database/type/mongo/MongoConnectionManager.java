package me.ohvalsgod.bukkitlib.database.type.mongo;

import me.ohvalsgod.bukkitlib.database.Database;
import me.ohvalsgod.bukkitlib.database.DatabaseCredentials;
import me.ohvalsgod.bukkitlib.database.IDatabaseConnectionManager;
import me.ohvalsgod.bukkitlib.database.dao.DatabaseObjectDAO;
import me.ohvalsgod.bukkitlib.database.type.DatabaseType;

public class MongoConnectionManager<K, V> extends Database<K, V> {

    protected MongoConnectionManager(String name, DatabaseCredentials credentials, DatabaseType type) {
        super(name, credentials, type);
    }

    @Override
    public IDatabaseConnectionManager getConnectionManager() {
        return null;
    }

    @Override
    public DatabaseObjectDAO<K, V> getObjectDAO() {
        return null;
    }

    @Override
    public void cacheObject(V v) {

    }
}
