package com.guillaumevdn.customcommands.lib.condition.type.common;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.time.in.TimeInYear;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeTimeInYear extends ConditionTypeTimeIn<TimeInYear> {

	public ConditionTypeTimeInYear(String id) {
		super(id);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addTimeInYear("start", Need.required(), TextEditorCCMD.descriptionConditionYearTimeStart);
		condition.addTimeInYear("end", Need.required(), TextEditorCCMD.descriptionConditionYearTimeEnd);
	}

}
