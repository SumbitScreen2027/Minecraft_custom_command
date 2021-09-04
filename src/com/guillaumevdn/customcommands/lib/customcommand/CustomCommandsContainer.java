package com.guillaumevdn.customcommands.lib.customcommand;

import java.io.File;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.gcore.lib.element.struct.list.referenceable.ElementsContainer;

/**
 * @author GuillaumeVDN
 */
public class CustomCommandsContainer extends ElementsContainer<ElementCustomCommand> {

	public CustomCommandsContainer() {
		super(CustomCommands.inst(), "custom command", ElementCustomCommand.class, CustomCommands.inst().getDataFile("commands/"));
	}

	@Override
	protected ElementCustomCommand createElement(File file, String id) {
		return new ElementCustomCommand(file, id);
	}

}
