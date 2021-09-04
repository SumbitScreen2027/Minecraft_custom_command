package com.guillaumevdn.customcommands.lib.customcommand;

import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.struct.list.ListElement;
import com.guillaumevdn.gcore.lib.string.Text;

/**
 * @author GuillaumeVDN
 */
public class ElementPatternList extends ListElement<ElementPattern> {

	public ElementPatternList(Element parent, String id, Need need, Text editorDescription) {
		super(true, parent, id, need, editorDescription);
	}

	@Override
	protected ElementPattern createElement(String elementId) {
		return new ElementPattern(this, elementId, Need.optional(), null);
	}

	@Override
	public Mat editorIconType() {
		return CommonMats.REPEATER;
	}

}
