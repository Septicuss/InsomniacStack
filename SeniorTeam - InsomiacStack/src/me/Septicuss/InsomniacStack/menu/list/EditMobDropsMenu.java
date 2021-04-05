package me.Septicuss.InsomniacStack.menu.list;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.InsomniacStack;
import me.Septicuss.InsomniacStack.drops.DropHandler;
import me.Septicuss.InsomniacStack.input.prompts.SetMobDropChancePrompt;
import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.objects.ChanceItem;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;
import me.Septicuss.InsomniacStack.utils.Utils;

public class EditMobDropsMenu extends Menu {

	private String mobName;
	private boolean bool = false;

	public EditMobDropsMenu(final String mobName) {
		super("Editing Drops for " + (mobName.substring(0, 1).toUpperCase() + mobName.substring(1)), 6);

		this.mobName = mobName;
	}

	@Override
	public void loadIcons(Player player) {

		getInventory().setItem(0,
				new SimpleItemMaker(Material.EGG, "§eDrag Items here", "§7Drag items into this menu",
						"§7to add drops. You will then", "§7be asked to input a chance.", " ",
						"§7Custom drops will be saved", "§7once you exit this menu.").get());

		final Set<ChanceItem> drops = DropHandler.getDrops(mobName);

		for (ChanceItem drop : drops) {

			ItemStack item = drop.getItem().clone();

			setChanceItem(item, drop.getChance());

			getInventory().addItem(item);

		}

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		if (event.getClickedInventory().equals(event.getView().getTopInventory())) {
			event.setCancelled(true);
		}

		final int slot = event.getRawSlot();

		if (slot == 0) {
			return;
		}

		if (!event.getClickedInventory().equals(event.getView().getTopInventory())) {
			return;
		}

		if (event.getCurrentItem() != null) {

			if (event.getClick() == ClickType.RIGHT) {
				final ItemStack currentItem = event.getCurrentItem();
				getInventory().removeItem(currentItem);
			}

			if (event.getClick() == ClickType.LEFT) {

				// Transfer mob data, slot data
				Utils.closeInventory(player);

				Map<Object, Object> data = new HashMap<>();
				data.put("mob", mobName);
				data.put("item", event.getCurrentItem());

				ConversationFactory conversationFactory = new ConversationFactory(InsomniacStack.getInstance())
						.withModality(true).withFirstPrompt(new SetMobDropChancePrompt())
						.thatExcludesNonPlayersWithMessage("").withLocalEcho(false).withInitialSessionData(data);

				conversationFactory.buildConversation(player).begin();

				bool = true;
			}

			return;
		}

		if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) {
			return;
		}

		final ItemStack cursorItem = event.getCursor().clone();

		setChanceItem(cursorItem, 1D);

		getInventory().addItem(cursorItem);
		saveInventory();

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {

		if (!bool) {
			saveInventory();
		} else {
			bool = false;
		}

		new EditDropsMenu().openMenu(player);
	}

	public void saveInventory() {

		Set<ChanceItem> chanceItems = new HashSet<>();

		for (int i = 1; i < getInventory().getSize(); i++) {
			final ItemStack checkItem = getInventory().getItem(i);

			if (checkItem == null || !checkItem.hasItemMeta() || !checkItem.getItemMeta().hasLore()) {
				continue;
			}

			final ItemStack item = checkItem.clone();

			SimpleItemMaker.removeLastLoreLine(item);
			SimpleItemMaker.removeLastLoreLine(item);
			SimpleItemMaker.removeLastLoreLine(item);

			final String chanceLine = ChatColor
					.stripColor(SimpleItemMaker.getLoreLine(item, item.getItemMeta().getLore().size() - 1));
			final Double chance = Double.valueOf(chanceLine.replaceAll("Chance : ", "").replaceAll("%", ""));

			SimpleItemMaker.removeLastLoreLine(item);
			SimpleItemMaker.removeLastLoreLine(item);

			final ChanceItem chanceItem = new ChanceItem(item, chance);
			chanceItems.add(chanceItem);
		}

		DropHandler.setDrops(mobName, chanceItems);

	}

	public void setChanceItem(ItemStack item, Double chance) {

		if (item == null) {
			return;
		}

		SimpleItemMaker.addLoreLine(item, " ");
		SimpleItemMaker.addLoreLine(item, "§7Chance : §f" + chance + "%");
		SimpleItemMaker.addLoreLine(item, " ");
		SimpleItemMaker.addLoreLine(item, "§7Left-click to change the chance");
		SimpleItemMaker.addLoreLine(item, "§7Right-click to remove this drop");

	}

}
