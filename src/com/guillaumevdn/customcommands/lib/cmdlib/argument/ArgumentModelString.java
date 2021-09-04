package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;

public class ArgumentModelString extends ArgumentModel {

	public ArgumentModelString(String model, String def, String description) {
		super(model, def, description);
	}

	@Override
	public CommandPatternResult call(CommandSender sender, String raw) {
		return new CommandPatternResult(Result.MATCH, 1);
	}

	@Override
	public List<String> tabComplete() {
		return CollectionUtils.asList(getDescription().replace(' ', '_'));
	}

}
