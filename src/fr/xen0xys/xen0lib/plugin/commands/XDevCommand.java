package fr.xen0xys.xen0lib.plugin.commands;

import fr.xen0xys.xen0lib.bungeecord.Channel;
import fr.xen0xys.xen0lib.bungeecord.PluginMessage;
import fr.xen0xys.xen0lib.gui.Clickable;
import fr.xen0xys.xen0lib.gui.ClickableComponent;
import fr.xen0xys.xen0lib.gui.Component;
import fr.xen0xys.xen0lib.gui.Gui;
import fr.xen0xys.xen0lib.plugin.Xen0Lib;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class XDevCommand implements CommandExecutor, Clickable, PluginMessage {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player player){
            // GUI test
            Gui gui = new Gui(Xen0Lib.getInstance(), "Test", 9);
            gui.fill(new Component(Material.BLACK_STAINED_GLASS_PANE, 1, "-", new String[]{}));
            gui.setComponent(0, new ClickableComponent(Material.APPLE, 5, "Clickable Apple", new String[]{"Click me!"}, this));
            gui.setComponent(1, new Component(Material.GOLD_INGOT, 3, "Gold ingot :p", new String[]{"", ""}));
            gui.setComponent(2, new ClickableComponent(Material.DIAMOND, 1, "Clickable Diamond", new String[]{"Click me!"}, this));
            gui.refresh();
            gui.openInventory(player);

            // Bungeecord test
            Channel channel = new Channel(Xen0Lib.getInstance(), "CustomChannel", this);
            channel.sendMessage("Target", "Hello", player);
        }
        return false;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        System.out.println("Player click component");
        System.out.println(e.getCurrentItem().getType());
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        System.out.println(s);
    }
}
