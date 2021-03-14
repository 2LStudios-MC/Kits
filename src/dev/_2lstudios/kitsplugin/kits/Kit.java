package dev._2lstudios.kitsplugin.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kit {
	private final String name;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ItemStack[] contents;
	private Material icon;
	private int cooldown;
	private int price;

	public Kit(final String name) {
		this.name = name;
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
		return contents;
	}

	public void setContents(final PlayerInventory playerInventory) {
		this.contents = playerInventory.getContents();
		this.helmet = playerInventory.getHelmet();
		this.chestplate = playerInventory.getChestplate();
		this.leggings = playerInventory.getLeggings();
		this.boots = playerInventory.getBoots();
	}

	public Material getIcon() {
		return icon;
	}

	public void setIcon(final Material icon) {
		this.icon = icon;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(final int cooldown) {
		this.cooldown = cooldown;
	}

	public int getPrice() {
		return price;
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public ItemStack getChestplate() {
		return chestplate;
	}

	public ItemStack getLeggings() {
		return leggings;
	}

	public ItemStack getBoots() {
		return boots;
	}

	public void setPrice(final int price) {
		this.price = price;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}
}
