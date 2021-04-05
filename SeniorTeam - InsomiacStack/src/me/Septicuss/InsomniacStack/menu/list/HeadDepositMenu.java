package me.Septicuss.InsomniacStack.menu.list;

import java.math.BigDecimal;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;

import me.Septicuss.InsomniacStack.drops.HeadHandler;
import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;

public class HeadDepositMenu extends Menu {

	public HeadDepositMenu() {
		super("Deposit Mob Heads", 3);
	}

	@Override
	public void loadIcons(Player player) {

		for (int i = 0; i < getInventory().getSize(); i++) {
			getInventory().setItem(i, new SimpleItemMaker(Material.MAGENTA_STAINED_GLASS_PANE, " ", "").get());
		}

		for (int i = 10; i < 17; i++) {
			getInventory().setItem(i, new SimpleItemMaker(Material.GRAY_STAINED_GLASS_PANE, " ", "").get());
		}

		getInventory().setItem(13,
				new SimpleItemMaker(Material.PLAYER_HEAD, "§d§l§nDeposit", "§8§m                                    ",
						" ", "§7Right click to deposit all the", "§7mob heads in your inventory.", " ",
						"§8§m                                    ").get());

	}

	@Override
	public void onInventoryClick(Player player, InventoryClickEvent event) {

		if (!event.getClickedInventory().equals(event.getView().getTopInventory())) {
			return;
		}

		event.setCancelled(true);

		final Integer slot = event.getRawSlot();

		if (slot == 4 && event.getClick() == ClickType.RIGHT) {

			for (ItemStack item : player.getInventory().getContents()) {

				if (item == null || item.getType() != Material.PLAYER_HEAD) {
					continue;
				}

				final String entityName = HeadHandler.getEntityName(item);

				if (entityName == null || HeadHandler.getHeadValue(entityName) == null) {
					return;
				}

				final Integer headValue = HeadHandler.getHeadValue(entityName);
				final Integer itemAmount = item.getAmount();
				final Integer valueAdded = headValue * itemAmount;

				item.setAmount(0);

				addIslandValue(player, valueAdded);

				player.sendMessage("§f" + valueAdded + " §aworth has been added to your island.");
			}
		}

	}

	@Override
	public void onInventoryClose(Player player, InventoryCloseEvent event) {

	}

	private void addIslandValue(Player player, Integer value) {

		final Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

		if (island == null) {
			return;
		}

		final BigDecimal bonusWorth = island.getBonusWorth();
		SuperiorSkyblockAPI.getPlayer(player).getIsland().setBonusWorth(bonusWorth.add(BigDecimal.valueOf(value)));
	}

}
