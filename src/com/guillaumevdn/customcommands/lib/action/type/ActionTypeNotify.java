package com.guillaumevdn.customcommands.lib.action.type;

import java.util.List;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.container.ElementNotify;

/**
 * @author GuillaumeVDN
 */
public class ActionTypeNotify extends TargetableActionType {

	public ActionTypeNotify(String id) {
		super(id, CommonMats.REPEATER);
	}

	@Override
	protected void doFillTypeSpecificElements(ElementAction action) {
		super.doFillTypeSpecificElements(action);
		action.addNotify("notify", Need.required(), TextEditorCCMD.descriptionActionNotifyNotify);
	}

	@Override
	public void execute(ElementAction action, CustomCommandCall call, List<Player> players) {
		for (Player player : players) {
			action.getElementAs("notify", ElementNotify.class).sendAll(player, call.getReplacer().cloneReplacer().with("{player}", () -> player.getName()));
		}
	}

}
