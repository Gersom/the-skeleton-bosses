package gersom.utils;

import org.bukkit.Bukkit;

public class Console {

    // Función para enviar un mensaje formateado a la consola
    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(General.setColor(message));
    }

    // Función para imprimir una línea en blanco en la consola
    public static void printBlankLine() {
        Bukkit.getConsoleSender().sendMessage("");
    }

    public static void printFooter(String lang, String prefix) {    
        if (lang.equals("es")) {
            sendMessage("&b" + prefix + "&lGracias por usar mi plugin <3");
            sendMessage("&b" + prefix + "&lDesarrollador: &eGersom");
        } else {
            sendMessage("&b" + prefix + "&lThank you for using my plugin <3");
            sendMessage("&b" + prefix + "&lDeveloper: &eGersom");
        }
    }
}
