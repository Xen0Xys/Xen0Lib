package fr.xen0xys.xen0lib.plugin.commands;

import fr.xen0xys.xen0lib.gui.anvilgui.AnvilGUI;
import fr.xen0xys.xen0lib.plugin.Xen0Lib;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class XDevCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player player){
            // GUI test
            /**
             AnvilGui gui = new AnvilGui(Xen0Lib.getInstance(), "Test", false);
             gui.openInventory(player);
             */

            AnvilGUI.Builder builder = new AnvilGUI.Builder().plugin(Xen0Lib.getInstance()).itemLeft(new ItemStack(Material.PAPER)).title("Test").text("password");

            builder.onComplete((_player, text) -> {
                if(text.equalsIgnoreCase("test")) {
                    _player.sendMessage("You have magical powers!");
                    return AnvilGUI.Response.close();
                } else {
                    return AnvilGUI.Response.text("Incorrect.");
                }
            });

            builder.open(player);

        }
        return false;
    }
}
