package me.ohvalsgod.bukkitlib.database;

import lombok.Getter;
import me.ohvalsgod.bukkitlib.database.type.DatabaseType;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class DatabaseCredentials {

    private String host;
    private int port;
    private String username = "";
    private String password = "";
    private String database;
    private String table;

    public DatabaseCredentials(String host, int port, String database, String table) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.table = table;
    }

    public DatabaseCredentials(String host, int port, String database, String table, String password) {
        this(host, port, database, table);
        this.password = password;
    }

    public DatabaseCredentials(String host, int port, String database, String table, String username, String password) {
        this(host, port, database, table);
        this.username = username;
        this.password = password;
    }

    public boolean shouldAuthenticate() {
        return !password.isEmpty();
    }

    public boolean shouldAuthenticateWithUsernameAndPassword() {
        return !username.isEmpty() && !password.isEmpty();
    }

    public static DatabaseCredentials fromConfig(FileConfiguration configuration, DatabaseType type) {
        if (!configuration.contains(type.name().toLowerCase())) {
            return null;
        }

        return new DatabaseCredentials(
                configuration.getString(type.name().toLowerCase() + ".host"),
                configuration.getInt(type.name().toLowerCase() + ".port"),
                configuration.getString(type.name().toLowerCase() + ".database"),
                configuration.getString(type.name().toLowerCase() + ".username"),
                configuration.getString(type.name().toLowerCase() + ".password")
        );
    }

}
