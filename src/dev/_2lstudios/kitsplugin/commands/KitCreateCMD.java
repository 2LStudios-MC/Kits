package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitCreateCMD {
	private int getInt(final String[] args, final int i) {
		if (args.length > i) {
			try {
				return Integer.parseInt(args[i]);
			} catch (final NumberFormatException e) {
				// Ignore
			}
		}

		return 0;
	}

	KitCreateCMD(final KitManager kitManager, final CommandSender sender, final String label, final String[] args) {
		if (args.length > 1) {
			if (sender instanceof Player) {
				final String name = args[1].toUpperCase();

				if (kitManager.getKit(name) == null) {
					try {
						final int price = getInt(args, 2);
						final int cooldown = getInt(args, 3);
						final Player player = (Player) sender;
						final PlayerInventory playerInventory = player.getInventory();
						final ItemStack itemInHand = playerInventory.getItem(playerInventory.getHeldItemSlot());
						final Kit kit = kitManager.createKit(name);

						kit.setContents(playerInventory);
						kit.setPrice(price);
						kit.setCooldown(cooldown);

						if (itemInHand != null) {
							kit.setIcon(itemInHand.getType());
						} else {
							kit.setIcon(Material.IRON_HELMET);
						}

						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&aCreaste el kit &b" + name + "&a correctamente!"));
					} catch (NumberFormatException ignored) {
						sender.sendMessage(ChatColor.RED + "Ingresaste un numero invalido!");
					}
				} else {
					sender.sendMessage(
							ChatColor.RED + "El kit " + ChatColor.AQUA + name + ChatColor.RED + " ya existe!");
				}
			} else
				sender.sendMessage(ChatColor.RED + "No puedes ejecutar este comando desde la consola!");
		} else
			sender.sendMessage(ChatColor.RED + "/" + label + " create <nombre> [precio] [cooldown]");
	}
}
