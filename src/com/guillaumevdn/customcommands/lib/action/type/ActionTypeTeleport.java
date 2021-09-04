package com.guillaumevdn.customcommands.lib.action.type;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;

/**
 * @author GuillaumeVDN
 */
public class ActionTypeTeleport extends TargetableActionType {

	public ActionTypeTeleport(String id) {
		super(id, CommonMats.REPEATER);
	}

	@Override
	protected void doFillTypeSpecificElements(ElementAction action) {
		super.doFillTypeSpecificElements(action);
		action.addLocation("location", Need.required(), TextEditorCCMD.descriptionActionTeleportLocation);
	}

	@Override
	public void execute(ElementAction action, CustomCommandCall call, List<Player> players) {
		Location loc = action.directParseOrNull("location", call.getReplacer());
		if (loc != null) {
			players.forEach(pl -> pl.teleport(loc, TeleportCause.PLUGIN));
		} else {
			CustomCommands.inst().getMainLogger().error("Couldn't parse location in action " + action.getId() + " of command " + call.getCustomCommand().getId());
		}
	}

}
