package com.guillaumevdn.customcommands.lib.action.type;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.container.ElementItemMode;
import com.guillaumevdn.gcore.lib.item.ItemUtils;

/**
 * @author GuillaumeVDN
 */
public class ActionTypeGiveItem extends TargetableActionType {

	public ActionTypeGiveItem(String id) {
		super(id, CommonMats.REPEATER);
	}

	@Override
	protected void doFillTypeSpecificElements(ElementAction action) {
		super.doFillTypeSpecificElements(action);
		action.addItem("item", Need.required(), ElementItemMode.BUILDABLE, TextEditorCCMD.descriptionActionGiveItemItem);
	}

	@Override
	public void execute(ElementAction action, CustomCommandCall call, List<Player> players) {
		ItemStack item = action.directParseOrNull("item", call.getReplacer());
		if (item != null) {
			players.forEach(pl -> ItemUtils.give(pl, item, true));
		} else {
			CustomCommands.inst().getMainLogger().error("Couldn't parse item in action " + action.getId() + " of command " + call.getCustomCommand().getId());
		}
	}

}
