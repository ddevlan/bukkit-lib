package me.ohvalsgod.bukkitlib.util;

import org.bukkit.ChatColor;

public class WoolUtils {

    private static byte[] data;

    static {
        data = new byte[] {
                15, 11, 13, 9, 14, 10, 1, 8, 7, 3, 5, 9, 14, 2, 4, 0
        };
    }

    public static byte getData(ChatColor color) {
        if (!color.isColor()) {
            return 0;
        }

        return data[color.ordinal()];
    }

    public static ChatColor fromString(String source) {
        return ChatColor.getByChar(source.replace("§", "").charAt(0));
    }

}
