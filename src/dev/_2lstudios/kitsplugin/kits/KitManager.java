package dev._2lstudios.kitsplugin.kits;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.InventoryPlayer;
import dev._2lstudios.inventoryapi.InventoryUtil;
import dev._2lstudios.inventoryapi.InventoryWrapper;
import dev._2lstudios.kitsplugin.utils.ConfigUtil;

public class KitManager {
	private final Plugin plugin;
	private final ConfigUtil configurationUtil;
	private final Map<String, Kit> kitMap = new HashMap<>();

	public KitManager(final Plugin plugin, final ConfigUtil configurationUtil) {
		this.plugin = plugin;
		this.configurationUtil = configurationUtil;

		configurationUtil.create("%datafolder%/kits.yml");
		load();
	}

	public void openPreviewInventory(final Player player, final Kit kit) {
		final Inventory inventory = plugin.getServer().createInventory(player, 54, "Preview de " + kit.getName());
		final InventoryWrapper inventoryWrapper = new InventoryWrapper(1, "kitpreview", inventory);
		final InventoryPlayer inventoryPlayer = InventoryAPI.getInstance().getInventoryPlayerManager().get(player);

		inventory.setContents(kit.getContents());
		inventoryPlayer.openInventory(inventoryWrapper);
	}

	private void generateDescription(final KitPlayer kitPlayer, final Player player, final Kit kit,
			final String kitName, final ItemStack itemStack) {
		final ItemMeta itemMeta = itemStack.getItemMeta();
		final List<String> lore = new ArrayList<>();
		final long cooldown = kitPlayer.getCooldown(kitName);
		final ChatColor color;

		lore.add("");

		if (kit.getPrice() > 0) {
			lore.add(ChatColor.GRAY + "Precio: " + ChatColor.GOLD + kit.getPrice());
		}

		if (!player.hasPermission("kit.kits." + kit.getName().toLowerCase())) {
			color = ChatColor.RED;
			lore.add(ChatColor.GRAY + "Estado: " + ChatColor.RED + "BLOQUEADO");
			lore.add("");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&cSin permisos!"));
		} else if (cooldown > 0) {
			final int kitCooldown = kit.getCooldown();
			final long lastTakeTime = kitPlayer.getCooldown(kit.getName());
			final int milliseconds = (int) ((lastTakeTime + kitCooldown) - System.currentTimeMillis()),
					seconds = (int) (milliseconds / 1000) % 60, minutes = (int) ((milliseconds / (1000 * 60)) % 60),
					hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

			color = ChatColor.RED;
			lore.add(ChatColor.GRAY + "Estado: " + ChatColor.RED + "BLOQUEADO");
			lore.add("");
			lore.add(ChatColor.translateAlternateColorCodes('&',
					"&cEspera &e" + hours + "H " + minutes + "M " + seconds + "S"));
		} else {
			color = ChatColor.GREEN;
			lore.add(ChatColor.GRAY + "Estado: " + ChatColor.GREEN + "DESBLOQUEADO");
			lore.add("");
			lore.add(ChatColor.YELLOW + "Click para elegir!");
		}

		itemMeta.setLore(lore);
		itemMeta.setDisplayName(color + kitName);
		itemStack.setItemMeta(itemMeta);
	}

	public void openInventory(final Player player, final KitPlayer kitPlayer, int page) {
		final List<Kit> kits = new ArrayList<>(getKits());

		page = Math.max(1, page);
		kits.sort((o1, o2) -> Integer.compare(o1.getPrice(), o2.getPrice()));

		final Collection<ItemStack> items = new ArrayList<>();
		final int lastPage = 1 + ((kits.size() - 1) / 28);
		int skip = Math.min((page - 1) * 28, kits.size() - 1);

		for (final Kit kit : kits) {
			if (skip-- > 0) {
				continue;
			}

			final ItemStack itemStack = new ItemStack(kit.getIcon());

			generateDescription(kitPlayer, player, kit, kit.getName(), itemStack);

			items.add(itemStack);
		}

		final String title = "Kits";
		final InventoryAPI inventoryAPI = InventoryAPI.getInstance();
		final InventoryPlayer inventoryPlayer = inventoryAPI.getInventoryPlayerManager().get(player);
		final InventoryUtil inventoryUtil = inventoryAPI.getInventoryUtil();
		final InventoryWrapper inventoryWrapper = inventoryUtil.createDisplayInventory(title, player, page, title,
				items);

		if (page != 1) {
			inventoryWrapper.setItem(45, inventoryUtil.getBackItem(page - 1));
		}

		inventoryWrapper.setItem(49, inventoryUtil.getCloseItem());

		if (page != lastPage) {
			inventoryWrapper.setItem(53, inventoryUtil.getNextItem(page + 1));
		}

		inventoryPlayer.openInventory(inventoryWrapper);
	}

