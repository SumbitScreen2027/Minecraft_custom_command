package com.guillaumevdn.customcommands.lib.customcommand;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

import com.guillaumevdn.customcommands.CustomCommands;
import com.guillaumevdn.customcommands.TextEditorCCMD;
import com.guillaumevdn.customcommands.lib.cmdlib.Command;
import com.guillaumevdn.customcommands.lib.cmdlib.CustomCommandCall;
import com.guillaumevdn.gcore.lib.GPlugin;
import com.guillaumevdn.gcore.lib.collection.CollectionUtils;
import com.guillaumevdn.gcore.lib.compatibility.material.CommonMats;
import com.guillaumevdn.gcore.lib.compatibility.material.Mat;
import com.guillaumevdn.gcore.lib.configuration.YMLConfiguration;
import com.guillaumevdn.gcore.lib.element.struct.Element;
import com.guillaumevdn.gcore.lib.element.struct.Need;
import com.guillaumevdn.gcore.lib.element.struct.SuperElement;
import com.guillaumevdn.gcore.lib.element.struct.container.ContainerElement;
import com.guillaumevdn.gcore.lib.element.type.basic.ElementStringList;
import com.guillaumevdn.gcore.lib.reflection.Reflection;
import com.guillaumevdn.gcore.lib.reflection.ReflectionObject;

/**
 * @author GuillaumeVDN
 */
public class ElementCustomCommand extends ContainerElement implements SuperElement {

	private ElementStringList aliases = addStringList("aliases", Need.required(), TextEditorCCMD.descriptionCustomCommandAliases);
	private ElementPatternList patterns = add(new ElementPatternList(this, "patterns", Need.required(), TextEditorCCMD.descriptionCustomCommandPatterns));

	public ElementCustomCommand(File file, String id) {
		super(null, id, Need.optional(), null);
		this.file = file;
	}

	public ElementStringList getAliases() {
		return aliases;
	}

	public ElementPatternList getPatterns() {
		return patterns;
	}

	transient Command cmdlibCommand = null;  // the command from the cmd library, reset on editor changes (by super element)

	public Command getCmdlibCommand() {
		if (cmdlibCommand == null) {
			cmdlibCommand = new Command(aliases.parseGeneric().orEmptyList(), patterns.values().stream().map(p -> p.getCmdlibPattern()).collect(Collectors.toList()));
		}
		return cmdlibCommand;
	}

	private transient PluginCommand registeredCommand = null;

	public void unregisterBukkit() {
		try {
			if (registeredCommand != null) {
				ReflectionObject commandMap = ReflectionObject.of(Bukkit.getPluginManager()).getField("commandMap");
				synchronized (commandMap.justGet()) {  // not using a method from commandMap, so synchronizing manually
					registeredCommand.unregister(commandMap.get());
					commandMap.getField("knownCommands").invokeMethod("remove", "customcommands:" + registeredCommand.getName());
				}
			}
		} catch (Throwable exception) {
			CustomCommands.inst().getMainLogger().error("Could not unregister custom command " + getId(), exception);
		}
	}

	public void registerBukkit() {
		try {
			if (registeredCommand != null) {
				unregisterBukkit();
			}
			List<String> aliases = getAliases().parseGeneric().orEmptyList();
			if (!aliases.isEmpty()) {
				ReflectionObject commandMap = ReflectionObject.of(Bukkit.getPluginManager()).getField("commandMap");

				PluginCommand command = Reflection.newInstance("org.bukkit.command.PluginCommand", aliases.get(0), CustomCommands.inst()).get();
				List<String> al = CollectionUtils.asList(aliases);
				al.remove(0);
				command.setAliases(al);

				command.setExecutor((sender, __, label, args) -> {
					getCmdlibCommand().call(new CustomCommandCall(sender, label, this, args));
					return true;
				});
				command.setTabCompleter((sender, __, label, args) -> {
					List<String> l = CollectionUtils.asList(args);
					if (!l.isEmpty()) {
						if (!l.get(l.size() - 1).isEmpty()) {
							return new ArrayList<>(0);  // do not suggest anything if we're not starting a new argument
						}
						l.remove(l.size() - 1);
						args = l.toArray(new String[l.size()]);
					}
					CustomCommandCall call = new CustomCommandCall(sender, label, this, args);
					return getPatterns().values().stream().flatMap(pattern -> pattern.getCmdlibPattern().tabComplete(call).stream().filter(str -> str != null)).collect(Collectors.toList());
				});

				commandMap.invokeMethod("register", "customcommands", command);
			}
		} catch (Throwable exception) {
			CustomCommands.inst().getMainLogger().error("Could not register custom command " + getId(), exception);
		}
	}

	// ----- editor

	@Override
	public Mat editorIconType() {
		return CommonMats.CHEST;
	}

	// ----- super element

	private File file;
	private List<String> loadErrors = new ArrayList<>();
	protected YMLConfiguration config = null;

	@Override public GPlugin getPlugin() { return CustomCommands.inst(); }
	@Override public File getOwnFile() { return file; }
	@Override public List<String> getLoadErrors() { return Collections.unmodifiableList(loadErrors); }
	@Override public YMLConfiguration getConfiguration() { if (config == null) { reloadConfiguration(); } return config; }
	@Override public String getConfigurationPath() { return ""; }
	@Override public void addLoadError(String error) { loadErrors.add(error); }
	@Override public void reloadConfiguration() { this.config = new YMLConfiguration(getPlugin(), file); }

	@Override
	public void onEditorChange(Element changed) {
		unregisterBukkit();
		SuperElement.super.onEditorChange(changed);
		cmdlibCommand = null;
		patterns.values().forEach(pattern -> pattern.cmdlibPattern = null);
		registerBukkit();
	}

}
