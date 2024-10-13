package gersom.utils;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;

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

    public static BarColor generateBossBarColor(String color) {
        return switch (color) {
            case "BLUE" -> BarColor.BLUE;
            case "GREEN" -> BarColor.GREEN;
            case "PINK" -> BarColor.PINK;
            case "PURPLE" -> BarColor.PURPLE;
            case "RED" -> BarColor.RED;
            case "WHITE" -> BarColor.WHITE;
            case "YELLOW" -> BarColor.YELLOW;
            default -> BarColor.PURPLE;
        };
    }
}