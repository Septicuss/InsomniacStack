package me.Septicuss.InsomniacStack.menu;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.Septicuss.InsomniacStack.InsomniacStack;

public abstract class Menu {

	private static HashMap<Inventory, Menu> list = new HashMap<Inventory, Menu>();

	private Inventory inventory;
	private boolean updating;

	public Menu(String name, int slotRows) {
		this.inventory = Bukkit.createInventory(null, slotRows * 9, name);
		this.updating = false;

		list.put(inventory, this);
	}

	public abstract void loadIcons(Player player);

	public abstract void onInventoryClick(Player player, InventoryClickEvent event);

	public abstract void onInventoryClose(Player player, InventoryCloseEvent event);

	public Inventory getInventory() {
		return this.inventory;
	}

	public boolean isUpdating() {
		return updating;
	}

	public void setUpdating(boolean updating) {
		this.updating = updating;
	}

	public void openMenu(Player player) {

		if (isUpdating()) {

			new BukkitRunnable() {
				public void run() {

					if (inventory.getViewers().size() <= 0) {
						cancel();
						return;
					}

					loadIcons(player);

				}

			}.runTaskTimer(InsomniacStack.getInstance(), 0L, 10L);

		} else {
			loadIcons(player);
		}

		player.openInventory(inventory);

	}

	public void updateInventory(Player player) {
		getInventory().clear();
		loadIcons(player);
	}

	public static HashMap<Inventory, Menu> getList() {
		return list;
	}

}
