package net.coralmc.cmsupport.commands;

import net.coralmc.cmsupport.Main;
import net.coralmc.cmsupport.menus.SupportGUI;
import org.bukkit.entity.Player;
import xyz.theprogramsrc.supercoreapi.spigot.commands.CommandResult;
import xyz.theprogramsrc.supercoreapi.spigot.commands.SpigotCommand;
import xyz.theprogramsrc.supercoreapi.spigot.utils.SpigotConsole;

public class SupportCMD extends SpigotCommand {
    @Override
    public String getCommand() {
        return Main.plugin.getSettingsStorage().getConfig().getString("Settings.SupportCommand").toLowerCase();
    }

    @Override
    public CommandResult onPlayerExecute(Player player, String[] strings) {
        if (strings.length == 0){
            new SupportGUI(player);
        } else {
            return CommandResult.INVALID_ARGS;
        }
        return CommandResult.COMPLETED;
    }

    @Override
    public CommandResult onConsoleExecute(SpigotConsole spigotConsole, String[] strings) {
        return CommandResult.NO_ACCESS;
    }
}
