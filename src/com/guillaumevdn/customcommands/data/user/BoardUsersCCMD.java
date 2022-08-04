package com.guillaumevdn.customcommands.data.user;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.gcore.GCore;
import com.guillaumevdn.gcore.lib.bukkit.BukkitThread;
import com.guillaumevdn.gcore.lib.data.board.keyed.ConnectorKeyed;
import com.guillaumevdn.gcore.lib.data.board.keyed.ConnectorKeyedJson;
import com.guillaumevdn.gcore.lib.data.board.keyed.ConnectorKeyedSQL;
import com.guillaumevdn.gcore.lib.data.board.keyed.KeyedBoardRemote;
import com.guillaumevdn.gcore.lib.data.sql.SQLHandler;
import com.guillaumevdn.gcore.lib.data.sql.SQLiteHandler;
import com.guillaumevdn.gcore.lib.file.FileUtils;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.lib.player.PlayerUtils;

/**
 * @author GuillaumeVDN
 */
public class BoardUsersCCMD extends KeyedBoardRemote<UUID, UserCCMD> {

	private static BoardUsersCCMD instance = null;
	public static BoardUsersCCMD inst() { return instance; }

	public BoardUsersCCMD() {
		super(CustomCommands.inst(), "customcommands_users_v4", UserCCMD.class, 20 * 60);
		instance = this;
	}

	// ----------------------------------------------------------------------------------------------------
	// data
	// ----------------------------------------------------------------------------------------------------

	@Override
	protected void onInitialized() {
		pullOnline();
	}

	public void pullOnline() {
		Set<UUID> keys = new HashSet<>();
		for (Player player : PlayerUtils.getOnline()) {
			keys.add(player.getUniqueId());
		}
		pullElements(BukkitThread.ASYNC, keys, null);
	}

	@Override
	protected void pulledElement(BukkitThread thread, UUID reference, UserCCMD value) {
		// no value ; create it
		UserCCMD user = getCachedValue(reference);
		if (user == null) {
			putValue(reference, user = new UserCCMD(reference), null, true);
		}
	}

	@Override
	protected void beforeDisposeCacheElement(BukkitThread thread, UUID reference, UserCCMD value) {
		// not loaded
		UserCCMD user = getCachedValue(reference);
		if (user == null) {
			return;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// json
	// ----------------------------------------------------------------------------------------------------

	@Override
	protected ConnectorKeyed<UUID, UserCCMD> createConnectorJson() {
		return new ConnectorKeyedJson<UUID, UserCCMD>(this) {
			@Override
			public File getRoot() {
				return CustomCommands.inst().getDataFile("data_v4/users/");
			}

			@Override
			public File getFile(UUID key) {
				return CustomCommands.inst().getDataFile("data_v4/users/" + key + ".json");
			}

			@Override
			public UUID getKey(File file) {
				return ObjectUtils.uuidOrNull(FileUtils.getSimpleName(file));
			}
		};
	}

	// ----------------------------------------------------------------------------------------------------
	// mysql
	// ----------------------------------------------------------------------------------------------------

	private ConnectorKeyedSQL<UUID, UserCCMD> createConnectorSQL(SQLHandler handler) {
		return new ConnectorKeyedSQL<UUID, UserCCMD>(this, handler) {
			@Override
			public String keyName() {
				return "user_uuid";
			}

			@Override
			protected UUID decodeKey(String raw) {
				return UUID.fromString(raw);  // row can't contain an invalid UUID, since the query was built from a valid UUID object
			}

			@Override
			protected UserCCMD decodeValue(String jsonData) {
				return CustomCommands.inst().getGson().fromJson(jsonData, UserCCMD.class);
			}

			@Override
			protected String encodeValue(UserCCMD value) {
				return CustomCommands.inst().getGson().toJson(value);
			}
		};
	}

	@Override
	protected ConnectorKeyed<UUID, UserCCMD> createConnectorMySQL() {
		return createConnectorSQL(GCore.inst().getMySQLHandler());
	}

	@Override
	protected ConnectorKeyed<UUID, UserCCMD> createConnectorSQLite() {
		return createConnectorSQL(new SQLiteHandler(CustomCommands.inst().getDataFile("data_v4/users.sqlite.db")));
	}

}
