package fr.xen0xys.xen0lib.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import java.io.*;

/**
 * BungeeCord channel object, can be use for BungeeCord commands
 */
@SuppressWarnings("UnstableApiUsage")
public class BungeeChannel implements PluginMessageListener {

    private final Plugin plugin;
    private final String channelName = "BungeeCord";
    private final PluginMessage pluginMessage;

    public BungeeChannel(Plugin plugin, PluginMessage pluginMessage){
        this.plugin = plugin;
        this.pluginMessage = pluginMessage;

        this.registerChannel();
    }

    /**
     * Register BungeeCord channels
     */
    public void registerChannel(){
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, channelName);
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, this.channelName, this);
    }

    /**
     * Unregister BungeeCord channels
     */
    public void unregisterChannel(){
        this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(this.plugin, channelName);
        this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(this.plugin, this.channelName, this);
    }

    /**
     * Send given message to targeted server
     * @param player Bukkit Player
     * @param serverName BungeeCord target server name
     * @param message Message to send
     */
    public void sendMessage(Player player, String serverName, String message){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF(serverName);
        out.writeUTF(this.channelName);
        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(message);
        } catch (IOException exception){
            exception.printStackTrace();
        }
        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
        player.sendPluginMessage(this.plugin, this.channelName, out.toByteArray());
    }

    /**
     * Connect giver Player to given Server Name
     * @param player Target player
     * @param serverName BungeeCord target server name
     */
    public void connect(Player player, String serverName){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(this.plugin, this.channelName, out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(@Nonnull String channel, @Nonnull Player player, @Nonnull byte[] bytes) {
        if(this.pluginMessage != null && channel.equals(this.channelName)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subChannel = in.readUTF();
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);
            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                String message = msgin.readUTF(); // Read the data in the same way you wrote it
                this.pluginMessage.onPluginMessageReceived(player, message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
