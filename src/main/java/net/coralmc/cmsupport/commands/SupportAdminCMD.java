package net.coralmc.cmsupport.commands;

import net.coralmc.cmsupport.Main;
import net.coralmc.cmsupport.languages.LBase;
import net.coralmc.cmsupport.storage.Partner;
import net.coralmc.cmsupport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.theprogramsrc.supercoreapi.spigot.commands.CommandResult;
import xyz.theprogramsrc.supercoreapi.spigot.commands.SpigotCommand;
import xyz.theprogramsrc.supercoreapi.spigot.utils.SpigotConsole;

import java.util.Date;

public class SupportAdminCMD extends SpigotCommand {
    @Override
    public String getCommand() {
        return Main.plugin.getSettingsStorage().getConfig().getString("Settings.SupportAdminCommand").toLowerCase();
    }

    @Override
    public String getPermission() {
        return "cmsupport.admin";
    }

    @Override
    public CommandResult onPlayerExecute(Player player, String[] strings) {

        if (strings.length == 1){
            if (strings[0].equalsIgnoreCase("reload")){
                Main.plugin.getSettingsStorage().getConfig().save();
                Main.plugin.reloadConfig();
                Main.plugin.getSuperUtils().sendMessage(player, "&aConfiguration files reloaded successfully!");
                return CommandResult.COMPLETED;
            }
        }

        if (strings.length == 3){
            String arg1 = strings[0].toLowerCase();
            String arg2 = strings[1].toLowerCase();
            String arg3 = strings[2];
            Player t;
            Partner p1;
            if (arg2.equals("forceadd") || arg2.equals("forceremove")) {
                p1 = new Partner(arg3).setResetDate(Utils.getResetDate(new Date()));
            } else {
                t = Bukkit.getPlayer(arg3);
                if (t == null) {
                    Main.plugin.getSuperUtils().sendMessage(player, "&c" + arg3 + " not exist on the server!");
                    return CommandResult.INVALID_ARGS;
                }
                p1 = Main.plugin.getPartnerStorage().get(t.getDisplayName());
            }

            if (p1 == null){
                p1 = new Partner(arg3).setResetDate(Utils.getResetDate(new Date()));
            }

            if (arg1.equals("partner")){
                switch (arg2){
                    case "add":
                    case "forceadd": {
                        if (Main.plugin.getPartnerStorage().exists(arg3)){
                            Main.plugin.getSuperUtils().sendMessage(player, LBase.PARTNER_EXIST.get().translate());
                            return CommandResult.COMPLETED;
                        }
                        Main.plugin.getPartnerStorage().createUser(p1);
                        Main.plugin.getSuperUtils().sendMessage(player, LBase.ADDED_PARTNER.get().translate());
                        return CommandResult.COMPLETED;
                    }
                    case "remove":
                    case "forceremove":{
                        if (!Main.plugin.getPartnerStorage().exists(arg3)){
                            Main.plugin.getSuperUtils().sendMessage(player, LBase.PARTNER_NOT_EXIST.get().translate());
                            return CommandResult.COMPLETED;
                        }
                        Main.plugin.getPartnerStorage().remove(p1);
                        Main.plugin.getSuperUtils().sendMessage(player, LBase.REMOVED_PARTNER.get().translate());
                        return CommandResult.COMPLETED;
                    }
                }
            } else {
                return CommandResult.INVALID_ARGS;
            }
        }
        Main.plugin.getSuperUtils().sendMessage(player, LBase.USE_ADMIN_COMMAND.get().translate());
        return CommandResult.INVALID_ARGS;
    }

    @Override
    public CommandResult onConsoleExecute(SpigotConsole spigotConsole, String[] strings) {

        if (strings.length == 1){
            if (strings[0].equalsIgnoreCase("reload")){
                Main.plugin.getSettingsStorage().getConfig().load();
                Main.plugin.getMenuYML().load();
                Main.plugin.getSuperUtils().sendMessage(spigotConsole.parseConsoleCommandSender(), "&aConfiguration files reloaded successfully!");
                return CommandResult.COMPLETED;
            }
        }
        if (strings.length == 3){
            String arg1 = strings[0].toLowerCase();
            String arg2 = strings[1].toLowerCase();
            String arg3 = strings[2];
            Player t;
            Partner p1;
            if (arg2.equals("forceadd") || arg2.equals("forceremove")) {
                p1 = new Partner(arg3).setResetDate(Utils.getResetDate(new Date()));
            } else {
                t = Bukkit.getPlayer(arg3);
                if (t == null) {
                    Main.plugin.getSuperUtils().sendMessage(spigotConsole.parseConsoleCommandSender(), "&c" + arg3 + " not exist on the server!");
                    return CommandResult.INVALID_ARGS;
                }
                p1 = Main.plugin.getPartnerStorage().get(t.getDisplayName());
            }

            if (arg1.equals("partner")){
                switch (arg2){
                    case "add":
                    case "forceadd": {
                        if (Main.plugin.getPartnerStorage().exists(p1.getUsername())){
                            Main.plugin.getSuperUtils().sendMessage(spigotConsole.parseConsoleCommandSender(), LBase.PARTNER_EXIST.get().translate());
                            return CommandResult.COMPLETED;
                        }
                        Main.plugin.getPartnerStorage().createUser(p1);
                        Main.plugin.getSuperUtils().sendMessage(spigotConsole.parseConsoleCommandSender(), LBase.ADDED_PARTNER.get().translate());
                        return CommandResult.COMPLETED;
                    }
                    case "remove":
                    case "forceremove":{
                        if (!Main.plugin.getPartnerStorage().exists(p1.getUsername())){
                            Main.plugin.getSuperUtils().sendMessage(spigotConsole.parseConsoleCommandSender(), LBase.PARTNER_NOT_EXIST.get().translate());
                            return CommandResult.COMPLETED;
                        }
                        Main.plugin.getPartnerStorage().remove(p1);
                        Main.plugin.getSuperUtils().sendMessage(spigotConsole.parseConsoleCommandSender(), LBase.REMOVED_PARTNER.get().translate());
                        return CommandResult.COMPLETED;
                    }
                }
            } else {
                return CommandResult.INVALID_ARGS;
            }
        }
        Main.plugin.getSuperUtils().sendMessage(spigotConsole.parseConsoleCommandSender(), LBase.USE_ADMIN_COMMAND.get().translate());
        return CommandResult.INVALID_ARGS;
    }
}
