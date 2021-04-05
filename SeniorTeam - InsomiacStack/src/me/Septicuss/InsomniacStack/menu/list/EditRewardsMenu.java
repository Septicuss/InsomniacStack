package me.Septicuss.InsomniacStack.menu.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.InsomniacStack;
import me.Septicuss.InsomniacStack.drops.RewardHandler;
import me.Septicuss.InsomniacStack.input.prompts.RewardAddNewCommandPrompt;
import me.Septicuss.InsomniacStack.input.prompts.RewardSetNewChancePrompt;
import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.objects.Reward;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;
import me.Septicuss.InsomniacStack.utils.Utils;

public class EditRewardsMenu extends Menu {

	public EditRewardsMenu() {
		super("Edit Rewards", 6);
	}

	@Override
	public void loadIcons(Player player) {

		getInventory().setItem(0,
				new SimpleItemMaker(Material.BOOK, "§eClick to add new Reward", "§7Click on this item to create",
						"§7a new Reward.", " ", "§7Use §e%player% §7placeholder for the", "§7players name.").get());

		final List<Reward> rewards = RewardHandler.getRewards();

		if (rewards == null || rewards.isEmpty()) {
			return;
		}

		for (Reward reward : rewards) {
			final ItemStack rewardItem = new SimpleItemMaker(Material.PAPER, "§eReward", "").get();
			setRewardItem(rewardItem, reward);

			getInventory().setItem(this.getNextFreeSlot(), rewardItem);
		}

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		event.setCancelled(true);

		final Integer slot = event.getRawSlot();

		if (slot == 0) {

			final ItemStack rewardItem = new SimpleItemMaker(Material.PAPER, "§eReward", "").get();
			final Reward reward = new Reward(Arrays.asList("samplecommand"), 1D);
			setRewardItem(rewardItem, reward);

			getInventory().setItem(this.getNextFreeSlot(), rewardItem);

			return;
		}

		if (!event.getClickedInventory().equals(getInventory())) {
			return;
		}

		if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) {
			return;
		}

		final ClickType clickType = event.getClick();
		final ItemStack item = event.getCurrentItem();

		Reward reward = getReward(item);

		switch (clickType) {

		case LEFT:

			Utils.closeInventory(player);

			Map<Object, Object> data = new HashMap<>();
			data.put("slot", slot);

			ConversationFactory conversationFactory = new ConversationFactory(InsomniacStack.getInstance())
					.withModality(true).withFirstPrompt(new RewardAddNewCommandPrompt())
					.thatExcludesNonPlayersWithMessage("").withLocalEcho(false).withInitialSessionData(data);

			conversationFactory.buildConversation(player).begin();

			break;

		case RIGHT:

			Reward newReward = reward;

			if (newReward.getCommands().isEmpty() || newReward.getCommands().size() == 0) {
				getInventory().removeItem(item);
				return;
			}

			newReward.removeLastCommand();

			clearRewardItem(item);
			setRewardItem(item, newReward);

			break;

		case MIDDLE:

			Utils.closeInventory(player);

			data = new HashMap<>();
			data.put("slot", slot);

			conversationFactory = new ConversationFactory(InsomniacStack.getInstance()).withModality(true)
					.withFirstPrompt(new RewardSetNewChancePrompt()).thatExcludesNonPlayersWithMessage("")
					.withLocalEcho(false).withInitialSessionData(data);

			conversationFactory.buildConversation(player).begin();

			break;

		default:
			break;

		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {
		saveInventory();
		new DropSettingsMenu().openMenu(player);
	}

	private void saveInventory() {

		List<Reward> rewards = new ArrayList<>();

		for (int i = 1; i < getInventory().getSize(); i++) {

			if (getInventory().getItem(i) == null) {
				continue;
			}

			final ItemStack item = getInventory().getItem(i);
			rewards.add(getReward(item));

		}

		RewardHandler.clearRewards();

		Bukkit.getScheduler().runTaskLater(InsomniacStack.getInstance(), () -> {
			RewardHandler.setRewards(rewards);
		}, 1L);

	}

	public static void clearRewardItem(ItemStack item) {

		int toClear = 8;

		for (String loreLine : item.getItemMeta().getLore()) {
			if (loreLine.startsWith("§f/")) {
				toClear += 1;
			}
		}

		for (int i = 0; i < toClear; i++) {
			SimpleItemMaker.removeLastLoreLine(item);
		}
	}

	public static void setRewardItem(ItemStack item, Reward reward) {

		if (item == null) {
			return;
		}

		SimpleItemMaker.addLoreLine(item, " ");
		SimpleItemMaker.addLoreLine(item, "§7Current commands :");

		for (String command : reward.getCommands()) {
			SimpleItemMaker.addLoreLine(item, "§f/" + command);
		}

		SimpleItemMaker.addLoreLine(item, " ");
		SimpleItemMaker.addLoreLine(item, "§7Chance : §f" + reward.getChance() + "%");
		SimpleItemMaker.addLoreLine(item, " ");
		SimpleItemMaker.addLoreLine(item, "§7Left-Click to add a new Command");
		SimpleItemMaker.addLoreLine(item, "§7Right-Click to remove the last Command");
		SimpleItemMaker.addLoreLine(item, "§7Middle-Click to chance the Chance");
	}

	public static Reward getReward(ItemStack arg0) {

		ItemStack item = arg0.clone();

		SimpleItemMaker.removeLastLoreLine(item);
		SimpleItemMaker.removeLastLoreLine(item);
		SimpleItemMaker.removeLastLoreLine(item);
		SimpleItemMaker.removeLastLoreLine(item);

		final String chanceLine = ChatColor.stripColor(SimpleItemMaker.getLastLoreLine(item));
		final Double chance = Double.valueOf(chanceLine.replaceAll("Chance : ", "").replaceAll("%", ""));

		SimpleItemMaker.removeLastLoreLine(item);

		final List<String> commands = new ArrayList<>();

		for (String loreLine : item.getItemMeta().getLore()) {
			if (loreLine.startsWith("§f/")) {
				commands.add(ChatColor.stripColor(loreLine).replaceAll("/", ""));
			}
		}

		Reward reward = new Reward(commands, chance);

		return reward;

	}

	private Integer getNextFreeSlot() {
		for (int i = 1; i < getInventory().getSize(); i++) {

			if (getInventory().getItem(i) == null) {
				return i;
			}
		}

		return null;
	}

}
