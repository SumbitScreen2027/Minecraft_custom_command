package com.guillaumevdn.customcommands.lib.cmdlib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.customcommands.lib.cmdlib.argument.ArgumentModel;
import com.guillaumevdn.customcommands.lib.cmdlib.argument.ArgumentModelPhrase;
import com.guillaumevdn.customcommands.lib.cmdlib.argument.ArgumentString;
import com.guillaumevdn.customcommands.lib.cmdlib.error.InvalidPatternError;
import com.guillaumevdn.gcore.TextGeneric;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.object.ObjectUtils;
import com.guillaumevdn.gcore.lib.permission.Permission;
import com.guillaumevdn.gcore.lib.string.Text;

public class CommandPattern {

	private List<Argument> arguments = new ArrayList<>();
	private List<String> fullPatterns = new ArrayList<>();
	private String description;
	private Permission permission;
	private Text permissionErrorText;

	/**
	 * Creates a new command arguments pattern.
	 * <br>Separate aliases with | like <pre>alias1|alias2</pre>
	 * Set arguments optional by adding a default values with $ like <pre>[string]$default <b>or</b> [integer]$15</pre>
	 * Add description to model arguments with % like <pre>[integer]%amount_to_charge <b>or</b> [world]%world_name <b>(_ will be replaced by a space)</b></pre>
	 * @param rawPattern the raw pattern
	 */
	public CommandPattern(String rawPattern, String description, Permission permission, Text customNoPermissionMessage) throws InvalidPatternError {
		this.description = description;
		this.permission = permission;
		this.permissionErrorText = customNoPermissionMessage != null ? customNoPermissionMessage : TextGeneric.messageNoPermission;

		// load
		boolean hasOptionalArguments = false, hasPhraseArgument = false;
		int fullPatternsCount = 0;
		try {
			if (!rawPattern.isEmpty()) {
				for (String raw : rawPattern.split(" ")) {
					// trying to add argument but already has phrase argument before
					if (hasPhraseArgument) {
						throw new InvalidPatternError("trying to add argument " + raw + " after a phrase argument model");
					}

					// desc
					String desc;
					if (raw.contains("%")) {
						String[] split = raw.split("%");
						raw = split[0];
						desc = split[1].replace("_", " ");
					} else {
						desc = raw.replace("_", " ").replace("[", "").replace("]", "");
					}

					// default value
					String def = null;
					if (raw.contains("$")) {
						// set default value
						String[] split = raw.split("\\$");
						raw = split[0];
						def = split[1];
						hasOptionalArguments = true;
					}
					// trying to add required argument but already has optional arguments before
					else if (hasOptionalArguments) {
						throw new InvalidPatternError("trying to add optional argument " + raw + " after required arguments");
					}

					// model argument
					if (raw.startsWith("[")) {
						// model
						ArgumentModel argModel = ArgumentModel.decode(raw, def, desc);
						if (argModel == null) {
							throw new InvalidPatternError("could not decode argument model " + raw);
						} else if (argModel instanceof ArgumentModelPhrase) {
							hasPhraseArgument = true;
						}
						// add
						arguments.add(argModel);
					}
					// string argument
					else {
						ArgumentString argString = new ArgumentString(CollectionUtils.asLowercaseList(raw.split("\\|")), def);
						// fullPatterns count
						if (argString.getAliases().size() > fullPatternsCount) {
							fullPatternsCount = argString.getAliases().size();
						}
						// add
						arguments.add(argString);
					}
				}
			}
		} catch (Throwable throwable) {
			throw new InvalidPatternError("could not decode argument pattern " + rawPattern, throwable);
		}

		// load full patterns
		String[] fullPatterns = new String[fullPatternsCount];
		for (int j = 0; j < fullPatternsCount; j++) {
			fullPatterns[j] = "";
		}
		for (int i = 0; i < arguments.size(); i++) {
			Argument arg = arguments.get(i);
			List<String> aliases = new ArrayList<>();
			if (arg instanceof ArgumentModel) aliases.add(((ArgumentModel) arg).getModel());
			else if (arg instanceof ArgumentString) aliases.addAll(((ArgumentString) arg).getAliases());
			for (int j = 0; j < fullPatternsCount; j++) {
				fullPatterns[j] += (j < aliases.size() ? aliases.get(j) : aliases.get(0)) + " ";
			}
		}

		this.fullPatterns = CollectionUtils.asList(fullPatterns);
	}

	// getters
	public List<Argument> getArguments() {
		return arguments;
	}

	public List<String> getFullPatterns() {
		return fullPatterns;
	}

	public String getUsage() {
		if (!arguments.isEmpty()) {
			String usage = "";
			for (Argument arg : arguments) {
				String prefix = "", suffix = "";
				if (arg instanceof ArgumentModel) {
					prefix = arg.getDefaultValue() == null ? "[" : "(";
					suffix = arg.getDefaultValue() == null ? "]" : ")";
				}
				usage += " " + prefix + arg.getDescription() + suffix;
			}
			return usage.trim();
		}
		return "{no arguments}";
	}

