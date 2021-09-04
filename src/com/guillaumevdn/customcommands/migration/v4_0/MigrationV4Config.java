package com.guillaumevdn.customcommands.migration.v4_0;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.lib.serialization.SerializerCCMD;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.configuration.YMLConfiguration;
import com.guillaumevdn.gcore.lib.configuration.file.YMLError;
import com.guillaumevdn.gcore.lib.file.FileUtils;
import com.guillaumevdn.gcore.lib.file.ResourceExtractor;
import com.guillaumevdn.gcore.lib.migration.BackupBehavior;
import com.guillaumevdn.gcore.lib.migration.Migration;
import com.guillaumevdn.gcore.lib.migration.SilentFail;
import com.guillaumevdn.gcore.lib.number.NumberUtils;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.migration.YMLMigrationReading;
import com.guillaumevdn.gcore.migration.YMLMigrationWriting;

/**
 * @author GuillaumeVDN
 */
public final class MigrationV4Config extends Migration {

	public MigrationV4Config() {
		super(CustomCommands.inst(), "v3", "v3 -> v4 (config)", "data_v4/migrated_v4.0.0_config.DONTREMOVE");
	}

	@Override
	public boolean mustMigrate() {
		return super.mustMigrate() && getPluginFile("texts.yml").exists();
	}

	@Override
	protected void doMigrate() throws Throwable {
		// init serializers
		SerializerCCMD.init();

		// make sure all .yml files can be loaded with our new replacer
		List<String> errors = new ArrayList<>();
		attemptFilesOperation("making sure all files can be loaded by the new YAML parser", "file", BackupBehavior.RESTORE, getPluginFolder(), filterYMLIfNotOneOf("texts.yml"),
				file -> {
					try {
						new YMLConfiguration(getPlugin(), file);
					} catch (Throwable cause) {
						YMLError causeYML = ObjectUtils.findCauseOrNull(cause, YMLError.class);
						YMLError causeConfig = ObjectUtils.findCauseOrNull(cause, YMLError.class);
						List<String> errs = CollectionUtils.asList("This file can't be loaded with the new YAML parser : " + file.getPath());
						if (causeYML != null || causeConfig != null) {
							errs.add((causeYML != null ? causeYML : causeConfig).getMessage());
							errors.addAll(errs);
						} else {
							fail(errs, cause, BackupBehavior.RESTORE);
							throw new SilentFail();
						}
					}
				});

		attemptOperation("making sure all files can be loaded by the new YAML parser", BackupBehavior.RESTORE, () -> {
			if (!errors.isEmpty()) {
				fail(errors, null, BackupBehavior.RESTORE);
				throw new SilentFail();
			}
		});

		// extract new default configs
		attemptOperation("extracting default configs", BackupBehavior.RESTORE, () -> {
			if (!FileUtils.delete(getPluginFolder())) {
				fail("couldn't delete current folder", null, BackupBehavior.RESTORE);
				throw new SilentFail();
			}
			new ResourceExtractor(getPlugin(), getPluginFolder(), "resources/").extract(false, true);
		});

		// config
		File oldConfigFile = new File(getBackupFolder() + "/config.yml");
		attemptOperation("converting config.yml", BackupBehavior.RESTORE, () -> {
			if (oldConfigFile.exists()) {
				YMLMigrationReading oldConfig = new YMLMigrationReading(getPlugin(), oldConfigFile);
				YMLConfiguration config = new YMLConfiguration(getPlugin(), new File(getPluginFolder() + "/config.yml"));

				// data
				if (oldConfig.readString("data.backend", "").equalsIgnoreCase("MYSQL")) {
					config.write("data_backend.customcommands_users_v4", "MYSQL");
				}

				// save file
				countMod();
				config.save();
			}
		});

		// migration
		attemptSingleYMLFileToMultiple("converting commands", "command", BackupBehavior.RESTORE, oldConfigFile, "commands", id -> getPluginFile("commands/" + id + ".yml"), (src, o, target) -> {

			target.write("aliases", src.readStringList(o + ".aliases", null));

			for (String argId : src.readKeysForSection(o + ".arguments")) {
				String op = o + ".arguments." + argId;
				String n = "patterns." + argId;

				target.write(n + ".pattern", src.readString(op + ".pattern", src.readString(op + ".usage", null)));
				target.write(n + ".description", src.readString(op + ".description", null));
				target.write(n + ".permission", src.readString(op + ".permission", null));
				target.write(n + ".permission_error_message", src.readString(op + ".no_permission_message", null));
				target.write(n + ".worlds.whitelist", src.readStringList(op + ".worlds", null));
				int cooldown = src.readInteger(op + ".cooldown", 0);
				if (cooldown > 0) {
					target.write(n + ".cooldown", cooldown + " SECOND");
				}
				if (src.readBoolean(op + ".toggle", false)) {
					target.write(n + ".toggle_mode", true);
				}

				migratePerformList(src, op + ".perform_toggle_false", target, n + ".actions", false);
				migratePerformList(src, op + ".perform_toggle_true", target, n + ".actions", true);
				migratePerformList(src, op + ".perform", target, n + ".actions", null);
			}

			// count mod, this will save the file
			countMod();
		});

		// data is another migration, so we don't have to remigrate files if it fails
	}

