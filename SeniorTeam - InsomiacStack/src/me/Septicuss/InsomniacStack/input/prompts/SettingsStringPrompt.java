package me.Septicuss.InsomniacStack.input.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class SettingsStringPrompt extends StringPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return "§eType the new §ftext §evalue in chat.";
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		context.setSessionData("value", input);
		return new SettingsSetPrompt();
	}

}
