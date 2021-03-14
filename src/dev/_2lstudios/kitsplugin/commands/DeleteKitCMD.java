package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev._2lstudios.kitsplugin.kits.KitManager;

public class DeleteKitCMD implements CommandExecutor {
	private final KitManager kitManager;

	public DeleteKitCMD(final KitManager kitManager) {
		this.kitManager = kitManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0 && sender.hasPermission("kits.deletekit")) {
			final String name = args[0].toUpperCase();

			kitManager.deleteKit(name);
			sender.sendMessage(ChatColor.GREEN + "Creaste el kit correctamente!");
		} else
			sender.sendMessage(ChatColor.RED + "Permisos insuficientes!");

		return true;
	}
}
