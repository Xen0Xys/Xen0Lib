package fr.xen0xys.xen0lib.bungeecord;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Channel implements PluginMessageListener {

    private Plugin plugin;
    private String channelName;
    private PluginMessage pluginMessage;

    public Channel(Plugin plugin, String channelName, PluginMessage pluginMessage){
        this.plugin = plugin;
        this.channelName = ("Xen0Lib:" + channelName).toLowerCase();
        this.pluginMessage = pluginMessage;

        this.registerChannel();
    }

    public void registerChannel(){
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, channelName);
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, "BungeeCord", this);
    }

    public void unregisterChannel(){
        this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(this.plugin, channelName);
        this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(this.plugin, "BungeeCord", this);
    }

    public void sendMessage(String serverName, String message, Player player){
        this.sendMessage(serverName, message, player, "Custom");
    }

    @SuppressWarnings("UnstableApiUsage")
    public void sendMessage(String serverName, String message, Player player, String subChannel){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel);
        out.writeUTF(message);

        player.sendPluginMessage( this.plugin, this.channelName, out.toByteArray() );
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        this.pluginMessage.onPluginMessageReceived(s, player, bytes);
    }
}
