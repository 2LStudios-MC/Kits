package dev._2lstudios.kitsplugin.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigUtil {
	private final Plugin plugin;

	public ConfigUtil(final Plugin plugin) {
		this.plugin = plugin;
	}

	public YamlConfiguration get(final String filePath) {
		final File dataFolder = plugin.getDataFolder();
		final File file = new File(filePath.replace("%datafolder%", dataFolder.toPath().toString()));

		if (file.exists()) {
			return YamlConfiguration.loadConfiguration(file);
		} else {
			return new YamlConfiguration();
		}
	}

	public void create(String file) {
		try {
			final File dataFolder = plugin.getDataFolder();

			file = file.replace("%datafolder%", dataFolder.toPath().toString());

			final File configFile = new File(file);

			if (!configFile.exists()) {
				final String[] files = file.split("/");
				final InputStream inputStream = plugin.getClass().getClassLoader()
						.getResourceAsStream(files[files.length - 1]);
				final File parentFile = configFile.getParentFile();

				if (parentFile != null) {
					parentFile.mkdirs();
				}

				if (inputStream != null) {
					Files.copy(inputStream, configFile.toPath());
					plugin.getLogger().info("File " + configFile + " has been created!");
				} else {
					configFile.createNewFile();
				}
			}
		} catch (final IOException e) {
			plugin.getLogger().info("Unable to create configuration file!");
		}
	}

	public void delete(final String file) {
		final File file1 = new File(file.replace("%datafolder%", plugin.getDataFolder().toPath().toString()));

		if (file1.exists()) {
			file1.delete();
		}
	}

	public void deleteAsync(final String file) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> delete(file));
	}

	public void save(final YamlConfiguration yamlConfiguration, final String file) {
		try {
			final File dataFolder = plugin.getDataFolder();

			yamlConfiguration.save(file.replace("%datafolder%", dataFolder.toPath().toString()));
		} catch (final IOException e) {
			plugin.getLogger().info("[%pluginname%] Unable to save configuration file!");
		}
	}

	public void saveAsync(final YamlConfiguration yamlConfiguration, final String file) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> save(yamlConfiguration, file));
	}
}