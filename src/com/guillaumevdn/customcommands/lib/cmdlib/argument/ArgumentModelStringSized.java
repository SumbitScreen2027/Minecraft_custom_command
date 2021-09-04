package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;

public class ArgumentModelStringSized extends ArgumentModel {

	private int min;
	private int max;

	public ArgumentModelStringSized(String model, int min, int max, String def, String description) {
		super(model, def, description);
		this.min = min;
		this.max = max;
	}

	@Override
	public CommandPatternResult call(CommandSender sender, String raw) {
		return raw.length() >= min && raw.length() <= max ? new CommandPatternResult(Result.MATCH) : new CommandPatternResult(Result.ERROR, raw, TextCCMD.messageInvalidInputStringSize);
	}

	@Override
	public List<String> tabComplete() {
		return CollectionUtils.asList(getDescription().replace(' ', '_'));
	}

}

