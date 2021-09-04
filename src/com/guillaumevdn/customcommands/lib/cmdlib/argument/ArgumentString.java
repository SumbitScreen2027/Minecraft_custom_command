package com.guillaumevdn.customcommands.lib.cmdlib.argument;

import java.util.List;

import com.guillaumevdn.customcommands.lib.cmdlib.Argument;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.collection.LowerCaseArrayList;

public class ArgumentString extends Argument {

	private LowerCaseArrayList aliases;

	public ArgumentString(LowerCaseArrayList aliases, String def) {
		super(def, aliases.get(0).toLowerCase());
		this.aliases = aliases;
	}

	public LowerCaseArrayList getAliases() {
		return aliases;
	}

	@Override
	public List<String> tabComplete() {
		return CollectionUtils.asList(aliases.get(0));
	}

}
