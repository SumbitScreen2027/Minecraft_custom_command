package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.lib.player.PlayerUtils;

public class ArgumentModelPlayer extends ArgumentModel {

	public ArgumentModelPlayer(String model, String def, String description) {
		super(model, def, description);
	}

	@Override
	public CommandPatternResult call(CommandSender sender, String raw) {
		Player player = raw.equals("null") ? (ObjectUtils.instanceOf(sender, Player.class) ? (Player) sender : null) : Bukkit.getPlayer(raw);
		return player != null ? new CommandPatternResult(Result.MATCH) : new CommandPatternResult(Result.ERROR, raw, TextCCMD.messageInvalidInputPlayer);
	}

	@Override
	public List<String> tabComplete() {
		return PlayerUtils.getOnlineStream().map(OfflinePlayer::getName).collect(Collectors.toList());
	}

}
