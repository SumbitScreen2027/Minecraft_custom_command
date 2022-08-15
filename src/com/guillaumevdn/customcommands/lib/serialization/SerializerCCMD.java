package com.guillaumevdn.customcommands.lib.serialization;

import com.guillaumevdn.customcommands.lib.action.ActionType;
import com.guillaumevdn.customcommands.lib.action.ActionTypes;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.ConditionTypes;
import com.guillaumevdn.gcore.lib.serialization.Serializer;

/**
 * @author GuillaumeVDN
 */
public final class SerializerCCMD {

	public static final Serializer<ActionType> ACTION_TYPE = Serializer.ofTypable(ActionType.class, () -> ActionTypes.inst());
	public static final Serializer<ConditionType> CONDITION_TYPE = Serializer.ofTypable(ConditionType.class, () -> ConditionTypes.inst());

	public static void init() {}

}
