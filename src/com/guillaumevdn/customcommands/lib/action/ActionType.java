package com.guillaumevdn.customcommands.lib.action;

import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.TypableElementType;

/**
 * @author GuillaumeVDN
 */
public abstract class ActionType extends TypableElementType<ElementAction> {

	public ActionType(String id, Mat icon) {
		super(id, icon);
	}

	/**
	 * @return the amount of ticks to wait before executing the next action
	 */
	public abstract int execute(ElementAction action, CustomCommandCall call);

}
