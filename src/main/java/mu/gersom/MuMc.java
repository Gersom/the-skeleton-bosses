package mu.gersom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class MuMc extends JavaPlugin {
    private final String version = getDescription().getVersion();
    private final String name = getDescription().getName();
    public String prefix = "&l[" + name + "] ";

    // Función para generar marcos de texto
    private String generateTextFrame(String text) {
        return ChatColor.translateAlternateColorCodes('&', "&b=========== &l" + text + " &b===========");
    }

    // Función para generar línea separadora
    private String generateSeparator() {
        return ChatColor.translateAlternateColorCodes('&', "&b================================");
    }

    private void printFooter() {
        sendConsoleMessage("&b" + prefix + "&lThank you for using my plugin <3");
        sendConsoleMessage("&b" + prefix + "&lDeveloper: &eGersom");
    }

    // Función para imprimir una línea en blanco en la consola
    private void printBlankLine() {
        Bukkit.getConsoleSender().sendMessage("");
    }

    // Función para enviar un mensaje formateado a la consola
    private void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    } 

    @Override
    public void onEnable() {
        sendConsoleMessage(generateTextFrame(name + " v" + version));
        printBlankLine();
        sendConsoleMessage("&a" + prefix + "&a&l> plugin has been enabled!");
        printBlankLine();
        printFooter();
        printBlankLine();
        sendConsoleMessage(generateSeparator());
    }

    @Override
    public void onDisable() {
        sendConsoleMessage(generateTextFrame(name + " v" + version));
        printBlankLine();
        sendConsoleMessage("&c" + prefix + "&c&l> plugin has been disabled!");
        printBlankLine();
        printFooter();
        printBlankLine();
        sendConsoleMessage(generateSeparator());
    }
}