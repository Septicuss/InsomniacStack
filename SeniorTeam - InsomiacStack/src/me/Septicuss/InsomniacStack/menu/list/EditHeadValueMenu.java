package me.Septicuss.InsomniacStack.menu.list;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.InsomniacStack;
import me.Septicuss.InsomniacStack.drops.HeadHandler;
import me.Septicuss.InsomniacStack.input.prompts.HeadValueSetNewValuePrompt;
import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;
import me.Septicuss.InsomniacStack.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class EditHeadValueMenu extends Menu {

	public EditHeadValueMenu() {
		super("Edit Head Value", 6);
	}

	@Override
	public void loadIcons(Player player) {

		getInventory().setItem(0,
				new SimpleItemMaker(Material.EGG, "§eAdd a new Mob", "§7Click this item with a spawn",
						"§7egg to add a mob head value.", " ", "§cMake sure that the mob",
						"§cwhose head value you configure,", "§chas a mob head set up in", "§ctheir drops!").get());

		final HashMap<String, Integer> headValues = HeadHandler.getHeadValues();

		for (String mobName : headValues.keySet()) {

			ItemStack item = HeadHandler.getMobHead(mobName);

			SimpleItemMaker.setDisplayName(item, "§e" + (Utils.capitalizeFirstLetters(mobName).replaceAll("_", " ")));
			SimpleItemMaker.addLoreLine(item, " ");
			SimpleItemMaker.addLoreLine(item, "§7Value : §f" + headValues.get(mobName));
			SimpleItemMaker.addLoreLine(item, " ");
			SimpleItemMaker.addLoreLine(item, "§7Left-Click to change the value");
			SimpleItemMaker.addLoreLine(item, "§7Right-Click to remove this head");

			getInventory().addItem(item);
		}

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		if (event.getClickedInventory().equals(event.getView().getTopInventory())) {
			event.setCancelled(true);
		}

		final Integer slot = event.getSlot();

		if (slot == 0) {

			if (event.getCurrentItem() == null || event.getCursor() == null) {
				return;
			}

			final ItemStack cursor = event.getCursor();

			if (!cursor.getType().toString().contains("SPAWN_EGG") && cursor.getType() != Material.IRON_INGOT) {
				return;
			}

			String entityName = event.getCursor().getType().toString().replaceAll("_SPAWN_EGG", "").toLowerCase();

			if (cursor.getType() == Material.IRON_INGOT) {
				entityName = "iron_golem";
			}

			HeadHandler.setHeadValue(entityName, 1);
			updateInventory(player);

			return;
		}

		if (!event.getClickedInventory().equals(event.getView().getTopInventory())) {
			return;
		}

		if (event.getCurrentItem() == null) {
			return;
		}

		final ItemStack item = event.getCurrentItem();

		if (item.getType() != Material.PLAYER_HEAD) {
			return;
		}

		final String mobName = ChatColor
				.stripColor(item.getItemMeta().getDisplayName().replaceAll(" ", "_").toLowerCase());

		if (event.getClick() == ClickType.RIGHT) {

			HeadHandler.removeHeadValue(mobName);

			updateInventory(player);
			return;
		}

		if (event.getClick() == ClickType.LEFT) {

			Utils.closeInventory(player);

			Map<Object, Object> data = new HashMap<>();
			data.put("mob", ChatColor.stripColor(mobName));

			ConversationFactory conversationFactory = new ConversationFactory(InsomniacStack.getInstance())
					.withModality(true).withFirstPrompt(new HeadValueSetNewValuePrompt())
					.thatExcludesNonPlayersWithMessage("").withLocalEcho(false).withInitialSessionData(data);

			conversationFactory.buildConversation(player).begin();

			return;
		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {
		new DropSettingsMenu().openMenu(player);
	}

}
