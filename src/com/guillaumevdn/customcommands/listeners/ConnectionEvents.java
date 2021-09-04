package com.guillaumevdn.customcommands.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.guillaumevdn.customcommands.data.user.BoardUsersCCMD;
import com.guillaumevdn.gcore.lib.bukkit.BukkitThread;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.data.board.keyed.KeyReference;

/**
 * @author GuillaumeVDN
 */
public class ConnectionEvents implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		// init board
		BoardUsersCCMD.inst().pullElements(BukkitThread.ASYNC, CollectionUtils.asSet(KeyReference.of(player.getUniqueId())), null);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		BoardUsersCCMD.inst().disposeCacheElements(BukkitThread.ASYNC, CollectionUtils.asSet(KeyReference.of(player.getUniqueId())), null);
	}

}
