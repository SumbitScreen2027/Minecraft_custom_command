package com.guillaumevdn.customcommands.lib.action.type;

import java.util.List;

import org.bukkit.GameMode;
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
public class ActionTypeChangeGamemode extends TargetableActionType {

	public ActionTypeChangeGamemode(String id) {
		super(id, CommonMats.REPEATER);
	}

	@Override
	protected void doFillTypeSpecificElements(ElementAction action) {
		super.doFillTypeSpecificElements(action);
		action.addGameMode("gamemode", Need.required(), TextEditorCCMD.descriptionActionChangeGamemodeGamemode);
	}

	@Override
	public void execute(ElementAction action, CustomCommandCall call, List<Player> players) {
		GameMode gamemode = action.directParseOrNull("gamemode", call.getReplacer());
		if (gamemode != null) {
			players.forEach(pl -> pl.setGameMode(gamemode));
		} else {
			CustomCommands.inst().getMainLogger().error("Couldn't parse gamemode in action " + action.getId() + " of command " + call.getCustomCommand().getId());
		}
	}

}
