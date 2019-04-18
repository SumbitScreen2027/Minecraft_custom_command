package com.guillaumevdn.customcommands.commands.action;

import java.util.List;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.CustomCommands;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.messenger.Messenger;
import com.guillaumevdn.gcore.lib.util.Utils;

public class ActionActionbar implements Action
{
	// ------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------

	public ActionActionbar(Player sender, List<String> data, String[] args)
	{
		String target = CustomCommands.inst().replaceString(data.get(0).replace(" ", ""), sender, args);
		String bar = Utils.format(CustomCommands.inst().replaceString(data.get(1), sender, args));
		bar = Utils.fillPlaceholderAPI(sender, bar);

		// target player
		if (target.equalsIgnoreCase("player")) {
			Messenger.actionBar(sender, bar.replace("&7{receiver}", sender.getName()));
		}
		// target everyone
		else if (target.equalsIgnoreCase("everyone")) {
			for (Player pl : Utils.getOnlinePlayers()) {
				Messenger.actionBar(sender, bar.replace("&7{receiver}", pl.getName()));
			}
		}
		// target player in argument
		else {
			try {
				Player newTarget = Utils.getPlayer(target);
				Messenger.actionBar(sender, bar.replace("&7{receiver}", newTarget.getName()));
			} catch (Throwable exception) {
				GLocale.MSG_GENERIC_INVALIDPLAYER.send(sender, "{plugin}", CustomCommands.inst().getName(), "{player}", target);
			}
		}
	}

	// ------------------------------------------------------------
	// Override : is over
	// ------------------------------------------------------------

	@Override
	public boolean isOver() {
		return true;
	}
}
