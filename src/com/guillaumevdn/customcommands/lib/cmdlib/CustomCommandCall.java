package com.guillaumevdn.customcommands.lib.cmdlib;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.lib.customcommand.ElementCustomCommand;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.lib.permission.Permission;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

public class CustomCommandCall {

	private String commandName;
	private ElementCustomCommand customCommand;
	private CommandSender sender;
	private String[] arguments;

	public CustomCommandCall(CommandSender sender, String commandName, ElementCustomCommand customCommand, String[] arguments) {
		this.sender = sender;
		this.commandName = commandName;
		this.customCommand = customCommand;
		this.arguments = arguments;
	}

	public CommandSender getSender() {
		return sender;
	}

	public Player getSenderAsPlayer() {
		return ObjectUtils.castOrNull(sender, Player.class);
	}

	public boolean senderHasPermission(Permission permission) {
		return permission == null ? true : permission.has(sender);
	}

	public String getCommandName() {
		return commandName;
	}

	public ElementCustomCommand getCustomCommand() {
		return customCommand;
	}

	private Replacer replacer = Replacer.GENERIC;

	public Replacer getReplacer() {
		return replacer;
	}

	public void setReplacerPattern(CommandPattern pattern) {
		Player player = getSenderAsPlayer();
		replacer = Replacer
				.of("{sender}", () -> sender.getName())
				.with("{args}", () -> getFullArgsFrom(pattern, 0))
				.with("{playerworld}", () -> {
					return player != null ? player.getWorld().getName() : "/";
				});  // legacy thing
		if (player != null) {
			replacer = replacer.with(player);
		}
		for (int index = 0; index < Math.max(pattern.getArguments().size(), arguments.length); ++index) {
			final int i = index;
			replacer.with("{arg:" + (i + 1) + "}", () -> getArgAsString(pattern, i));
			replacer.with("{args:" + (i + 1) + "}", () -> getFullArgsFrom(pattern, i));
		}
	}

	private String getFullArgsFrom(CommandPattern pattern, int i) {
		String result = "";
		for (int j = i; j < Math.max(pattern.getArguments().size(), arguments.length); j++) result += " " + getArgAsString(pattern, j);
		if (result.length() > 0) result = result.substring(1);
		return result;
	}

	public String getAllArguments() {
		return getAllArguments(0);
	}

	public String getAllArguments(int beginIndex) {
		String result = "";
		for (int i = beginIndex; i < arguments.length; i++) {
			result += " " + arguments[i];
		}
		if (result.length() > 0) {
			result = result.substring(1);
		}
		return result;
	}

	public String[] getArgs() {
		return arguments;
	}

	public int getArgsSize() {
		return arguments.length;
	}

	public String getArgAsString(CommandPattern pattern, int index) {
		return index >= arguments.length ? pattern.getArguments().get(index).getDefaultValue() : arguments[index];
	}

	public int getArgAsInt(CommandPattern pattern, int index) {
		return Integer.parseInt(index >= arguments.length ? pattern.getArguments().get(index).getDefaultValue() : arguments[index]);
	}

	public double getArgAsDouble(CommandPattern pattern, int index) {
		return Double.parseDouble(index >= arguments.length ? pattern.getArguments().get(index).getDefaultValue() : arguments[index]);
	}

	public OfflinePlayer getArgAsOfflinePlayer(CommandPattern pattern, int index) {
		String name = index >= arguments.length ? pattern.getArguments().get(index).getDefaultValue() : arguments[index];
		return Objects.equals(name, "null") ? getSenderAsPlayer() : Bukkit.getOfflinePlayer(name);
	}

	public Player getArgAsPlayer(CommandPattern pattern, int index) {
		String name = index >= arguments.length ? pattern.getArguments().get(index).getDefaultValue() : arguments[index];
		return Objects.equals(name, "null") ? getSenderAsPlayer() : Bukkit.getPlayer(name);
	}

}
