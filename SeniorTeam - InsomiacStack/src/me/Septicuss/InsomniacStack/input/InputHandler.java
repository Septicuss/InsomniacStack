package me.Septicuss.InsomniacStack.input;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import me.Septicuss.InsomniacStack.InsomniacStack;
import me.Septicuss.InsomniacStack.input.prompts.SettingsNumberPrompt;
import me.Septicuss.InsomniacStack.input.prompts.SettingsStringPrompt;
import me.Septicuss.InsomniacStack.utils.Utils;

public class InputHandler {

	public enum InputType {
		TEXT, NUMBER, LIST;
	}

	public enum SettingType {
		STACK, DROPS
	}

	public static void inputSetting(Player player, InputType inputType, SettingType settingType, String settingPath) {

		/* Completely closing the players inventory */
		Utils.closeInventory(player);

		/* Setting data */
		Map<Object, Object> data = new HashMap<>();
		data.put("path", settingPath);
		data.put("type", inputType.toString());
		data.put("setting", settingType.toString());

		/* Setting the prompt */
		Prompt firstPrompt = null;

		switch (inputType) {
		case NUMBER:
			firstPrompt = new SettingsNumberPrompt();
			break;
		case TEXT:
			firstPrompt = new SettingsStringPrompt();
			break;
		default:
			firstPrompt = new SettingsStringPrompt();
			break;
		}

		/* Setting up and starting conversation */
		ConversationFactory conversationFactory = new ConversationFactory(InsomniacStack.getInstance())
				.withModality(true).withFirstPrompt(firstPrompt).thatExcludesNonPlayersWithMessage("")
				.withLocalEcho(false).withInitialSessionData(data);

		conversationFactory.buildConversation(player).begin();

	}

}
