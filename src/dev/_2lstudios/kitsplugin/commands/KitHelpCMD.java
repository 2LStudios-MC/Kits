package dev._2lstudios.kitsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class KitHelpCMD {
    private void addHelpLine(final StringBuilder builder, final String label, final String command, final String description) {
        builder.append("\n&e/" + label + command + "&7 - &b" + description + "!");
    }

    KitHelpCMD(final CommandSender sender, String label) {
        final StringBuilder builder = new StringBuilder("&aComandos de Kits:");

        label = label + " ";

        addHelpLine(builder, null, "kit <nombre>", "Obten un kit!");
        addHelpLine(builder, null, "kitpreview <nombre>", "Previsualiza un kit!");
        addHelpLine(builder, label, "create <nombre> [precio] [cooldown]", "Crea un nuevo kit!");
        addHelpLine(builder, label, "delete <nombre>", "Elimina un kit!");
        addHelpLine(builder, label, "setcontent <nombre>", "Cambia el contenido de un kit!");
        addHelpLine(builder, label, "seticon <nombre>", "Cambia el icono de un kit!");
        addHelpLine(builder, label, "setprice <nombre> <precio>", "Cambia el precio de un kit!");
        addHelpLine(builder, label, "setcooldown <nombre> <cooldown>", "Cambia el cooldown de un kit!");

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', builder.toString()));
    }
}
