package com.guillaumevdn.customcommands.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.guillaumevdn.customcommands.ConfigCCMD;
import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.customcommands.lib.customcommand.ElementCustomCommand;

/**
 * @author GuillaumeVDN
 */
public final class PlayerEvents implements Listener {

	/*private static final WeakHashMap<Player, List<String>> performingCommandsAs = new WeakHashMap<>();  // for action "EXECUTE_COMMANDS" with "as_player: true"

	public static void performCommandAs(Player player, String command) {
		performingCommandsAs.computeIfAbsent(player, __ -> new ArrayList<>()).add(command.toLowerCase());
	}*/

	private long last = 0L;

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void event(PlayerCommandPreprocessEvent event) {
		if (System.currentTimeMillis() - last < 20L) return;
		last = System.currentTimeMillis();

		Player sender = event.getPlayer();
		// performing command as
		/*List<String> performingAs = performingCommandsAs.get(sender);
		if (performingAs != null && performingAs.remove(event.getMessage())) {
			return;
		}*/

		// look for custom command
		int index = event.getMessage().indexOf(' ');
		String root = event.getMessage().substring(1, index != -1 ? index : event.getMessage().length());
		String[] args = index != -1 ? event.getMessage().substring(index + 1).split(" ") : new String[0];
		for (ElementCustomCommand command : ConfigCCMD.customCommands.values()) {
			for (String alias : command.getAliases().parseGeneric().orEmptyList()) {
				if (alias.equalsIgnoreCase(root)) {
					event.setCancelled(true);
					CustomCommands.inst().operateSync(() -> {
						try {
							command.getCmdlibCommand().call(new CustomCommandCall(sender, alias, command, args));
						} catch (Throwable exception) {
							CustomCommands.inst().getMainLogger().error("An error occured during processing command " + command.getId() + " (/'" + alias + "')", exception);
							sender.sendMessage("§cAn error occured during processing of the command.");
						}
					});
					return;
				}
			}
		}
	}

}
