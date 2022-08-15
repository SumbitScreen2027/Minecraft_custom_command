package com.guillaumevdn.customcommands.lib.condition.type.common;

import java.util.List;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementString;
import com.guillaumevdn.gcore.lib.logic.LogicUtils;
import com.guillaumevdn.gcore.lib.string.StringUtils;
import com.guillaumevdn.gcore.lib.string.Text;
import com.guillaumevdn.gcore.lib.string.TextElement;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeLogic extends ConditionType {

	public ConditionTypeLogic(String id) {
		super(id, CommonMats.COMMAND_BLOCK);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.add(new ElementString(condition, "logic", Need.required(), ConditionTypeLogic.buildLogicDescription()));
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("logic", replacer, (String logic) -> {
			return LogicUtils.match(logic, replacer.cloneReplacer().formatNumbers(false));
		}).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

	// ----- static
	public static Text buildLogicDescription(String... placeholders) {
		if (placeholders == null || placeholders.length == 0) {
			return TextEditorCCMD.descriptionConditionGenericLogicLogicPlaceholder;
		}
		List<String> lines = TextEditorCCMD.descriptionConditionGenericLogicLogicPlaceholder.parseLines();
		lines.addAll(TextEditorCCMD.descriptionConditionGenericLogicLogicPlaceholder.replace("{placeholders}", () -> StringUtils.toTextString(", ", (Object[]) placeholders)).parseLines());
		return new TextElement(lines);
	}

}
