package me.oddlyoko.chestbreak;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This class allow you to interact with an yml file.<br />
 * <br />
 * 
 * @author 0ddlyoko<br />
 *         <br />
 *         Created: 12 oct. 2016 (18:49:55);<br />
 *         Last edit: 6 fev. 2017 (22:54:43);<br />
 */
public class Config {
	private File fichierConfig;
	private FileConfiguration fconfig;

	public Config(File file) {
		this.fichierConfig = file;
		loadConfig();
	}

	public void save() {
		try {
			fconfig.save(fichierConfig);
		} catch (IOException ex) {
			Bukkit.getLogger().severe("An error has occured while saving file " + fichierConfig.getPath());
		}
	}

	private void loadConfig() {
		fconfig = YamlConfiguration.loadConfiguration(fichierConfig);
	}

	public void set(String path, Object obj) {
		fconfig.set(path, obj);
		save();
	}

	public String getString(String path) {
		String name = fconfig.getString(path);
		return name == null ? null : name.replace("&", "§");
	}

	public int getInt(String path) {
		return fconfig.getInt(path);
	}

	public long getLong(String path) {
		return fconfig.getLong(path);
	}

	public boolean getBoolean(String path) {
		return fconfig.getBoolean(path);
	}

	public double getDouble(String path) {
		return fconfig.getDouble(path);
	}

	public List<String> getStringList(String path) {
		List<String> name = new ArrayList<>();
		for (String nom : fconfig.getStringList(path)) {
			name.add(nom.replace("&", "§"));
		}
		return name;
	}

	public List<Integer> getIntegerList(String path) {
		List<Integer> name = new ArrayList<>();
		for (Integer nom : fconfig.getIntegerList(path)) {
			name.add(nom);
		}
		return name;
	}

	public List<String> getKeys(String path) {
		List<String> list = new ArrayList<>();
		if ("".equalsIgnoreCase(path)) {
			for (String section : fconfig.getKeys(false)) {
				list.add(section);
			}
		} else {
			for (String section : fconfig.getConfigurationSection(path).getKeys(false)) {
				list.add(section);
			}
		}
		return list;
	}

	public boolean exist(String path) {
		return fconfig.contains(path);
	}
}
