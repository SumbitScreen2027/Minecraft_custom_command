package com.guillaumevdn.customcommands.lib.serialization.adapter;

import java.util.UUID;

import com.guillaumevdn.customcommands.data.user.UserCCMD;
import com.guillaumevdn.gcore.lib.concurrency.RWLowerCaseHashMap;
import com.guillaumevdn.gcore.lib.serialization.adapter.DataAdapter;
import com.guillaumevdn.gcore.lib.serialization.data.DataIO;

/**
 * @author GuillaumeVDN
 */
public final class AdapterUserCCMD extends DataAdapter<UserCCMD> {

	public static final AdapterUserCCMD INSTANCE = new AdapterUserCCMD();

	private AdapterUserCCMD() {
		super(UserCCMD.class, 1);
	}

	@Override
	public void write(UserCCMD user, DataIO writer) throws Throwable {
		writer.write("uuid", user.getUniqueId());

		writer.writeObject("lastUses", w -> user.getLastUses().forEach((id, last) -> w.write(id, last)));
		writer.writeObject("toggles", w -> user.getToggles().forEach((id, last) -> w.write(id, last)));
	}

	@Override
	public UserCCMD read(int version, DataIO reader) throws Throwable {
		if (version == 1) {
			UUID uuid = reader.readSerialized("uuid", UUID.class);

			RWLowerCaseHashMap<Long> lastUses = reader.readSimpleMap("lastUses", String.class, new RWLowerCaseHashMap<>(5, 1f), (key, __, r) -> r.readLong(key));
			if (lastUses == null) lastUses = new RWLowerCaseHashMap<>(5, 1f);

			RWLowerCaseHashMap<Boolean> toggles = reader.readSimpleMap("toggles", String.class, new RWLowerCaseHashMap<>(5, 1f), (key, __, r) -> r.readBoolean(key));
			if (toggles == null) toggles = new RWLowerCaseHashMap<>(5, 1f);

			return new UserCCMD(uuid, lastUses, toggles);
		}
		throw new IllegalArgumentException("unknown adapter version " + version);
	}

}
