package com.guillaumevdn.customcommands;

import org.bukkit.entity.Player;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.command.CommandArgument;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CommandSaveLocation extends CommandArgument {

	private static final Param paramLocation = new Param(Utils.asList("location", "l"), "id", null, true, true);

	public CommandSaveLocation() {
		super(CustomCommands.inst(), null, Utils.asList("savelocation", "setlocation"), "save an location", CCPerm.CUSTOMCOMMANDS_ADMIN, true, paramLocation);
	}

	@Override
	public void perform(CommandCall call) {
		String id = paramLocation.getStringAlphanumeric(call);
		if (id != null) {
			Player player = call.getSenderAsPlayer();

			// existing location
			if (CustomCommands.inst().getData().getBoard().getLocation(id) != null) {
				GLocale.MSG_GENERIC_IDTAKEN.send(player, "{plugin}", CustomCommands.inst().getName(), "{id}", id);
				return;
			}

			// set location
			CustomCommands.inst().getData().getBoard().setLocation(id, player.getLocation());
			CCLocale.MSG_CUSTOMCOMMANDS_ITEMSAVE.send(player, "{name}", id);
		}
	}
}

