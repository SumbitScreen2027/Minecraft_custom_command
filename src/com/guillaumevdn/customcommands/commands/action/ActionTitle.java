package com.guillaumevdn.customcommands.commands.action;

import java.util.List;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.CustomCommands;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.Logger;
import com.guillaumevdn.gcore.lib.Logger.Level;
import com.guillaumevdn.gcore.lib.messenger.Messenger;
import com.guillaumevdn.gcore.lib.util.Utils;

public class ActionTitle implements Action
{
	// ------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------

	public ActionTitle(Player sender, List<String> data, String[] args)
	{
		String target = CustomCommands.inst().replaceString(data.get(0).replace(" ", ""), sender, args);
		String title = Utils.fillPlaceholderAPI(sender, Utils.format(CustomCommands.inst().replaceString(data.get(1), sender, args))), subtitle = Utils.fillPlaceholderAPI(sender, Utils.format(CustomCommands.inst().replaceString(data.get(2), sender, args)));

		int fadeIn, duration, fadeOut;

		try
		{
			fadeIn = Integer.parseInt(data.get(3));
			duration = Integer.parseInt(data.get(4));
			fadeOut = Integer.parseInt(data.get(5));
		}
		catch (Throwable exception) {
			Logger.log(Level.WARNING, CustomCommands.inst(), "Could not find the fadeIn, duration or fadeOut.");
			return;
		}

		// target player
		if (target.equalsIgnoreCase("player")) {
			Messenger.title(sender, title.replace("&7{receiver}", sender.getName()), subtitle.replace("&7{receiver}", sender.getName()), fadeIn, duration, fadeOut);
		}
		// target everyone
		else if (target.equalsIgnoreCase("everyone")) {
			for (Player pl : Utils.getOnlinePlayers()) {
				Messenger.title(pl, title.replace("&7{receiver}", pl.getName()), subtitle.replace("&7{receiver}", pl.getName()), fadeIn, duration, fadeOut);
			}
		}
		// target player in argument
		else {
			try {
				Player newTarget = Utils.getPlayer(target);
				Messenger.title(newTarget, title.replace("&7{receiver}", newTarget.getName()), subtitle.replace("&7{receiver}", newTarget.getName()), fadeIn, duration, fadeOut);
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
