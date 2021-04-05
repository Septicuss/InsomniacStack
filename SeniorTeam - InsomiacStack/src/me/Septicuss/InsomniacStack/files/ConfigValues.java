package me.Septicuss.InsomniacStack.files;

import org.bukkit.configuration.file.FileConfiguration;

import me.Septicuss.InsomniacStack.files.Files.FileType;

public class ConfigValues {

	public static String getString(String path) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);

		return String.valueOf(CONFIG.get(path));

	}

	public static void setValue(String path, Object object) {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		CONFIG.set(path, object);

		Files.saveConfig(FileType.CONFIG, CONFIG);

	}

}
