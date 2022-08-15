package com.guillaumevdn.customcommands.lib.condition.element;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.ConditionTypes;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.ElementTypableElementType;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.TypableContainerElement;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementBoolean;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementText;
import com.guillaumevdn.gcore.lib.string.Text;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ElementCondition extends TypableContainerElement<ConditionType> {

	private ElementText errorMessage = addText("error_message", Need.optional(), TextEditorCCMD.descriptionConditionErrorMessage);
	private ElementBoolean takeAfterCompletion = addBoolean("must_take", Need.optional(false), TextEditorCCMD.descriptionConditionMustTake);

	public ElementCondition(Element parent, String id, Need need, Text editorDescription) {
		super(ConditionTypes.inst(), parent, id, need, editorDescription);
	}

	@Override
	protected final ElementTypableElementType<ConditionType> addType() {
		return add(new ElementConditionType(this, "type", TextEditorCCMD.descriptionConditionType));
	}

	public ElementText getErrorMessage() {
		return errorMessage;
	}

	public ElementBoolean getTakeAfterCompletion() {
		return takeAfterCompletion;
	}

	public boolean match(Player player, Replacer replacer, boolean sendErrorMessage) {
		boolean match = getType().match(this, player, replacer);
		if (!match && sendErrorMessage) {
			directParseAndIfPresentDo("error_message", replacer, (Text errorMessage) -> {
				errorMessage.send(player);
			});
		}
		return match;
	}

	public void takeIfNeededAndSupported(Player player, Replacer replacer) {
		directParseAndIfPresentDo("must_take", replacer, (Boolean mustTake) -> {
			if (mustTake) {
				getType().takeIfSupported(this, player, replacer);
			}
		});
	}

}
