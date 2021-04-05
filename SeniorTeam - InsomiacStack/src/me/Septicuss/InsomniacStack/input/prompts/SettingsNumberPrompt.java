package me.Septicuss.InsomniacStack.input.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class SettingsNumberPrompt extends NumericPrompt {

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, Number number) {
		context.setSessionData("value", number.doubleValue());
		return new SettingsSetPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return "§eType the new §fnumber §evalue in chat.";
	}


}
