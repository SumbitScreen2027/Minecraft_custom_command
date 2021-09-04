package com.guillaumevdn.customcommands.lib.action.element;

import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.struct.list.ListElement;
import com.guillaumevdn.gcore.lib.string.Text;

/**
 * @author GuillaumeVDN
 */
public class ElementActionList extends ListElement<ElementAction> {

	public ElementActionList(Element parent, String id, Need need, Text editorDescription) {
		super(true, parent, id, need, editorDescription);
	}

	// ----- element
	@Override
	protected ElementAction createElement(String elementId) {
		return new ElementAction(this, elementId, Need.optional(), null);
	}

	// ----- editor
	@Override
	public Mat editorIconType() {
		return CommonMats.REPEATER;
	}

}
