package fr.xen0xys.xen0lib.gui.anvilgui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AnvilGui implements Listener {

    private final Plugin plugin;
    private final Inventory inventory;
    private final boolean preventClosing;

    public AnvilGui(Plugin plugin, String title, boolean preventClosing){
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, InventoryType.ANVIL, title);
        this.inventory.setItem(0, new ItemStack(Material.PAPER));
        this.preventClosing = preventClosing;
    }

    public void openInventory(Player player){
        player.openInventory(this.inventory);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        System.out.println("Here");
    }

    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent e){
        if(e.getInventory() == this.inventory && this.preventClosing){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    e.getPlayer().openInventory(inventory);
                }
            }, 1L); //20 Tick (1 Second) delay before run() is called
        }
    }

}
