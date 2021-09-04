package com.guillaumevdn.customcommands.lib.action.type;

import com.guillaumevdn.customcommands.lib.action.ActionType;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;

/**
 * @author GuillaumeVDN
 */
public class ActionTypeNone extends ActionType {

	public ActionTypeNone(String id) {
		super(id, CommonMats.REPEATER);
	}

	@Override
	public int execute(ElementAction action, CustomCommandCall call) {
		return 0;
	}

}
