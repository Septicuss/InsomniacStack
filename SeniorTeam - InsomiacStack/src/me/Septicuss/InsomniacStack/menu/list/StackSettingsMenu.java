package me.Septicuss.InsomniacStack.menu.list;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Septicuss.InsomniacStack.files.ConfigValues;
import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.input.InputHandler;
import me.Septicuss.InsomniacStack.input.InputHandler.InputType;
import me.Septicuss.InsomniacStack.input.InputHandler.SettingType;
import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;

public class StackSettingsMenu extends Menu {

	public StackSettingsMenu() {
		super("Stack Settings", 6);
		setUpdating(true);
	}

	@Override
	public void loadIcons(Player player) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);

		final String stackName = CONFIG.getString("settings.stack.name");
		final Integer rangeX = CONFIG.getInt("settings.stack.range.x");
		final Integer rangeY = CONFIG.getInt("settings.stack.range.y");
		final Integer rangeZ = CONFIG.getInt("settings.stack.range.z");
		final Integer maxStackSize = CONFIG.getInt("settings.stack.max");

		final List<String> blacklistedMobs = CONFIG.getStringList("settings.stack.blacklist");
		final List<String> blacklistedWorlds = CONFIG.getStringList("settings.stack.world-blacklist");

		final boolean stackTamed = CONFIG.getBoolean("settings.stack.tamed");
		final boolean differentAge = CONFIG.getBoolean("settings.stack.different-age");
		final boolean killAll = CONFIG.getBoolean("settings.stack.kill-all");

		getInventory().setItem(10, new SimpleItemMaker(Material.PURPLE_DYE, "§eMob Stack Name",
				"§7Determines the name of a mob stack.", " ", "§7Current : " + c(stackName)).get());

		getInventory().setItem(11, new SimpleItemMaker(Material.PURPLE_DYE, "§eSearch Range X",
				"§7Determines the stack searching range", "§7on §fX axis", " ", "§7Current : §e" + rangeX).get());
		getInventory().setItem(12, new SimpleItemMaker(Material.PURPLE_DYE, "§eSearch Range Y",
				"§7Determines the stack searching range", "§7on §fY axis", " ", "§7Current : §e" + rangeY).get());
		getInventory().setItem(13, new SimpleItemMaker(Material.PURPLE_DYE, "§eSearch Range Z",
				"§7Determines the stack searching range", "§7on §fZ axis", " ", "§7Current : §e" + rangeZ).get());
		getInventory().setItem(14, new SimpleItemMaker(Material.PURPLE_DYE, "§eMax Stack Size",
				"§7What should the max stack size be?", " ", "§7Current : §e" + maxStackSize).get());
		getInventory().setItem(15,
				new SimpleItemMaker(Material.PURPLE_DYE, "§eBlacklisted Mobs",
						"§7Which mobs should not be able to stack?", " ", "§7Current : ")
								.addList("§f- %item%", blacklistedMobs).get());
		getInventory().setItem(16,
				new SimpleItemMaker(Material.PURPLE_DYE, "§eBlacklisted Worlds",
						"§7In which worlds should entities not stack in?", " ", "§7Current : ")
								.addList("§f- %item%", blacklistedWorlds).get());
		getInventory().setItem(19,
				new SimpleItemMaker(b(stackTamed), "§eStack Tamed", "§7Should tamed mobs be stacked?").get());
		getInventory().setItem(20,
				new SimpleItemMaker(b(differentAge), "§eStack Different Age", "§7Should mobs with different age stack?")
						.get());
		getInventory().setItem(21, new SimpleItemMaker(b(killAll), "§eKill All",
				"§7Should all entities in a stack be killed", "§7once the entity dies?").get());

//
//		CONFIG.addDefault("settings.drops.enabled", true);
//		CONFIG.addDefault("settings.drops.multiply", true);
//		CONFIG.addDefault("settings.drops.zombie.0.chance", 60);
//		CONFIG.addDefault("settings.drops.zombie.0.items", 2);
//
//		CONFIG.addDefault("settings.heads.zombie", 5);
//
//				
	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		event.setCancelled(true);

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final int slot = event.getRawSlot();

		switch (slot) {

		case 10:
			InputHandler.inputSetting(player, InputType.TEXT, SettingType.STACK, "settings.stack.name");
			break;
		case 11:
			InputHandler.inputSetting(player, InputType.NUMBER, SettingType.STACK, "settings.stack.range.x");
			break;
		case 12:
			InputHandler.inputSetting(player, InputType.NUMBER, SettingType.STACK, "settings.stack.range.y");
			break;
		case 13:
			InputHandler.inputSetting(player, InputType.NUMBER, SettingType.STACK, "settings.stack.range.z");
			break;
		case 14:
			InputHandler.inputSetting(player, InputType.NUMBER, SettingType.STACK, "settings.stack.max");
			break;
		case 15:

			if (event.getClick() == ClickType.LEFT) {
				InputHandler.inputSetting(player, InputType.LIST, SettingType.STACK, "settings.stack.blacklist");
			}

			if (event.getClick() == ClickType.RIGHT) {

				List<String> list = CONFIG.getStringList("settings.stack.blacklist");

				if (list.size() == 0) {
					return;
				}

				list.remove(list.get(list.size() - 1));
				CONFIG.set("settings.stack.blacklist", list);
				Files.saveConfig(FileType.CONFIG, CONFIG);
			}

			break;
		case 16:

			if (event.getClick() == ClickType.LEFT) {
				InputHandler.inputSetting(player, InputType.LIST, SettingType.STACK, "settings.stack.world-blacklist");
			}

			if (event.getClick() == ClickType.RIGHT) {

				List<String> list = CONFIG.getStringList("settings.stack.world-blacklist");

				if (list.size() == 0) {
					return;
				}

				list.remove(list.get(list.size() - 1));
				CONFIG.set("settings.stack.world-blacklist", list);
				Files.saveConfig(FileType.CONFIG, CONFIG);
			}
			break;
		case 19:
			ConfigValues.setValue("settings.stack.tamed", !CONFIG.getBoolean("settings.stack.tamed"));
			return;
		case 20:
			ConfigValues.setValue("settings.stack.different-age", !CONFIG.getBoolean("settings.stack.different-age"));
			return;
		case 21:
			ConfigValues.setValue("settings.stack.kill-all", !CONFIG.getBoolean("settings.stack.kill-all"));
			return;

		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {

		new MainSettingsMenu().openMenu(player);

	}

	private String c(String arg0) {
		return ChatColor.translateAlternateColorCodes('&', arg0);
	}

	private Material b(boolean bool) {
		return (bool ? Material.LIME_DYE : Material.GRAY_DYE);
	}

}
