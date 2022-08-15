package com.guillaumevdn.customcommands.lib.condition.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import com.guillaumevdn.gcore.lib.string.Text;

/**
 * @author GuillaumeVDN
 */
public final class ConditionsCheckResult {

	public static final ConditionsCheckResult VALID = new ConditionsCheckResult();
	private static final List<Text> NO_ERRORS = Collections.unmodifiableList(new ArrayList<>());

	private final boolean valid;
	private final Player atFault;
	private final List<Text> errors;

	public ConditionsCheckResult() {
		this.valid = true;
		this.atFault = null;
		this.errors = NO_ERRORS;
	}

	public ConditionsCheckResult(Player atFault, List<Text> errors) {
		this.valid = false;
		this.atFault = atFault;
		this.errors = errors != null ? Collections.unmodifiableList(errors) : NO_ERRORS;
	}

	public boolean isValid() {
		return valid;
	}

	public Player getAtFault() {
		return atFault;
	}

	public List<Text> getErrors() {
		return errors;
	}

}
