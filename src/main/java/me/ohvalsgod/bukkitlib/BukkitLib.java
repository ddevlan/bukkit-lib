package me.ohvalsgod.bukkitlib;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.bukkitlib.board.Assemble;
import me.ohvalsgod.bukkitlib.command.CommandHandler;
import me.ohvalsgod.bukkitlib.item.ItemListener;
import me.ohvalsgod.bukkitlib.nametag.NametagHandler;
import me.ohvalsgod.bukkitlib.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLib extends JavaPlugin {

    @Getter private static BukkitLib library;

    @Getter @Setter private Assemble assemble;

    @Override
    public void onEnable() {
        library = this;
        saveDefaultConfig();

        CommandHandler.init();
        NametagHandler.init();
        ItemUtils.load();
        ItemListener.init();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        library = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // This shouldn't be changed -- we NEVER want to let someone
        // in game use /eval, for obvious reasons.
        if (sender instanceof ConsoleCommandSender) {
            CommandHandler.evalCommand(sender, Joiner.on(" ").join(args));
        } else {
            sender.sendMessage(ChatColor.RED + "This is a console-only utility command. It cannot be used from game.");
        }

        return (true);
    }

}
