package com.guillaumevdn.customcommands.lib.serialization;

import com.guillaumevdn.customcommands.lib.action.ActionType;
import com.guillaumevdn.customcommands.lib.action.ActionTypes;
import com.guillaumevdn.gcore.lib.serialization.Serializer;

/**
 * @author GuillaumeVDN
 */
public final class SerializerCCMD {

	public static final Serializer<ActionType> ACTION_TYPE = Serializer.ofTypable(ActionType.class, () -> ActionTypes.inst());

	public static void init() {}

}
