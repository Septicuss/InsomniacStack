package me.Septicuss.InsomniacStack.input.prompts;

import java.util.Set;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.drops.DropHandler;
import me.Septicuss.InsomniacStack.menu.list.EditMobDropsMenu;
import me.Septicuss.InsomniacStack.objects.ChanceItem;
import me.Septicuss.InsomniacStack.utils.SimpleItemMaker;

public class SetMobDropChancePrompt extends NumericPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return "§eType the new §fnumber §evalue in chat.";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, Number number) {

		try {
			final Player player = (Player) context.getForWhom();

			final String mobName = (String) context.getSessionData("mob");
			final ItemStack item = (ItemStack) context.getSessionData("item");

			if (mobName == null || item == null) {
				return Prompt.END_OF_CONVERSATION;
			}

			for (int i = 0; i < 5; i++) {
				SimpleItemMaker.removeLastLoreLine(item);
			}

			Set<ChanceItem> drops = DropHandler.getDrops(mobName);

			for (ChanceItem cItem : drops) {

				if (cItem.getItem().isSimilar(item)) {
					cItem.setChance(number.doubleValue());
				}

			}

			DropHandler.setDrops(mobName, drops);

			new EditMobDropsMenu(mobName).openMenu(player);

		} catch (Exception exc) {
			exc.printStackTrace();
			return Prompt.END_OF_CONVERSATION;
		}

		return Prompt.END_OF_CONVERSATION;
	}

}
