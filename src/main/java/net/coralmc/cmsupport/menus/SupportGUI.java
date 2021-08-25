package net.coralmc.cmsupport.menus;

import net.coralmc.cmsupport.Main;
import net.coralmc.cmsupport.languages.LBase;
import net.coralmc.cmsupport.storage.Partner;
import net.coralmc.cmsupport.storage.User;
import net.coralmc.cmsupport.utils.ItemsUtil;
import net.coralmc.cmsupport.utils.SimpleItem;
import net.coralmc.cmsupport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.theprogramsrc.supercoreapi.spigot.gui.Gui;
import xyz.theprogramsrc.supercoreapi.spigot.gui.objets.*;
import xyz.theprogramsrc.supercoreapi.spigot.utils.SpigotUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SupportGUI extends Gui {
    private final List<Button> buttons;
    private final SimpleItem emptyItem;

    public SupportGUI(Player player) {
        super(player);
        buttons = new ArrayList<>();

        for (String s : Main.plugin.getMenuYML().getSection("extraItems").getKeys(false)){
            buttons.add(new Button(Main.plugin.getMenuYML().getSection("extraItems."+s), player));
        }

        emptyItem = ItemsUtil.createItem(Main.plugin.getMenuYML().getSection("items.partner.empty"), player);
    }

    @Override
    public GuiTitle getTitle() {
        return GuiTitle.of(Main.plugin.getMenuYML().getString("settings.title"));
    }

    @Override
    public GuiRows getRows() {
        return GuiRows.valueOf(Main.plugin.getMenuYML().getString("settings.rows").toUpperCase());
    }

    @Override
    public void onBuild(GuiModel guiModel) {
        for (Button b: buttons){
            guiModel.setButton(b.getSlot(), b.getGUIEntry());
        }

        User user = Main.plugin.getUserStorage().get(player.getDisplayName());

        if (Main.plugin.getPartnerStorage().getPartners().length != 0){
            Partner[] partners = Main.plugin.getPartnerStorage().getPartners();
            int i = 10;
            for (Partner partner : partners){
                guiModel.setButton(i, getPartner(user, partner));
                i++;
            }
        } else {
            guiModel.setButton(getCenter(), new GuiEntry(emptyItem.build()));
        }

    }

    private GuiEntry getPartner(User user, Partner partner){
        SimpleItem item;
        boolean isAllowed = true;
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("{partner}", partner.getUsername());
        placeholders.put("{votes}", partner.getVotes()+"");
        placeholders.put("{voteDate}", Utils.getFormattedDate(user.getVotedTime(partner.getUsername())));

        if (user.getVotes().contains(partner.getUsername())) {
            isAllowed = false;
        }

        if (isAllowed){
            item = ItemsUtil.createItem(Main.plugin.getMenuYML().getSection("items.partner.vote"), player, placeholders).clone();
        } else {
            item = ItemsUtil.createItem(Main.plugin.getMenuYML().getSection("items.partner.voted"), player, placeholders).clone();
        }


        boolean finalIsAllowed = isAllowed;
        return new GuiEntry(item.build(), l->executeAction(l, partner, user, finalIsAllowed));
    }

    private void executeAction(GuiAction obj, Partner partner, User user, boolean allowed){
        if (allowed){
            if (user.getVotes()!=null && !Main.allowedMultipleVotes()){
                Main.plugin.getSuperUtils().sendMessage(Bukkit.getPlayer(user.getUsername()), LBase.ALREADY_VOTED.get().translate());
                return;
            }
            user.addPartnerVote(partner, new Date());
            partner.addVote();
            Main.plugin.getSuperUtils().sendMessage(Bukkit.getPlayer(user.getUsername()), LBase.VOTED_SUCCESSFULLY.get().translate().replace("{partner}", partner.getUsername()));
        } else {
            Main.plugin.getSuperUtils().sendMessage(Bukkit.getPlayer(user.getUsername()), LBase.ALREADY_VOTED.get().translate());
        }
    }


    private int getCenter(){
        switch (getRows().name().toLowerCase()){
            case "one":
            case "two": {
                return 4;
            }
            case "three":
            case "four": {
                return 13;
            }
            case "five":
            case "six": {
                return 22;
            }
        }

        return 10;
    }

}
