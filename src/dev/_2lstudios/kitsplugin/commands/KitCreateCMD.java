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
	KitCreateCMD(final KitManager kitManager, final CommandSender sender, final String[] args) {
		if (args.length > 2) {
			if (sender.hasPermission("kits.create")) {
				if (sender instanceof Player) {
					final String name = args[0].toUpperCase();

					if (kitManager.getKit(name) == null) {
						try {
							final int price = Integer.parseInt(args[1]);
							final int cooldown = Integer.parseInt(args[2]);
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

							sender.sendMessage(ChatColor.GREEN + "Creaste el kit correctamente!");
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
				sender.sendMessage(ChatColor.RED + "Permisos insuficientes!");
		} else
			sender.sendMessage(ChatColor.RED + "/createkit <nombre> <cooldown>");
	}
}
