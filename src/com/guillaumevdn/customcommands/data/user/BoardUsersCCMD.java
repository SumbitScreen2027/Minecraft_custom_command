package com.guillaumevdn.customcommands.data.user;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.gcore.GCore;
import com.guillaumevdn.gcore.lib.bukkit.BukkitThread;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.data.Query;
import com.guillaumevdn.gcore.lib.data.board.keyed.KeyReference;
import com.guillaumevdn.gcore.lib.data.board.keyed.UniKeyedBoardRemote;
import com.guillaumevdn.gcore.lib.file.FileUtils;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.lib.player.PlayerUtils;
import com.guillaumevdn.gcore.lib.serialization.Serializer;

/**
 * @author GuillaumeVDN
 */
public class BoardUsersCCMD extends UniKeyedBoardRemote<UUID, UserCCMD> {

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
		Set<KeyReference<UUID>> keys = new HashSet<>();
		for (Player player : PlayerUtils.getOnline()) {
			keys.add(new KeyReference<>(player.getUniqueId()));
		}
		pullElements(BukkitThread.ASYNC, keys, null);
	}

	@Override
	protected void pulledElement(BukkitThread thread, KeyReference<UUID> reference, UserCCMD value) {
		// no value ; create it
		UserCCMD user = getCachedValue(reference.getKey());
		if (user == null) {
			putValue(reference.getKey(), user = new UserCCMD(reference.getKey()), null, true);
		}
	}

	@Override
	protected void beforeDisposeCacheElement(BukkitThread thread, KeyReference<UUID> reference, UserCCMD value) {
		// not loaded
		UserCCMD user = getCachedValue(reference.getKey());
		if (user == null) {
			return;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// json
	// ----------------------------------------------------------------------------------------------------

	// file
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

	@Override
	protected void remotePullAllJson() throws Throwable {
		throw new UnsupportedOperationException();
	}

	// ----------------------------------------------------------------------------------------------------
	// mysql
	// ----------------------------------------------------------------------------------------------------

	// init
	private final String TABLE_NAME = getId();

	@Override
	protected void remoteInitMySQL() throws Throwable {
		GCore.inst().getMySQLConnector().performUpdateQuery(getPlugin(), getLogger(), ""
				+ "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
				+ "user_uuid CHAR(36) NOT NULL,"
				+ "data LONGTEXT NOT NULL,"
				+ "PRIMARY KEY(user_uuid)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET = 'utf8';"
				);
	}

	@Override
	protected void remotePullAllMySQL() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void remotePullElementsMySQL(Set<KeyReference<UUID>> references) throws Throwable {
		List<UUID> keys = references.stream().map(KeyReference::getKey).collect(Collectors.toList());
		GCore.inst().getMySQLConnector().performGetQuery(getPlugin(), getLogger(), Query.buildSelectKeysIn(TABLE_NAME, "user_uuid", Serializer.UUID.serialize(keys)), set -> {
			while (set.next()) {
				UUID uuid = UUID.fromString(set.getString("user_uuid")); // row can't contain an invalid UUID, since the query above was built from a valid UUID object
				String rawData = set.getString("data");
				try {
					UserCCMD user = CustomCommands.inst().getGson().fromJson(rawData, UserCCMD.class);
					if (user == null) {
						getLogger().warning("Found invalid user data for '" + uuid + "' in database, skipped it");
						continue;
					}
					cache.put(uuid, user);
				} catch (Throwable exception) {
					exception.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void remotePushElementsMySQL(Set<KeyReference<UUID>> references) throws Throwable {
		if (references.isEmpty()) return;  // let's avoid deleting the whole table just because there's no WHERE clause
		for (Collection<KeyReference<UUID>> splitElement : CollectionUtils.splitCollection(references, 999)) {  // multiple VALUES are limited to 1000 elements ; https://stackoverflow.com/questions/452859/inserting-multiple-rows-in-a-single-sql-query#comment22032805_452934
			Query query = new Query("INSERT INTO " + TABLE_NAME + "(user_uuid,data) VALUES ");
			int i = -1;
			for (KeyReference<UUID> reference : splitElement) {
				String comma = (++i + 1 < splitElement.size() ? "," : "");
				query.add("(" + Query.escapeValue(reference.getKey().toString()) + "," + Query.escapeValue(CustomCommands.inst().getGson().toJson(getCachedValue(reference.getKey()))) + ")" + comma);
			}
			query.add(" ON DUPLICATE KEY UPDATE data=VALUES(data);");
			GCore.inst().getMySQLConnector().performUpdateQuery(getPlugin(), getLogger(), query);
		}
	}

	@Override
	protected void remoteDeleteElementsMySQL(Set<KeyReference<UUID>> references) throws Throwable {
		if (references.isEmpty()) return;  // let's avoid deleting the whole table just because there's no WHERE clause
		Query query = new Query("DELETE FROM " + TABLE_NAME + " ");
		int i = -1;
		for (KeyReference<UUID> reference : references) {
			query.add((++i == 0 ? "WHERE" : "OR") + " (user_uuid=" + Query.escapeValue(reference.getKey().toString()) + ")");
		}
		query.add(";");
		GCore.inst().getMySQLConnector().performUpdateQuery(getPlugin(), getLogger(), query);
	}

}
