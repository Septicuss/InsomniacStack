package me.Septicuss.InsomniacStack.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.scheduler.BukkitRunnable;

import me.Septicuss.InsomniacStack.InsomniacStack;
import me.Septicuss.InsomniacStack.files.ConfigValues;
import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;

public class StackUtils {

	public static void attemptToStack(final Entity entity) {

		if (!isStackable(entity)) {
			return;
		}

		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);

		final Integer rangeX = CONFIG.getInt("settings.stack.range.x");
		final Integer rangeY = CONFIG.getInt("settings.stack.range.y");
		final Integer rangeZ = CONFIG.getInt("settings.stack.range.z");

		setStackedName(entity);

		new BukkitRunnable() {

			@Override
			public void run() {

				if (entity == null || entity.isDead()) {
					cancel();
				}

				List<Entity> nearbyEntities = entity.getNearbyEntities(rangeX, rangeY, rangeZ);

				for (Entity nearbyEntity : nearbyEntities) {

					if (!(nearbyEntity instanceof LivingEntity) || nearbyEntity.isDead()) {
						continue;
					}

					stackEntities(nearbyEntity, entity);

				}

			}

		}.runTaskTimer(InsomniacStack.getInstance(), 0L, 10L);

	}

	public static boolean stackEntities(final Entity firstEntity, final Entity secondEntity) {

		if (!canStack(firstEntity, secondEntity)) {
			return false;
		}

		final Integer maxStack = Files.getConfig(FileType.CONFIG).getInt("settings.stack.max");

		final Integer firstAmount = getStackedAmount(firstEntity);
		final Integer secondAmount = getStackedAmount(secondEntity);

		final Integer newAmount = (firstAmount + secondAmount);

		if (newAmount > maxStack) {
			return false;
		}

		Entity stackedEntity = (firstAmount >= secondAmount ? firstEntity : secondEntity);

		setStackedAmount(stackedEntity, newAmount);
		setStackedName(stackedEntity);

		if (stackedEntity.equals(firstEntity)) {
			secondEntity.remove();
		} else {
			firstEntity.remove();
		}

		return true;

	}

	public static boolean canStack(final Entity firstEntity, final Entity secondEntity) {

		if (!(firstEntity instanceof LivingEntity) || !(secondEntity instanceof LivingEntity)) {
			return false;
		}

		if (firstEntity.getType() != secondEntity.getType()) {
			return false;
		}

		if (firstEntity.isDead() || secondEntity.isDead()) {
			return false;
		}

		if (firstEntity.equals(secondEntity)) {
			return false;
		}

		if (!isStackable(firstEntity) || !isStackable(secondEntity)) {
			return false;
		}

		if (!Files.getConfig(FileType.CONFIG).getBoolean("settings.stack.tamed")
				&& (firstEntity instanceof Tameable || secondEntity instanceof Tameable)) {

			if (((Tameable) firstEntity).isTamed() || ((Tameable) secondEntity).isTamed()) {
				return false;
			}

		}

		if (!Files.getConfig(FileType.CONFIG).getBoolean("settings.stack.different-age")
				&& firstEntity instanceof Ageable && secondEntity instanceof Ageable) {

			Ageable firstAge = (Ageable) firstEntity;
			Ageable secondAge = (Ageable) secondEntity;

			if ((firstAge.isAdult() && !secondAge.isAdult()) || (!firstAge.isAdult()) && secondAge.isAdult()) {
				return false;
			}
		}

		return true;

	}

	public static boolean isStackable(final Entity entity) {

		if (entity == null || entity.isDead()) {
			return false;
		}

		List<String> blacklisted = Files.getConfig(FileType.CONFIG).getStringList("settings.stack.blacklist");
		List<String> blacklistedWorlds = Files.getConfig(FileType.CONFIG)
				.getStringList("settings.stack.world-blacklist");

		if (blacklisted.stream().anyMatch(entity.getType().toString()::equalsIgnoreCase)) {
			return false;
		}

		if (blacklistedWorlds.stream().anyMatch(entity.getWorld().getName()::equalsIgnoreCase)) {
			return false;
		}

		return true;
	}

	public static Integer getStackedAmount(final Entity entity) {

		if (entity == null) {
			setStackedAmount(entity, 1);
			return 1;
		}

		if (DataUtils.hasPersistentData(entity, "amount")) {
			return Integer.valueOf(DataUtils.getPersistentData(entity, "amount"));
		}

		return 1;

	}

	public static boolean isStackedEntity(final Entity entity) {

		if (entity == null || !(entity instanceof LivingEntity) || !DataUtils.hasPersistentData(entity, "amount")) {
			return false;
		}

		return true;

	}

	public static void setStackedAmount(final Entity entity, Integer amount) {

		DataUtils.setPersistentData(entity, "amount", String.valueOf(amount));

	}

	public static void setStackedName(final Entity entity) {

		final String stackedName = ConfigValues.getString("settings.stack.name");
		final Integer stackedAmount = (getStackedAmount(entity));

		String newName = ChatColor.translateAlternateColorCodes('&', stackedName);
		newName = newName.replace("%type%", entity.getType().toString());
		newName = newName.replace("%amount%", String.valueOf(stackedAmount));

		entity.setCustomName(newName);
		entity.setCustomNameVisible(true);

	}

}
