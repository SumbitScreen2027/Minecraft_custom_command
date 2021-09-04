package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.lib.player.PlayerUtils;

public class ArgumentModelOfflinePlayer extends ArgumentModel {

	public ArgumentModelOfflinePlayer(String model, String def, String description) {
		super(model, def, description);
	}

	@Override
	public CommandPatternResult call(CommandSender sender, String raw) {
		OfflinePlayer player = raw.equals("null") ? (ObjectUtils.instanceOf(sender, OfflinePlayer.class) ? (OfflinePlayer) sender : null) : Bukkit.getOfflinePlayer(raw);
		return player != null ? new CommandPatternResult(Result.MATCH) : new CommandPatternResult(Result.ERROR, raw, TextCCMD.messageInvalidInputOfflinePlayer);
	}

	@Override
	public List<String> tabComplete() {
		return PlayerUtils.getOnlineStream().map(OfflinePlayer::getName).collect(Collectors.toList());
	}

}