	public void openInventory(final Player player, final KitPlayer kitPlayer) {
		openInventory(player, kitPlayer, 1);
	}

	private String getDataFolder(final String name) {
		return "%datafolder%/kits/" + name + ".yml";
	}

	public void load(final String name) {
		final Configuration configuration = configurationUtil.get(getDataFolder(name));
		final Kit kit = createKit(name);
		final int cooldown = configuration.getInt("cooldown", 0);
		final int price = configuration.getInt("price", 0);
		final ItemStack icon = configuration.getItemStack("icon", new ItemStack(Material.STONE));
		final ItemStack helmet = configuration.getItemStack("helmet");
		final ItemStack chestplate = configuration.getItemStack("chestplate");
		final ItemStack leggings = configuration.getItemStack("leggings");
		final ItemStack boots = configuration.getItemStack("boots");
		final ItemStack[] contents = new ItemStack[36];
		final ConfigurationSection itemsSection = configuration.getConfigurationSection("items");

		if (itemsSection != null) {
			for (final String slot : itemsSection.getKeys(false)) {
				final ItemStack itemStack = configuration.getItemStack("items." + slot);

				if (itemStack != null) {
					try {
						contents[Integer.parseInt(slot)] = itemStack;
					} catch (final NumberFormatException e) {
						// Ignored
					}
				}
			}
		}

		kit.setHelmet(helmet);
		kit.setChestplate(chestplate);
		kit.setLeggings(leggings);
		kit.setBoots(boots);
		kit.setContents(contents);
		kit.setPrice(price);
		kit.setCooldown(cooldown);

		kit.setIcon(icon);
	}

	public void load() {
		final File kitFolder = new File(plugin.getDataFolder() + "/kits/");
		final String[] kitFiles = kitFolder.list();

		kitFolder.mkdirs();
		kitMap.clear();

		if (kitFiles != null) {
			for (final String kitFileName : kitFiles) {
				load(kitFileName.replace(".yml", ""));
			}
		}
	}

	public Kit getKit(final String name) {
		return kitMap.getOrDefault(name, null);
	}

	public void save(final Kit kit) {
		final String name = kit.getName();
		final ItemStack[] contents = kit.getContents();
		final YamlConfiguration yamlConfiguration = new YamlConfiguration();

		for (int i = 0; i < contents.length; i++) {
			yamlConfiguration.set("items." + i, contents[i]);
		}

		yamlConfiguration.set("cooldown", kit.getCooldown());
		yamlConfiguration.set("price", kit.getPrice());
		yamlConfiguration.set("icon", kit.getIcon());
		yamlConfiguration.set("helmet", kit.getHelmet());
		yamlConfiguration.set("chestplate", kit.getChestplate());
		yamlConfiguration.set("leggings", kit.getLeggings());
		yamlConfiguration.set("boots", kit.getBoots());

		configurationUtil.save(yamlConfiguration, getDataFolder(name));
	}

	public Kit createKit(final String name) {
		final Kit kit = new Kit(name);

		kitMap.put(name, kit);

		return kit;
	}

	public void deleteKit(final String name) {
		configurationUtil.deleteAsync(getDataFolder(name));
		kitMap.remove(name);
	}

	public boolean rename(final String oldName, final String newName) {
		final Kit oldKit = getKit(oldName);

		if (oldKit != null) {
			deleteKit(oldName);

			final Kit newKit = createKit(newName);

			newKit.setBoots(oldKit.getBoots());
			newKit.setLeggings(oldKit.getLeggings());
			newKit.setChestplate(oldKit.getChestplate());
			newKit.setHelmet(oldKit.getHelmet());
			newKit.setContents(oldKit.getContents());
			newKit.setCooldown(oldKit.getCooldown());
			newKit.setPrice(oldKit.getPrice());
			newKit.setIcon(oldKit.getIcon());

			return true;
		}

		return false;
	}

	public Collection<Kit> getKits() {
		return kitMap.values();
	}

	public Collection<String> getKitNames() {
		return kitMap.keySet();
	}

	public void save() {
		for (final Kit kit : getKits()) {
			save(kit);
		}
	}
}
