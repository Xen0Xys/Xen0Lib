package fr.xen0xys.xen0lib.gui;

import fr.xen0xys.xen0lib.plugin.Xen0Lib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Gui implements Listener {

    private final Inventory inventory;
    private final HashMap<Integer, Component> components;

    public Gui(Plugin plugin, String name, int size){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.inventory = Bukkit.createInventory(null, size, name);
        this.components = new HashMap<>();
    }

    public void setComponent(int slot, Component component){
        this.components.put(slot, component);
    }

    public void refresh(){
        this.inventory.clear();
        for(int slot: this.components.keySet()){
            this.inventory.setItem(slot, this.components.get(slot).getItemComponent());
        }
    }

    public void fill(int startSlot, int stopSlot, Component component){
        for(int slot = startSlot; slot < stopSlot; slot++){
            this.setComponent(slot, component);
        }
    }

    public void fill(Component component){
        this.fill(0, this.inventory.getSize(), component);
    }

    public void openInventory(Player player){
        player.openInventory(this.inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem != null){
            for(Component component: this.components.values()){
                if(clickedItem.isSimilar(component.getItemComponent())){
                    if(component instanceof ClickableComponent){
                        ((ClickableComponent) component).onClick(e);
                    }
                }
            }
        }
    }

}
