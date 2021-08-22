package net.coralmc.cmsupport.utils;

import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.theprogramsrc.supercoreapi.global.utils.Utils;
import xyz.theprogramsrc.supercoreapi.libs.xseries.XEnchantment;
import xyz.theprogramsrc.supercoreapi.libs.xseries.XMaterial;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleItem {
    private XMaterial material;
    private int amount = 1;
    private List<String> lore;
    private String displayName;
    private String skinTexture;
    private short data = 0;
    private HashMap<XEnchantment, Integer> enchantments;
    private List<ItemFlag> flags;
    private final HashMap<String, String> placeholders;
    private Color color;
    private ItemStack item = null;
    private boolean hideFlags = false;

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public FireworkEffectMeta getFireworkEffectMeta() {
        return fireworkEffectMeta;
    }
    public SimpleItem setColor(Color var1) {
        if (material.name().contains("LEATHER_")) {
            this.color = var1;
        }

        return this;
    }

    public SimpleItem clone() {
        SimpleItem var1 = new SimpleItem(material);
        if (item != null){
            var1.setItem(item);
        }
        var1.setDisplayName(displayName);
        var1.setLore(lore);
        var1.setSkin(skinTexture);

        for (Map.Entry<XEnchantment, Integer> enchantmentIntegerEntry : enchantments.entrySet()) {
            Map.Entry var3 = enchantmentIntegerEntry;
            var1.addEnchantment(((XEnchantment) var3.getKey()), (Integer) var3.getValue());
        }

        var1.setColor(this.color);
        return var1;
    }

    public void setFireworkEffectMeta(FireworkEffectMeta fireworkEffectMeta) {
        this.fireworkEffectMeta = fireworkEffectMeta;
    }

    private FireworkEffectMeta fireworkEffectMeta = null;

    public SimpleItem(ItemStack itemStack) {
        this.lore = new ArrayList();
        this.enchantments = new HashMap();
        this.flags = new ArrayList();
        this.material = XMaterial.matchXMaterial(itemStack);
        this.amount = itemStack.getAmount();
        this.data = itemStack.getDurability();
        if (itemStack.getItemMeta() != null && itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta)meta;
                skinTexture = skullMeta.getOwner();
            }
            this.lore = meta.getLore();
            this.displayName = meta.getDisplayName();
            this.flags = new ArrayList(meta.getItemFlags());
        }

        this.enchantments = new HashMap(itemStack.getEnchantments());
        this.placeholders = new HashMap();
    }

    public SimpleItem(Boolean skull, String skinTexture) {
        this.material = XMaterial.PLAYER_HEAD;
        this.data = 3;
        this.skinTexture = skinTexture;
        this.enchantments = new HashMap();
        this.flags = new ArrayList();
        this.lore = new ArrayList();
        this.placeholders = new HashMap();
    }

    public SimpleItem(XMaterial material) {
        this.material = material;
        this.enchantments = new HashMap<>();
        this.flags = new ArrayList();
        this.lore = new ArrayList();
        this.placeholders = new HashMap();
    }

    public SimpleItem(String displayName) {
        this.material = XMaterial.STONE;
        this.displayName = displayName;
        this.enchantments = new HashMap();
        this.flags = new ArrayList();
        this.lore = new ArrayList();
        this.placeholders = new HashMap();
    }

    public SimpleItem addPlaceholder(String key, String value) {
        this.placeholders.put(key, value);
        return this;
    }

    public SimpleItem removePlaceholder(String key) {
        this.placeholders.remove(key);
        return this;
    }

    public SimpleItem setMaterial(XMaterial material) {
        this.material = material;
        return this;
    }

    public SimpleItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public SimpleItem setLore(List<String> lore) {
        this.lore = new ArrayList(lore);
        return this;
    }

    public SimpleItem setLore(String... lore) {
        this.lore = toList(lore);
        return this;
    }

    public SimpleItem setFirstLoreLine(String text) {
        List<String> lore = new ArrayList();
        lore.add(text);
        lore.addAll(this.lore);
        this.lore = lore;
        return this;
    }

    public SimpleItem addLoreLine(String text) {
        this.lore.add(text);
        return this;
    }

    public SimpleItem setDurability(int i){
        this.data = (short)i;
        return this;
    }

    public SimpleItem addLoreLines(String... lore) {
        this.lore.addAll(toList(lore));
        return this;
    }

    public SimpleItem setLoreLine(int index, String text) {
        if (index < 0) {
            index = this.lore.size();
        }

        this.lore.set(index, text);
        return this;
    }

    public SimpleItem removeLastLoreLine() {
        this.lore.remove(this.lore.size() - 1);
        return this;
    }

    public SimpleItem removeLoreLine(int index) {
        if (index >= this.lore.size()) {
            index = this.lore.size() - 1;
        }

        if (index < 0) {
            index = 0;
        }

        this.lore.remove(index);
        return this;
    }

    public SimpleItem setSkin(String skin) {
        this.skinTexture = skin;
        return this;
    }

    public SimpleItem addEnchantment(XEnchantment enchantment) {
        return this.addEnchantment(enchantment, 1);
    }

    public SimpleItem addEnchantment(XEnchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public SimpleItem removeEnchantment(XEnchantment enchantment) {
        this.enchantments.remove(enchantment);
        return this;
    }

    public SimpleItem addFlag(ItemFlag flag) {
        this.flags.add(flag);
        return this;
    }

    public SimpleItem addFlags(ItemFlag... flags) {
        this.flags.addAll(toList(flags));
        return this;
    }

    public static <T> List<T> toList(T... array) {
        List<T> list = new ArrayList();
        Collections.addAll(list, array);
        return list;
    }

    public SimpleItem removeFlag(ItemFlag flag) {
        this.flags.remove(flag);
        return this;
    }

    public SimpleItem setShowEnchantments(boolean showEnchantments) {
        ItemFlag flag = ItemFlag.HIDE_ENCHANTS;
        if (showEnchantments) {
            this.flags.remove(flag);
        } else {
            this.flags.add(flag);
        }

        return this;
    }

    public SimpleItem setGlowing(boolean glowing) {
        ItemFlag flag = ItemFlag.HIDE_ENCHANTS;
        if (glowing) {
            this.addFlag(flag);
            this.addEnchantment(XEnchantment.DURABILITY);
        } else {
            this.removeFlag(flag);
            this.removeEnchantment(XEnchantment.DURABILITY);
        }

        return this;
    }

    public SimpleItem setShowAttributes(boolean showAttributes) {
        ItemFlag flag = ItemFlag.HIDE_ATTRIBUTES;
        if (showAttributes) {
            this.flags.remove(flag);
        } else {
            this.flags.add(flag);
        }

        return this;
    }

    public SimpleItem setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    public SimpleItem setEnchantments(HashMap<XEnchantment, Integer> enchantments) {
        this.enchantments = new HashMap(enchantments);
        return this;
    }

    public SimpleItem setFlags(List<ItemFlag> flags) {
        this.flags = new ArrayList(flags);
        return this;
    }

    public XMaterial getMaterial() {
        return this.material;
    }

    public int getAmount() {
        return this.amount;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public short getDurability(){
        return this.data;
    }

    public String getSkinTexture() {
        return this.skinTexture;
    }

    public HashMap<XEnchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    public List<ItemFlag> getFlags() {
        return this.flags;
    }

    public boolean hasEnchantments() {
        return this.getEnchantments().size() != 0;
    }

    public boolean hasFlags() {
        return this.getFlags().size() != 0;
    }

    public boolean hasSkin() {
        return this.getSkinTexture() != null;
    }

    public SimpleItem duplicate() {
        return (new SimpleItem(this.getMaterial())).setAmount(this.getAmount()).setLore(this.getLore()).setDisplayName(this.getDisplayName()).setSkin(this.getSkinTexture()).setEnchantments(this.getEnchantments()).setFlags(this.getFlags());
    }

    public ItemStack build() {
        if (getDisplayName() == null) {
            if (getMaterial() != null) {
                setDisplayName(this.displayName);
            } else {
                setDisplayName("&cDisplayName is null!. You can also set just '& 7'");
            }
        }
        if (item == null){
            item = new ItemStack(material.parseMaterial(), 1);
        }
        Object meta = item.getItemMeta();
        if (meta instanceof LeatherArmorMeta && color != null) {
            LeatherArmorMeta LMeta = (LeatherArmorMeta)meta;
            LMeta.setColor(color);
            item.setItemMeta(LMeta);
        }
        if (meta instanceof SkullMeta && skinTexture != null) {
            SkullMeta SMeta = (SkullMeta) meta;
            SMeta.setOwner(skinTexture);
            item.setItemMeta(SMeta);
        }
        if (meta instanceof FireworkEffectMeta && fireworkEffectMeta != null) {
            item.setItemMeta(fireworkEffectMeta);
        }
        meta = item.getItemMeta();
        for (Map.Entry<XEnchantment, Integer> enchantmentSet : getEnchantments().entrySet()){
            item.addUnsafeEnchantment(enchantmentSet.getKey().parseEnchantment(), enchantmentSet.getValue());
        }
        if (meta != null) {
            ((ItemMeta)meta).setDisplayName(apply(Utils.ct(getDisplayName())));
            ((ItemMeta)meta).setLore(apply(Utils.ct(getLore())));
            Object finalMeta = meta;
            getFlags().forEach(((ItemMeta) finalMeta)::addItemFlags);
            if (hideFlags) {
                ((ItemMeta)finalMeta).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
            }
            if (data == 0) {
                item.setDurability(material.getData());
            } else {
                item.setDurability(data);
            }
        }
        item.setItemMeta(((ItemMeta)meta));
        item.setAmount(getAmount());
        return item;
    }

    private String apply(String text) {
        AtomicReference<String> r = new AtomicReference(text);
        this.placeholders.forEach((k, v) -> {
            r.set(r.get().replace(k, v));
        });
        return r.get();
    }

    private List<String> apply(List<String> list) {
        List<String> r = new ArrayList();
        list.forEach((s) -> {
            r.add(this.apply(s));
        });
        return r;
    }

    public boolean hasLore() {
        return this.getLore() != null;
    }

    public boolean hasDisplayName() {
        return this.getDisplayName() != null;
    }

    public boolean hasDurability(){
        return this.getDurability() != 0;
    }

    public void setHideFlags(boolean b) {
        hideFlags = b;
    }
}
