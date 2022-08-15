package com.guillaumevdn.customcommands.lib.condition.type.common;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.time.in.TimeInDay;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeTimeInDay extends ConditionTypeTimeIn<TimeInDay> {

	public ConditionTypeTimeInDay(String id) {
		super(id);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addTimeInDay("start", Need.required(), TextEditorCCMD.descriptionConditionDayTimeStart);
		condition.addTimeInDay("end", Need.required(), TextEditorCCMD.descriptionConditionDayTimeEnd);
	}

}
