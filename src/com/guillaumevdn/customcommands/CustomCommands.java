package com.guillaumevdn.customcommands;

import java.io.File;
import java.util.List;

import com.guillaumevdn.customcommands.command.CustomcommandsEdit;
import com.guillaumevdn.customcommands.data.user.BoardUsersCCMD;
import com.guillaumevdn.customcommands.data.user.UserCCMD;
import com.guillaumevdn.customcommands.lib.action.ActionTypes;
import com.guillaumevdn.customcommands.lib.serialization.SerializerCCMD;
import com.guillaumevdn.customcommands.lib.serialization.adapter.AdapterUserCCMD;
import com.guillaumevdn.customcommands.listeners.ConnectionEvents;
import com.guillaumevdn.customcommands.listeners.PlayerEvents;
import com.guillaumevdn.customcommands.migration.v4_0.MigrationV4Config;
import com.guillaumevdn.customcommands.migration.v4_0.MigrationV4Data;
import com.guillaumevdn.gcore.integration.placeholderapi.IntegrationInstancePlaceholderAPI;
import com.guillaumevdn.gcore.lib.GPlugin;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.integration.Integration;
import com.guillaumevdn.gcore.lib.string.TextFile;
import com.guillaumevdn.gcore.libs.com.google.gson.GsonBuilder;

/**
 * @author GuillaumeVDN
 */
public final class CustomCommands extends GPlugin<ConfigCCMD, PermissionCCMD> {

	private static CustomCommands instance;
	public static CustomCommands inst() { return instance; }

	public CustomCommands() {
		super(14363, "customcommands", "ccmd", ConfigCCMD.class, PermissionCCMD.class, "data_v4",
				MigrationV4Config.class, MigrationV4Data.class
				);
		instance = this;
	}

	@Override
	public GsonBuilder createGsonBuilder() {
		return super.createGsonBuilder().registerTypeAdapter(UserCCMD.class, AdapterUserCCMD.INSTANCE.getGsonAdapter());
	}

	// ----- data

	private ActionTypes actionTypes;

	public ActionTypes getActionTypes() {
		return actionTypes;
	}

	// ----- plugin

	@Override
	public void registerTypes() {
		SerializerCCMD.init();
		actionTypes = new ActionTypes();
	}

	@Override
	protected List<String> extractDefaultConfigIgnoreStartingPaths() {
		File commandsFile = getDataFile("commands");
		return commandsFile.exists() ? CollectionUtils.asList("commands/") : null;
	}

	@Override
	protected void registerTexts() {
		registerTextFile(new TextFile<>(CustomCommands.inst(), "customcommands.yml", TextCCMD.class));
		registerTextFile(new TextFile<>(CustomCommands.inst(), "customcommands_editor.yml", TextCCMD.class));
	}

	@Override
	protected void registerAndEnableIntegrations() {
		registerAndEnableIntegration(new Integration<>(this, "PlaceholderAPI", IntegrationInstancePlaceholderAPI.class));
	}

	@Override
	protected void registerData() {
		registerDataBoard(new BoardUsersCCMD());
	}

	@Override
	protected void enable() throws Throwable {
		getMainCommand().setSubcommand(new CustomcommandsEdit());

		registerListener("player", new PlayerEvents());
		registerListener("connection", new ConnectionEvents());
	}

	@Override
	protected void disable() throws Throwable {
		ConfigCCMD.customCommands.values().forEach(cmd -> cmd.unregisterBukkit());
	}

}
