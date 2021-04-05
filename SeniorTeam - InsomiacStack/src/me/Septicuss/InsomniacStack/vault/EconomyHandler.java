package me.Septicuss.InsomniacStack.vault;

import org.bukkit.entity.Player;

import me.Septicuss.InsomniacStack.InsomniacStack;

public class EconomyHandler {

	public static boolean has(Player player, Double amount) {

		if (InsomniacStack.getEconomy() == null) {
			return false;
		}

		return InsomniacStack.getEconomy().has(player, amount);
	}

	public static void take(Player player, Double amount) {

		if (InsomniacStack.getEconomy() == null) {
			return;
		}

		InsomniacStack.getEconomy().withdrawPlayer(player, amount);
	}

}
