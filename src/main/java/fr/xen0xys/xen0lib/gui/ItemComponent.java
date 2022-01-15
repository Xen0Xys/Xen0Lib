package fr.xen0xys.xen0lib.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemComponent {

    private final Material material;
    private final int count;
    private final String name;
    private final String[] description;
    private final boolean isEnchanted;

    public ItemComponent(Material material, int count, String name, String[] description) {
        this.material = material;
        this.count = count;
        this.name = name;
        this.description = description;
        this.isEnchanted = false;
    }

    public ItemComponent(Material material, int count, String name, String[] description, boolean isEnchanted) {
        this.material = material;
        this.count = count;
        this.name = name;
        this.description = description;
        this.isEnchanted = isEnchanted;
    }

    public ItemStack getItemComponent(){
        ItemStack item = new ItemStack(this.material, this.count);
        if(this.isEnchanted){
            item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        }
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            meta.setDisplayName(this.name);
            meta.setLore(Arrays.asList(this.description));
            if(this.isEnchanted){
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
