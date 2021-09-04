package com.guillaumevdn.customcommands.migration.v4_0;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.data.user.UserCCMD;
import com.guillaumevdn.customcommands.lib.customcommand.CustomCommandsContainer;
import com.guillaumevdn.customcommands.lib.customcommand.ElementCustomCommand;
import com.guillaumevdn.customcommands.lib.customcommand.ElementPattern;
import com.guillaumevdn.customcommands.lib.serialization.SerializerCCMD;
import com.guillaumevdn.customcommands.listeners.V3UserCCMD;
import com.guillaumevdn.gcore.GCore;
import com.guillaumevdn.gcore.lib.concurrency.RWLowerCaseHashMap;
import com.guillaumevdn.gcore.lib.configuration.YMLConfiguration;
import com.guillaumevdn.gcore.lib.data.Query;
import com.guillaumevdn.gcore.lib.file.FileUtils;
import com.guillaumevdn.gcore.lib.migration.BackupBehavior;
import com.guillaumevdn.gcore.lib.migration.Migration;
import com.guillaumevdn.gcore.lib.number.NumberUtils;
import com.guillaumevdn.gcore.libs.com.google.gson.Gson;
import com.guillaumevdn.gcore.migration.v8_0.data.InstantMySQL;

/**
 * @author GuillaumeVDN
 */
public final class MigrationV4Data extends Migration {

	public MigrationV4Data() {
		super(CustomCommands.inst(), null, "v3 -> v4 (data)", "data_v4/migrated_v4.0.0_data.DONTREMOVE");
	}

	@Override
	public boolean mustMigrate() {
		return true;  // if not done already
	}

	private final Gson gson = getPlugin().createGsonBuilder().create();
	private final Gson prettyGson = getPlugin().createGsonBuilder().setPrettyPrinting().create();
	private InstantMySQL mysql = null;
	private CustomCommandsContainer customCommands = new CustomCommandsContainer();

	@Override
	protected void doMigrate() throws Throwable {
		YMLConfiguration config = getPlugin().loadConfigurationFile("config.yml");

		// init QC serializers
		SerializerCCMD.init();

		// load custom commands (needed for user cooldowns conversion)
		CustomCommands.inst().registerTypes();
		customCommands.load();

		// mysql
		if (config.readString("data_backend.customcommands_users_v4", "JSON").equalsIgnoreCase("MYSQL")) {
			// connect
			attemptOperation("connecting to MySQL", BackupBehavior.NONE, () -> {
				YMLConfiguration gcoreConfig = GCore.inst().loadConfigurationFile("config.yml");
				String host = gcoreConfig.readMandatoryString("mysql.host");
				String name = gcoreConfig.readMandatoryString("mysql.name");
				String usr = gcoreConfig.readMandatoryString("mysql.user");
				String pwd = gcoreConfig.readMandatoryString("mysql.pass");
				String customArgs = gcoreConfig.readString("mysql.args", "");
				String url = "jdbc:mysql://" + host + "/" + name + "?allowMultiQueries=true" + customArgs;
				mysql = new InstantMySQL(url, usr, pwd);
			});

			// create tables
			attemptOperation("creating MySQL tables", BackupBehavior.NONE, () -> {
				mysql.performUpdateQuery(getPlugin(), new Query("DROP TABLE IF EXISTS customcommands_users_v4;"
						+ "CREATE TABLE customcommands_users_v4("
						+ "user_uuid CHAR(36) NOT NULL,"
						+ "data LONGTEXT NOT NULL,"
						+ "PRIMARY KEY(user_uuid)"
						+ ") ENGINE=InnoDB DEFAULT CHARSET = 'utf8';"));
			});

			// arenas
			attemptOperation("converting MySQL arenas board", BackupBehavior.NONE, () -> {
				ResultSet set = mysql.performGetQuery(getPlugin(), new Query("SELECT * FROM customcommands_users"));
				while (set.next()) {
					UUID uuid = null;
					try {
						// read
						uuid = migrateUserInfo(set.getString("id"));
						UserCCMD arena = migrateUserData(uuid,
								set.getString("cooldown_ends"),
								set.getString("toggles"),
								null);
						// write
						mysql.performUpdateQuery(getPlugin(), new Query("INSERT INTO customcommands_users_v4 (user_uuid,data) VALUES (" + Query.escapeValue(uuid.toString()) + ", " + Query.escapeValue(gson.toJson(arena)) + ");"));
						countMod();
					} catch (Throwable exception) {
						error("Couldn't convert saved arena " + uuid + ", skipping", exception);
					}
				}
				set.close();
			});

			// close connection
			mysql.close();
		}
		// json
		else {

			// users
			attemptOperation("converting JSON users board", BackupBehavior.NONE, () -> {
				File srcUsers = new File(getPluginFolder().getParentFile() + "/GCore_backup_on_v7/userdata");
				if (srcUsers.exists()) {
					File targetUsers = new File(getPluginFolder() + "/data_v4/users");
					for (File userRoot : srcUsers.listFiles()) {
						File srcUser = new File(userRoot + "/customcommands_user.json");
						if (srcUser.exists()) {
							try {
								UserCCMD user = migrateUserData(migrateUserInfo(userRoot.getName()), null, null, new FileReader(srcUser));
								toJson(user, new File(targetUsers + "/" + user.getUniqueId() + ".json"));
								countMod();
							} catch (Throwable exception) {
								error("Couldn't convert saved user " + FileUtils.getSimpleName(srcUser) + ", skipping", exception);
							}
						}
					}
				}
			});
		}
	}

