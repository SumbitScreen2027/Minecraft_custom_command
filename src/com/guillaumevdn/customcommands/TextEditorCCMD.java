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
