package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitSetCooldownCMD {
    KitSetCooldownCMD(final CommandSender sender, final KitManager kitManager, final String[] args, final String label) {
        if (args.length > 2) {
            if (sender instanceof Player) {
                final Kit kit = kitManager.getKit(args[1]);

                if (kit != null) {
                    try {
                        final int cooldown = Integer.parseInt(args[2]);

                        kit.setCooldown(cooldown);

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aEstableciste el cooldown del kit &b" + kit.getName() + "&a a &6" + cooldown + "s&a!"));
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
    }
}