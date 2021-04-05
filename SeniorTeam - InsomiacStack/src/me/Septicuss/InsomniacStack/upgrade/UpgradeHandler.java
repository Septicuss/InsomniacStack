package me.Septicuss.InsomniacStack.upgrade;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;

public class UpgradeHandler {

	public enum UpgradeType {

		DOUBLE_DROPS("doubledrops"), REWARD_UPGRADE("rewards");

		private String name;

		UpgradeType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	public static boolean hasUpgrade(Player player, UpgradeType upgradeType) {

		return (getUpgradeLevel(player, upgradeType) > 0);
	}

	public static Integer getUpgradeLevel(Player player, UpgradeType upgradeType) {
		final FileConfiguration UPGRADES = Files.getConfig(FileType.UPGRADES);
		final String path = "upgrades." + player.getUniqueId() + "." + upgradeType.getName();

		if (!UPGRADES.isSet(path)) {
			return 0;
		}

		return UPGRADES.getInt(path);
	}

	public static void setUpgradeLevel(Player player, UpgradeType upgradeType, Integer level) {
		final FileConfiguration UPGRADES = Files.getConfig(FileType.UPGRADES);
		final String path = "upgrades." + player.getUniqueId() + "." + upgradeType.getName();

		UPGRADES.set(path, level);
		Files.saveConfig(FileType.UPGRADES, UPGRADES);
	}

	public static Integer getUpgradeMaxLevel(UpgradeType upgradeType) {
		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.upgrades." + upgradeType.getName();

		if (!CONFIG.isSet(path)) {
			return 0;
		}

		int level = 1;

		level += CONFIG.getConfigurationSection(path).getKeys(false).size();

		return level;

	}

	public static Double getUpgradeCost(UpgradeType upgradeType, Integer level) {
		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.upgrades." + upgradeType.getName() + "." + level + "." + ".price";

		if (!CONFIG.isSet(path)) {
			return 0D;
		}

		return CONFIG.getDouble(path);
	}

	public static Double getUpgradePercentage(UpgradeType upgradeType, Integer level) {
		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.upgrades." + upgradeType.getName() + "." + level + "." + ".percentage";

		if (!CONFIG.isSet(path)) {
			return 0D;
		}

		return CONFIG.getDouble(path);
	}

}
