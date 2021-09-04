package com.guillaumevdn.customcommands.lib.action.element;

import java.util.Comparator;
import java.util.List;

import com.guillaumevdn.customcommands.lib.action.ActionType;
import com.guillaumevdn.customcommands.lib.action.ActionTypes;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.concurrency.RWWeakHashMap;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.ElementTypableElementType;
import com.guillaumevdn.gcore.lib.string.Text;

/**
 * @author GuillaumeVDN
 */
public final class ElementActionType extends ElementTypableElementType<ActionType> {

	public ElementActionType(Element parent, String id, Text editorDescription) {
		super(ActionTypes.inst(), parent, id, editorDescription);
	}

	private static RWWeakHashMap<Object, List<ActionType>> cache = new RWWeakHashMap<>(1, 1f);
	@Override
	protected List<ActionType> cacheOrBuild() {
		return cachedOrBuild(cache, () -> ActionTypes.inst().values().stream().sorted(Comparator.comparing(e -> e.getId())));
	}

	@Override
	public Mat editorIconType() {
		return parseGeneric().ifPresentMap(ActionType::getIcon).orElse(CommonMats.REDSTONE);
	}

}
