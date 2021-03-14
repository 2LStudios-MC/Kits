package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitSetContentCMD {
    KitSetContentCMD(final CommandSender sender, final KitManager kitManager, final String[] args, final String label) {
        if (args.length > 1) {
            if (sender instanceof Player) {
                final Kit kit = kitManager.getKit(args[1].toUpperCase());

                if (kit != null) {
                    final PlayerInventory inventory = ((Player) sender).getInventory();

                    kit.setContents(inventory.getContents());

                    sender.sendMessage(
                            ChatColor.translateAlternateColorCodes('&', "&aEstableciste el contenido correctamente!"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEl kit especificado no existe!"));
                }
            } else {
                sender.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "&cNo puedes usar ese comando desde la consola!"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/" + label + " setcontent <kit>"));
        }
    }
}
