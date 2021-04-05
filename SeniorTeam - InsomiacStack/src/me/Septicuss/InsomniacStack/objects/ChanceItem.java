package me.Septicuss.InsomniacStack.objects;

import org.bukkit.inventory.ItemStack;

public class ChanceItem {

	private ItemStack item;
	private Double chance;

	public ChanceItem(ItemStack item, Double chance) {
		this.item = item;
		this.chance = chance;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public Double getChance() {
		return chance;
	}

	public void setChance(Double chance) {
		this.chance = chance;
	}

}
