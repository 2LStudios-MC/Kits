package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitDeleteCMD {
	KitDeleteCMD(final KitManager kitManager, final CommandSender sender, final String label, final String[] args) {
		if (args.length > 1) {
		final String name = args[1].toUpperCase();

		kitManager.deleteKit(name);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEliminaste el kit &b" + name + "&a correctamente!"));
		} else {
		sender.sendMessage(ChatColor.RED + "/" + label + " delete <nombre>");
		}
	}
}
