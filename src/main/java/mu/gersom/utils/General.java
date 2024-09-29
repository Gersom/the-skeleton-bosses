package mu.gersom.utils;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

public class General {
    public static String version;
    public static String name;
    public static String prefix;
    public static String author;

    public static void initialize(PluginDescriptionFile plugin) {
        name = plugin.getName();
        version = plugin.getVersion();
        author = String.join(", ", plugin.getAuthors());
        prefix = "[" + name + "] ";
    }

    // Función para setear el color de un texto
    public static String setColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    // Función para generar marcos de texto
    public static String generateTextFrame(String text) {
        return setColor("&b=========== &l" + text + " &b===========");
    }

    // Función para generar marco de texto de cabecera
    public static String generateHeadFrame() {
        return generateTextFrame(name + " v" + version);
    }

    // Función para generar línea separadora
    public static String generateSeparator() {
        return setColor("&b================================");
    }

}