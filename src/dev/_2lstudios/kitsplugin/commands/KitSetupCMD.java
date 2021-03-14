package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev._2lstudios.kitsplugin.kits.KitManager;

public class KitSetupCMD implements CommandExecutor {
    private final KitManager kitManager;

    public KitSetupCMD(final KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (sender.hasPermission("kits.setup")) {
            if (args.length < 1) {
                new KitHelpCMD(sender, label);
            } else if (args[0].equals("create")) {
                new KitCreateCMD(kitManager, sender, args);
            } else if (args[0].equals("delete")) {
                new KitDeleteCMD(kitManager, sender, args);
            } else if (args[0].equals("seticon")) {
                new KitSetIconCMD(sender, kitManager, args, label);
            } else if (args[0].equals("setcontent")) {
                new KitSetContentCMD(sender, kitManager, args, label);
            } else if (args[0].equals("setcooldown")) {
                new KitSetCooldownCMD(sender, kitManager, args, label);
            } else if (args[0].equals("setprice")) {
                new KitSetPriceCMD(sender, kitManager, args, label);
            } else {
                new KitHelpCMD(sender, label);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Permisos insuficientes!");
        }

        return true;
    }
}