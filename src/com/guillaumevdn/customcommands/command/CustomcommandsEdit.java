package com.guillaumevdn.customcommands.command;

import com.guillaumevdn.customcommands.ConfigCCMD;
import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.PermissionCCMD;
import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Subcommand;
import com.guillaumevdn.gcore.lib.element.editor.EditorGUI;

public class CustomcommandsEdit extends Subcommand {

	public CustomcommandsEdit() {
		super(true, PermissionCCMD.inst().customcommandsEdit, TextCCMD.commandDescriptionCustomcommandsEdit, ConfigCCMD.commandsAliasesEdit);
	}

	@Override
	public void perform(CommandCall call) {
		EditorGUI gui = ConfigCCMD.customCommands.editorGUI(CustomCommands.inst(), "CustomCommands", null);
		if (gui != null) {
			gui.openFor(call.getSenderPlayer(), null);
		}
	}

}
