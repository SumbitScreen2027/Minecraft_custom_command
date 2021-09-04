package com.guillaumevdn.customcommands.lib.action.element;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.action.ActionType;
import com.guillaumevdn.customcommands.lib.action.ActionTypes;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.ElementTypableElementType;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.TypableContainerElement;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementBoolean;
import com.guillaumevdn.gcore.lib.string.Text;

/**
 * @author GuillaumeVDN
 */
public class ElementAction extends TypableContainerElement<ActionType> {

	private ElementBoolean onToggleOn = addBoolean("on_toggle_on", Need.optional(true), TextEditorCCMD.descriptionActionOnToggleOn);

	public ElementAction(Element parent, String id, Need need, Text editorDescription) {
		super(ActionTypes.inst(), parent, id, need, editorDescription);
	}

	@Override
	protected final ElementTypableElementType<ActionType> addType() {
		return add(new ElementActionType(this, "type", TextEditorCCMD.descriptionActionType));
	}

	public ElementBoolean getOnToggleOn() {
		return onToggleOn;
	}

}
