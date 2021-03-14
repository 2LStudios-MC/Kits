package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitPreviewCMD implements CommandExecutor {
	private final KitManager kitManager;

	public KitPreviewCMD(final KitManager kitManager) {
		this.kitManager = kitManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final int length = args.length;

		if (length > 0) {
			if (sender instanceof Player) {
				final String kitName = args[0].toUpperCase();
				final Kit kit = kitManager.getKit(kitName);

				if (kit != null) {
					final Player player = (Player) sender;

					kitManager.openPreviewInventory(player, kit);
				} else
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEse kit no existe!"));
			} else
				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&cNo puedes revisar kits desde la consola!"));
		} else
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/kitpreview <kit>!"));

		return true;
	}
}
