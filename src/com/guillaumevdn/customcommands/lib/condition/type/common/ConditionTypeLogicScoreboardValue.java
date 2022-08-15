package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementString;
import com.guillaumevdn.gcore.lib.logic.LogicUtils;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeLogicScoreboardValue extends ConditionType {

	public ConditionTypeLogicScoreboardValue(String id) {
		super(id, CommonMats.COMMAND_BLOCK);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.add(new ElementString(condition, "logic", Need.required(), ConditionTypeLogic.buildLogicDescription("{value}")));
		condition.addString("tag_name", Need.required(), TextEditorCCMD.descriptionConditionLogicScoreboardValueObjectiveName);
		condition.addString("score_name", Need.required(), TextEditorCCMD.descriptionConditionLogicScoreboardValueScoreName);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("logic", "objective_name", "score_name", replacer, (String logic, String objectiveName, String scoreName) -> {
			Scoreboard scoreboard = player.getScoreboard();
			Objective objective = scoreboard == null ? null : scoreboard.getObjective(objectiveName);
			Score score = objective == null ? null : objective.getScore(scoreName);
			if (score == null || !score.isScoreSet()) return false;
			return LogicUtils.match(logic, replacer.cloneReplacer().formatNumbers(false).with("{value}", () -> score.getScore()));
		}).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

}
