package fr.xen0xys.xen0lib.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * With this object, you can get a configuration file
 * Usable with class extends this, for custom getting
 */
public class ConfigurationReader {

    private final Plugin plugin;
    private final String configName;
    private final FileConfiguration configuration;

    /**
     * Constructor of ConfigurationReader
     * @param plugin Plugin instance
     * @param configName Name of the configuration file (with path if not in src folder)
     */
    public ConfigurationReader(Plugin plugin, String configName){
        this.plugin = plugin;
        this.configName = configName;

        File file = new File(this.plugin.getDataFolder(), configName);
        if(!file.exists()) {
            this.plugin.saveResource(this.configName, false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(file);


    }

    /**
     * Save the configuration file in the server, with overriding older
     */
    public void save(){
        this.plugin.saveResource(this.configName, true);
    }

    /**
     * Get local FileConfiguration
     * @return FileConfiguration
     */
    public FileConfiguration getConfiguration(){
        return this.configuration;
    }

}
