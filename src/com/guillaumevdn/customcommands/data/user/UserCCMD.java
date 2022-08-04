package com.guillaumevdn.customcommands.data.user;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.guillaumevdn.gcore.lib.bukkit.BukkitThread;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.concurrency.RWLowerCaseHashMap;

/**
 * @author GuillaumeVDN
 */
public final class UserCCMD {

	private final UUID playerUUID;

	private RWLowerCaseHashMap<Long> lastUses;
	private RWLowerCaseHashMap<Boolean> toggles;

	UserCCMD(UUID playerUUID) {
		this(playerUUID, new RWLowerCaseHashMap<>(5, 1f), new RWLowerCaseHashMap<>(5, 1f));
	}

	public UserCCMD(UUID playerUUID, RWLowerCaseHashMap<Long> lastUses, RWLowerCaseHashMap<Boolean> toggles) {
		this.playerUUID = playerUUID;
		this.lastUses = lastUses;
		this.toggles = toggles;
	}

	public UUID getUniqueId() {
		return playerUUID;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	// ----- last uses

	public RWLowerCaseHashMap<Long> getLastUses() {
		return lastUses;
	}

	public Long getLastUse(String commandId, String patternId) {
		return lastUses.get(commandId + "." + patternId);
	}

	public void setLastUse(String commandId, String patternId) {
		lastUses.put(commandId + "." + patternId, System.currentTimeMillis());
		setToSave();
	}

	// ----- toggles

	public RWLowerCaseHashMap<Boolean> getToggles() {
		return toggles;
	}

	public boolean isToggled(String commandId, String patternId) {
		return toggles.getOrDefault(commandId + "." + patternId, false);
	}

	public void toggle(String commandId, String patternId) {
		toggles.put(commandId + "." + patternId, !isToggled(commandId, patternId));
		setToSave();
	}

	// ----- data

	public void setToSave() {
		BoardUsersCCMD.inst().addCachedToSave(playerUUID);
	}

	// ----- cached operations / process / fetch / etc

	public static void fetch(Player key, Consumer<UserCCMD> consumer) { BoardUsersCCMD.inst().fetchValue(key.getUniqueId(), consumer, null, false, true); }
	public static void process(UUID key, Consumer<UserCCMD> processor) { BoardUsersCCMD.inst().fetchValue(key, user -> { processor.accept(user); if (Bukkit.getPlayer(key) == null) { BoardUsersCCMD.inst().disposeCacheElements(BukkitThread.ASYNC, CollectionUtils.asSet(key), null); } }, null, false, true); }
	public static UserCCMD cachedOrNull(Player key) { return key == null ? null : cachedOrNull(key.getUniqueId()); }
	public static UserCCMD cachedOrNull(UUID key) { return BoardUsersCCMD.inst().getCachedValue(key); }
	public static void forCached(Player key, Consumer<UserCCMD> ifCached) { UserCCMD user = cachedOrNull(key); if (user != null) { ifCached.accept(user); } }
	public static void forCached(UUID key, Consumer<UserCCMD> ifCached) { UserCCMD user = cachedOrNull(key); if (user != null) { ifCached.accept(user); } }
	public static <T> T forCached(Player key, Function<UserCCMD, T> ifCached, T def) { UserCCMD user = cachedOrNull(key); return user == null ? def : ifCached.apply(user); }

}
