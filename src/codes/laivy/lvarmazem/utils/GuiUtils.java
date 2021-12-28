package codes.laivy.lvarmazem.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiUtils {

    public ItemStack createItem(Material material, Integer data, String name, String... lore) {
        return createItem(material, data, name, Arrays.asList(lore));
    }
    public ItemStack createItem(ItemStack item, String name, String... lore) {
        return createItem(item, name, Arrays.asList(lore));
    }

    public ItemStack createItem(Material material, Integer data, String name, List<String> lore) {
        ItemStack is;
        if (data == null) is = new ItemStack(material);
        else is = new ItemStack(material, 1, Short.parseShort(String.valueOf(data)));

        return createItem(is, name, lore);
    }
    public ItemStack createItem(ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        } if (lore != null) {
            ArrayList<String> array = new ArrayList<>();
            for (String str : lore) {
                str = str + " ";
                for (String str2 : str.split("%nl%")) {
                    if (!str2.contains("%noline%")) {
                        array.add(ChatColor.translateAlternateColorCodes('&', str2));
                    }
                }
            }
            meta.setLore(array);
        }
        item.setItemMeta(meta);

        return item;
    }

    public Boolean checkName(InventoryClickEvent e, Inventory inventory, Inventory clickedinventory, boolean contains, String name) {
        if (contains) {
            if (inventory.getName().contains(ChatColor.translateAlternateColorCodes('&', name))) {
                e.setCancelled(true);
                if (clickedinventory != null) {
                    return clickedinventory.getName().contains(ChatColor.translateAlternateColorCodes('&', name));
                }
            }
        } else {
            if (inventory.getName().equals(ChatColor.translateAlternateColorCodes('&', name))) {
                e.setCancelled(true);
                if (clickedinventory != null) {
                    return clickedinventory.getName().equals(ChatColor.translateAlternateColorCodes('&', name));
                }
            }
        }
        return false;
    }

}
