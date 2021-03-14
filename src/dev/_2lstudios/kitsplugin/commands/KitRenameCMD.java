package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitRenameCMD {
	KitRenameCMD(final KitManager kitManager, final CommandSender sender, final String label, final String[] args) {
		if (args.length > 2) {
			if (sender instanceof Player) {
				final String oldName = args[1].toUpperCase();
				final String newName = args[2].toUpperCase();

				if (kitManager.rename(oldName, newName)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&aRenombraste el kit &b" + oldName + "&a a &b" + newName + "&a!"));
				} else {
					sender.sendMessage(ChatColor.RED + "El kit " + ChatColor.AQUA + oldName + ChatColor.RED
							+ " no pudo ser renombrado!");
				}
			} else
				sender.sendMessage(ChatColor.RED + "No puedes ejecutar este comando desde la consola!");
		} else
			sender.sendMessage(ChatColor.RED + "/" + label + " rename <nombre viejo> <nombre nuevo>");
	}
}
