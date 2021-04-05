package me.Septicuss.InsomniacStack.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;

import me.Septicuss.InsomniacStack.menu.list.HeadDepositMenu;
import me.Septicuss.InsomniacStack.menu.list.UpgradeMenu;

public class SuperiorSkyblockCommandInterceptor implements Listener {

	@EventHandler
	public void onSuperiorSkyblockCommand(PlayerCommandPreprocessEvent event) {

		if (!event.getMessage().toLowerCase().startsWith("/island")
				&& !event.getMessage().toLowerCase().startsWith("/is")) {
			return;
		}

		final String[] args = event.getMessage().toLowerCase().split(" ");

		if (args.length == 0) {
			return;
		}

		if (args.length >= 2 && args[1].toLowerCase().startsWith("mob")) {
			new UpgradeMenu().openMenu(event.getPlayer());
			event.setCancelled(true);
			return;
		}

		if (args.length >= 2 && args[1].toLowerCase().startsWith("head")) {

			final Player player = event.getPlayer();
			final Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

			if (island == null) {
				player.sendMessage("§cYou do not have an island.");
			}

			new HeadDepositMenu().openMenu(event.getPlayer());
			event.setCancelled(true);
		}

	}

}
