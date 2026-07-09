package kurosio.kurosioats.util;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static final String PREFIX =
            ChatColor.translateAlternateColorCodes('&',
                    "&f[&b&l即売会&aシステム&f] ");


    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public static String format(String message) {
        return PREFIX + color(message);
    }

}