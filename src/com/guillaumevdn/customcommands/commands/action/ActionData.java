package com.guillaumevdn.customcommands.commands.action;

import java.util.List;

import com.guillaumevdn.gcore.lib.configuration.YMLConfiguration;
import com.guillaumevdn.gcore.lib.util.Utils;

public class ActionData
{
	// ------------------------------------------------------------
	// Fields and constructor
	// ------------------------------------------------------------

	private String path, type;
	private List<String> data;

	public ActionData(YMLConfiguration file, String path)
	{
		this.path = path;
		this.type = file.getString(path + ".type", null);
		this.data = file.getList(path + ".data", Utils.emptyList());
	}

	// ------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------

	public String getPath() {
		return path;
	}

	public String getType() {
		return type;
	}

	public List<String> getData() {
		return data;
	}
}
