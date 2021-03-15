package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitSetIconCMD {
    KitSetIconCMD(final CommandSender sender, final KitManager kitManager, final String[] args, final String label) {
        if (args.length > 1) {
            if (sender instanceof Player) {
                final Kit kit = kitManager.getKit(args[1].toUpperCase());

                if (kit != null) {
                    final PlayerInventory inventory = ((Player) sender).getInventory();
                    final ItemStack itemInHand = inventory.getItem(inventory.getHeldItemSlot());

                    if (itemInHand != null) {
                        kit.setIcon(itemInHand);

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aEstableciste el icono del kit &b" + kit.getName() + "&a correctamente!"));
                    } else {
                        sender.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', "&cNo tienes ningun item en la mano!"));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEl kit especificado no existe!"));
                }
            } else {
                sender.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "&cNo puedes usar ese comando desde la consola!"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/" + label + " seticon <kit>"));
        }
    }
}
