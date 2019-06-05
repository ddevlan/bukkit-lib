package me.ohvalsgod.bklib.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SideboardListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        SideboardHandler.create(event.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        SideboardHandler.remove(event.getPlayer());
    }

}
