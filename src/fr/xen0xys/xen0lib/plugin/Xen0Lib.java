package fr.xen0xys.xen0lib.plugin;

import fr.xen0xys.xen0lib.bungeecord.BungeeChannel;
import fr.xen0xys.xen0lib.bungeecord.PluginMessage;
import fr.xen0xys.xen0lib.plugin.commands.DisabledXDevCommand;
import fr.xen0xys.xen0lib.plugin.commands.XDevCommand;
import fr.xen0xys.xen0lib.utils.ConfigurationReader;
import fr.xen0xys.xen0lib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Xen0Lib extends JavaPlugin implements PluginMessage{

    private static Plugin instance;
    private static BungeeChannel channel;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if(Xen0Lib.isDevModEnable()){
            channel.unregisterChannel();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        super.onEnable();
        if(Xen0Lib.isDevModEnable()){
            channel = new BungeeChannel(this, this);
            Bukkit.getPluginCommand("xdev").setExecutor(new XDevCommand());
            ConfigurationReader configurationReader = new ConfigurationReader(this, "resources/custom_config.yml");
            System.out.println(configurationReader.getConfiguration().get("yolo"));
        }else{
            Bukkit.getPluginCommand("xdev").setExecutor(new DisabledXDevCommand());
        }

    }

    public static Plugin getInstance() {
        return instance;
    }

    private static boolean isDevModEnable(){
        return false;
    }

    public static BungeeChannel getChannel() {
        return channel;
    }

    @Override
    public void onPluginMessageReceived(Player player, String message) {
        player.sendMessage(message);
    }
}
