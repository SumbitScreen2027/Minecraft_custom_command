package com.guillaumevdn.customcommands.lib.condition.type.common;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.time.in.TimeInWeek;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeTimeInWeek extends ConditionTypeTimeIn<TimeInWeek> {

	public ConditionTypeTimeInWeek(String id) {
		super(id);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addTimeInWeek("start", Need.required(), TextEditorCCMD.descriptionConditionWeekTimeStart);
		condition.addTimeInWeek("end", Need.required(), TextEditorCCMD.descriptionConditionWeekTimeEnd);
	}

}
