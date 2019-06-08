package me.ohvalsgod.bukkitlib.database.redis;

import lombok.Getter;
import me.ohvalsgod.bukkitlib.database.DatabaseCredentials;

@Getter
public class RedisCredentials extends DatabaseCredentials {

    private String password;
    private boolean authenticate;

    public RedisCredentials(String host, int port, String database) {
        super(host, port, database);
    }
}
