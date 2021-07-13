package fr.xen0xys.xen0lib.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigurationReader {

    private final Plugin plugin;
    private final String configName;
    private final FileConfiguration configuration;

    public ConfigurationReader(Plugin plugin, String configName){
        this.plugin = plugin;
        this.configName = configName;

        File file = new File(this.plugin.getDataFolder(), configName);
        if(!file.exists()) {
            this.plugin.saveResource(this.configName, false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(file);


    }

    public void save(){
        this.plugin.saveResource(this.configName, true);
    }

    public FileConfiguration getConfiguration(){
        return this.configuration;
    }

}
