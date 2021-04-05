package me.Septicuss.InsomniacStack.drops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.objects.ChanceItem;
import me.Septicuss.InsomniacStack.objects.RandomCollection;
import me.Septicuss.InsomniacStack.utils.DataUtils;

public class DropHandler {

	public static ItemStack getDrop(String entityName) {

		final Set<ChanceItem> drops = getDrops(entityName);

		if (drops.isEmpty()) {
			return null;
		}

		final RandomCollection<ItemStack> randomDrops = new RandomCollection<>();

		drops.forEach(drop -> {
			randomDrops.add(drop.getChance(), drop.getItem());
		});

		if (randomDrops == null || randomDrops.next() == null) {
			return null;
		}

		randomDrops.setFullPercentage(true);

		return randomDrops.next();

	}

	public static Set<ChanceItem> getDrops(String entityName) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.drops." + entityName.toLowerCase();

		if (!CONFIG.isSet(path)) {
			return null;
		}

		final Set<ChanceItem> drops = new HashSet<>();

		for (String data : CONFIG.getStringList(path)) {
			final ChanceItem chanceItem = DataUtils.deserializeChanceItem(data);
			drops.add(chanceItem);
		}

		return drops;
	}

	public static void setDrops(String entityName, Set<ChanceItem> drops) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.drops." + entityName.toLowerCase();

		List<String> dataString = new ArrayList<>();

		for (ChanceItem drop : drops) {
			dataString.add(DataUtils.serializeChanceItem(drop));
		}

		CONFIG.set(path, dataString);
		Files.saveConfig(FileType.CONFIG, CONFIG);
	}

	public static void createNewMobDrop(String entityName) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.drops." + entityName.toLowerCase();

		List<String> dataString = new ArrayList<>();

		ItemStack mobHead = HeadHandler.getMobHead(entityName);
		Double dropChance = 50D;

		ChanceItem chanceItem = new ChanceItem(mobHead, dropChance);
		dataString.add(DataUtils.serializeChanceItem(chanceItem));

		CONFIG.set(path, dataString);
		Files.saveConfig(FileType.CONFIG, CONFIG);

	}

	public static void removeMobDrop(String entityName) {
		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.drops." + entityName.toLowerCase();
		CONFIG.set(path, null);
		Files.saveConfig(FileType.CONFIG, CONFIG);
	}

	public static HashMap<String, Set<ChanceItem>> getMobDrops() {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.drops";

		final HashMap<String, Set<ChanceItem>> mobDrops = new HashMap<>();

		if (!CONFIG.isSet(path)) {
			return null;
		}

		for (String entityName : CONFIG.getConfigurationSection(path).getKeys(false)) {

			if (entityName.equalsIgnoreCase("multiply") || entityName.equalsIgnoreCase("enabled")) {
				continue;
			}

			final Set<ChanceItem> drops = getDrops(entityName);
			mobDrops.put(entityName, drops);

		}

		return mobDrops;
	}

	public static boolean hasDrops(String entityName) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.drops." + entityName.toLowerCase();

		if (!CONFIG.isSet(path)) {
			return false;
		}

		return true;

	}

}
