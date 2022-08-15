package com.guillaumevdn.customcommands.lib.condition.type.common;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.economy.Currency;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementString;
import com.guillaumevdn.gcore.lib.logic.ComparisonType;
import com.guillaumevdn.gcore.lib.logic.LogicUtils;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;
import com.guillaumevdn.gcore.lib.tuple.Triple;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeLogicMoney extends ConditionType {

	public ConditionTypeLogicMoney(String id) {
		super(id, CommonMats.GOLD_INGOT);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addCurrency("currency", Need.optional(Currency.VAULT), TextEditorCCMD.descriptionConditionLogicMoneyCurrency);
		condition.add(new ElementString(condition, "logic", Need.required(), ConditionTypeLogic.buildLogicDescription("{money}")));
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("logic", "currency", replacer, (String logic, Currency currency) -> {
			double money = currency.get(player);
			return LogicUtils.match(logic, replacer.cloneReplacer().formatNumbers(false).with("{money}", () -> money));
		}).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return true;
	}

	@Override
	public void doTake(ElementCondition condition, Player player, Replacer replacer) {
		condition.directParseAndIfPresentDo("logic", "currency", replacer, (String logic, Currency currency) -> {
			List<Triple<Double, Double, ComparisonType>> addToListIfMatch = new ArrayList<>();
			LogicUtils.match(logic, replacer.cloneReplacer().formatNumbers(false).with("{money}", () -> currency.get(player)), addToListIfMatch);
			addToListIfMatch.forEach(triple -> {
				if (triple.getC().canTake()) {
					Double newValue = triple.getC().take(currency.get(player) /* don't take triple.getA() otherwise if multiple values it'll be the same fixed number */, triple.getB() == null ? 0d : triple.getB(), null);
					currency.set(player, newValue);
				}
			});
		});
	}

}
