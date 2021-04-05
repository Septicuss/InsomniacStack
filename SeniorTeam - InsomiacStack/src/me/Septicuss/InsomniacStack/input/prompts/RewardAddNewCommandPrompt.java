package me.Septicuss.InsomniacStack.input.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Septicuss.InsomniacStack.menu.Menu;
import me.Septicuss.InsomniacStack.menu.list.EditRewardsMenu;
import me.Septicuss.InsomniacStack.objects.Reward;

public class RewardAddNewCommandPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String text) {

		try {

			final Player player = (Player) context.getForWhom();
			final Integer slot = (Integer) context.getSessionData("slot");

			Menu rewardsMenu = new EditRewardsMenu();
			rewardsMenu.openMenu(player);

			ItemStack item = rewardsMenu.getInventory().getItem(slot);
			Reward reward = EditRewardsMenu.getReward(item);

			reward.addCommand(text);

			EditRewardsMenu.clearRewardItem(item);
			EditRewardsMenu.setRewardItem(item, reward);

		} catch (Exception e) {
			return Prompt.END_OF_CONVERSATION;
		}
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext cpmtext) {
		return "§eType the new §ftext §evalue in chat.";
	}

}
