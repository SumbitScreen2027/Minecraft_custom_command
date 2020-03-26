package com.guillaumevdn.customcommands.commands;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.guillaumevdn.customcommands.CCLocale;
import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.commands.action.Action;
import com.guillaumevdn.customcommands.commands.action.ActionData;
import com.guillaumevdn.customcommands.data.CCUser;
import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CustomPattern extends CommandPattern {

	// fields and constructor
	private String argumentsId;
	private int cooldown;
	private boolean toggle;
	private List<ActionData> actionsToggleFalse, actionsToggleTrue;
	private List<String> worlds;

	public CustomPattern(String argumentsId, int cooldown, boolean toggle, List<ActionData> actionsToggleFalse, List<ActionData> actionsToggleTrue, List<String> worlds,
			String rawPattern, String description, String permission, String customNoPermissionMessage) throws InvalidPatternError {
		super(rawPattern, description, permission, true, customNoPermissionMessage);
		this.argumentsId = argumentsId;
		this.cooldown = cooldown;
		this.toggle = toggle;
		this.actionsToggleFalse = actionsToggleFalse;
		this.actionsToggleTrue = actionsToggleTrue;
		this.worlds = worlds;
	}

	// getters
	public String getArgumentsId() {
		return argumentsId;
	}

	public int getCooldown() {
		return cooldown;
	}

	public boolean isToggle() {
		return toggle;
	}

	public List<ActionData> getActionsToggleFalse() {
		return actionsToggleFalse;
	}

	public List<ActionData> getActionsToggleTrue() {
		return actionsToggleTrue;
	}

	public List<String> getWorlds() {
		return worlds;
	}

	// methods
	@Override
	public void perform(CommandCall call) {
		final Player sender = call.getSenderAsPlayer();
		final CCUser customCommandsUser = CustomCommands.inst().getData().getUsers().getElement(sender);

		// cooldown
		if (getCooldown() > 0 && customCommandsUser.hasCooldown(this)) {
			GLocale.MSG_GENERIC_COOLDOWN.send(call.getSender(), "{plugin}", CustomCommands.inst().getName(), "{time}", Utils.formatDurationMillis(customCommandsUser.getCooldownEnd(this) - System.currentTimeMillis()));
			return;
		}

		// worlds
		if (!worlds.isEmpty() && !worlds.contains(call.getSenderAsPlayer().getWorld().getName())) {
			CCLocale.MSG_CUSTOMCOMMANDS_UNAUTHORIZEDWORLD.send(call.getSender());
			return;
		}

		// get actions
		final List<ActionData> actions = isToggle() && customCommandsUser.isToggled(this) ? actionsToggleTrue : actionsToggleFalse;
		final String[] args = call.getArgs();

		if (isToggle()) {
			customCommandsUser.toggle(this);
		}

		if (getCooldown() > 0) {
			customCommandsUser.setCooldownEnd(this, System.currentTimeMillis() + Utils.getSecondsInMillis(cooldown));
		}

		// execute actions
		new BukkitRunnable() {

			private int index = -1;
			private Action current = null;

			@Override
			public void run() {
				if (current == null) {
					if (++index < actions.size()) {
						ActionData data = actions.get(index);
						current = CustomCommands.inst().createAction(argumentsId, data.getType(), sender, data.getData(), args);
					} else {
						cancel();
						return;
					}
				}

				if (current != null && current.isOver()) {
					current = null;
				}
			}
		}.runTaskTimer(CustomCommands.inst(), 0L, 1L);
	}

}
