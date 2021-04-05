package me.Septicuss.InsomniacStack.input.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.menu.list.EditRewardsMenu;
import me.Septicuss.InsomniacStack.objects.Reward;
import me.Septicuss.InsomniacStack.utils.Utils;

public class RewardSetNewChancePrompt extends StringPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return "§eType the new §fnumber §evalue in chat.";
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String text) {
		try {

			final Player player = (Player) context.getForWhom();
			final Integer slot = (Integer) context.getSessionData("slot");

			Menu rewardsMenu = new EditRewardsMenu();
			rewardsMenu.openMenu(player);

			ItemStack item = rewardsMenu.getInventory().getItem(slot);
			Reward reward = EditRewardsMenu.getReward(item);

			Double value = Double.parseDouble(text);

			reward.setChance(Utils.round(value, text.length() - 1));

			EditRewardsMenu.clearRewardItem(item);
			EditRewardsMenu.setRewardItem(item, reward);

		} catch (Exception e) {
			new EditRewardsMenu().openMenu((Player) context.getForWhom());
			return Prompt.END_OF_CONVERSATION;
		}
		return Prompt.END_OF_CONVERSATION;
	}

}
