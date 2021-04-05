package me.Septicuss.InsomniacStack.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Septicuss.InsomniacStack.InsomniacStack;

public class Utils {

	public static String capitalizeFirstLetters(String string) {

		StringBuilder stringBuilder = new StringBuilder();

		for (String word : string.split(" ")) {
			stringBuilder.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ");
		}

		return stringBuilder.toString().trim();

	}

	public static void closeInventory(Player player) {

		new BukkitRunnable() {
			public void run() {

				if (player.isOnline() && player.getOpenInventory() != null
						&& !player.getOpenInventory().getTitle().equals("Crafting")) {
					player.closeInventory();
				} else {
					this.cancel();
				}

			}

		}.runTaskTimer(InsomniacStack.getInstance(), 0L, 2L);
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
