package me.Septicuss.InsomniacStack.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import me.Septicuss.InsomniacStack.utils.StackUtils;

public class ChunkLoadListener implements Listener {

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {

		if (event.getChunk().getEntities().length == 0) {
			return;
		}

		for (Entity entity : event.getChunk().getEntities()) {

			if (entity == null || entity.isDead()) {
				continue;
			}

			if (!(entity instanceof LivingEntity)) {
				continue;
			}

			if (StackUtils.isStackedEntity(entity)) {
				continue;
			}

			StackUtils.attemptToStack(entity);

		}

	}

}
