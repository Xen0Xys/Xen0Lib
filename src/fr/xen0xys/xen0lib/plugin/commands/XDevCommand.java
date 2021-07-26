package fr.xen0xys.xen0lib.plugin.commands;

import fr.xen0xys.xen0lib.gui.anvilgui.IAnvilGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XDevCommand implements CommandExecutor, IAnvilGui {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player player){
            // GUI test
            /**
             AnvilGui gui = new AnvilGui(Xen0Lib.getInstance(), "Test", false);
             gui.openInventory(player);
             */
        }
        return false;
    }

    @Override
    public void onTextChange() {

    }

    @Override
    public void onTextConfirm() {

    }
}
