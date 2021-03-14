package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitSetPriceCMD {
    KitSetPriceCMD(final CommandSender sender, final KitManager kitManager, final String[] args, final String label) {
        if (sender.hasPermission("kits.create")) {
        if (args.length > 2) {
            if (sender instanceof Player) {
                final Kit kit = kitManager.getKit(args[1]);

                if (kit != null) {
                    try {
                        final int price = Integer.parseInt(args[2]);

                        kit.setPrice(price);

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aEstableciste el precio del kit &b" + kit.getName() + "&a a &6$" + price + "&a!"));
                    } catch (final NumberFormatException e) {
                        sender.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', "&cIngresaste un numero invalido!"));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEl kit especificado no existe!"));
                }
            } else {
                sender.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "&cNo puedes usar ese comando desde la consola!"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/" + label + " setprice <kit> <precio>"));
        }
    } else {
        sender.sendMessage(ChatColor.RED + "Permisos insuficientes!");
    }
}
}