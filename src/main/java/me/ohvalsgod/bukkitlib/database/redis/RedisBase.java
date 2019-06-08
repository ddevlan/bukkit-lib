package me.ohvalsgod.bukkitlib.database.redis;

import me.ohvalsgod.bukkitlib.database.Database;
import me.ohvalsgod.bukkitlib.database.DatabaseType;
import me.ohvalsgod.bukkitlib.database.Deserializable;
import me.ohvalsgod.bukkitlib.database.Serializable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisBase<K, V extends Serializable & Deserializable> extends Database<K, V> {

    private RedisCredentials credentials;

    private JedisPool jedisPool;

    public RedisBase(String name, RedisCredentials credentials) {
        super(name, DatabaseType.REDIS);

        this.credentials = credentials;
    }

    @Override
    public boolean connect() {
        this.jedisPool = new JedisPool(credentials.getHost(), credentials.getPort());

        if (credentials.isAuthenticate()) {
            if (!credentials.getPassword().isEmpty()) {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.auth(credentials.getPassword());
                } catch (Exception e) {
                    System.out.println("[Redis] Could not connect to jedis...");
                    e.printStackTrace();
                }
            } else {
                System.out.println("[Redis] Could not connect to jedis...");
                System.out.println("[Redis] Authentication is true and password is empty.");
                return false;
            }
        }

        return true;
    }

    @Override
    public void close() {
        jedisPool.close();
    }

    @Override
    public void save(V v) {

    }

    @Override
    public V load(K k) {
        return null;
    }
}
