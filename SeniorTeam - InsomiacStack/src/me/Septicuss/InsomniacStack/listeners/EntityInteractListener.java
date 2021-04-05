package me.Septicuss.InsomniacStack.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

import me.Septicuss.InsomniacStack.menu.list.EntityDespawnMenu;
import me.Septicuss.InsomniacStack.utils.StackUtils;

public class EntityInteractListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteractEntity(PlayerInteractEntityEvent event) {

		final Entity entity = event.getRightClicked();

		if (entity == null || !(entity instanceof LivingEntity) || entity.isDead()) {
			return;
		}

		if (!StackUtils.isStackedEntity(entity)) {
			return;
		}

		final Player player = event.getPlayer();
		final SuperiorPlayer sPlayer = SuperiorSkyblockAPI.getPlayer(player);

		if (sPlayer.getIsland() == null || !sPlayer.getIsland().isInside(player.getLocation())) {
			return;
		}

		new EntityDespawnMenu(entity).openMenu(player);

	}

}
