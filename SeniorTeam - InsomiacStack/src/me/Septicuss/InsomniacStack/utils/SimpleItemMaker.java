package me.Septicuss.InsomniacStack.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SimpleItemMaker {

	ItemStack item;

	public SimpleItemMaker(Material material, String name, String... lore) {
		ItemStack i = new ItemStack(material);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

		if (lore[0] != "") {
			im.setLore(Arrays.asList(lore));
		}

		im.addItemFlags(ItemFlag.values());
		i.setItemMeta(im);
		item = i;
	}

	/**
	 * 
	 * Format : %item%
	 * 
	 * @param format
	 */
	public SimpleItemMaker addList(String format, List<?> list) {
		ItemMeta im = item.getItemMeta();

		List<String> lore = im.getLore();

		for (Object object : list) {
			lore.add(ChatColor.translateAlternateColorCodes('&',
					format.replaceAll("%item%", object.toString().toUpperCase())));
		}

		im.setLore(lore);
		item.setItemMeta(im);

		return this;
	}

	public ItemStack get() {
		return item;
	}

	public static void addLoreLine(ItemStack item, String line) {

		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = new ArrayList<>();

		if (itemMeta.hasLore())
			lore = itemMeta.getLore();

		lore.add(ChatColor.translateAlternateColorCodes('&', line));

		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}

	public static void removeLastLoreLine(ItemStack item) {
		ItemMeta itemMeta = item.getItemMeta();

		if (!itemMeta.hasLore())
			return;

		List<String> lore = itemMeta.getLore();
		lore.remove(lore.size() - 1);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}

	public static String getLastLoreLine(ItemStack item) {
		return getLoreLine(item, item.getItemMeta().getLore().size() - 1);
	}

	public static String getLoreLine(ItemStack item, Integer index) {
		return item.getItemMeta().getLore().get(index);
	}

	public static void setDisplayName(ItemStack item, String name) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(itemMeta);
	}

}
