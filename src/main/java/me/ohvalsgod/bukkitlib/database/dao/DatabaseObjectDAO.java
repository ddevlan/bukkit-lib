package me.ohvalsgod.bukkitlib.database.dao;

import java.util.List;

public interface DatabaseObjectDAO<K, V> {

    V getByKey(K k);
    void saveObject(K k);
    void deleteObject(K k);

    List<K> getAllObjects();

}
