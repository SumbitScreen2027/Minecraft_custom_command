package com.guillaumevdn.customcommands;

import java.util.List;

import com.guillaumevdn.customcommands.lib.customcommand.CustomCommandsContainer;
import com.guillaumevdn.gcore.lib.GPluginConfig;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.configuration.YMLConfiguration;

/**
 * @author GuillaumeVDN
 */
public class ConfigCCMD extends GPluginConfig {

	public static YMLConfiguration baseConfig;

	public static List<String> commandsAliasesEdit;

	public static CustomCommandsContainer customCommands;

	@Override
	protected YMLConfiguration doLoad() throws Throwable {
		baseConfig = CustomCommands.inst().loadConfigurationFile("config.yml");

		commandsAliasesEdit = CollectionUtils.asLowercaseList(baseConfig.readMandatoryStringList("commands_aliases.edit"));

		(customCommands = new CustomCommandsContainer()).load();
		customCommands.values().forEach(cmd -> cmd.registerBukkit());

		return baseConfig;
	}

}
