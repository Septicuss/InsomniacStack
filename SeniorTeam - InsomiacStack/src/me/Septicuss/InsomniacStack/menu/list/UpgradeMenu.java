package me.Septicuss.InsomniacStack.menu.list;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.upgrade.UpgradeHandler;
import me.Septicuss.InsomniacStack.upgrade.UpgradeHandler.UpgradeType;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;
import me.Septicuss.InsomniacStack.vault.EconomyHandler;

public class UpgradeMenu extends Menu {

	public UpgradeMenu() {
		super("Mob Stack Upgrades", 3);
	}

	@Override
	public void loadIcons(Player player) {

		final Integer doubleDropsLevel = UpgradeHandler.getUpgradeLevel(player, UpgradeType.DOUBLE_DROPS);
		final Integer rewardsLevel = UpgradeHandler.getUpgradeLevel(player, UpgradeType.REWARD_UPGRADE);

		final Integer doubleDropsMaxLevel = UpgradeHandler.getUpgradeMaxLevel(UpgradeType.DOUBLE_DROPS);
		final Integer rewardsMaxLevel = UpgradeHandler.getUpgradeMaxLevel(UpgradeType.REWARD_UPGRADE);

		final Double doubleDropsCost = UpgradeHandler.getUpgradeCost(UpgradeType.DOUBLE_DROPS, doubleDropsLevel + 1);
		final Double rewardsCost = UpgradeHandler.getUpgradeCost(UpgradeType.REWARD_UPGRADE, rewardsLevel + 1);

		for (int i = 0; i < getInventory().getSize(); i++) {
			getInventory().setItem(i, new SimpleItemMaker(Material.MAGENTA_STAINED_GLASS_PANE, " ", "").get());
		}

		for (int i = 10; i < 17; i++) {
			getInventory().setItem(i, new SimpleItemMaker(Material.GRAY_STAINED_GLASS_PANE, " ", "").get());
		}

		set(12, new SimpleItemMaker(Material.ROTTEN_FLESH, "§d§l§nDouble Drops",
				"§8§m                                    ", " ", "§7The chance to double all drops", "§7from mobs.",
				" ", "§d§lStatus:", "§7Your Level : " + (doubleDropsLevel == 0 ? "§f0" : "§f" + doubleDropsLevel),
				"§7Next Level : " + (doubleDropsLevel >= doubleDropsMaxLevel ? "§cMax Level reached"
						: "§f" + (doubleDropsLevel + 1)),
				" ", "§d§lUpgrade:", "§7Cost: §f$" + doubleDropsCost, " ", "§8§m                                    ")
						.get());

		set(14, new SimpleItemMaker(Material.GOLD_INGOT, "§d§l§nRewards", "§8§m                                    ",
				" ", "§7Increases the chances to", "§7win a reward while killing mobs.", " ", "§d§lStatus:",
				"§7Your Level : " + (rewardsLevel == 0 ? "§f0" : "§f" + rewardsLevel),
				"§7Next Level : "
						+ (rewardsLevel >= rewardsMaxLevel ? "§cMax Level reached" : "§f" + (rewardsLevel + 1)),
				" ", "§d§lUpgrade:", "§7Cost: §f$" + rewardsCost, " ", "§8§m                                    ")
						.get());

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		event.setCancelled(true);

		final Integer slot = event.getRawSlot();

		if (slot == 12) {

			final Integer doubleDropsLevel = UpgradeHandler.getUpgradeLevel(player, UpgradeType.DOUBLE_DROPS);
			final Integer doubleDropsMaxLevel = UpgradeHandler.getUpgradeMaxLevel(UpgradeType.DOUBLE_DROPS);

			if (doubleDropsLevel >= doubleDropsMaxLevel) {
				player.sendMessage("§cMaximum Level reached.");
				return;
			}

			final Double doubleDropsCost = UpgradeHandler.getUpgradeCost(UpgradeType.DOUBLE_DROPS,
					doubleDropsLevel + 1);

			if (!EconomyHandler.has(player, doubleDropsCost)) {
				player.sendMessage("§cNot enough funds.");
				return;
			}

			EconomyHandler.take(player, doubleDropsCost);
			UpgradeHandler.setUpgradeLevel(player, UpgradeType.DOUBLE_DROPS, doubleDropsLevel + 1);
			player.sendMessage("§aUpgrade successful!");

			new UpgradeMenu().openMenu(player);

			return;
		}

		if (slot == 14) {

			final Integer rewardsLevel = UpgradeHandler.getUpgradeLevel(player, UpgradeType.REWARD_UPGRADE);
			final Integer rewardsMaxLevel = UpgradeHandler.getUpgradeMaxLevel(UpgradeType.REWARD_UPGRADE);

			if (rewardsLevel >= rewardsMaxLevel) {
				player.sendMessage("§cMaximum Level reached.");
				return;
			}

			final Double rewardsCost = UpgradeHandler.getUpgradeCost(UpgradeType.REWARD_UPGRADE, rewardsLevel + 1);

			if (!EconomyHandler.has(player, rewardsCost)) {
				player.sendMessage("§cNot enough funds.");
				return;
			}

			EconomyHandler.take(player, rewardsCost);
			UpgradeHandler.setUpgradeLevel(player, UpgradeType.REWARD_UPGRADE, rewardsLevel + 1);
			player.sendMessage("§aUpgrade successful!");

			new UpgradeMenu().openMenu(player);

			return;
		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {
		return;
	}

	private void set(int slot, ItemStack item) {
		getInventory().setItem(slot, item);
	}

}
