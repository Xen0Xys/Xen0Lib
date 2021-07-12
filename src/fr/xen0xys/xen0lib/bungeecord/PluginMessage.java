package fr.xen0xys.xen0lib.bungeecord;

import org.bukkit.entity.Player;

public interface PluginMessage {
    public void onPluginMessageReceived(String s, Player player, byte[] bytes);
}
