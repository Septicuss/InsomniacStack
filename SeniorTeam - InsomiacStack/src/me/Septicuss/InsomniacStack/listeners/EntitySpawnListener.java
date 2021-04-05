package me.Septicuss.InsomniacStack.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import me.Septicuss.InsomniacStack.utils.StackUtils;

public class EntitySpawnListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onSpawn(CreatureSpawnEvent event) {

		StackUtils.setStackedAmount(event.getEntity(), 1);
		StackUtils.attemptToStack(event.getEntity());

	}

}
