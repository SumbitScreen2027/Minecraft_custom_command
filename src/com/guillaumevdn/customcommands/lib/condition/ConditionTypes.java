package com.guillaumevdn.customcommands.lib.condition;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeGameTime;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeInventoryFreeForItems;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeItems;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeLogic;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeLogicMoney;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeLogicOnline;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeLogicScoreboardValue;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeLogicXPLevel;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeNone;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypePermission;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypePermissionOnline;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypePosition;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeScoreboardTag;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeTimeInDay;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeTimeInMonth;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeTimeInWeek;
import com.guillaumevdn.customcommands.lib.condition.type.common.ConditionTypeTimeInYear;
import com.guillaumevdn.gcore.lib.compatibility.Version;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.TypableElementTypes;

/**
 * @author GuillaumeVDN
 */
public final class ConditionTypes extends TypableElementTypes<ConditionType> {

	public ConditionTypes() {
		super(ConditionType.class);
	}

	// ----- types : common
	public final ConditionTypeGameTime 					GAME_TIME 					= register(new ConditionTypeGameTime("GAME_TIME"));
	public final ConditionTypeInventoryFreeForItems		INVENTORY_FREE_FOR_ITEMS 	= register(new ConditionTypeInventoryFreeForItems("INVENTORY_FREE_FOR_ITEMS"));
	public final ConditionTypeItems						ITEMS 						= register(new ConditionTypeItems("ITEMS"));
	public final ConditionTypeLogic						LOGIC		 				= register(new ConditionTypeLogic("LOGIC"));
	public final ConditionTypeLogicOnline				LOGIC_ONLINE 				= register(new ConditionTypeLogicOnline("LOGIC_ONLINE"));
	public final ConditionTypeLogicMoney 				LOGIC_MONEY 				= register(new ConditionTypeLogicMoney("LOGIC_MONEY"));
	public final ConditionTypeLogicXPLevel 				LOGIC_XP_LEVEL 				= register(new ConditionTypeLogicXPLevel("LOGIC_XP_LEVEL"));
	public final ConditionTypeLogicScoreboardValue		LOGIC_SCOREBOARD_VALUE		= register(new ConditionTypeLogicScoreboardValue("SCOREBOARD_VALUE_LOGIC"));
	public final ConditionTypeNone 						NONE 						= register(new ConditionTypeNone("NONE"));
	public final ConditionTypePermission				PERMISSION 					= register(new ConditionTypePermission("PERMISSION"));
	public final ConditionTypePermissionOnline			PERMISSION_ONLINE			= register(new ConditionTypePermissionOnline("PERMISSION_ONLINE"));
	public final ConditionTypePosition					POSITION					= register(new ConditionTypePosition("POSITION"));
	public final ConditionTypeScoreboardTag				SCOREBOARD_TAG				= !Version.ATLEAST_1_11 ? null : register(new ConditionTypeScoreboardTag("SCOREBOARD_TAG"));
	public final ConditionTypeTimeInDay	 				TIME_IN_DAY					= register(new ConditionTypeTimeInDay("TIME_IN_DAY"));
	public final ConditionTypeTimeInWeek	 			TIME_IN_WEEK				= register(new ConditionTypeTimeInWeek("TIME_IN_WEEK"));
	public final ConditionTypeTimeInMonth	 			TIME_IN_MONTH				= register(new ConditionTypeTimeInMonth("TIME_IN_MONTH"));
	public final ConditionTypeTimeInYear	 			TIME_IN_YEAR				= register(new ConditionTypeTimeInYear("TIME_IN_YEAR"));

	// ----- values
	public static ConditionTypes inst() {
		return CustomCommands.inst().getConditionTypes();
	}

	@Override
	public ConditionType defaultValue() {
		return NONE;
	}

}
