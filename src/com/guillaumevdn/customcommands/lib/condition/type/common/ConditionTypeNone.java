package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public final class ConditionTypeNone extends ConditionType {

	public ConditionTypeNone(String id) {
		super(id, CommonMats.GREEN_WOOL);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return true;
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

}
