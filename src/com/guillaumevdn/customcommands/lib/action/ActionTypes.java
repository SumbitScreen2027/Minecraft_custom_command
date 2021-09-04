package com.guillaumevdn.customcommands.lib.action;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.lib.action.type.ActionTypeChangeGamemode;
import com.guillaumevdn.customcommands.lib.action.type.ActionTypeExecuteCommands;
import com.guillaumevdn.customcommands.lib.action.type.ActionTypeGiveItem;
import com.guillaumevdn.customcommands.lib.action.type.ActionTypeNone;
import com.guillaumevdn.customcommands.lib.action.type.ActionTypeNotify;
import com.guillaumevdn.customcommands.lib.action.type.ActionTypeTeleport;
import com.guillaumevdn.customcommands.lib.action.type.ActionTypeWait;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.TypableElementTypes;

/**
 * @author GuillaumeVDN
 */
public final class ActionTypes extends TypableElementTypes<ActionType> {

	public ActionTypes() {
		super(ActionType.class);
	}

	public final ActionTypeNone					NONE					= register(new ActionTypeNone("NONE"));

	public final ActionTypeChangeGamemode		CHANGE_GAMEMODE			= register(new ActionTypeChangeGamemode("CHANGE_GAMEMODE"));
	public final ActionTypeExecuteCommands		EXECUTE_COMMANDS		= register(new ActionTypeExecuteCommands("EXECUTE_COMMANDS"));
	public final ActionTypeGiveItem				GIVE_ITEM				= register(new ActionTypeGiveItem("GIVE_ITEM"));
	public final ActionTypeNotify				NOTIFY		 			= register(new ActionTypeNotify("NOTIFY"));
	public final ActionTypeTeleport				TELEPORT				= register(new ActionTypeTeleport("TELEPORT"));
	public final ActionTypeWait					WAIT					= register(new ActionTypeWait("WAIT"));

	public static ActionTypes inst() {
		return CustomCommands.inst().getActionTypes();
	}

	@Override
	public ActionType defaultValue() {
		return NONE;
	}

}
