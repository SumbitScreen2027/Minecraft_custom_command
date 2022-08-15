package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.permission.Permission;
import com.guillaumevdn.gcore.lib.player.PlayerUtils;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypePermissionOnline extends ConditionType {

	public ConditionTypePermissionOnline(String id) {
		super(id, CommonMats.IRON_DOOR);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addPermission("permission", Need.required(), TextEditorCCMD.descriptionConditionPermissionPermission);
		condition.addInteger("goal", Need.optional(1), 1, TextEditorCCMD.descriptionConditionPermissionOnlineGoal);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("permission", "goal", replacer, (Permission permission, Integer goal) -> {
			int remaining = goal;
			for (Player pl : PlayerUtils.getOnline()) {
				if (permission.has(pl) && --remaining <= 0) {
					break;
				}
			}
			return remaining <= 0;
		}).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

}
