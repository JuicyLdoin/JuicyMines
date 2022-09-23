package net.juicy.mines.commands;

import net.juicy.api.utils.command.Command;
import net.juicy.api.utils.command.ICommand;
import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.mine.MineManager;
import org.bukkit.command.CommandSender;

public class MineDeleteCommand implements ICommand {

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    @Command(aliases = "delete", permissions = "juicymines.*", onlyPlayers = true)
    public void onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 1) {

            commandSender.sendMessage(plugin.replace("%prefix% &cНеверные аргументы, используйте: /jmines delete [name]"));
            return;

        }

        MineManager mineManager = JuicyMinesPlugin.getPlugin().getMineManager();

        String name = args[0];

        if (!mineManager.isCreated(name))
            commandSender.sendMessage(plugin.replace("%prefix% &cШахты с именем " + name + " не существует!"));
        else {

            mineManager.delete(name);
            commandSender.sendMessage(plugin.replace("%prefix% &fВы успешно удалили шахту с именем &e" + name));

        }
    }
}