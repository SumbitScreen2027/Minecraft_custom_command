package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.permission.Permission;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypePermission extends ConditionType {

	public ConditionTypePermission(String id) {
		super(id, CommonMats.IRON_DOOR);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addPermission("permission", Need.required(), TextEditorCCMD.descriptionConditionPermissionPermission);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("permission", replacer, (Permission permission) -> permission.has(player)).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

}
