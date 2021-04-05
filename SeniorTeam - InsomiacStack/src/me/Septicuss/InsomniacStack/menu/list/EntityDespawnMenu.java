package me.Septicuss.InsomniacStack.menu.list;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;
import me.Septicuss.InsomniacStack.utils.StackUtils;

public class EntityDespawnMenu extends Menu {

	private Entity entity;

	public EntityDespawnMenu(Entity entity) {
		super("Despawn " + entity.getType().toString().substring(0, 1).toUpperCase()
				+ entity.getType().toString().substring(1).toLowerCase(), 3);

		this.entity = entity;
	}

	@Override
	public void loadIcons(Player player) {

		if (!StackUtils.isStackedEntity(entity)) {
			return;
		}

		final LivingEntity stackedEntity = (LivingEntity) entity;
		final String entityType = entity.getType().toString();
		final String entityName = entityType.substring(0, 1).toUpperCase() + entityType.substring(1).toLowerCase();
		final Integer stackedAmount = StackUtils.getStackedAmount(stackedEntity);

		getInventory().setItem(13, new SimpleItemMaker(Material.IRON_AXE,
				"§cDespawn §f" + stackedAmount + " §cof §f" + entityName, "§7Click to despawn selected entity.").get());

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		event.setCancelled(true);

		final int slot = event.getRawSlot();

		if (slot == 13) {

			if (entity == null || entity.isDead()) {
				return;
			}

			entity.remove();

			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10, 1);
			player.closeInventory();
		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {

	}

}
