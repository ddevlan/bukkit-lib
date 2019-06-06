package me.ohvalsgod.bukkitlib.nametag;

import me.ohvalsgod.bukkitlib.BukkitLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

final class NametagListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (NametagHandler.isInitiated()) {
            event.getPlayer().setMetadata("ohvalsTag-LoggedIn", new FixedMetadataValue(BukkitLib.getLibrary(), true));
            NametagHandler.initiatePlayer(event.getPlayer());
            NametagHandler.reloadPlayer(event.getPlayer());
            NametagHandler.reloadOthersFor(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().removeMetadata("ohvalsTag-LoggedIn", BukkitLib.getLibrary());
        NametagHandler.getTeamMap().remove(event.getPlayer().getName());
    }

}