	private UUID migrateUserInfo(String userInfo) {
		if (userInfo == null) { // might happen
			return null;
		}
		int index = userInfo.indexOf('_');
		return UUID.fromString(index == -1 ? userInfo : userInfo.substring(0, index));
	}

	private UserCCMD migrateUserData(UUID uuid, String cooldownEndsRaw, String togglesRaw, FileReader fromFile) throws Throwable {
		RWLowerCaseHashMap<Long> lastUses = new RWLowerCaseHashMap<>(10, 1f);
		RWLowerCaseHashMap<Boolean> toggles = new RWLowerCaseHashMap<>(10, 1f);

		if (fromFile != null) {
			V3UserCCMD v3 = gson.fromJson(fromFile, V3UserCCMD.class);
			if (v3.cooldownEnds != null) {
				v3.cooldownEnds.forEach((id, end) -> {
					String[] split = id.split("\\.");
					String commandId = split[0];
					String patternId = split[1];
					ElementCustomCommand command = customCommands.getElement(commandId).orNull();
					ElementPattern pattern = command == null ? null : command.getPatterns().getElement(patternId).orNull();
					Long cooldown = pattern == null ? null : pattern.getCooldown().parseGeneric().orElse(0L);
					if (pattern != null && cooldown > 0L) {
						lastUses.put(id, end - cooldown);
					}
				});
			}
			if (v3.toggles != null) {
				toggles.putAll(v3.toggles);
			}
		} else {
			Map<String, String> mapCooldownEnds = cooldownEndsRaw == null ? null : gson.fromJson(cooldownEndsRaw, new HashMap<String, String>().getClass());
			Map<String, String> mapToggles = togglesRaw == null ? null : gson.fromJson(togglesRaw, new HashMap<String, String>().getClass());

			if (mapCooldownEnds != null) {
				mapCooldownEnds.forEach((id, endRaw) -> {
					Long end = NumberUtils.longOrNull(endRaw);
					if (end != null) {
						String[] split = id.split("\\.");
						String commandId = split[0];
						String patternId = split[1];
						ElementCustomCommand command = customCommands.getElement(commandId).orNull();
						ElementPattern pattern = command == null ? null : command.getPatterns().getElement(patternId).orNull();
						Long cooldown = pattern == null ? null : pattern.getCooldown().parseGeneric().orElse(0L);
						if (pattern != null && cooldown > 0L) {
							lastUses.put(id, end - cooldown);
						}
					}
				});
			}
			if (mapToggles != null) {
				mapToggles.forEach((id, toggle) -> {
					toggles.put(id, Boolean.valueOf(toggle));
				});
			}
		}

		return new UserCCMD(uuid, lastUses, toggles);
	}

	private void toJson(Object object, File file) throws IOException {
		file.getParentFile().mkdirs();
		FileUtils.reset(file);
		try (FileWriter writer = new FileWriter(file)) {
			prettyGson.toJson(object, writer);
		}
	}

}
