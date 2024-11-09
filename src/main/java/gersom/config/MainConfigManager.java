package gersom.config;

import java.util.List;

import gersom.TSB;

public class MainConfigManager {
    private final CustomConfig configFile;
    private LanguageManager languageManager;
    private final TSB plugin;

    public MainConfigManager(TSB plugin) {
        this.plugin = plugin;
        this.configFile = new CustomConfig("config.yml", null, plugin);
        this.configFile.registerConfig();
    }

    public void initialize() {
        this.languageManager = new LanguageManager(plugin);
    }

    public void reloadConfig() {
        configFile.reloadConfig();
        languageManager.reloadLanguageConfig();
    }

    public void setLanguage(String language) {
        configFile.getConfig().set("language", language);
        configFile.saveConfig();
        languageManager.reloadLanguageConfig();
    }

    public String getLanguage() {
        return configFile.getString("language", "en");
    }

    public String getPrefix() {
        return configFile.getString("prefix", "[TSB] ");
    }

    public Boolean getIsLogs() {
        return configFile.getBoolean("logs",  false);
    }

    // Spawn data
    public Boolean getSpawnEnabled() {
        return configFile.getBoolean("spawn.enabled", false);
    }
    public String getSpawnWorld() {
        return configFile.getString("spawn.world", "world");
    }
    public int getSpawnInterval() {
        return configFile.getInt("spawn.interval", 20);
    }
    public int getSpawnMinRadius() {
        return configFile.getInt("spawn.min_radius", 0);
    }
    public int getSpawnMaxRadius() {
        return configFile.getInt("spawn.max_radius", 10);
    }
    public double getSpawnChance() {
        return configFile.getInt("spawn.chance", 100) / 100;
    }
    public int getSpawnLocationX() {
        return configFile.getInt("spawn.location.x", 0);
    }
    public int getSpawnLocationY() {
        // This field is irrelevant, 
        // it is only there as a reference to generate
        // the Location variable, since position «y» 
        // will be the highest non-empty coordinate (impassable).
        return 150;
    }
    public int getSpawnLocationZ() {
        return configFile.getInt("spawn.location.z", 0);
    }
    public boolean getMessageJoinServer() {
        return configFile.getBoolean("message_join_server.enabled", true);
    }
    public int getMessageJoinServerDelay() {
        return configFile.getInt("message_join_server.delay", 60);
    }

    // BOSSES
    // Percentage of all bosses that will spawn
    public double  getBossKingPercentage() {
        return configFile.getDouble("skeleton_king.percentage", 33.33);
    }
    public double  getBossEmperorPercentage() {
        return configFile.getDouble("skeleton_emperor.percentage", 33.33);
    }
    public double  getBossWinterLordPercentage() {
        return configFile.getDouble("skeleton_winter_lord.percentage", 33.34);
    }
    // Color Boss
    public String getBossKingColor() {
        return configFile.getString("skeleton_king.color", "&d");
    }
    public String getBossEmperorColor() {
        return configFile.getString("skeleton_emperor.color", "&6");
    }
    public String getBossWinterLordColor() {
        return configFile.getString("skeleton_winter_lord.color", "&b");
    }
    // Health of the bosses
    public double getBossKingHealth() {
        return configFile.getDouble("skeleton_king.health", 350.0);
    }
    public double getBossEmperorHealth() {
        return configFile.getDouble("skeleton_emperor.health", 250.0);
    }
    public double getBossWinterLordHealth() {
        return configFile.getDouble("skeleton_winter_lord.health", 300.0);
    }
    // Damage of the bosses
    public int getBossKingDamage() {
        return configFile.getInt("skeleton_king.damage", 20);
    }
    public int getBossEmperorPower() {
        return configFile.getInt("skeleton_emperor.power", 35);
    }
    public int getBossWinterLordPower() {
        return configFile.getInt("skeleton_winter_lord.power", 35);
    }
    // Projectile evasion of the bosses
    public int getBossKingProjectileEvasion() {
        return configFile.getInt("skeleton_king.projectile_evasion", 75);
    }
    public int getBossEmperorProjectileEvasion() {
        return configFile.getInt("skeleton_emperor.projectile_evasion", 50);
    }
    public int getBossWinterLordProjectileEvasion() {
        return configFile.getInt("skeleton_winter_lord.projectile_evasion", 75);
    }
    // Experience of the bosses
    public int getBossKingExp() {
        return configFile.getInt("skeleton_king.drop_experience", 350);
    }
    public int getBossEmperorExp() {
        return configFile.getInt("skeleton_emperor.drop_experience", 250);
    }
    public int getBossWinterLordExp() {
        return configFile.getInt("skeleton_winter_lord.drop_experience", 300);
    }
    // Bossbar messages
    public String getBossKingBossbarTitle() {
        return configFile.getString("skeleton_king.bossbar.title", "{boss_color} &l{boss_name} ({health}/{max_health})❤");
    }
    public String getBossKingBossbarColor() {
        return configFile.getString("skeleton_king.bossbar.color", "PURPLE");
    }
    public String getBossEmperorBossbarTitle() {
        return configFile.getString("skeleton_emperor.bossbar.title", "{boss_color} &l{boss_name} ({health}/{max_health})❤");
    }
    public String getBossEmperorBossbarColor() {
        return configFile.getString("skeleton_emperor.bossbar.color", "YELLOW");
    }
    public String getBossWinterLordBossbarTitle() {
        return configFile.getString("skeleton_winter_lord.bossbar.title", "{boss_color} &l{boss_name} ({health}/{max_health})❤");
    }
    public String getBossWinterLordBossbarColor() {
        return configFile.getString("skeleton_winter_lord.bossbar.color", "BLUE");
    }
    // Commands for player killed the boss
    public Boolean getBossesCommandsEnabled() {
        return configFile.getBoolean("commands_after_death", false);
    }
    public List<String> getBossKingCommands() {
        return configFile.getStringList("skeleton_king.commands");
    }
    public List<String> getBossEmperorCommands() {
        return configFile.getStringList("skeleton_emperor.commands");
    }
    public List<String> getBossWinterLordCommands() {
        return configFile.getStringList("skeleton_winter_lord.commands");
    }
    // Commands for nearby players
    public boolean getBossKingNearbyCommandsEnabled() {
        return configFile.getBoolean("skeleton_king.commands_for_nearby_players.enabled", false);
    }
    public int getBossKingNearbyCommandsRadius() {
        return configFile.getInt("skeleton_king.commands_for_nearby_players.radius", 40);
    }
    public List<String> getBossKingNearbyCommands() {
        return configFile.getStringList("skeleton_king.commands_for_nearby_players.commands");
    }
    public boolean getBossEmperorNearbyCommandsEnabled() {
        return configFile.getBoolean("skeleton_emperor.commands_for_nearby_players.enabled", false);
    }
    public int getBossEmperorNearbyCommandsRadius() {
        return configFile.getInt("skeleton_emperor.commands_for_nearby_players.radius", 40);
    }
    public List<String> getBossEmperorNearbyCommands() {
        return configFile.getStringList("skeleton_emperor.commands_for_nearby_players.commands");
    }
    public boolean getBossWinterLordNearbyCommandsEnabled() {
        return configFile.getBoolean("skeleton_winter_lord.commands_for_nearby_players.enabled", false);
    }
    public int getBossWinterLordNearbyCommandsRadius() {
        return configFile.getInt("skeleton_winter_lord.commands_for_nearby_players.radius", 40);
    }
    public List<String> getBossWinterLordNearbyCommands() {
        return configFile.getStringList("skeleton_winter_lord.commands_for_nearby_players.commands");
    }

