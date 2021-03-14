package dev._2lstudios.kitsplugin.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kit {
	private final int cooldown;
	private final String name;
	private final ItemStack helmet;
	private final ItemStack chestplate;
	private final ItemStack leggings;
	private final ItemStack boots;
	private final ItemStack[] contents;
	private final Material icon;

	public Kit(final int cooldown, final String name, final ItemStack helmet, final ItemStack chestplate,
			final ItemStack leggings, final ItemStack boots, final ItemStack[] contents, final Material icon) {
		this.cooldown = cooldown;
		this.name = name;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		this.contents = contents;
		this.icon = icon;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void give(final Player player) {
		final PlayerInventory playerInventory = player.getInventory();

		if (helmet != null) {
			if (playerInventory.getHelmet() == null) {
				playerInventory.setHelmet(helmet.clone());
			} else {
				playerInventory.addItem(helmet.clone());
			}
		}

		if (chestplate != null) {
			if (playerInventory.getChestplate() == null) {
				playerInventory.setChestplate(chestplate.clone());
			} else {
				playerInventory.addItem(chestplate.clone());
			}
		}

		if (leggings != null) {
			if (playerInventory.getLeggings() == null) {
				playerInventory.setLeggings(leggings.clone());
			} else {
				playerInventory.addItem(leggings.clone());
			}
		}

		if (boots != null) {
			if (playerInventory.getBoots() == null) {
				playerInventory.setBoots(boots.clone());
			} else {
				playerInventory.addItem(boots.clone());
			}
		}

		for (final ItemStack itemStack : contents) {
			if (itemStack != null) {
				if (playerInventory.firstEmpty() == -1) {
					player.getWorld().dropItem(player.getLocation(), itemStack.clone());
				} else {
					playerInventory.addItem(itemStack.clone());
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public ItemStack[] getContents() {
		return this.contents.clone();
	}

    public Material getIcon() {
        return icon;
    }
}
