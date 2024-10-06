package gersom.utils;

import org.bukkit.ChatColor;

public class General {
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
        return generateTextFrame(Vars.name + " v" + Vars.version);
    }

    // Función para generar línea separadora
    public static String generateSeparator() {
        return setColor("&b================================");
    }

}