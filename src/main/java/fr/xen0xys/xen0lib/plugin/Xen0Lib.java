package fr.xen0xys.xen0lib.plugin;

import fr.xen0xys.xen0lib.bungeecord.BungeeChannel;
import fr.xen0xys.xen0lib.bungeecord.PluginMessage;
import fr.xen0xys.xen0lib.plugin.commands.DisabledXDevCommand;
import fr.xen0xys.xen0lib.plugin.commands.XDevCommand;
import net.wesjd.anvilgui.AnvilGUI;
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

        AnvilGUI.Builder gui = new AnvilGUI.Builder();

        if(Xen0Lib.isDevModEnable()){
            Bukkit.getPluginCommand("xdev").setExecutor(new XDevCommand());
            /*
            channel = new BungeeChannel(this, this);
            ConfigurationReader configurationReader = new ConfigurationReader(this, "resources/custom_config.yml");
            System.out.println(configurationReader.getConfiguration().get("yolo")); // Return test

            // SQLite database test:
            Database database = new Database(this.getDataFolder().getPath(), "DiscordAuth");
            database.connect();
            Table accountTable = new Table("DiscordAuth_Accounts", database);
            database.openTableAndCreateINE(accountTable, "id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "UUID VARCHAR(50)," +
                    "minecraftName VARCHAR(30)," +
                    "discordId BIGINT," +
                    "password VARCHAR(100)," +
                    "ip VARCHAR(100)," +
                    "lastLogin BIGINT," +
                    "hasSession TINYINT");
             */
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
