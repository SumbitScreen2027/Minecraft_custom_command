package com.guillaumevdn.customcommands.lib.condition.type.common;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.time.in.TimeInMonth;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeTimeInMonth extends ConditionTypeTimeIn<TimeInMonth> {

	public ConditionTypeTimeInMonth(String id) {
		super(id);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addTimeInMonth("start", Need.required(), TextEditorCCMD.descriptionConditionMonthTimeStart);
		condition.addTimeInMonth("end", Need.required(), TextEditorCCMD.descriptionConditionMonthTimeEnd);
	}

}
