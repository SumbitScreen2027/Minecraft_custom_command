package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;

public class ArgumentModelPhrase extends ArgumentModel {

	public ArgumentModelPhrase(String model, String def, String description) {
		super(model, def, description);
	}

	private static final int modelPhraseWildcards = 1000;

	@Override
	public CommandPatternResult call(CommandSender sender, String raw) {
		if (raw == null) {  // empty phrase
			return new CommandPatternResult(Result.ERROR, raw, TextCCMD.messageInvalidInputPhrase);
		}
		return new CommandPatternResult(Result.MATCH, modelPhraseWildcards);
	}

	@Override
	public List<String> tabComplete() {
		return CollectionUtils.asList(getDescription().replace(' ', '_'));
	}

}
