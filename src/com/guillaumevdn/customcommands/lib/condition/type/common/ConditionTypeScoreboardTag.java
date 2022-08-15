package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeScoreboardTag extends ConditionType {

	public ConditionTypeScoreboardTag(String id) {
		super(id, CommonMats.REPEATER);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addString("tag_name", Need.required(), TextEditorCCMD.descriptionConditionScoreboardTagTagName);
		condition.addBoolean("must_have", Need.optional(true), TextEditorCCMD.descriptionConditionScoreboardTagMustHave);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("tag_name", "must_have", replacer, (String tagName, Boolean must_have) -> {
			if (must_have) {
				return player.getScoreboardTags() != null && CollectionUtils.containsIgnoreCase(player.getScoreboardTags(), tagName);
			} else {
				return player.getScoreboardTags() == null || !CollectionUtils.containsIgnoreCase(player.getScoreboardTags(), tagName);
			}
		}).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseOrElse("must_have", replacer, true);
	}

	@Override
	protected void doTake(ElementCondition condition, Player player, Replacer replacer) {
		condition.directParseAndIfPresentDo("tag_name", replacer, (String tagName) -> {
			player.removeScoreboardTag(tagName);
		});
	}

}
