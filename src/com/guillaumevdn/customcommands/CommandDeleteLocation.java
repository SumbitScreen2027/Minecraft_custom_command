package com.guillaumevdn.customcommands;

import org.bukkit.entity.Player;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.command.CommandArgument;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CommandDeleteLocation extends CommandArgument {

	private static final Param paramLocation = new Param(Utils.asList("location", "l"), "id", null, true, true);

	public CommandDeleteLocation() {
		super(CustomCommands.inst(), null, Utils.asList("deletelocation", "removelocation"), "remove a location", CCPerm.CUSTOMCOMMANDS_ADMIN, true, paramLocation);
	}

	@Override
	public void perform(CommandCall call) {
		String id = paramLocation.getStringAlphanumeric(call);
		if (id != null) {
			Player sender = call.getSenderAsPlayer();
			// not existing location
			if (CustomCommands.inst().getData().getBoard().getItem(id) != null) {
				GLocale.MSG_GENERIC_IDUNKNOWN.send(sender, "{plugin}", CustomCommands.inst().getName(), "{id}", id);
				return;
			}
			// remove item
			CustomCommands.inst().getData().getBoard().removeLocation(id);
			CCLocale.MSG_CUSTOMCOMMANDS_LOCATIONDELETE.send(sender, "{name}", id);
		}
	}
}

