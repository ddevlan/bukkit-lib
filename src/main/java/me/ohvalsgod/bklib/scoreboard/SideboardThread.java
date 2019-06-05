package me.ohvalsgod.bklib.scoreboard;

import me.ohvalsgod.bklib.BukkitLib;
import org.bukkit.entity.Player;

public class SideboardThread extends Thread {

    public SideboardThread() {
        super("Core - Scoreboard Thread");
        setDaemon(false);
    }

    public void run() {
        while (true) {
            for (Player online : BukkitLib.getLibrary().getServer().getOnlinePlayers()) {
                try {
                    SideboardHandler.updateScoreboard(online);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(SideboardHandler.getUpdateInterval() * 50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
