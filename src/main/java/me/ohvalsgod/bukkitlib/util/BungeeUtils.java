package me.ohvalsgod.bukkitlib.util;

import me.ohvalsgod.bukkitlib.BukkitLib;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BungeeUtils {

    // Static utility class -- cannot be created.
    private BungeeUtils() {
    }

    /**
     * Sends a player to a server.
     *
     * @param player The player to move.
     * @param server The server to move the given player to.
     */
    public static void send(Player player, String server) {
        player.sendMessage("§6Sending you to " + server + "...");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {

        }

        player.sendPluginMessage(BukkitLib.getLibrary(), "BungeeCord", b.toByteArray());
    }

    /**
     * Sends all players to a server.
     *
     * @param server The server to move all players to.
     */
    public static void sendAll(String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            // Can never happen
        }

        for (Player player : BukkitLib.getLibrary().getServer().getOnlinePlayers()) {
            player.sendPluginMessage(BukkitLib.getLibrary(), "BungeeCord", b.toByteArray());
        }
    }

}