package me.Septicuss.InsomniacStack.drops;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.utils.Utils;

public class HeadHandler {

	public static Integer getHeadValue(String mobName) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.heads." + mobName.toLowerCase().replaceAll(" ", "_");

		if (!CONFIG.isSet(path)) {
			return null;
		}

		return CONFIG.getInt(path);

	}

	public static void setHeadValue(String mobName, Integer value) {
		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.heads." + mobName.toLowerCase().replaceAll(" ", "_");

		CONFIG.set(path, value);
		Files.saveConfig(FileType.CONFIG, CONFIG);
	}

	public static void removeHeadValue(String mobName) {
		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final String path = "settings.heads." + mobName.toLowerCase().replaceAll(" ", "_");

		CONFIG.set(path, null);
		Files.saveConfig(FileType.CONFIG, CONFIG);
	}

	public static HashMap<String, Integer> getHeadValues() {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);

		if (!CONFIG.isSet("settings.heads")) {
			return null;
		}

		HashMap<String, Integer> headValues = new HashMap<>();

		for (String path : CONFIG.getConfigurationSection("settings.heads").getKeys(false)) {
			final String mobName = path.toLowerCase();
			final Integer value = CONFIG.getInt("settings.heads." + mobName);

			headValues.put(mobName, value);
		}

		return headValues;

	}

	@SuppressWarnings("deprecation")
	public static String getEntityName(ItemStack head) {

		if (head == null || head.getType() != Material.PLAYER_HEAD) {
			return null;
		}

		SkullMeta itemMeta = (SkullMeta) head.getItemMeta();

		if (itemMeta.getOwner() == null) {
			return null;
		}

		String owner = itemMeta.getOwner().toLowerCase().replaceAll("mhf_", "");

		switch (owner) {

		case "blaze":
			owner = "blaze";
			break;
		case "golem":
			owner = "iron_golem";
			break;
		case "cavespider":
			owner = "cave_spider";
			break;
		case "chicken":
			owner = "chicken";
			break;
		case "cow":
			owner = "cow";
			break;
		case "creeper":
			owner = "creeper";
			break;
		case "enderman":
			owner = "enderman";
			break;
		case "ghast":
			owner = "ghast";
			break;
		case "lavaslime":
			owner = "magmacube";
			break;
		case "mushroomcow":
			owner = "mooshroom";
			break;
		case "ocelot":
			owner = "ocelot";
			break;
		case "pig":
			owner = "pig";
			break;
		case "pigzombie":
			owner = "zombie_pigman";
			break;
		case "sheep":
			owner = "sheep";
			break;
		case "skeleton":
			owner = "skeleton";
			break;
		case "slime":
			owner = "slime";
			break;
		case "spider":
			owner = "spider";
			break;
		case "squid":
			owner = "squid";
			break;
		case "villager":
			owner = "villager";
			break;
		case "wskeleton":
			owner = "wither_skeleton";
			break;
		case "zombie":
			owner = "zombie";
			break;
		}

		return owner;

	}

	@SuppressWarnings("deprecation")
	public static ItemStack getMobHead(String entityName) {

		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

		String owner = "MHF_Alex";

		switch (entityName.toLowerCase()) {

		case "blaze":
			owner = "MHF_Blaze";
			break;
		case "iron_golem":
			owner = "MHF_Golem";
			break;
		case "cave_spider":
			owner = "MHF_CaveSpider";
			break;
		case "chicken":
			owner = "MHF_Chicken";
			break;
		case "cow":
			owner = "MHF_Cow";
			break;
		case "creeper":
			owner = "MHF_Creeper";
			break;
		case "enderman":
			owner = "MHF_Enderman";
			break;
		case "ghast":
			owner = "MHF_Ghast";
			break;
		case "magma_cube":
			owner = "MHF_LavaSlime";
			break;
		case "mooshroom":
			owner = "MHF_MushroomCow";
			break;
		case "ocelot":
			owner = "MHF_Ocelot";
			break;
		case "pig":
			owner = "MHF_Pig";
			break;
		case "zombie_pigman":
			owner = "MHF_PigZombie";
			break;
		case "sheep":
			owner = "MHF_Sheep";
			break;
		case "skeleton":
			owner = "MHF_Skeleton";
			break;
		case "slime":
			owner = "MHF_Slime";
			break;
		case "spider":
			owner = "MHF_Spider";
			break;
		case "squid":
			owner = "MHF_Squid";
			break;
		case "villager":
			owner = "MHF_Villager";
			break;
		case "wither_skeleton":
			owner = "MHF_WSkeleton";
			break;
		case "zombie":
			owner = "MHF_Zombie";
			break;
		}

		itemMeta.setOwner(owner);

		final String entityNameFormatted = Utils.capitalizeFirstLetters(entityName.replaceAll("_", " "));

		itemMeta.setDisplayName("§e" + entityNameFormatted + "'s head");
		item.setItemMeta(itemMeta);

		return item;

	}

}
