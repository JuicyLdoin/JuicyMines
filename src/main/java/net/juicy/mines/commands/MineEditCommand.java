package net.juicy.mines.commands;

import net.juicy.api.utils.command.Command;
import net.juicy.api.utils.command.CommandArgument;
import net.juicy.api.utils.command.ICommand;
import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.listener.listeners.mine.edit.MineEditListener;
import net.juicy.mines.mine.MineManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(aliases = { "juicymines", "jmines", "jm" }, permissions = "juicymines.*", onlyPlayers = true)
public class MineEditCommand implements ICommand {

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    @CommandArgument(aliases = "edit")
    public void onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 1)
            commandSender.sendMessage(plugin.replace("%prefix% &cНеверные аргументы, используйте: /jmines edit [name]"));
        else {

            MineManager mineManager = JuicyMinesPlugin.getPlugin().getMineManager();

            String name = args[0];

            if (!mineManager.isCreated(name))
                commandSender.sendMessage(plugin.replace("%prefix% &cШахты с именем " + name + " не существует!"));
            else
                MineEditListener.mineEditor.createForPlayer((Player) commandSender, mineManager.getMine(name));

        }
    }
}