package com.guillaumevdn.customcommands;

import java.io.File;
import java.util.List;

import com.guillaumevdn.gcore.lib.messenger.Text;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CCLocale {

	public static final File file = new File(CustomCommands.inst().getDataFolder() + "/texts.yml");

	private static Text n(String id, Object... content) {
		return new Text(id, file, content);
	}

	private static List<String> l(String... content) {
		return Utils.asList(content);
	}

	public static final Text MSG_CUSTOMCOMMANDS_INVALIDARGUMENTSSIZE= n(
			"MSG_CUSTOMCOMMANDS_INVALIDARGUMENTSSIZE",
			"en_US", l("&6CustomCommands >> &7Invalid text argument size : &c{error}&7."),
			"fr_FR", l("&6CustomCommands >> &7Taille de l'argument texte invalide : &c{error}&7."),
			"hu_HU", l("&6CustomCommands >> &7Érvénytelen szöveges argumentumméret : &c{error}&7."),
			"pl_PL", l("&6CustomCommands >> &7Niepoprawna wielkość argumentu : &c{error}&7.")
			);

	public static final Text MSG_CUSTOMCOMMANDS_UNKNOWNCOMMAND= n(
			"MSG_CUSTOMCOMMANDS_UNKNOWNCOMMAND",
			"en_US", l("&6CustomCommands >> &7An error occured while processing your arguments. Maybe you're misusing the command. Closest command :"),
			"fr_FR", l("&6CustomCommands >> &7Une erreur est apparue lors de l'analyse de vos arguments. Peut-être utilisez vous mal la commande. Commande la plus proche :"),
			"hu_HU", l("&6CustomCommands >> &7Hiba történt az érvek feldolgozása során. Talán rosszul használja a parancsot. Legközelebbi parancs :"),
			"pl_PL", l("&6CustomCommands >> &7Wystąpił błąd podczas przetwarzania argumentów. Być może jest to niewłaściwa komenda. Najbardziej podobna komenda :")
			);

	public static final Text MSG_CUSTOMCOMMANDS_CLOSESTCOMMAND= n(
			"MSG_CUSTOMCOMMANDS_CLOSESTCOMMAND",
			"en_US", l("&c{command}")
			);

	public static final Text MSG_CUSTOMCOMMANDS_UNAUTHORIZEDWORLD= n(
			"MSG_CUSTOMCOMMANDS_UNAUTHORIZEDWORLD",
			"en_US", l("&6CustomCommands >> &7This command isn't enabled in this world."),
			"fr_FR", l("&6CustomCommands >> &7Cette commande n'est pas activée dans ce monde."),
			"hu_HU", l("&6CustomCommands >> &7Ez a parancs nem engedélyezett ebben a világban.")
			);

	public static final Text MSG_CUSTOMCOMMANDS_ITEMSAVE= n(
			"MSG_CUSTOMCOMMANDS_ITEMSAVE",
			"en_US", l("&6CustomCommands >> &7Item was saved with name &a{name}&7."),
			"fr_FR", l("&6CustomCommands >> &7L'item a été sauvegardé sous le nom &a{name}&7."),
			"hu_HU", l("&6CustomCommands >> &7Ez az elem mentett névvel &a{name}&7.")
			);

	public static final Text MSG_CUSTOMCOMMANDS_LOCATIONSAVE= n(
			"MSG_CUSTOMCOMMANDS_LOCATIONSAVE",
			"en_US", l("&6CustomCommands >> &7Location was saved with name &a{name}&7."),
			"fr_FR", l("&6CustomCommands >> &7L'endroit a été sauvegardé sous le nom &a{name}&7."),
			"hu_HU", l("&6CustomCommands >> &7Ez a hely neve mentett el &a{name}&7.")
			);

	public static final Text MSG_CUSTOMCOMMANDS_ITEMDELETE= n(
			"MSG_CUSTOMCOMMANDS_ITEMDELETE",
			"en_US", l("&6CustomCommands >> &7Item with name &a{name} &7was removed."),
			"fr_FR", l("&6CustomCommands >> &7L'item sous le nom &a{name} &7a été supprimé.")
			);

	public static final Text MSG_CUSTOMCOMMANDS_LOCATIONDELETE= n(
			"MSG_CUSTOMCOMMANDS_LOCATIONDELETE",
			"en_US", l("&6CustomCommands >> &7Location with name &a{name} &7was removed."),
			"fr_FR", l("&6CustomCommands >> &7L'endroit sous le nom &a{name} &7a été supprimé.")
			);

}
