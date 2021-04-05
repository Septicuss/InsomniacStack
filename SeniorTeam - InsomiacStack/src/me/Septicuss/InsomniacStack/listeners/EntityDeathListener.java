package me.Septicuss.InsomniacStack.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.drops.DropHandler;
import me.Septicuss.InsomniacStack.drops.RewardHandler;
import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.objects.Reward;
import me.Septicuss.InsomniacStack.upgrade.UpgradeHandler;
import me.Septicuss.InsomniacStack.upgrade.UpgradeHandler.UpgradeType;
import me.Septicuss.InsomniacStack.utils.StackUtils;

public class EntityDeathListener implements Listener {

	private static Set<Entity> cancelDrops = new HashSet<>();

	@EventHandler
	public void onDeath(EntityDeathEvent event) {

		if (!StackUtils.isStackedEntity(event.getEntity())) {
			return;
		}

		final boolean MULTIPLY_DROPS = Files.getConfig(FileType.CONFIG).getBoolean("settings.drops.multiply");
		final boolean DROPS_ENABLED = Files.getConfig(FileType.CONFIG).getBoolean("settings.drops.enabled");

		final Entity entity = event.getEntity();

		if (cancelDrops.contains(entity)) {

			event.getDrops().clear();
			cancelDrops.remove(entity);

			return;
		}

		if (DROPS_ENABLED && DropHandler.hasDrops(entity.getType().toString().toLowerCase())) {

			processEntityDeath(entity, null);
			event.getDrops().clear();

			return;
		}

		final Integer stackedAmount = StackUtils.getStackedAmount(entity);
		final Integer multiplier = (MULTIPLY_DROPS ? stackedAmount : 1);

		for (ItemStack eventDrop : event.getDrops()) {
			eventDrop.setAmount(eventDrop.getAmount() * multiplier);
		}

		if (MULTIPLY_DROPS) {
			event.setDroppedExp(event.getDroppedExp() * multiplier);
		}

	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {

		if (!(event.getDamager() instanceof Player) || !StackUtils.isStackedEntity(event.getEntity())) {
			return;
		}

		final Player damager = (Player) event.getDamager();
		final LivingEntity damaged = (LivingEntity) event.getEntity();

		final Double currentHealth = damaged.getHealth();
		final Double newHealth = currentHealth - event.getDamage();

		if (newHealth > 0) {
			return;
		}

		processEntityDeath(damaged, damager);

	}

	private void processEntityDeath(Entity entity, Player player) {

		final boolean KILL_ALL = Files.getConfig(FileType.CONFIG).getBoolean("settings.stack.kill-all");
		final boolean DROPS_ENABLED = Files.getConfig(FileType.CONFIG).getBoolean("settings.drops.enabled");
		final boolean MULTIPLY_DROPS = Files.getConfig(FileType.CONFIG).getBoolean("settings.drops.multiply");

		boolean DOUBLE_DROPS = false;

		final Integer stackedAmount = StackUtils.getStackedAmount(entity);
		final String entityName = entity.getType().toString().toLowerCase();

		if (player != null) {
			processPlayerReward(player);

			if (UpgradeHandler.hasUpgrade(player, UpgradeType.DOUBLE_DROPS)) {
				DOUBLE_DROPS = true;
			}
		}

		if (KILL_ALL) {

			entity.setCustomName(null);
			entity.setCustomNameVisible(false);

			if (DROPS_ENABLED) {

				cancelDrops(entity);

				if (!DropHandler.hasDrops(entityName)) {
					return;
				}

				final int lootAmount = (MULTIPLY_DROPS ? stackedAmount : 1);

				for (int i = 0; i < lootAmount; i++) {

					final ItemStack drop = DropHandler.getDrop(entityName);

					if (drop == null) {
						continue;
					}

					if (DOUBLE_DROPS)
						drop.setAmount(drop.getAmount() * 2);

					entity.getWorld().dropItemNaturally(entity.getLocation(), drop);

				}

			}

			return;
		}

		entity.setCustomName(null);
		entity.setCustomNameVisible(false);

		if (stackedAmount > 1) {

			final Entity newEntity = entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
			StackUtils.setStackedAmount(newEntity, stackedAmount - 1);
			StackUtils.setStackedName(newEntity);

		}

		if (DROPS_ENABLED) {

			cancelDrops(entity);

			if (!DropHandler.hasDrops(entityName)) {
				return;
			}

			final ItemStack drop = DropHandler.getDrop(entityName);

			if (drop == null) {
				return;
			}

			if (DOUBLE_DROPS)
				drop.setAmount(drop.getAmount() * 2);

			entity.getWorld().dropItemNaturally(entity.getLocation(), drop);
		}

	}

	private void processPlayerReward(Player player) {

		final boolean REWARDS_ENABLED = Files.getConfig(FileType.CONFIG).getBoolean("settings.rewards.enabled");

		if (!REWARDS_ENABLED) {
			return;
		}

		final Integer REWARD_UPGRADE_LEVEL = UpgradeHandler.getUpgradeLevel(player, UpgradeType.REWARD_UPGRADE);
		final Double REWARD_UPGRADE_PERCENTAGE = UpgradeHandler.getUpgradePercentage(UpgradeType.REWARD_UPGRADE,
				REWARD_UPGRADE_LEVEL);

		final Double chanceBooster = REWARD_UPGRADE_PERCENTAGE;
		Reward reward = RewardHandler.getRandomReward(chanceBooster);

		if (reward == null) {
			return;
		}

		reward.execute(player);

	}

	private void cancelDrops(Entity entity) {
		if (!cancelDrops.contains(entity)) {
			cancelDrops.add(entity);
		}
	}

}
