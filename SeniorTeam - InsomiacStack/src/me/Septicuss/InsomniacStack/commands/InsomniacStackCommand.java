package me.Septicuss.InsomniacStack.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Septicuss.InsomniacStack.menu.list.MainSettingsMenu;

public class InsomniacStackCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§c/insomniacstack help");
			return true;
		}

		if (!sender.hasPermission("insomniacstack.command")) {
			sender.sendMessage("§cNo permission.");
			return true;
		}

		if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help"))) {
			sender.sendMessage("§e§lHELP");
			sender.sendMessage("§e/insomniacstack help");
			sender.sendMessage("§e/insomniacstack settings");
			return true;
		}

		final Player player = (Player) sender;

		if (args[0].equalsIgnoreCase("settings")) {

			new MainSettingsMenu().openMenu(player);

		}

		return true;
	}

}
