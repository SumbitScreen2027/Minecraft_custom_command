package com.guillaumevdn.customcommands;

import org.bukkit.entity.Player;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.command.CommandArgument;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CommandDeleteItem extends CommandArgument {

	private static final Param paramItem = new Param(Utils.asList("item", "i"), "id", null, true, true);

	public CommandDeleteItem() {
		super(CustomCommands.inst(), null, Utils.asList("deleteitem", "removeitem"), "remove an item", CCPerm.CUSTOMCOMMANDS_ADMIN, true, paramItem);
	}

	@Override
	public void perform(CommandCall call) {
		String id = paramItem.getStringAlphanumeric(call);
		if (id != null) {
			Player sender = call.getSenderAsPlayer();
			// not existing item
			if (CustomCommands.inst().getData().getBoard().getItem(id) != null) {
				GLocale.MSG_GENERIC_IDUNKNOWN.send(sender, "{plugin}", CustomCommands.inst().getName(), "{id}", id);
				return;
			}
			// remove item
			CustomCommands.inst().getData().getBoard().removeItem(id);
			CCLocale.MSG_CUSTOMCOMMANDS_ITEMDELETE.send(sender, "{name}", id);
		}
	}
}

