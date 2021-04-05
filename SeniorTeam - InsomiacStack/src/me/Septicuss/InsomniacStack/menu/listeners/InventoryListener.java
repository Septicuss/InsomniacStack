package me.Septicuss.InsomniacStack.menu.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Septicuss.InsomniacStack.InsomniacStack;
import me.Septicuss.InsomniacStack.menu.Menu;

public class InventoryListener implements Listener {

	private static Set<Menu> closeCancel = new HashSet<>();

	@EventHandler
	public static void onInventoryClick(InventoryClickEvent event) {

		Player player = (Player) event.getWhoClicked();
		Menu menu = Menu.getList().get(event.getInventory());

		if (menu == null) {
			return;
		}

		if (event.getRawSlot() == -999) {
			return;
		}

		if (event.getCurrentItem() != null) {
			player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 10);
			closeCancel.add(menu);

			Bukkit.getScheduler().runTaskLater(InsomniacStack.getInstance(), () -> {
				closeCancel.remove(menu);
			}, 2L);
		}

		menu.onInventoryClick(player, event);

	}

	@EventHandler
	public static void onInventoryClose(InventoryCloseEvent event) {
		Bukkit.getScheduler().runTaskLater(InsomniacStack.getInstance(), () -> {

			Player player = (Player) event.getPlayer();
			Menu menu = Menu.getList().get(event.getInventory());

			if (menu == null) {
				return;
			}

			if (closeCancel.contains(menu)) {
				closeCancel.remove(menu);
				return;
			}

			player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10, 1);

			menu.onInventoryClose(player, event);

		}, 1L);
	}

}
