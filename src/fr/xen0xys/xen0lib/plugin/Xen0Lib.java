package fr.xen0xys.xen0lib.plugin;

import fr.xen0xys.xen0lib.plugin.commands.XDevCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Xen0Lib extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Bukkit.getPluginCommand("xdev").setExecutor(new XDevCommand());
    }

    public static Plugin getInstance() {
        return instance;
    }
}
