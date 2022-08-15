package com.guillaumevdn.customcommands.lib.condition.element;

import java.util.Comparator;
import java.util.List;

import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.ConditionTypes;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.concurrency.RWWeakHashMap;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.ElementTypableElementType;
import com.guillaumevdn.gcore.lib.string.Text;

/**
 * @author GuillaumeVDN
 */
public final class ElementConditionType extends ElementTypableElementType<ConditionType> {

	public ElementConditionType(Element parent, String id, Text editorDescription) {
		super(ConditionTypes.inst(), parent, id, editorDescription);
	}

	private static RWWeakHashMap<Object, List<ConditionType>> cache = new RWWeakHashMap<>(1, 1f);
	@Override
	protected List<ConditionType> cacheOrBuild() {
		return cachedOrBuild(cache, () -> ConditionTypes.inst().values().stream().sorted(Comparator.comparing(e -> e.getId())));
	}

	@Override
	public Mat editorIconType() {
		return parseGeneric().ifPresentMap(ConditionType::getIcon).orElse(CommonMats.REDSTONE);
	}

}
