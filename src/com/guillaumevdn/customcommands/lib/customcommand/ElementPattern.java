
package com.guillaumevdn.customcommands.lib.customcommand;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.data.user.UserCCMD;
import com.guillaumevdn.customcommands.lib.action.element.ElementAction;
import com.guillaumevdn.customcommands.lib.action.element.ElementActionList;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPattern;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.customcommands.lib.condition.element.ConditionsCheckResult;
import com.guillaumevdn.customcommands.lib.condition.element.ElementConditions;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.economy.Currency;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.struct.container.ContainerElement;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementBoolean;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementPermission;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementString;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementText;
import com.guillaumevdn.gcore.lib.element.type.container.ElementWorldRestriction;
import com.guillaumevdn.gcore.lib.element.type.map.ElementCurrencyDoubleMap;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.lib.string.StringUtils;
import com.guillaumevdn.gcore.lib.string.Text;
import com.guillaumevdn.gcore.lib.string.placeholder.Replacer;
import com.guillaumevdn.gcore.lib.time.duration.ElementDuration;
import com.guillaumevdn.gcore.lib.wrapper.WrapperInteger;

/**
 * @author GuillaumeVDN
 */
public class ElementPattern extends ContainerElement {

	private ElementString pattern = addString("pattern", Need.required(), TextEditorCCMD.descriptionCustomCommandPatternDescription);
	private ElementString description = addString("description", Need.optional(), TextEditorCCMD.descriptionCustomCommandPatternDescription);
	private ElementPermission permission = addPermission("permission", Need.optional(), TextEditorCCMD.descriptionCustomCommandPatternPermission);
	private ElementText permissionErrorMessage = addText("permission_error_message", Need.optional(), TextEditorCCMD.descriptionCustomCommandPatternPermissionErrorMessage);
	private ElementWorldRestriction worlds = addWorldRestriction("worlds", Need.optional(), TextEditorCCMD.descriptionCustomCommandPatternWorlds);
	private ElementDuration cooldown = addDuration("cooldown", Need.optional(), null, null, TextEditorCCMD.descriptionCustomCommandPatternCooldown);
	private ElementCurrencyDoubleMap currencyCost = addCurrencyDoubleMap("currency_cost", Need.optional(), TextEditorCCMD.descriptionCustomCommandPatternCurrencyCost);
	private ElementConditions conditions = add(new ElementConditions(this, "conditions", Need.optional(), TextEditorCCMD.descriptionCustomCommandPatternConditions));
	private ElementBoolean toggleMode = addBoolean("toggle_mode", Need.optional(false), TextEditorCCMD.descriptionCustomCommandPatternToggleMode);
	private ElementActionList actions = add(new ElementActionList(this, "actions", Need.required(), TextEditorCCMD.descriptionCustomCommandPatternActions));

	public ElementPattern(Element parent, String id, Need need, Text editorDescription) {
		super(parent, id, need, editorDescription);
	}

	public ElementString getPattern() {
		return pattern;
	}

	public ElementString getDescription() {
		return description;
	}

	public ElementPermission getPermission() {
		return permission;
	}

	public ElementText getPermissionErrorMessage() {
		return permissionErrorMessage;
	}

	public ElementWorldRestriction getWorlds() {
		return worlds;
	}

	public ElementDuration getCooldown() {
		return cooldown;
	}

	public ElementCurrencyDoubleMap getCurrencyCost() {
		return currencyCost;
	}

	public ElementConditions getConditions() {
		return conditions;
	}

	public ElementBoolean getToggleMode() {
		return toggleMode;
	}

	public ElementActionList getActions() {
		return actions;
	}

	@Override
	public Mat editorIconType() {
		return CommonMats.PAPER;
	}

	transient CommandPattern cmdlibPattern = null;  // the pattern from the cmd library, reset on editor changes (by super element)

	public CommandPattern getCmdlibPattern() {
		if (cmdlibPattern == null) {

			final ElementCustomCommand command = (ElementCustomCommand) getParent().getParent();

			cmdlibPattern = new CommandPattern(pattern.parseGeneric().orElse(""), description.parseGeneric().orNull(), permission.parseGeneric().orNull(), permissionErrorMessage.parseGeneric().orNull()) {
				@Override
				public void perform(CustomCommandCall call) {

					final CommandSender sender = call.getSender();
					final Player player = ObjectUtils.castOrNull(sender, Player.class);
					final UserCCMD user = sender instanceof Player ? UserCCMD.cachedOrNull((Player) sender) : null;
					final Replacer rep = Replacer.justPlayer(player);

					// worlds
					if (player != null && !worlds.isAllowed(player.getWorld(), rep)) {
						TextCCMD.messageUnauthorizedWorld.send(sender);
						return;
					}

					// cooldown
					if (user != null) {
						Long cooldownMillis = getCooldown().parseGeneric().orElse(0L);
						if (cooldownMillis > 0L) {
							Long last = user.getLastUse(command.getId(), ElementPattern.this.getId());
							if (last != null && last + cooldownMillis > System.currentTimeMillis()) {
								TextCCMD.messageCooldown.replace("{time}", () -> StringUtils.formatDurationMillis((last + cooldownMillis) - System.currentTimeMillis())).send(sender);
								return;
							}
						}
					}

					// cost
					final Map<Currency, Double> cost = currencyCost.parse(rep).orEmptyMap();
					for (Entry<Currency, Double> c : cost.entrySet()) {
						if (!c.getKey().ensureHas(player, c.getValue(), true)) {
							return;
						}
					}

					// conditions
					final ConditionsCheckResult res = conditions.match(player, rep, true);
					if (!res.isValid()) {
						res.getErrors().forEach(text -> text.send(player));
						return;
					}

					// take money
					for (Entry<Currency, Double> c : cost.entrySet()) {
						c.getKey().take(player, c.getValue());
					}

					// take conditions
					conditions.takeIfNeededAndSupported(player, rep);

					// update user
					final boolean toggleMode = getToggleMode().parseGeneric().orElse(false);
					if (user != null) {
						if (toggleMode) {
							user.toggle(command.getId(), ElementPattern.this.getId());
						}
						user.setLastUse(command.getId(), ElementPattern.this.getId());
					}

					// execute actions
					call.setReplacerPattern(this);

					final String taskId = "command_" + command.getId() + "_" + sender.getName() + "_" + StringUtils.generateRandomAlphanumericString(5);
					final boolean isNowToggled = user == null || user.isToggled(command.getId(), ElementPattern.this.getId());
					final List<ElementAction> remainingActions = CollectionUtils.asList(getActions().values());
					final WrapperInteger ticksToWait = WrapperInteger.of(0);

					CustomCommands.inst().registerTask(taskId, false, 1, () -> {
						if (remainingActions.isEmpty()) {
							CustomCommands.inst().stopTask(taskId);
							return;
						}
						if (ticksToWait.get() > 0 && ticksToWait.alter(-1) > 0) {
							return;
						}
						ElementAction action = remainingActions.remove(0);
						if (!toggleMode || (action.getOnToggleOn().parseGeneric().orElse(true) ? isNowToggled : !isNowToggled)) {
							int wait = action.getType().execute(action, call);
							if (wait > 0) {
								ticksToWait.set(wait);
							}
						}
					});
				}
			};
		}
		return cmdlibPattern;
	}

}
