package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.lib.cmdlib.Argument;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult;
import com.guillaumevdn.gcore.lib.number.NumberUtils;

public abstract class ArgumentModel extends Argument {

	private String model;

	public ArgumentModel(String model, String def, String description) {
		super(def, description);
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	public abstract CommandPatternResult call(CommandSender sender, String rawArgument);

	public static ArgumentModel decode(String model, String def, String description) {
		model = model.toLowerCase();
		if (model.startsWith("[string]")) {
			return new ArgumentModelString("[string]", def, description);
		} else if (model.startsWith("[string:")) {
			String[] split = model.substring("[string:".length()).replace("]", "").split("\\:");
			if (split.length == 2) {
				Integer min = NumberUtils.integerOrNull(split[0]);
				Integer max = NumberUtils.integerOrNull(split[1]);
				if (min != null && max != null) {
					return new ArgumentModelStringSized("[string:" + min + ":" + max + "]", min, max, def, description);
				}
			}
			return null;
		} else if (model.startsWith("[integer]")) {
			return new ArgumentModelInteger("[integer]", def, description);
		} else if (model.startsWith("[double]")) {
			return new ArgumentModelDouble("[double]", def, description);
		} else if (model.startsWith("[player]")) {
			return new ArgumentModelOfflinePlayer("[player]", def, description);
		} else if (model.startsWith("[player-online]")) {
			return new ArgumentModelPlayer("[player-online]", def, description);
		} else if (model.startsWith("[phrase]") || model.startsWith("[infinite]")) {
			return new ArgumentModelPhrase("[phrase]", def, description);
		}
		return null;
	}

}
