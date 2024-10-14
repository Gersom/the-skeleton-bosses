package gersom.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import gersom.TSB;
import gersom.utils.Console;

public class LanguageManager {
    private final TSB plugin;
    private CustomConfig langConfig;
    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("en", "es", "other");

    public LanguageManager(TSB plugin) {
        this.plugin = plugin;
        createLanguageFiles();
        loadLanguageConfig();
    }

    private void createLanguageFiles() {
        for (String lang : SUPPORTED_LANGUAGES) {
            File langFile = new File(plugin.getDataFolder() + File.separator + "lang", lang + ".yml");
            if (!langFile.exists()) {
                plugin.saveResource("lang" + File.separator + lang + ".yml", false);
                Console.sendMessage("&aCreated language file: " + lang + ".yml");
            }
        }
    }

    private void loadLanguageConfig() {
        String language = plugin.getConfigs().getLanguage();
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            Console.sendMessage("&cWarning: Unsupported language '" + language + "'. Defaulting to 'en'.");
            language = "en";
        }
        
        Console.sendMessage("&aLoading language: " + language);
        langConfig = new CustomConfig(language + ".yml", "lang", plugin);
        langConfig.registerConfig();
    }

    public void reloadLanguageConfig() {
        loadLanguageConfig();
    }

    public String getMessage(String path, String defaultValue) {
        String message = langConfig.getString(path, defaultValue);
        return message.isEmpty() ? defaultValue : message;
    }

    public List<String> getMessageList(String path) {
        List<String> messages = langConfig.getStringList(path);
        return messages.isEmpty() ? Arrays.asList(path + " not found") : messages;
    }

    // Add methods for each message you need to retrieve
    // Plugin messages
    public String getLangPluginEnabled() {
        return getMessage("plugin.enabled", "Plugin has been enabled!");
    }
    public String getLangPluginDisabled() {
        return getMessage("plugin.disabled", "Plugin has been disabled!");
    }

    // About messages
    public String getLangWelcome() {
        return getMessage("plugin.about.welcome", "Welcome!");
    }
    public List<String> getLangDescription() {
        return getMessageList("plugin.about.description");
    }

    // Commands messages
    public String getLangCommandsList() {
        return getMessage("plugin.commands.list", "List of commands:");
    }
    public String getLangCommandPlayerOnly() {
        return getMessage("plugin.commands.player_only", "This command can only be used in-game!");
    }
    public String getLangCommandHelpText() {
        return getMessage("plugin.commands.help_text", "To see the list of commands, type");
    }
    public String getLangCommandReload() {
        return getMessage("plugin.commands.reload", "Reload the configs");
    }
    public String getLangCommandNotFound() {
        return getMessage("plugin.commands.not_found", "Command not found!");
    }
    public String getLangCommandNotPermission() {
        return getMessage("plugin.commands.no_permission", "You don't have permission to use this command!");
    }

    // Name of the bosses
    public String getLangBossEmperorName() {
        return getMessage("bosses_names.skeleton_emperor", "Skeleton Emperor");
    }
    public String getLangBossKingName() {
        return getMessage("bosses_names.skeleton_king", "Skeleton King");
    }

    // Messages of the bosses
    public String getLangBossesMsgSpawn() {
        return getMessage("events.spawn", "&a{prefix} ¡{boss_color}The {boss_name} &r&ahas spawned! &7coords: &e{coords}");
    }
    public String getLangBossesMsgDeath() {
        return getMessage("events.death_without_killer", "&c{prefix} ¡{boss_color}The {boss_name} &r&chas died!");
    }
    public String getLangBossesMsgKilled() {
        return getMessage("events.death_with_killer", "&c{prefix} ¡{boss_color}The {boss_name} &r&chas been killed by &l&n{player_killer}!");
    }

    // Name of the objects left behind when he dies
    public String getLangBossesItemBow() {
        return getMessage("items_names.bow", "Bow");
    }
    public String getLangBossesItemSword() {
        return getMessage("items_names.sword", "Sword");
    }
    public String getLangBossesItemHelmet() {
        return getMessage("items_names.helmet", "Helmet");
    }
}