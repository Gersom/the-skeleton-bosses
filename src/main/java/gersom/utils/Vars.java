/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.utils;

import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author Gersom
 */
public class Vars {
    public static String version;
    public static String name;
    public static String title;
    public static String author;

    public static void initialize(PluginDescriptionFile plugin) {
        name = plugin.getName();
        version = plugin.getVersion();
        author = String.join(", ", plugin.getAuthors());
    }
}
