package com.guillaumevdn.customcommands.lib.condition.type.common;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.type.list.ElementItemMatchList;
import com.guillaumevdn.gcore.lib.item.ItemCheck;
import com.guillaumevdn.gcore.lib.item.ItemReference;
import com.guillaumevdn.gcore.lib.item.ItemUtils;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public class ConditionTypeInventoryFreeForItems extends ConditionType {

	public ConditionTypeInventoryFreeForItems(String id) {
		super(id, CommonMats.GOLDEN_APPLE);
	}

	// ----- elements
	@Override
	protected void doFillTypeSpecificElements(ElementCondition condition) {
		super.doFillTypeSpecificElements(condition);
		condition.addItemMatchList("items", Need.required(), false, TextEditorCCMD.descriptionConditionInventoryFreeForItemsItems);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		// parse
		ElementItemMatchList items = condition.getElementAs("items");
		Map<ItemReference, Integer> remaining = new HashMap<>();
		items.values().forEach(match -> match.parse(replacer).ifPresentDo(parsed -> remaining.put(parsed.getReference(), parsed.getGoal())));
		if (items.size() != remaining.size()) {
			return false;
		}
		// check
		for (int i = 0; i < 36; ++i) {
			ItemStack item = player.getInventory().getItem(i);
			if (!Mat.isVoid(item)) {
				for (ItemReference match : remaining.keySet()) {
					if (ItemUtils.match(item, match, ItemCheck.ExactSame)) {
						int itemFree = item.getMaxStackSize() - item.getAmount();
						if (itemFree != 0) {
							int newFree = remaining.get(match) - itemFree;
							if (newFree <= 0) {
								remaining.remove(match);
							} else {
								remaining.put(match, newFree);
							}
						}
						if (remaining.isEmpty()) {
							return true;
						}
						continue;
					}
				}
			}
		}
		// no match
		return false;

	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

}
