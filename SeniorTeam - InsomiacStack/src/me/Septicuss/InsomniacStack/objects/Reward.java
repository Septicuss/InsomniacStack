package me.Septicuss.InsomniacStack.objects;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reward {

	private List<String> commands;
	private Double chance;

	public Reward(List<String> commands, Double chance) {
		this.commands = commands;
		this.chance = chance;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public void addCommand(String command) {
		this.commands.add(command);
	}

	public void removeCommand(String command) {
		this.commands.remove(command);
	}

	public void removeLastCommand() {
		this.commands.remove(commands.size() - 1);
	}

	public Double getChance() {
		return chance;
	}

	public void setChance(Double chance) {
		this.chance = chance;
	}

	public void execute(Player player) {

		if (commands == null || commands.isEmpty()) {
			return;
		}

		for (String command : this.commands) {

			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));

		}

	}

}