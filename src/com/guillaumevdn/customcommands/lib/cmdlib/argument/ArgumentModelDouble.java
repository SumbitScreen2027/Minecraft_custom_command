package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.number.NumberUtils;

public class ArgumentModelDouble extends ArgumentModel {

	public ArgumentModelDouble(String model, String def, String description) {
		super(model, def, description);
	}

	@Override
	public CommandPatternResult call(CommandSender sender, String raw) {
		return NumberUtils.doubleOrNull(raw) != null ? new CommandPatternResult(Result.MATCH) : new CommandPatternResult(Result.ERROR, raw, TextCCMD.messageInvalidInputDouble);
	}

	@Override
	public List<String> tabComplete() {
		return CollectionUtils.asList("1", "5", "10", "20");
	}

}
