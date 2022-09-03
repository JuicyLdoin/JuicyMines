package net.juicy.mines.commands;

import net.juicy.api.utils.command.Command;
import net.juicy.api.utils.command.ICommand;
import net.juicy.mines.JuicyMines;
import net.juicy.mines.listener.listeners.mine.edit.MineEditListener;
import net.juicy.mines.mine.MineManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MineFillCommand implements ICommand {

    private final JuicyMines plugin = JuicyMines.getPlugin();

    @Command(aliases = "fill", permissions = "juicymines.*", onlyPlayers = true)
    public void onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 1) {

            commandSender.sendMessage(plugin.replace("%prefix% &cНеверные аргументы, используйте: /jmines fill [name]"));
            return;

        }

        MineManager mineManager = JuicyMines.getPlugin().getMineManager();

        String name = args[0];

        if (!mineManager.isCreated(name))
            commandSender.sendMessage(plugin.replace("%prefix% &cШахты с именем " + name + " не существует!"));
        else {

            mineManager.getMine(name).fill();
            commandSender.sendMessage(plugin.replace("%prefix% &fВы успешно заполнили шахту с именем &e" + name));

        }
    }
}