package me.ohvalsgod.bklib.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleGetter {

    private String defaultTitle;

    public TitleGetter(String defaultTitle) {
        this.defaultTitle = ChatColor.translateAlternateColorCodes('&', defaultTitle);
    }

    public String getTitle(Player player) {
        return defaultTitle;
    }

}
