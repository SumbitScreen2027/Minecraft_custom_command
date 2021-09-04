package com.guillaumevdn.customcommands.lib.cmdlib;

import java.util.List;

public abstract class Argument {

	private String def;
	private String description;

	public Argument(String def, String description) {
		this.def = def;
		this.description = description;
	}

	public String getDefaultValue() {
		return def;
	}

	public String getDescription() {
		return description;
	}

	public abstract List<String> tabComplete();

}
