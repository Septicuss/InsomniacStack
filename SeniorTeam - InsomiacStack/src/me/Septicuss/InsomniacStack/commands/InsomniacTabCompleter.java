package me.Septicuss.InsomniacStack.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class InsomniacTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("insomniacstack") && args.length == 1
				&& sender.hasPermission("insomniacstack.command")) {
			return Arrays.asList("settings", "help");
		} else {
			return Arrays.asList("");
		}
	}

}
