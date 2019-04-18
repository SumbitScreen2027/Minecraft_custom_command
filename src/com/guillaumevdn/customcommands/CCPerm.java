package com.guillaumevdn.customcommands;

import com.guillaumevdn.gcore.lib.Perm;

public class CCPerm {

	public static final Perm CUSTOMCOMMANDS_ROOT = new Perm(null, "customcommands.*");
	public static final Perm CUSTOMCOMMANDS_ADMIN = new Perm(CUSTOMCOMMANDS_ROOT, "customcommands.admin");

}
