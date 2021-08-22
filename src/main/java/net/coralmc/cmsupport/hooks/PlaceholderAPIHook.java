package net.coralmc.cmsupport.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.coralmc.cmsupport.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return Main.plugin.getPlugin().getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return Main.plugin.getPlugin().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return Main.plugin.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return super.onPlaceholderRequest(player, params);
    }
}
