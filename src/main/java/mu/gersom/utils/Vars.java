/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mu.gersom.utils;

import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author Gersom
 */
public class Vars {
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
}
