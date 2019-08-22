package com.guillaumevdn.customcommands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.command.CommandArgument;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.material.Mat;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CommandSaveItem extends CommandArgument {

	private static final Param paramItem = new Param(Utils.asList("item", "i"), "id", null, true, true);

	public CommandSaveItem() {
		super(CustomCommands.inst(), null, Utils.asList("saveitem", "setitem"), "save an item", CCPerm.CUSTOMCOMMANDS_ADMIN, true, paramItem);
	}

	@Override
	public void perform(CommandCall call) {
		String id = paramItem.getStringAlphanumeric(call);
		if (id != null) {
			Player player = call.getSenderAsPlayer();

			// valid item
			ItemStack item = player.getItemInHand();
			if (item == null || Mat.fromItem(item).isAir()) {
				GLocale.MSG_GENERIC_NOHANDITEM.send(player, "{plugin}", CustomCommands.inst().getName());
				return;
			}

			// existing item
			if (CustomCommands.inst().getData().getBoard().getItem(id) != null) {
				GLocale.MSG_GENERIC_IDTAKEN.send(player, "{plugin}", CustomCommands.inst().getName(), "{id}", id);
				return;
			}

			// set item
			CustomCommands.inst().getData().getBoard().setItem(id, item);
			CCLocale.MSG_CUSTOMCOMMANDS_ITEMSAVE.send(player, "{name}", id);
		}
	}
}

