package me.Septicuss.InsomniacStack.input.prompts;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.input.InputHandler.InputType;
import me.Septicuss.InsomniacStack.input.InputHandler.SettingType;
import me.Septicuss.InsomniacStack.menu.list.StackSettingsMenu;

public class SettingsSetPrompt extends MessagePrompt {

	public String getPromptText(ConversationContext context) {
		final FileConfiguration CONFIG = Files.getConfig(FileType.CONFIG);
		final Player player = (Player) context.getForWhom();

		final InputType inputType = InputType.valueOf(String.valueOf(context.getSessionData("type")));
		final SettingType settingType = SettingType.valueOf(String.valueOf(context.getSessionData("setting")));

		final String path = String.valueOf(context.getSessionData("path"));
		final Object object = context.getSessionData("value");

		if (inputType == InputType.LIST) {

			List<String> list = CONFIG.getStringList(path);
			list.add(String.valueOf(object));

			CONFIG.set(path, list);

		} else {

			CONFIG.set(path, object);

		}

		Files.saveConfig(FileType.CONFIG, CONFIG);

		switch (settingType) {
		case DROPS:
			// new DropSettingsMenu().openMenu(player);
			break;
		case STACK:
			new StackSettingsMenu().openMenu(player);
			break;
		}

		return "§aSetting Updated";
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}
}
