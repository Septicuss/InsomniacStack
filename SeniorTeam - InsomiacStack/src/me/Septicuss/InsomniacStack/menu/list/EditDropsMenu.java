package me.Septicuss.InsomniacStack.menu.list;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.drops.DropHandler;
import me.Septicuss.InsomniacStack.drops.HeadHandler;
import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.objects.ChanceItem;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;

public class EditDropsMenu extends Menu {

	public EditDropsMenu() {
		super("Edit Drops", 6);
	}

	@Override
	public void loadIcons(Player player) {

		getInventory().setItem(4, new SimpleItemMaker(Material.EGG, "§eAdd a new Mob", "§7Click this item with a spawn",
				"§7egg to add a new mob.").get());

		final HashMap<String, Set<ChanceItem>> mobDrops = DropHandler.getMobDrops();
		final int startingSlot = 9;

		int slot = startingSlot;

		if (mobDrops != null) {
			for (String mobName : mobDrops.keySet()) {

				final String mobNameFormatted = (mobName.substring(0, 1).toUpperCase() + mobName.substring(1))
						.replaceAll("_", " ");

				ItemStack mobHead = HeadHandler.getMobHead(mobName);
				SimpleItemMaker.setDisplayName(mobHead, "§e" + mobNameFormatted);
				SimpleItemMaker.addLoreLine(mobHead, "§7Left-Click to edit " + mobNameFormatted + ".");
				SimpleItemMaker.addLoreLine(mobHead, "§7Right-Click to remove.");
				SimpleItemMaker.addLoreLine(mobHead, " ");
				SimpleItemMaker.addLoreLine(mobHead, "§7Drops : §f" + mobDrops.get(mobName).size());

				getInventory().setItem(slot, mobHead);
				slot += 1;
			}

		}
	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		if (event.getClickedInventory().equals(event.getView().getTopInventory())) {
			event.setCancelled(true);
		}

		final int slot = event.getRawSlot();

		if (slot == 4) {

			if (event.getCurrentItem() == null || event.getCursor() == null) {
				return;
			}

			final ItemStack cursor = event.getCursor();

			if (!cursor.getType().toString().contains("SPAWN_EGG") && cursor.getType() != Material.IRON_INGOT) {
				return;
			}

			String entityName = event.getCursor().getType().toString().replaceAll("_SPAWN_EGG", "").toLowerCase();

			if (cursor.getType() == Material.IRON_INGOT) {
				entityName = "iron_golem";
			}

			DropHandler.createNewMobDrop(entityName);
			updateInventory(player);
			return;
		}

		if (!event.getClickedInventory().equals(event.getView().getTopInventory())) {
			return;
		}

		if (event.getCurrentItem() == null) {
			return;
		}

		final ItemStack item = event.getCurrentItem();

		if (item.getType() != Material.PLAYER_HEAD) {
			return;
		}

		final String mobName = item.getItemMeta().getDisplayName().replaceAll(" ", "_");

		if (event.getClick() == ClickType.RIGHT) {
			DropHandler.removeMobDrop(ChatColor.stripColor(mobName));
			updateInventory(player);
			return;
		}

		new EditMobDropsMenu(ChatColor.stripColor(mobName)).openMenu(player);

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {
		new DropSettingsMenu().openMenu(player);
	}

}
