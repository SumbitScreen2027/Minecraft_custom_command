package com.guillaumevdn.customcommands;

import com.guillaumevdn.gcore.lib.string.TextElement;
import com.guillaumevdn.gcore.lib.string.TextEnumElement;

/**
 * @author GuillaumeVDN
 */
public enum TextCCMD implements TextEnumElement {

	commandDescriptionCustomcommandsEdit,
	
	messageInvalidInputInteger,
	messageInvalidInputDouble,
	messageInvalidInputOfflinePlayer,
	messageInvalidInputPlayer,
	messageInvalidInputStringSize,

	messageNoHelp,
	messageCooldown,
	messageUnauthorizedWorld,
	messageUnknownCommand,
	messageUnknownCommandClosest,

	;

	private TextElement text = new TextElement();

	TextCCMD() {
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
