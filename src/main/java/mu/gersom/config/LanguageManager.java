package mu.gersom.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import mu.gersom.MuMc;
import mu.gersom.utils.Console;

public class LanguageManager {
    private final MuMc plugin;
    private CustomConfig langConfig;
    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("en", "es", "other");

    public LanguageManager(MuMc plugin) {
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
    public String getMsgPluginEnabled() {
        return getMessage("messages.plugin.enabled", "Plugin has been enabled!");
    }

    public String getMsgPluginDisabled() {
        return getMessage("messages.plugin.disabled", "Plugin has been disabled!");
    }

    public String getHelpText() {
        return getMessage("messages.commands.help_text", "To see the list of commands, type");
    }

    public String getCommandNotFound() {
        return getMessage("messages.commands.not_found", "Command not found!");
    }

    public String getPlayerOnlyCommand() {
        return getMessage("messages.commands.player_only", "This command can only be used in-game!");
    }

    public String getWelcomeMessage() {
        return getMessage("messages.about.welcome", "Welcome!");
    }

    public List<String> getDescriptionMessages() {
        return getMessageList("messages.about.description");
    }

    public String getListCommands() {
        return getMessage("messages.commands.list", "List of commands:");
    }

    public String getReloadText() {
        return getMessage("messages.commands.reload", "Reload the configs");
    }

    public String getNotPermission() {
        return getMessage("messages.commands.no_permission", "You don't have permission to use this command!");
    }
}