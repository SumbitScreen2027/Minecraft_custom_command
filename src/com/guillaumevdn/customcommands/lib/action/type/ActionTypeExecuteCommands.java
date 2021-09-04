package com.guillaumevdn.customcommands.lib.action.type;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;

/**
 * @author GuillaumeVDN
 */
public class ActionTypeExecuteCommands extends TargetableActionType {

	public ActionTypeExecuteCommands(String id) {
		super(id, CommonMats.REPEATER);
	}

	@Override
	protected void doFillTypeSpecificElements(ElementAction action) {
		super.doFillTypeSpecificElements(action);
		action.addStringList("commands", Need.required(), TextEditorCCMD.descriptionActionExecuteCommmandsCommands);
		action.addBoolean("as_player", Need.optional(false), TextEditorCCMD.descriptionActionExecuteCommmandsAsPlayer);
	}

	@Override
	public void execute(ElementAction action, CustomCommandCall call, List<Player> players) {
		List<String> commands = action.directParseOrNull("commands", call.getReplacer());
		if (commands != null) {
			boolean asPlayer = action.directParseOrElse("as_player", call.getReplacer(), false);
			players.forEach(pl -> commands.forEach(cmd -> {
				cmd = cmd.replace("{player}", pl.getName());
				/*if (asPlayer) {
					PlayerEvents.performCommandAs(pl, cmd);
				}*/
				Bukkit.dispatchCommand(asPlayer ? pl : Bukkit.getConsoleSender(), cmd);
			}));
		} else {
			CustomCommands.inst().getMainLogger().error("Couldn't parse commands in action " + action.getId() + " of command " + call.getCustomCommand().getId());
		}
	}

}
