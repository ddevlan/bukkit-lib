package me.ohvalsgod.bukkitlib.command.commands;

import me.ohvalsgod.bukkitlib.command.Command;
import me.ohvalsgod.bukkitlib.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Raw {

    @Command(names = "raw", permissionNode = "core.raw")
    public static void raw(Player sender, @Parameter(wildcard = true, name = "message") String broadcast) {
        String msg = broadcast.replaceAll("(&([a-f0-9l-or]))", "\u00A7$2");
        Bukkit.broadcastMessage(msg);
    }

}
