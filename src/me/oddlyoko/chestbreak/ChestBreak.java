/**
 * 
 */
package me.oddlyoko.chestbreak;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class allow you to <br />
 * <br />
 * Created: 17 févr. 2018 (13:23:39);<br />
 * Last edit: 17 févr. 2018 (13:23:39);<br />
 * 
 * @author 0ddlyoko<br />
 *         <br />
 */
public class ChestBreak extends JavaPlugin {
	private Config config;
	private Config configChests;
	private List<Location> chests;

	@Override
	public void onEnable() {
		boolean create = false;
		File f = new File("plugins" + File.separator + Variables.PLUGINNAME + File.separator + "chests.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			create = true;
		}
		configChests = new Config(f);
		if (create)
			configChests.set("enable", false);
		loadConfig();
		Bukkit.getPluginManager().registerEvents(new Events(this), this);
		System.out.println(Variables.PLUGINNAMECOLOR + ChatColor.GREEN + "Plugin Loaded");
	}

	@Override
	public void onDisable() {
		System.out.println(Variables.PLUGINNAMECOLOR + ChatColor.GREEN + "Plugin UnLoaded");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ("chestbreak".equalsIgnoreCase(label) || "cb".equalsIgnoreCase(label)) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.YELLOW + "********** " + Variables.PLUGINNAME + "**********");
				sender.sendMessage(ChatColor.YELLOW + "Created by " + ChatColor.GOLD + "0ddlyoko");
				sender.sendMessage(ChatColor.YELLOW + "WebSite: " + ChatColor.GOLD + "http://www.0ddlyoko.be");
			} else {
				if (("on".equalsIgnoreCase(args[0]) || "off".equalsIgnoreCase(args[0]))
						&& sender.hasPermission("chestbreak.toggle")) {
					if ("on".equalsIgnoreCase(args[0])) {
						setEnable(true);
						sender.sendMessage(Variables.PLUGINNAMECOLOR + ChatColor.GREEN + "Plugin On !");
					} else if ("off".equalsIgnoreCase(args[0])) {
						setEnable(false);
						sender.sendMessage(Variables.PLUGINNAMECOLOR + ChatColor.RED + "Plugin Off !");
					}
				}
			}
		}
		return false;
	}

	public Config getMainConfig() {
		return config;
	}

	public Config getChestsConfig() {
		return configChests;
	}

	private void loadConfig() {
		chests = new ArrayList<>();
		for (String str : configChests.getStringList("locs"))
			chests.add(getLocationFromString(str));
	}

	public void setEnable(boolean enable) {
		configChests.set("enable", enable);
	}

	public boolean isEnable() {
		return configChests.getBoolean("enable");
	}

	public void addChest(Location loc) {
		chests.add(loc);
		configChests.set("chests", format(chests));
	}

	public void removeChest(Location loc) {
		chests.remove(loc);
		configChests.set("chests", format(chests));
	}

	public boolean hasChest(Location loc) {
		return chests.contains(loc);
	}

	private List<String> format(List<Location> locs) {
		ArrayList<String> lists = new ArrayList<>();
		locs.forEach(loc -> lists.add(getStringFromLocation(loc)));
		return lists;
	}

	public static Location getLocationFromString(String locString) {
		if (locString == null || "".equalsIgnoreCase(locString))
			return null;
		String[] strs = locString.split(";");
		if (strs.length == 4) {
			World w = Bukkit.getWorld(strs[0]);
			return new Location(w == null ? Bukkit.getWorld("world") : w, Integer.parseInt(strs[1]),
					Integer.parseInt(strs[2]), Integer.parseInt(strs[3]));
		} else
			return null;
	}

	public static String getStringFromLocation(Location loc) {
		if (loc == null)
			return null;
		return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
	}
}
