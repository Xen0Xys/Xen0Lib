package fr.xen0xys.xen0lib.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickableComponent extends Component{

    private final Clickable clickableObject;

    public ClickableComponent(Material material, int count, String name, String[] description, Clickable clickableObject) {
        super(material, count, name, description);
        this.clickableObject = clickableObject;
    }

    public void onClick(InventoryClickEvent e){
        this.clickableObject.onClick(e);
    }

}
