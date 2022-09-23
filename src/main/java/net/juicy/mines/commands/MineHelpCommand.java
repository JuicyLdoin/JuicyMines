package net.juicy.mines.commands;

import net.juicy.api.utils.command.Command;
import net.juicy.api.utils.command.ICommand;
import net.juicy.mines.JuicyMinesPlugin;
import org.bukkit.command.CommandSender;

public class MineHelpCommand implements ICommand {

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    @Command(aliases = "help", permissions = "juicymines.*")
    public void onCommand(CommandSender commandSender, String[] args) {

        commandSender.sendMessage(plugin.replace("%prefix% &e&lJuicyMines"));
        commandSender.sendMessage(plugin.replace("%prefix% &e/jmines create [name] &f- создать шахту с именем &e[name]"));
        commandSender.sendMessage(plugin.replace("%prefix% &e/jmines delete [name] &f- удалить шахту с именем &e[name]"));
        commandSender.sendMessage(plugin.replace("%prefix% &e/jmines edit [name] &f- редактировать шахту с именем &e[name]"));
        commandSender.sendMessage(plugin.replace("%prefix% &e/jmines fill [name] &f- заполнить шахту с именем &e[name]"));

    }
}