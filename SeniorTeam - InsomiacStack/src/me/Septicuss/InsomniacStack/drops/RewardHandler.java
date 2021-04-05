package me.Septicuss.InsomniacStack.drops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import me.Septicuss.InsomniacStack.files.Files;
import me.Septicuss.InsomniacStack.files.Files.FileType;
import me.Septicuss.InsomniacStack.objects.RandomCollection;
import me.Septicuss.InsomniacStack.objects.Reward;

public class RewardHandler {

	// Reward is a list of commands with a chance

	public static void addReward(Reward reward) {

		final FileConfiguration DATA = Files.getConfig(FileType.DATA);
		List<String> rewards = new ArrayList<>();

		if (DATA.isSet("rewards")) {
			rewards = DATA.getStringList("rewards");
		}

		final String serializedReward = serializeReward(reward);
		rewards.add(serializedReward);

		DATA.set("rewards", rewards);
		Files.saveConfig(FileType.DATA, DATA);

	}

	public static void removeReward(Reward reward) {

		final FileConfiguration DATA = Files.getConfig(FileType.DATA);

		if (!DATA.isSet("rewards")) {
			return;
		}

		final List<String> rewards = DATA.getStringList("rewards");
		final String serializedReward = serializeReward(reward);

		rewards.remove(serializedReward);

		DATA.set("rewards", rewards);
		Files.saveConfig(FileType.DATA, DATA);

	}

	public static List<Reward> getRewards() {

		final FileConfiguration DATA = Files.getConfig(FileType.DATA);

		if (!DATA.isSet("rewards")) {
			return null;
		}

		final List<String> rewardsData = DATA.getStringList("rewards");
		List<Reward> rewards = new ArrayList<>();

		rewardsData.forEach(serializedReward -> {
			rewards.add(deserializeReward(serializedReward));
		});

		return rewards;
	}

	public static Reward getRandomReward(Double chanceBooster) {

		final List<Reward> rewards = getRewards();
		RandomCollection<Reward> randomRewards = new RandomCollection<>();
		randomRewards.setFullPercentage(true);

		if (rewards == null || rewards.isEmpty()) {
			return null;
		}

		Double chances = 0D;

		for (Reward reward : rewards) {
			randomRewards.add(reward.getChance() + chanceBooster, reward);
			chances += reward.getChance();
		}

		return randomRewards.next();
	}

	public static void clearRewards() {

		final FileConfiguration DATA = Files.getConfig(FileType.DATA);

		if (!DATA.isSet("rewards")) {
			return;
		}

		DATA.set("rewards", null);
		Files.saveConfig(FileType.DATA, DATA);
	}

	public static void setRewards(List<Reward> rewards) {

		final FileConfiguration DATA = Files.getConfig(FileType.DATA);

		List<String> serializedRewards = new ArrayList<>();

		rewards.forEach(reward -> {
			serializedRewards.add(serializeReward(reward));
		});

		DATA.set("rewards", serializedRewards);
		Files.saveConfig(FileType.DATA, DATA);
	}

	public static String serializeReward(Reward reward) {

		final List<String> commands = reward.getCommands();
		final Double chance = reward.getChance();

		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append(chance);

		for (String command : commands) {
			sBuilder.append("!" + command);
		}

		return sBuilder.toString();

	}

	public static Reward deserializeReward(String data) {

		final String[] dataArray = data.split("!");

		final Double chance = Double.valueOf(dataArray[0]);
		final List<String> commands = new ArrayList<>();

		for (int i = 1; i < dataArray.length; i++) {
			commands.add(dataArray[i]);
		}

		return new Reward(commands, chance);

	}

}
