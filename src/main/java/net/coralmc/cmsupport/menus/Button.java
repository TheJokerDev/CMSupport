package net.coralmc.cmsupport.menus;

import me.clip.placeholderapi.PlaceholderAPI;
import net.coralmc.cmsupport.Main;
import net.coralmc.cmsupport.utils.ItemsUtil;
import net.coralmc.cmsupport.utils.SimpleItem;
import net.coralmc.cmsupport.utils.visual.Animation;
import net.coralmc.cmsupport.utils.visual.Color;
import net.coralmc.cmsupport.utils.visual.MinecraftColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.theprogramsrc.supercoreapi.global.utils.Utils;
import xyz.theprogramsrc.supercoreapi.libs.simpleyaml.configuration.ConfigurationSection;
import xyz.theprogramsrc.supercoreapi.spigot.gui.objets.GuiAction;
import xyz.theprogramsrc.supercoreapi.spigot.gui.objets.GuiEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Button {
    private final SimpleItem icon;
    private final boolean hasWaveEffect;
    private final boolean hasWaveColors;
    private int slot;
    private String slots;
    private final List<String> leftclick;
    private final List<String> rightclick;
    private final List<String> shiftclick;
    private final List<String> multiclick;
    private final ConfigurationSection config;
    private final Player player;

    public Button(ConfigurationSection section, Player player){
        config = section;
        this.player = player;
        icon = ItemsUtil.createItem(section, player);
        hasWaveEffect = section.get("waveEffect")!=null && section.getBoolean("waveEffect");
        hasWaveColors = section.get("waveColors")!=null;
        if (section.getString("slot")!=null) {
            try {
                Integer.parseInt(Objects.requireNonNull(section.getString("slot")));
                slot = section.getInt("slot");
            } catch (NumberFormatException e) {
                slots = section.getString("slot");
            }
        }
        leftclick = section.getStringList("actions.leftclick");
        rightclick = section.getStringList("actions.rightclick");
        shiftclick = section.getStringList(".actions.shiftclick");
        multiclick = section.getStringList(".actions.multiclick");
    }

    public Player getPlayer() {
        return player;
    }

    public SimpleItem getIcon() {
        if (hasWaveEffect){
            SimpleItem item = icon.clone();
            Color[] colors;
            if (hasWaveColors) {
                String[] colorsString = Objects.requireNonNull(config.getString("waveColors")).split(",");
                List<Color> colorList = new ArrayList<>();
                Color color = null;
                for (String s : colorsString) {
                    try {
                        color = MinecraftColor.valueOf(s.toUpperCase()).getColor();
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                    if (color != null) {
                        colorList.add(color);
                    }
                }
                if (colorsString.length == 0) {
                    try {
                        color = MinecraftColor.valueOf(Objects.requireNonNull(config.getString("waveColors")).toUpperCase()).getColor();
                    } catch (IllegalArgumentException ignored) {
                    }
                    colors = new Color[]{color};
                } else {
                    colors = colorList.toArray(new Color[0]);
                }
                if (colors.length == 0){
                    colors = new Color[]{ MinecraftColor.WHITE.getColor(), MinecraftColor.GOLD.getColor()};
                }
            } else {
                colors = new Color[]{ MinecraftColor.WHITE.getColor(), MinecraftColor.GOLD.getColor()};
            }
            item.setDisplayName(Animation.wave(item.getDisplayName(), colors));
            return item;
        }
        return icon;
    }

    public int getSlot() {
        return slot;
    }

    public String getSlots() {
        return slots;
    }

    public GuiEntry getGUIEntry(){
        return new GuiEntry(getIcon(), a->player.closeInventory());
    }

    private void getActions(GuiAction obj) {
        GuiAction.ClickType var1 = obj.clickType;
        Player p = player;
        Object actions = config.get("actions");
        boolean hasActions = actions != null;
        if (hasActions) {
            List<String> commands = new ArrayList<>();
            if (var1 == GuiAction.ClickType.LEFT_CLICK) {
                commands.addAll(leftclick);
                commands.addAll(multiclick);
            } else if (var1 == GuiAction.ClickType.RIGHT_CLICK) {
                commands.addAll(rightclick);
                commands.addAll(multiclick);
            } else if (var1 == GuiAction.ClickType.SHIFT_LEFT || var1 == GuiAction.ClickType.SHIFT_RIGHT) {
                commands.addAll(shiftclick);
                commands.addAll(multiclick);
            } else {
                commands.addAll(multiclick);
            }
            for (String command : commands) {
                String string;
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    command = PlaceholderAPI.setPlaceholders(p, command);
                }
                if (command.startsWith("[player]")) {
                    string = command.replace("[player]", "");
                    p.chat(string);
                } else if (command.startsWith("[console]")) {
                    string = command.replace("[console]", "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replaceFirst("/", ""));
                } else if (command.equals("[close]")) {
                    p.closeInventory();
                }
            }
        }
    }
}
