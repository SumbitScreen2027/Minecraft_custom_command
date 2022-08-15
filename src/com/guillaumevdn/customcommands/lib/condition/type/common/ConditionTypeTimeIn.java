package com.guillaumevdn.customcommands.lib.condition.type.common;

import java.time.ZonedDateTime;

import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.lib.condition.ConditionType;
import com.guillaumevdn.customcommands.lib.condition.element.ElementCondition;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;
import com.guillaumevdn.gcore.lib.time.in.TimeIn;

/**
 * @author GuillaumeVDN
 */
public abstract class ConditionTypeTimeIn<T extends TimeIn> extends ConditionType {

	public ConditionTypeTimeIn(String id) {
		super(id, CommonMats.CLOCK);
	}

	// ----- match
	@Override
	public boolean match(ElementCondition condition, Player player, Replacer replacer) {
		return condition.directParseAndIfPresentMap("start", "end", replacer, (T start, T end) -> {
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime startTime = start.getCurrent();
			ZonedDateTime endTime = end.getCurrent();
			if (endTime.isBefore(startTime)) {  // going across the period
				endTime = end.getDelta(1);
			}
			return now.isAfter(startTime) && now.isBefore(endTime);
		}).orElse(false);
	}

	// ----- take
	@Override
	public boolean supportsTake(ElementCondition condition, Player player, Replacer replacer) {
		return false;
	}

}
