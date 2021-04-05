package me.Septicuss.InsomniacStack.menu.list;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;

public class MainSettingsMenu extends Menu {

	public MainSettingsMenu() {
		super("InsomniacStack Settings", 3);
	}

	@Override
	public void loadIcons(Player player) {

		getInventory().setItem(12, new SimpleItemMaker(Material.NAME_TAG, "&eStack Settings",
				"§7Click to edit settings", "§7related to mob stacks.").get());

		getInventory().setItem(13, new SimpleItemMaker(Material.ROTTEN_FLESH, "§eDrops Settings",
				"§7Click to edit settings", "§7related to mob drops.").get());

		getInventory()
				.setItem(14,
						new SimpleItemMaker(Material.GOLD_NUGGET, "§eUpgrades Settings", "§7Click to edit settings",
								"§7related to upgrades.", " ", "§cThese have to be edited", "§cin the §econfig.yml§c.")
										.get());

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		event.setCancelled(true);

		if (event.getRawSlot() == 12) {
			new StackSettingsMenu().openMenu(player);
		}

		if (event.getRawSlot() == 13) {
			new DropSettingsMenu().openMenu(player);
		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {

	}

}
