package dev._2lstudios.kitsplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;
import dev._2lstudios.kitsplugin.kits.KitPlayer;
import dev._2lstudios.kitsplugin.kits.KitPlayerManager;

public class KitCMD implements CommandExecutor {
	private final KitManager kitManager;
	private final KitPlayerManager kitPlayerManager;

	public KitCMD(final KitManager kitManager, final KitPlayerManager kitPlayerManager) {
		this.kitManager = kitManager;
		this.kitPlayerManager = kitPlayerManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final int length = args.length;

		if (length > 0) {
			if (length < 2) {
				if (sender instanceof Player) {
					final KitPlayer kitPlayer = kitPlayerManager.getPlayer((Player) sender);

					kitManager.openInventory((Player) sender, kitPlayer);

					if (sender.hasPermission("kits.kit")) {
						final String kitName = args[0].toUpperCase();
						final Kit kit = kitManager.getKit(kitName);

						if (kit != null) {
							if (sender.hasPermission("kits.kit." + kitName.toLowerCase())) {
								final Player player = (Player) sender;

								if (!kitPlayer.hasCooldown(kit.getName(), kit.getCooldown())) {
									kit.give(player);

									if (kit.getCooldown() != 0)
										kitPlayer.setCooldown(kit.getName());

									sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&aRecogiste el kit &b" + kitName + "&a!"));
								} else {
									final int kitCooldown = kit.getCooldown();
									final long lastTakeTime = kitPlayer.getCooldown(kit.getName());
									final int milliseconds = (int) ((lastTakeTime + kitCooldown)
											- System.currentTimeMillis()), seconds = (int) (milliseconds / 1000) % 60,
											minutes = (int) ((milliseconds / (1000 * 60)) % 60),
											hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

									sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&cDebes esperar &e" + hours + "H " + minutes + "M " + seconds + "S"
													+ "&c para usar este kit!"));
								}
							} else
								sender.sendMessage(
										ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para usar ese kit!"));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEse kit no existe!"));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPermisos insuficientes!"));
					}
				} else
					sender.sendMessage(
							ChatColor.translateAlternateColorCodes('&', "&cNo puedes recoger kits desde la consola!"));
			} else {
				if (sender.hasPermission("kits.others")) {
					final String kitName = args[0].toUpperCase();
					final Player player = Bukkit.getPlayer(args[1]);
					final Kit kit = kitManager.getKit(kitName);

					if (kit != null) {
						if (player != null) {
							kit.give(player);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aKit &b" + kitName
									+ "&a entregado al jugador &7" + player.getDisplayName() + "&a!"));
						} else
							sender.sendMessage(
									ChatColor.translateAlternateColorCodes('&', "&cEl jugador no esta en linea!"));
					} else
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEse kit no existe!"));
				} else
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPermisos insuficientes!"));
			}
		} else {
			if (sender instanceof Player) {
				final Iterable<Kit> kits = kitManager.getKits();
				final StringBuilder coloredKits = new StringBuilder("&bKits disponibles: &a");
				final KitPlayer kitPlayer = kitPlayerManager.getPlayer((Player) sender);
				boolean iterated = false;

				for (final Kit kit : kits) {
					final String kitName = kit.getName();

					if (iterated) {
						coloredKits.append("&7, ");
					} else {
						iterated = true;
					}

					if (sender.hasPermission("kits." + kitName) && !kitPlayer.hasCooldown(kitName, kit.getCooldown())) {
						coloredKits.append("&a");
					} else {
						coloredKits.append("&c");
					}

					coloredKits.append(kitName);
				}

				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', coloredKits.toString()));
			} else {
				sender.sendMessage(ChatColor.RED + "/kits <kit> [jugador]");
			}
		}

		return true;
	}
}
