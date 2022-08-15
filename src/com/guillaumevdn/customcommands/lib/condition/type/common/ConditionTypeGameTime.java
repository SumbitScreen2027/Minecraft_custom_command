package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeGameTime extends ConditionType {

	public ConditionTypeGameTime(String id) {
		super(id, CommonMats.CLOCK);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addInteger("start_ticks", Need.required(), 0, 23999, TextEditorCCMD.descriptionConditionGameTimeStartTicks);
		condition.addInteger("end_ticks", Need.required(), 0, 23999, TextEditorCCMD.descriptionConditionGameTimeEndTicks);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("start_ticks", "end_ticks", replacer, (Integer startTicks, Integer endTicks) -> {
			int now = (int) player.getWorld().getTime(); // 0 to 24000
			return endTicks < startTicks ? now >= startTicks || now <= endTicks : now >= startTicks && now <= endTicks;
		}).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

}
