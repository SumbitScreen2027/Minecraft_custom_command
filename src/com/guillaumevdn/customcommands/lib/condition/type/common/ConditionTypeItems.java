package com.guillaumevdn.customcommands.lib.condition.type.common;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.concurrency.RWWeakHashMap;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.container.ElementItemsNeeded;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeItems extends ConditionType {

	public ConditionTypeItems(String id) {
		super(id, CommonMats.GOLDEN_APPLE);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addItemsNeeded("items", Need.required(), TextEditorCCMD.descriptionConditionItemsItems);
	}

	// ----- match
	private RWWeakHashMap<Player, Object> refs = new RWWeakHashMap<>(5, 1f);  // tricky trick yes

	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		Object ref = new Object();
		refs.put(player, ref);
		return condition.getElementAs("items", ElementItemsNeeded.class).match(ref, replacer, CollectionUtils.asList(player.getUniqueId()), CollectionUtils.asList(player), false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return condition.getElementAs("items", ElementItemsNeeded.class).getCount().parse(replacer).orElse(999) > 0;
	}
	
	@Override
	protected void doTake(ElementCondition condition, Player player, Replacer replacer) {
		condition.getElementAs("items", ElementItemsNeeded.class).takeIfNeeded(refs.remove(player), replacer, CollectionUtils.asList(player));
	}

}
