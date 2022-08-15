package com.guillaumevdn.customcommands.lib.condition;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.element.struct.container.typable.TypableElementType;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;

/**
 * @author GuillaumeVDN
 */
public abstract class ConditionType extends TypableElementType<ElementCondition> {

	public ConditionType(String id, Mat icon) {
		super(id, icon);
	}

	public abstract boolean match(ElementCondition condition, Player player, @Nonnull Replacer replacer);

	public abstract boolean supportsTake(ElementCondition condition, Player player, @Nonnull Replacer replacer);

	public void takeIfSupported(ElementCondition condition, Player player, @Nonnull Replacer replacer) {
		if (supportsTake(condition, player, replacer)) {
			doTake(condition, player, replacer);
		}
	}

	protected void doTake(ElementCondition condition, Player player, @Nonnull Replacer replacer) {
		throw new UnsupportedOperationException("can't take condition type " + getId());
	}

}
