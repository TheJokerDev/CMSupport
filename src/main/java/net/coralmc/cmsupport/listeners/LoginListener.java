package net.coralmc.cmsupport.listeners;

import net.coralmc.cmsupport.Main;
import net.coralmc.cmsupport.storage.PartnerStorage;
import net.coralmc.cmsupport.storage.User;
import net.coralmc.cmsupport.storage.UserStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LoginListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerJoinEvent event){
        String username = event.getPlayer().getName();
        UserStorage userStorage = Main.plugin.getUserStorage();

        User user;
        if (!userStorage.exists(username)){
            user = new User(username).loadVotes("");
            userStorage.save(user);
        } else {
            user = userStorage.get(username);
            user.getUsername();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Main.plugin.getPartnerStorage().requestPartners(true);
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        String name = p.getName();

        Main.plugin.getUserStorage().removeCache(name);
    }
}
