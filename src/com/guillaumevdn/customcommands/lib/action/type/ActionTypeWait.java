package com.guillaumevdn.customcommands.lib.action.type;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.action.ActionType;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.time.TimeUnit;

/**
 * @author GuillaumeVDN
 */
public class ActionTypeWait extends ActionType {

	public ActionTypeWait(String id) {
		super(id, CommonMats.REPEATER);
	}

	@Override
	protected void doFillTypeSpecificElements(ElementAction action) {
		super.doFillTypeSpecificElements(action);
		action.addDuration("duration", Need.required(), 1, TimeUnit.SECOND, TextEditorCCMD.descriptionActionNotifyNotify);
	}

	@Override
	public int execute(ElementAction action, CustomCommandCall call) {
		return (int) (action.directParseOrElse("duration", call.getReplacer(), 0L) / 50L);
	}

}
