package com.guillaumevdn.customcommands.lib.action.type;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.action.ActionType;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.player.PlayerUtils;

/**
 * @author GuillaumeVDN
 */
public abstract class TargetableActionType extends ActionType {

	public TargetableActionType(String id, Mat icon) {
		super(id, icon);
	}

	@Override
	protected void doFillTypeSpecificElements(ElementAction value) {
		super.doFillTypeSpecificElements(value);
		value.addString("target", Need.optional("{sender}"), TextEditorCCMD.descriptionActionTypeTarget);
	}

	@Override
	public final int execute(ElementAction action, CustomCommandCall call) {
		String target = action.directParseOrNull("target", call.getReplacer());
		if (target != null) {
			if (target.equalsIgnoreCase("everyone")) {
				execute(action, call, PlayerUtils.getOnline());
			} else {
				Player player = Bukkit.getPlayer(target);
				if (player != null) {
					execute(action, call, CollectionUtils.asList(player));
				} else {
					CustomCommands.inst().getMainLogger().error("Couldn't find player '" + target + "' to execute action " + action.getId() + " of command " + call.getCustomCommand().getId());
				}
			}
		}
		return 0;
	}

	public abstract void execute(ElementAction action, CustomCommandCall call, List<Player> players);

}
