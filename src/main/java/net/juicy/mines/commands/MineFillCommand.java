package net.juicy.mines.commands;

import net.juicy.api.utils.command.Command;
import net.juicy.api.utils.command.CommandArgument;
import net.juicy.api.utils.command.ICommand;
import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.mine.MineManager;
import org.bukkit.command.CommandSender;


@Command(aliases = { "juicymines", "jmines", "jm" }, permissions = "juicymines.*", onlyPlayers = true)
public class MineFillCommand implements ICommand {

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    @CommandArgument(aliases = "fill")
    public void onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 1)
            commandSender.sendMessage(plugin.replace("%prefix% &cНеверные аргументы, используйте: /jmines fill [name]"));
        else {

            MineManager mineManager = JuicyMinesPlugin.getPlugin().getMineManager();

            String name = args[0];

            if (!mineManager.isCreated(name))
                commandSender.sendMessage(plugin.replace("%prefix% &cШахты с именем " + name + " не существует!"));
            else {

                mineManager.getMine(name).fill();
                commandSender.sendMessage(plugin.replace("%prefix% &fВы успешно заполнили шахту с именем &e" + name));

            }
        }
    }
}