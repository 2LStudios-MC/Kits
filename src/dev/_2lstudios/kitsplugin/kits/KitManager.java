package dev._2lstudios.kitsplugin.kits;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.kitsplugin.utils.ConfigUtil;

public class KitManager {
	private final Plugin plugin;
	private final ConfigUtil configurationUtil;
	// This is used to prevent players from taking items from this inventories
	private final Collection<Inventory> kitInvenories = new HashSet<>();
	private final Map<String, Kit> kitMap = new HashMap<>();

	public KitManager(final Plugin plugin, final ConfigUtil configurationUtil) {
		this.plugin = plugin;
		this.configurationUtil = configurationUtil;

		configurationUtil.create("%datafolder%/kits.yml");
		reload();
	}

	public void addKitInventory(final Inventory kitInventory) {
		this.kitInvenories.add(kitInventory);
	}

	public void removeKitInventory(final Inventory kitInventory) {
		kitInventory.clear();

		this.kitInvenories.remove(kitInventory);
	}

	public boolean isKitInventory(final Inventory kitInventory) {
		return this.kitInvenories.contains(kitInventory);
	}

	public Collection<Inventory> getKitInventories() {
		return this.kitInvenories;
	}

	public void openPreviewInventory(final Player player, final Kit kit) {
		final Inventory previewInventory = plugin.getServer().createInventory(player, 54,
				"Preview de " + kit.getName());

		addKitInventory(previewInventory);
		previewInventory.setContents(kit.getContents());
	}

	private void generateDescription(final KitPlayer kitPlayer, final Kit kit, final String kitName,
			final ItemStack itemStack) {
		final ItemMeta itemMeta = itemStack.getItemMeta();
		final List<String> lore = new ArrayList<>();
		final long cooldown = kitPlayer.getCooldown(kitName);
		final ChatColor color;

		lore.add("");

		if (cooldown > 0) {
			final int kitCooldown = kit.getCooldown();
									final long lastTakeTime = kitPlayer.getCooldown(kit.getName());
			final int milliseconds = (int) ((lastTakeTime + kitCooldown)
			- System.currentTimeMillis()), seconds = (int) (milliseconds / 1000) % 60,
			minutes = (int) ((milliseconds / (1000 * 60)) % 60),
			hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

			color = ChatColor.RED;
			lore.add(ChatColor.RED + "BLOQUEADO");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&cEspera &e" + hours + "H " + minutes + "M " + seconds + "S"));
		} else {
			color = ChatColor.GREEN;
			lore.add(ChatColor.GREEN + "Click para elegir!");
		}

		itemMeta.setLore(lore);
		itemMeta.setDisplayName(color + kitName);
		itemStack.setItemMeta(itemMeta);
	}

	public void openInventory(final Player player, final KitPlayer kitPlayer, int page) {
		// Each inventory can hold up to 28 kits
		// Kits start populating from slot 10
		// Every 7 kits 2 slots are skipped empty
		final Collection<Kit> kits = getKits();
		final Inventory inventory = plugin.getServer().createInventory(player, 54, "Kits");
		final int lastPage = 1 + ((kits.size() - 1) / 28);

		if (page < 1) {
			page = 1;
		} else if (page > lastPage) {
			page = lastPage;
		}

		if (page != 1) {
			inventory.setItem(45, new ItemStack(Material.ARROW));
		}

		inventory.setItem(49, new ItemStack(Material.ARROW));

		if (page != lastPage) {
			inventory.setItem(53, new ItemStack(Material.ARROW));
		}

		int skipKits = 28 * (page - 1);
		int slot = 10;
		int kitCount = 0;

		if (skipKits >= kits.size()) {
			skipKits = 0;
		}

		for (final Kit kit : kits) {	
			if (skipKits <= 0) {
				final ItemStack itemStack = new ItemStack(kit.getIcon());

				generateDescription(kitPlayer, kit, kit.getName(), itemStack);

				inventory.setItem(slot, itemStack);

				kitCount++;

				if (kitCount % 7 == 0) {
					slot += 3;
				} else {
					slot++;
				}

				if (slot > 43) {
					break;
				}
			} else {
				skipKits--;
			}
		}

		addKitInventory(inventory);

		player.openInventory(inventory);
	}

	public void openInventory(final Player player, final KitPlayer kitPlayer) {
		openInventory(player, kitPlayer, 0);
	}

	public void reload() {
		final File kitFolder = new File(plugin.getDataFolder() + "/kits/");
		final String[] kitFiles = kitFolder.list();

		kitFolder.mkdirs();
		kitMap.clear();

		if (kitFiles != null)
			for (final String kitFileName : kitFiles) {
				final Configuration configuration = configurationUtil
						.get("%datafolder%/kits/" + kitFileName);
				final String name = kitFileName.replace(".yml", "");
				final int cooldown = configuration.getInt("cooldown");
				ItemStack helmet = null;
				ItemStack chestplate = null;
				ItemStack leggings = null;
				ItemStack boots = null;
				final ItemStack[] contents = new ItemStack[36];
				final ConfigurationSection itemsSection = configuration.getConfigurationSection("items");

				if (itemsSection != null) {
					for (final String slot : itemsSection.getKeys(false)) {
						final ItemStack itemStack = configuration.getItemStack("items." + slot);

						if (itemStack != null) {
							if (slot.equals("helmet"))
								helmet = itemStack;
							if (slot.equals("chestplate"))
								chestplate = itemStack;
							if (slot.equals("leggings"))
								leggings = itemStack;
							if (slot.equals("boots"))
								boots = itemStack;
							else
								try {
									contents[Integer.parseInt(slot)] = itemStack;
								} catch (final NumberFormatException e) {
									// Ignored
								}
						}
					}

					kitMap.put(name, new Kit(cooldown, name, helmet, chestplate, leggings, boots,
							contents, contents[0] != null ? contents[0].getType() : Material.STONE));
				}
			}
	}

	public Kit getKit(final String name) {
		return kitMap.getOrDefault(name, null);
	}

	public void createKit(final String name, final int cooldown, final PlayerInventory playerInventory) {
		final YamlConfiguration yamlConfiguration = configurationUtil
				.get("%datafolder%/kits/" + name + ".yml");
		final ItemStack helmet = playerInventory.getHelmet();
		final ItemStack chestplate = playerInventory.getChestplate();
		final ItemStack leggings = playerInventory.getLeggings();
		final ItemStack boots = playerInventory.getBoots();
		final ItemStack[] contents = playerInventory.getContents();

		kitMap.put(name, new Kit(cooldown, name, helmet, chestplate, leggings, boots, contents,
				contents[0] != null ? contents[0].getType() : Material.STONE));

		yamlConfiguration.set("cooldown", cooldown);
		yamlConfiguration.set("items.helmet", helmet);
		yamlConfiguration.set("items.chestplate", chestplate);
		yamlConfiguration.set("items.leggings", leggings);
		yamlConfiguration.set("items.boots", boots);

		for (int i = 0; i < contents.length; i++) {
			yamlConfiguration.set("items." + i, contents[i]);
		}

		configurationUtil.saveAsync(yamlConfiguration, "%datafolder%/kits/" + name + ".yml");
	}

	public void deleteKit(final String name) {
		configurationUtil.deleteAsync("%datafolder%/kits/" + name + ".yml");
		kitMap.remove(name);
	}

	public Collection<Kit> getKits() {
		return kitMap.values();
	}
}
