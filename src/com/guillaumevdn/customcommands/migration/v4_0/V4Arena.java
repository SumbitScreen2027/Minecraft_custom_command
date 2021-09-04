package com.guillaumevdn.customcommands.migration.v4_0;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;

public class V4Arena {

	public int min;
	public int max;
	public Location lobby;
	public Location deathmatch;
	public Map<Integer, Location> spawns;
	public Map<Integer, Location> bonuses;
	public List<Location> signs;

}
