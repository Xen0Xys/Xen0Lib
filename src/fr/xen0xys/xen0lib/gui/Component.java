package fr.xen0xys.xen0lib.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Component {

    private final Material material;
    private final int count;
    private final String name;
    private final String[] description;

    public Component(Material material, int count, String name, String[] description) {
        this.material = material;
        this.count = count;
        this.name = name;
        this.description = description;
    }

    public ItemStack getItemComponent(){
        ItemStack item = new ItemStack(this.material, this.count);
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            meta.setDisplayName(this.name);
            meta.setLore(Arrays.asList(this.description));
            item.setItemMeta(meta);
        }
        return item;
    }
}
