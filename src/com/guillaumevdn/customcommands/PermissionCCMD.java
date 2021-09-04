package com.guillaumevdn.customcommands;

import com.guillaumevdn.gcore.lib.permission.Permission;
import com.guillaumevdn.gcore.lib.permission.PermissionContainer;

/**
 * @author GuillaumeVDN
 */
public class PermissionCCMD extends PermissionContainer {

	private static PermissionCCMD instance = null;
	public static PermissionCCMD inst() { return instance; }

	public PermissionCCMD() {
		super(CustomCommands.inst());
		instance = this;
	}

	public final Permission customcommandsAdmin = setAdmin("customcommands.admin");
	public final Permission customcommandsEdit = set("customcommands.edit");

}
