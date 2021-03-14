package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitDeleteCMD {
	KitDeleteCMD(final KitManager kitManager, final CommandSender sender, final String[] args) {
		final String name = args[0].toUpperCase();

		kitManager.deleteKit(name);
		sender.sendMessage(ChatColor.GREEN + "Eliminaste el kit correctamente!");
	}
}