	// methods
	public CommandPatternResult call(CustomCommandCall call) {
		// no permission
		if (permission != null && !call.senderHasPermission(permission)) {
			return new CommandPatternResult(Result.ERROR, null, permissionErrorText);
		}

		// result
		Result callResult = Result.NONE;
		int callWildcards = 0;
		String callErrorArg = null;
		Text callErrorText = null;

		// empty args
		if (arguments.isEmpty()) {
			if (call.getArgsSize() == 0) {
				callResult = Result.MATCH;
			}
		}
		// check arguments
		else {
			for (int argIndex = 0; argIndex < arguments.size(); argIndex++) {
				Argument arg = arguments.get(argIndex);
				String callArg = call.getArgAsString(this, argIndex);

				// sender didn't enter this required argument AND the argument isn't "[phrase]"
				if (callArg == null && !(arg instanceof ArgumentModelPhrase)) {
					callResult = Result.NONE;
					break;
				}

				// argument model
				if (arg instanceof ArgumentModel) {
					ArgumentModel argModel = (ArgumentModel) arg;
					CommandPatternResult result = argModel.call(call.getSender(), callArg);

					// error
					if (result.getResult().equals(Result.ERROR)) {
						callResult = Result.ERROR;
						callErrorArg = result.getErrorArg();
						callErrorText = result.getErrorText();
						break;
					}
					// match, so continue
					else if (result.getResult().equals(Result.MATCH)) {
						callResult = Result.MATCH;
						callWildcards += result.getWildcards();
						continue;
					}
				}
				// argument string
				else if (arg instanceof ArgumentString) {
					ArgumentString argString = (ArgumentString) arg;
					// error
					if (!argString.getAliases().contains(call.getArgAsString(this, argIndex))) {
						callResult = Result.NONE;
						break;
					}
					// match, so continue
					else {
						callResult = Result.MATCH;
						continue;
					}
				}
				// unknown arg type
				else {
					callResult = Result.NONE;
					break;
				}
			}
		}

		// unknown
		return new CommandPatternResult(callResult, callResult.isAllowWildcards() ? callWildcards : 0, callResult.isAllowError() ? callErrorArg : null, callResult.isAllowError() ? callErrorText : null);
	}

	public List<String> tabComplete(CustomCommandCall call) {
		// no permission
		if (permission != null && !call.senderHasPermission(permission)) {
			return new ArrayList<>(0);
		}

		// empty args
		if (arguments.isEmpty()) {
			return new ArrayList<>(0);
		}
		// check arguments
		else {
			for (int argIndex = 0; argIndex < Math.min(arguments.size(), call.getArgsSize()); argIndex++) {
				Argument arg = arguments.get(argIndex);
				String callArg = call.getArgAsString(this, argIndex);

				// argument model
				if (arg instanceof ArgumentModel) {
					ArgumentModel argModel = (ArgumentModel) arg;
					CommandPatternResult result = argModel.call(call.getSender(), callArg);

					// error
					if (result.getResult().equals(Result.ERROR)) {
						return new ArrayList<>(0);
					}
					// match, so continue
					else if (result.getResult().equals(Result.MATCH)) {
						continue;
					}
				}
				// argument string
				else if (arg instanceof ArgumentString) {
					ArgumentString argString = (ArgumentString) arg;
					// error
					if (!argString.getAliases().contains(call.getArgAsString(this, argIndex))) {
						return new ArrayList<>(0);
					}
					// match, so continue
					else {
						continue;
					}
				}
				// unknown arg type
				else {
					return new ArrayList<>(0);
				}
			}

			// suggest next argument if previous arguments are valid
			return call.getArgsSize() >= arguments.size() ? new ArrayList<>(0) : arguments.get(call.getArgsSize()).tabComplete();
		}
	}

	/**
	 * @return true if help was shown
	 */
	public boolean showHelp(String commandHelpName, CommandSender sender) {
		if (description == null) return false;
		if (permission != null && !permission.has(sender)) return false;
		String usage = getUsage();

		sender.sendMessage("§a/" + commandHelpName + (usage.isEmpty() ? "" : " " + usage) + "§f - " + description);
		return true;
	}

	public void perform(CustomCommandCall call) {
		showHelp(call.getCommandName(), call.getSender());
	}

	@Override
	public boolean equals(Object obj) {
		if (!ObjectUtils.instanceOf(obj, CommandPattern.class)) return false;
		CommandPattern other = (CommandPattern) obj;
		return this.arguments.equals(other.arguments) && Objects.equals(this.description, other.description) && Objects.equals(this.permission, other.permission);
	}

}
