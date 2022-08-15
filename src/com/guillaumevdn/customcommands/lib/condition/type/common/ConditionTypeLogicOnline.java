package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementString;
import com.guillaumevdn.gcore.lib.logic.LogicUtils;
import com.guillaumevdn.gcore.lib.player.PlayerUtils;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeLogicOnline extends ConditionType {

	public ConditionTypeLogicOnline(String id) {
		super(id, CommonMats.COMMAND_BLOCK);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.add(new ElementString(condition, "logic", Need.required(), ConditionTypeLogic.buildLogicDescription()));
		condition.addInteger("goal", Need.optional(1), 1, TextEditorCCMD.descriptionConditionGenericLogicOnlineGoal);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("goal", replacer, (Integer goal) -> {
			int remaining = goal;
			for (Player pl : PlayerUtils.getOnline()) {
				Replacer rep = replacer.cloneReplacer().with(pl).formatNumbers(false);  // parse logic separately for each player, since PAPI and others things will already be parsed for the quest leader but we want to check online
				String logic = condition.directParseOrNull("logic", rep);
				if (rep != null && LogicUtils.match(logic, rep) && --remaining <= 0) {
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
