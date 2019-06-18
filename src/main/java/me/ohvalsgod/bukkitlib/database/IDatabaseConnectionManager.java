package me.ohvalsgod.bukkitlib.database;

public interface IDatabaseConnectionManager {

    DatabaseCredentials getCredentials();
    boolean connect();
    void close();
    boolean isConnected();

}
