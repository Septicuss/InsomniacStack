package me.Septicuss.InsomniacStack;

import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Septicuss.InsomniacStack.commands.InsomniacStackCommand;
import me.Septicuss.InsomniacStack.commands.InsomniacTabCompleter;
import me.Septicuss.InsomniacStack.commands.SuperiorSkyblockCommandInterceptor;
import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.listeners.EntityDeathListener;
import me.Septicuss.InsomniacStack.listeners.EntityInteractListener;
import me.Septicuss.InsomniacStack.listeners.EntitySpawnListener;
import me.Septicuss.InsomniacStack.menu.listeners.InventoryListener;
import net.milkbowl.vault.economy.Economy;

public class InsomniacStack extends JavaPlugin implements Listener {

	/**
	 * PERMISSIONS
	 * 
	 * The InsomniacStack command : - insomniacstack.command
	 * 
	 */

	private static InsomniacStack instance;
	private static Economy economy = null;

	public void onEnable() {

		instance = this;
		Files.initialize(this);
		setupConfig();

		getCommand("insomniacstack").setExecutor(new InsomniacStackCommand());

		getServer().getPluginManager().registerEvents(new EntityInteractListener(), this);
		getServer().getPluginManager().registerEvents(new EntitySpawnListener(), this);
		getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(new SuperiorSkyblockCommandInterceptor(), this);

		getCommand("insomniacstack").setTabCompleter(new InsomniacTabCompleter());

		setupEconomy();

	}

	public static InsomniacStack getInstance() {
		return instance;
	}

	public static Economy getEconomy() {
		return economy;
	}

	private void setupConfig() {

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);

		CONFIG.addDefault("settings.stack.name", "&f%type% &e%amount%x");
		CONFIG.addDefault("settings.stack.range.x", 5);
		CONFIG.addDefault("settings.stack.range.y", 5);
		CONFIG.addDefault("settings.stack.range.z", 5);
		CONFIG.addDefault("settings.stack.tamed", false);
		CONFIG.addDefault("settings.stack.different-age", false);
		CONFIG.addDefault("settings.stack.kill-all", false);
		CONFIG.addDefault("settings.stack.max", 128);
		CONFIG.addDefault("settings.stack.blacklist", Arrays.asList("ENDER_DRAGON", "WITHER"));
		CONFIG.addDefault("settings.stack.world-blacklist", Arrays.asList("disabled-world"));

		CONFIG.addDefault("settings.drops.enabled", true);
		CONFIG.addDefault("settings.drops.multiply", true);
		CONFIG.addDefault("settings.rewards.enabled", true);

		CONFIG.addDefault("settings.upgrades.doubledrops.1.price", 100);
		CONFIG.addDefault("settings.upgrades.rewards.1.price", 200);
		CONFIG.addDefault("settings.upgrades.rewards.1.percentage", 1.5D);
		CONFIG.addDefault("settings.upgrades.rewards.2.price", 300);
		CONFIG.addDefault("settings.upgrades.rewards.2.percentage", 3.0D);
		CONFIG.addDefault("settings.upgrades.rewards.3.price", 400);
		CONFIG.addDefault("settings.upgrades.rewards.3.percentage", 5.0D);

		CONFIG.addDefault("settings.heads.zombie", 5);

		CONFIG.options().copyDefaults(true);
		Files.saveConfig(FileType.CONFIG, CONFIG);

	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}
}
