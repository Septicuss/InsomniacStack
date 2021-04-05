package me.Septicuss.InsomniacStack.input.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import me.Septicuss.InsomniacStack.drops.HeadHandler;
import me.Septicuss.InsomniacStack.menu.list.EditHeadValueMenu;

public class HeadValueSetNewValuePrompt extends StringPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return "§eType the new §fnumber §evalue in chat.";
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String text) {
		try {

			final Player player = (Player) context.getForWhom();
			final String mobName = (String) context.getSessionData("mob");

			HeadHandler.setHeadValue(mobName, Integer.valueOf(text));
			new EditHeadValueMenu().openMenu(player);

		} catch (Exception e) {
			new EditHeadValueMenu().openMenu((Player) context.getForWhom());
			return Prompt.END_OF_CONVERSATION;
		}
		return Prompt.END_OF_CONVERSATION;
	}
}