package me.Septicuss.InsomniacStack.menu.list;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Septicuss.InsomniacStack.files.ConfigValues;
import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;

public class DropSettingsMenu extends Menu {

	public DropSettingsMenu() {
		super("Drop Settings", 6);
		setUpdating(true);
	}

	@Override
	public void loadIcons(Player player) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);

		final boolean dropsEnabled = CONFIG.getBoolean("settings.drops.enabled");
		final boolean dropsMultiply = CONFIG.getBoolean("settings.drops.multiply");
		final boolean rewardsEnabled = CONFIG.getBoolean("settings.rewards.enabled");

		getInventory().setItem(10,
				new SimpleItemMaker(Material.PURPLE_DYE, "§eEdit Drops", "§7Edit drops for different mobs.").get());

		getInventory().setItem(11,
				new SimpleItemMaker(Material.PURPLE_DYE, "§eEdit Rewards", "§7Edit rewards for different mobs.").get());

		getInventory().setItem(12, new SimpleItemMaker(Material.PURPLE_DYE, "§eEdit Head Island Value",
				"§7Edit Island Worth given by mob heads.").get());

		getInventory().setItem(19, new SimpleItemMaker(b(dropsEnabled), "§eCustom drops enabled",
				"§7Should custom drops configured", "§7above be used?").get());
		getInventory().setItem(20, new SimpleItemMaker(b(rewardsEnabled), "§eCustom rewards enabled",
				"§7Should custom rewards configured", "§7above be used?").get());
		getInventory()
				.setItem(21,
						new SimpleItemMaker(b(dropsMultiply), "§eMultiply Drops", "§7Should mob drops be multiplied",
								"§7by the amount of entities in a stack?", "§7(Works only if 'Kill All' is enabled)")
										.get());
	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		event.setCancelled(true);

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final int slot = event.getRawSlot();

		switch (slot) {

		case 10:
			new EditDropsMenu().openMenu(player);
			return;
		case 11:
			new EditRewardsMenu().openMenu(player);
			return;
		case 12:
			new EditHeadValueMenu().openMenu(player);
			return;
		case 19:
			ConfigValues.setValue("settings.drops.enabled", !CONFIG.getBoolean("settings.drops.enabled"));
			return;
		case 20:
			ConfigValues.setValue("settings.rewards.enabled", !CONFIG.getBoolean("settings.rewards.enabled"));
			return;
		case 21:
			ConfigValues.setValue("settings.drops.multiply", !CONFIG.getBoolean("settings.drops.multiply"));
			return;

		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {
		new MainSettingsMenu().openMenu(player);
	}

	private Material b(boolean bool) {
		return (bool ? Material.LIME_DYE : Material.GRAY_DYE);
	}

}
