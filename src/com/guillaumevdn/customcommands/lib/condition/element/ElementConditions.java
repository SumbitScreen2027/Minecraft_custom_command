package com.guillaumevdn.customcommands.lib.condition.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.struct.container.ContainerElement;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementInteger;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementText;
import com.guillaumevdn.gcore.lib.string.StringUtils;
import com.guillaumevdn.gcore.lib.string.Text;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ElementConditions extends ContainerElement {

	protected ElementConditionList conditions = add(new ElementConditionList(this, "conditions", Need.required(), TextEditorCCMD.descriptionConditionsConditions));
	protected ElementInteger matchAmount = addInteger("match_amount", Need.optional(999), TextEditorCCMD.descriptionConditionsMatchAmount);
	protected ElementInteger noMatchAmount = addInteger("no_match_amount", Need.optional(0), TextEditorCCMD.descriptionConditionsNoMatchAmount);
	protected ElementText errorMessage = addText("error_message", Need.optional(), TextEditorCCMD.descriptionConditionsErrorMessage);

	public ElementConditions(Element parent, String id, Need need, Text editorDescription) {
		super(parent, id, need, editorDescription);
	}

	// ----- get
	public ElementConditionList getConditions() {
		return conditions;
	}

	public ElementInteger getMatchAmount() {
		return matchAmount;
	}

	public ElementInteger getNoMatchAmount() {
		return noMatchAmount;
	}

	public ElementText getErrorMessage() {
		return errorMessage;
	}

	// ----- match
	public ConditionsCheckResult match(Player player, Replacer replacer, boolean getErrorMessage) {
		// no conditions
		if (conditions.isEmpty()) {
			return ConditionsCheckResult.VALID;
		}

		// parse
		int matchAmount = directParseOrElse("match_amount", replacer, 999);
		int noMatchAmount = directParseOrElse("no_match_amount", replacer, 0);

		// check for match
		List<ElementCondition> match = new ArrayList<>(conditions.size());
		List<ElementCondition> noMatch = new ArrayList<>(conditions.size());

		for (ElementCondition condition : conditions.values()) {
			(condition.match(player, replacer, false) ? match : noMatch).add(condition);
		}

		return matchResult(player, match, noMatch, matchAmount, noMatchAmount, getErrorMessage, replacer);
	}

	private ConditionsCheckResult matchResult(Player player, List<ElementCondition> match, List<ElementCondition> noMatch, int matchAmount, int noMatchAmount, boolean getErrorMessage, Replacer replacer) {
		if (matchAmount > conditions.size()) {
			matchAmount = conditions.size();
		}
		if (noMatchAmount > conditions.size()) {
			noMatchAmount = conditions.size();
		}
		final int matchAmountF = matchAmount;
		final int noMatchAmountF = noMatchAmount;

		Supplier<List<Text>> buildErrors = () -> {
			List<Text> errors = new ArrayList<>();
			if (getErrorMessage) {
				// general error message
				Text general = errorMessage.parse(replacer).orNull();
				if (general != null) {
					errors.add(general);
				}
				// per-condition error message
				else {
					Stream<ElementCondition> errorsToParse;
					if (matchAmountF > 0 && noMatchAmountF <= 0) {
						errorsToParse = noMatch.stream();
					} else if (noMatchAmountF > 0 && matchAmountF <= 0) {
						errorsToParse = match.stream();
					} else {
						errorsToParse = conditions.values().stream();
					}
					errorsToParse.forEach(condition -> {
						condition.getErrorMessage().parse(replacer).ifPresentDo(msg -> errors.add(msg));
					});
				}
			}
			return errors;
		};

		// don't have the required amount
		if (match.size() < matchAmountF) {
			return new ConditionsCheckResult(player, buildErrors.get());
		}
		if (noMatch.size() < noMatchAmountF) {
			return new ConditionsCheckResult(player, buildErrors.get());
		}

		// success
		return ConditionsCheckResult.VALID;
	}

	// ----- take
	public void takeIfNeededAndSupported(Player player, Replacer replacer) {
		if (!conditions.isEmpty()) {
			conditions.values().forEach(condition -> condition.takeIfNeededAndSupported(player, replacer));
		}
	}

	// ----- editor
	@Override
	public Mat editorIconType() {
		return CommonMats.CHEST_MINECART;
	}

	@Override
	public List<String> editorCurrentValue() {
		if (conditions.size() == 0) {
			return null;
		}
		List<String> desc = new ArrayList<>();
		int match = matchAmount.parseGeneric().orElse(999);
		int noMatch = noMatchAmount.parseGeneric().orElse(0);
		if (match >= conditions.size()) {
			desc.add("all must match");
		} else if (noMatch >= conditions.size()) {
			desc.add("all mustn't match");
		} else if (noMatch != 0) {
			desc.add(match + " must match, " + noMatch + " musn't match");
		} else {
			desc.add(match + " must match");
		}
		desc.add(StringUtils.toTextString(", ", conditions.values().stream().map(ElementCondition::getType)));
		return desc;
	}

}
