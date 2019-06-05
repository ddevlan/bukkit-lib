package me.ohvalsgod.bklib;

import lombok.Getter;
import me.ohvalsgod.bklib.board.sidebar.SidebarHandler;
import me.ohvalsgod.bklib.board.sidebar.example.ExampleAdapter;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLib extends JavaPlugin {

    @Getter private static BukkitLib library;

    @Getter private SidebarHandler sidebarHandler;

    @Override
    public void onEnable() {
        library = this;

        sidebarHandler = new SidebarHandler(library, new ExampleAdapter());
    }

    @Override
    public void onDisable() {
        library = null;
    }
}
