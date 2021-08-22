package net.coralmc.cmsupport.menus;

import net.coralmc.cmsupport.Main;
import org.bukkit.entity.Player;
import xyz.theprogramsrc.supercoreapi.spigot.gui.Gui;
import xyz.theprogramsrc.supercoreapi.spigot.gui.objets.*;

import java.util.ArrayList;
import java.util.List;

public class SupportGUI extends Gui {
    private List<Button> buttons;


    public SupportGUI(Player player) {
        super(player);
        buttons = new ArrayList<>();

        for (String s : Main.plugin.getMenuYML().getSection("extraItems").getKeys(false)){
            buttons.add(new Button(Main.plugin.getMenuYML().getSection("extraItems."+s), player));
        }
    }

    @Override
    public GuiTitle getTitle() {
        return GuiTitle.of(Main.plugin.getMenuYML().getString("settings.rows"));
    }

    @Override
    public GuiRows getRows() {
        return GuiRows.valueOf(Main.plugin.getMenuYML().getString("settings.rows"));
    }

    @Override
    public void onBuild(GuiModel guiModel) {
        for (Button b: buttons){
            guiModel.setButton(b.getSlot(), b.getGUIEntry());
        }
    }
}
