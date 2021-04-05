package me.Septicuss.InsomniacStack.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.Septicuss.InsomniacStack.InsomniacStack;
import me.Septicuss.InsomniacStack.objects.ChanceItem;

public class DataUtils {

	public static void setPersistentData(final Entity entity, final String key, final String data) {

		NamespacedKey namespacedKey = new NamespacedKey(InsomniacStack.getInstance(), key);
		entity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, data);

	}

	public static String getPersistentData(final Entity entity, final String key) {

		NamespacedKey namespacedKey = new NamespacedKey(InsomniacStack.getInstance(), key);
		return entity.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);

	}

	public static boolean hasPersistentData(final Entity entity, final String key) {

		NamespacedKey namespacedKey = new NamespacedKey(InsomniacStack.getInstance(), key);
		return entity.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);

	}

	public static String itemStackListToBase64(List<ItemStack> items) {
		return itemStackArrayToBase64(items.toArray(new ItemStack[0]));
	}

	public static List<ItemStack> itemStackListFromBase64(String data) {
		return Arrays.asList(itemStackArrayFromBase64(data));
	}

	public static String itemStackArrayToBase64(ItemStack[] items) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(items.length);

			// Save every element in the list
			for (int i = 0; i < items.length; i++) {
				dataOutput.writeObject(items[i]);
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			return null;
		}
	}

	public static ItemStack[] itemStackArrayFromBase64(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] items = new ItemStack[dataInput.readInt()];

			// Read the serialized inventory
			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}

			dataInput.close();
			return items;
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}

	public static String itemStackToBase64(ItemStack item) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeObject(item);

			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			return null;
		}
	}

	public static ItemStack itemStackFromBase64(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

			ItemStack item = (ItemStack) dataInput.readObject();

			dataInput.close();
			return item;
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}

	public static String serializeChanceItem(ChanceItem chanceItem) {
		String itemStackBase64 = itemStackToBase64(chanceItem.getItem());
		String chanceString = chanceItem.getChance() + "!";

		return (chanceString + itemStackBase64);
	}

	public static ChanceItem deserializeChanceItem(String data) {
		String[] separatedData = data.split("!");

		Double chance = Double.valueOf(separatedData[0]);
		ItemStack item = itemStackFromBase64(separatedData[1]);

		return new ChanceItem(item, chance);
	}

}
