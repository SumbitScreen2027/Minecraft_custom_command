package com.guillaumevdn.customcommands.lib.cmdlib;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.guillaumevdn.customcommands.TextCCMD;
import com.guillaumevdn.customcommands.lib.cmdlib.CommandPatternResult.Result;
import com.guillaumevdn.customcommands.lib.cmdlib.error.DuplicatePatternError;
import com.guillaumevdn.gcore.lib.collection.SortedHashMap;
import com.guillaumevdn.gcore.lib.tuple.Pair;

public class Command {

	private List<String> aliases;
	private List<CommandPattern> patterns;

	public Command(List<String> aliases, List<CommandPattern> patterns) throws DuplicatePatternError {
		this.aliases = aliases;
		this.patterns = patterns;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void addPattern(CommandPattern pattern) throws DuplicatePatternError {
		if (pattern == null) return;
		if (patterns.contains(pattern)) throw new DuplicatePatternError("trying to add existing pattern '" + pattern.getUsage() + "' to command " + aliases.get(0));
		patterns.add(pattern);
	}

	public void showHelp(CommandSender sender) {
		boolean empty = true;
		for (CommandPattern pattern : patterns) {
			boolean show = pattern.showHelp(aliases.get(0), sender);
			if (empty && show) empty = false;
		}
		if (empty) TextCCMD.messageNoHelp.send(sender);
	}

	public void call(CustomCommandCall call) {
		// has args
		SortedHashMap<CommandPattern, CommandPatternResult> match = SortedHashMap.valueSorted();
		Pair<CommandPattern, CommandPatternResult> error = null;

		// calling all patterns
		for (CommandPattern pattern : patterns) {
			CommandPatternResult result = pattern.call(call);
			result.setArgsSize(pattern.getArguments().size());
			if (result.getResult().equals(Result.MATCH)) {
				match.put(pattern, result);
			} else if (result.getResult().equals(Result.ERROR)) {
				if (error == null) {
					error = Pair.of(pattern, result);
				}
			}
		}

		// has matching patterns
		if (!match.isEmpty()) {
			match.getKeyAt(0).perform(call);
			return;
		}

		// has error patterns
		if (error != null) {
			CommandPatternResult result = error.getB();
			result.getErrorText().replace("{error}", () -> result.getErrorArg() == null ? "?" : result.getErrorArg()).send(call.getSender());
			return;
		}

		// if sender entered no arguments and we're at this point, just show help
		if (call.getArgsSize() == 0) {
			showHelp(call.getSender());
			return;
		}

		// find closest command
		CommandPattern closest = null;
		int closestSimilarity = -1;
		String[] currentSplit = call.getAllArguments().split(" ");
		for (CommandPattern commandPattern : patterns) {
			for (String pattern : commandPattern.getFullPatterns()) {
				// fill arguments
				String current = "";
				String[] patternSplit = pattern.split(" ");
				for (int i = 0; i < patternSplit.length; i++) {
					// has typed that much
					if (i < currentSplit.length) {
						// is pattern replacable
						if (patternSplit[i].contains("[")) {
							current += currentSplit[i] + " ";
						} else {
							current += patternSplit[0] + " ";
						}
					}
				}
				// not compared yet
				if (closestSimilarity == -1) {
					closestSimilarity = getLevenshteinSimilarity(current, call.getAllArguments());
					closest = commandPattern;
				}
				// compare
				else {
					int newSimilarity = getLevenshteinSimilarity(current, call.getAllArguments());
					if (newSimilarity < closestSimilarity) {
						closestSimilarity = newSimilarity;
						closest = commandPattern;
					}
				}
			}
		}

		// send error message, with suggestion if found
		if (closest != null) {
			final CommandPattern closestF = closest;
			TextCCMD.messageUnknownCommandClosest.replace("{closest}", () -> "/" + aliases.get(0) + " " + closestF.getUsage()).send(call.getSender());
		} else {
			TextCCMD.messageUnknownCommand.send(call.getSender());
		}
	}

	/**
	 * @return a number depending on the similarity of the two strings ; 0 = same strings, and the more it's up, the more it's similar ; potential similarity = 3
	 */
	private static int getLevenshteinSimilarity(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0) {
					costs[j] = j;
				} else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						}
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0) {
				costs[s2.length()] = lastValue;
			}
		}
		return costs[s2.length()];
	}

}