    // Delegate message methods to LanguageManager
    // Plugin messages
    public String getLangPluginEnabled() {
        return languageManager.getLangPluginEnabled();
    }
    public String getLangPluginDisabled() {
        return languageManager.getLangPluginDisabled();
    }

    // About messages
    public String getLangWelcome() {
        return languageManager.getLangWelcome();
    }
    public List<String> getLangDescription() {
        return languageManager.getLangDescription();
    }

    // Commands messages
    public String getLangCommandsList() {
        return languageManager.getLangCommandsList();
    }
    public String getLangCommandPlayerOnly() {
        return languageManager.getLangCommandPlayerOnly();
    }
    public String getLangCommandHelpText() {
        return languageManager.getLangCommandHelpText();
    }
    public String getLangCommandReload() {
        return languageManager.getLangCommandReload();
    }
    public String getLangCommandNotFound() {
        return languageManager.getLangCommandNotFound();
    }
    public String getLangCommandNotPermission() {
        return languageManager.getLangCommandNotPermission();
    }
    public String getLangCommandAlreadyExist() {
        return languageManager.getLangCommandAlreadyExist();
    }
    public String getLangCommandClearRecords() {
        return languageManager.getLangCommandClearRecords();
    }
    
    // BOSSES MESSAGES LANGUAGE
    // Name of the bosses
    public String getLangBossEmperorName() {
        return languageManager.getLangBossEmperorName();
    }
    public String getLangBossKingName() {
        return languageManager.getLangBossKingName();
    }
    public String getLangBossWinterLordName() {
        return languageManager.getLangBossWinterLordName();
    }

    // Name of the Henchmens
    public String getLangBossKingSentinel() {
        return languageManager.getLangBossKingSentinel();
    }
    public String getLangBossEmperorGuard() {
        return languageManager.getLangBossEmperorGuard();
    }
    public String getLangBossWinterLordMinion() {
        return languageManager.getLangBossWinterLordMinion();
    }
    
    // Messages of the bosses
    public String getLangBossesMsgSpawn() {
        return languageManager.getLangBossesMsgSpawn();
    }
    public String getLangBossesMsgDeath() {
        return languageManager.getLangBossesMsgDeath();
    }
    public String getLangBossesMsgKilled() {
        return languageManager.getLangBossesMsgKilled();
    }

    // Name of the objects left behind when he dies
    public String getLangBossesItemBow() {
        return languageManager.getLangBossesItemBow();
    }
    public String getLangBossesItemSword() {
        return languageManager.getLangBossesItemSword();
    }
    public String getLangBossesItemHelmet() {
        return languageManager.getLangBossesItemHelmet();
    }
    public String getLangBossesItemBoots() {
        return languageManager.getLangBossesItemBoots();
    }
}