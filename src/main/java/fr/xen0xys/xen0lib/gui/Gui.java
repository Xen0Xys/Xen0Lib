package fr.xen0xys.xen0lib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Gui implements Listener {

    private final Plugin plugin;
    private final Inventory inventory;
    private final HashMap<Integer, Component> components;
    private final boolean preventClosing;
    private Component closeComponent;

    public Gui(Plugin plugin, String name, int linesNumber, boolean preventClosing){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.inventory = Bukkit.createInventory(null, linesNumber * 9, name);
        this.components = new HashMap<>();
        this.preventClosing = preventClosing;
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
        this.openInventory(player, true);
    }

    public void openInventory(Player player, boolean refresh){
        if(refresh)
            this.refresh();
        player.openInventory(this.inventory);
    }

    public void setCloseComponent(int slot, Component component){
        this.closeComponent = component;
        this.setComponent(slot, component);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory() == this.inventory){
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem();
            if(clickedItem != null){
                for(Component component: this.components.values()){
                    if(clickedItem.isSimilar(component.getItemComponent())){
                        if(component instanceof ClickableComponent){
                            ((ClickableComponent) component).onClick(e);
                        }else if(clickedItem.isSimilar(this.closeComponent.getItemComponent())){
                            e.getWhoClicked().closeInventory();
                        }
                    }
                }
            }
        }
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
