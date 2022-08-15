package com.guillaumevdn.customcommands;

import com.guillaumevdn.gcore.lib.string.TextElement;
import com.guillaumevdn.gcore.lib.string.TextEnumElement;

/**
 * @author GuillaumeVDN
 */
public enum TextEditorCCMD implements TextEnumElement {

	// ----- custom commands

	descriptionCustomCommandAliases,
	descriptionCustomCommandPatterns,

	descriptionCustomCommandPatternPattern,
	descriptionCustomCommandPatternDescription,
	descriptionCustomCommandPatternPermission,
	descriptionCustomCommandPatternPermissionErrorMessage,
	descriptionCustomCommandPatternWorlds,
	descriptionCustomCommandPatternCooldown,
	descriptionCustomCommandPatternCurrencyCost,
	descriptionCustomCommandPatternConditions,
	descriptionCustomCommandPatternToggleMode,
	descriptionCustomCommandPatternActions,

	// ----- actions

	descriptionActionOnToggleOn,
	descriptionActionType,
	descriptionActionTypeTarget,

	descriptionActionChangeGamemodeGamemode,

	descriptionActionExecuteCommmandsCommands,
	descriptionActionExecuteCommmandsAsPlayer,

	descriptionActionGiveItemItem,

	descriptionActionNotifyNotify,

	descriptionActionTeleportLocation,

	descriptionActionWaitDuration,

	// ----- conditions
	descriptionConditionsConditions,
	descriptionConditionsMatchAmount,
	descriptionConditionsNoMatchAmount,
	descriptionConditionsErrorMessage,

	// ----- condition : common
	descriptionConditionType,
	descriptionConditionErrorMessage,

	descriptionConditionGameTimeStartTicks,
	descriptionConditionGameTimeEndTicks,

	descriptionConditionInventoryFreeForItemsItems,
	descriptionConditionItemsItems,

	descriptionConditionGenericLogicLogic,
	descriptionConditionGenericLogicLogicPlaceholder,
	
	descriptionConditionGenericLogicOnlineGoal,

	descriptionConditionLogicMoneyCurrency,

	descriptionConditionPermissionPermission,

	descriptionConditionPermissionOnlineGoal,

	descriptionConditionPositionPosition,

	descriptionConditionScoreboardTagTagName,
	descriptionConditionScoreboardTagMustHave,

	descriptionConditionLogicScoreboardValueObjectiveName,
	descriptionConditionLogicScoreboardValueScoreName,

	descriptionConditionDayTimeStart,
	descriptionConditionDayTimeEnd,

	descriptionConditionWeekTimeStart,
	descriptionConditionWeekTimeEnd,

	descriptionConditionMonthTimeStart,
	descriptionConditionMonthTimeEnd,

	descriptionConditionYearTimeStart,
	descriptionConditionYearTimeEnd,

	// ----- condition
	descriptionConditionMustTake,

	;

	private TextElement text = new TextElement();

	TextEditorCCMD() {
	}

	@Override
	public String getId() {
		return name();
	}

	@Override
	public TextElement getText() {
		return text;
	}

}