	private static Map<String, String> types = CollectionUtils.asMap(
			"send message", "NOTIFY",
			"send title", "NOTIFY",
			"send actionbar", "NOTIFY",
			"wait ticks", "WAIT",
			"execute commands as", "EXECUTE_COMMANDS",
			"execute commands for", "EXECUTE_COMMANDS",
			"give item", "GIVE_ITEM",
			"teleport", "TELEPORT",
			"change gamemode", "CHANGE_GAMEMODE"
			);

	private void migratePerformList(YMLMigrationReading src, String o, YMLMigrationWriting target, String n, Boolean onToggleOn) {
		for (String key : src.readKeysForSection(o)) {
			
			String type = src.readString(o + "." + key + ".type", null);
			List<String> data = src.readStringList(o + "." + key + ".data", null);
			if (type != null && types.containsKey(type.toLowerCase()) && data != null && !data.isEmpty()) {

				// generic
				target.write(n + "." + key + ".type", types.get(type.toLowerCase()));

				if (!type.equals("wait ticks") && !type.equals("execute commands for") && !data.get(0).equalsIgnoreCase("player")) {
					target.write(n + "." + key + ".target", data.get(0));
				}

				if (onToggleOn != null) {
					target.write(n + "." + key + ".on_toggle_on", onToggleOn);
				}

				// type-specific settings
				if (type.equalsIgnoreCase("send message")) {
					List<String> msg = new ArrayList<>();
					for (int i = 1; i < data.size(); ++i) {
						msg.add(data.get(i).replace("{player}", "{sender}").replace("{receiver}", "{player}"));
					}
					target.write(n + "." + key + ".notify.message", msg);
				} else if (type.equalsIgnoreCase("send title")) {
					if (data.size() == 6) {
						target.write(n + "." + key + ".notify.title", data.get(1).replace("{player}", "{sender}").replace("{receiver}", "{player}"));
						target.write(n + "." + key + ".notify.title_subtitle", data.get(2).replace("{player}", "{sender}").replace("{receiver}", "{player}"));
						target.write(n + "." + key + ".notify.title_fade_in", data.get(3));
						target.write(n + "." + key + ".notify.title_duration", data.get(4));
						target.write(n + "." + key + ".notify.title_fade_out", data.get(5));
					}
				} else if (type.equalsIgnoreCase("send actionbar")) {
					if (data.size() == 2) {
						target.write(n + "." + key + ".notify.actionbar", data.get(1).replace("{player}", "{sender}").replace("{receiver}", "{player}"));
					}
				} else if (type.equalsIgnoreCase("wait ticks")) {
					if (data.size() == 1) {
						target.write(n + "." + key + ".duration", data.get(1) + " TICK");
					}
				} else if (type.equalsIgnoreCase("execute commands as")) {
					List<String> commands = new ArrayList<>();
					for (int i = 1; i < data.size(); ++i) {
						commands.add(data.get(i));
					}
					target.write(n + "." + key + ".as_player", true);
					target.write(n + "." + key + ".commands", commands);
				} else if (type.equalsIgnoreCase("execute commands for")) {
					List<String> commands = new ArrayList<>();
					for (int i = 0; i < data.size(); ++i) {
						commands.add(data.get(i));
					}
					target.write(n + "." + key + ".as_player", false);
					target.write(n + "." + key + ".commands", commands);
				} else if (type.equalsIgnoreCase("give item")) {
					// they will need to reconfigure the item
				} else if (type.equalsIgnoreCase("teleport")) {
					// they will need to reconfigure the location
				} else if (type.equalsIgnoreCase("change gamemode")) {
					if (data.size() == 2) {
						Integer nb = NumberUtils.integerOrNull(data.get(0));
						if (nb != null) {
							target.write(n + "." + key + ".commands", nb == 1 ? "CREATIVE" : (nb == 2 ? "ADVENTURE" : (nb == 3 ? "SPECTATOR" : "SURVIVAL")));
						}
					}
				}

			}
		}
	}

}
