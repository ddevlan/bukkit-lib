package me.ohvalsgod.bukkitlib.database;

import lombok.Getter;

@Getter
public abstract class DatabaseCredentials {

    private String host;
    private int port;
    private String database;

    public DatabaseCredentials(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.database = database;
    }

}
