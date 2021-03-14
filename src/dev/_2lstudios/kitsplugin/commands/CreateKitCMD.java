package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import dev._2lstudios.kitsplugin.kits.KitManager;

public class CreateKitCMD implements CommandExecutor {
	private final KitManager kitManager;

	public CreateKitCMD(final KitManager kitManager) {
		this.kitManager = kitManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 1) {
			if (sender.hasPermission("kits.create")) {
				if (sender instanceof Player) {
					final String name = args[0].toUpperCase();

					if (kitManager.getKit(name) == null) {
						try {
							final int cooldown = Integer.parseInt(args[1]);
							final Player player = (Player) sender;
							final PlayerInventory playerInventory = player.getInventory();

							kitManager.createKit(name, cooldown, playerInventory);

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

		return true;
	}
}
