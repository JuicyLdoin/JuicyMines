package net.juicy.mines.commands;

import net.juicy.api.utils.command.Command;
import net.juicy.api.utils.command.CommandArgument;
import net.juicy.api.utils.command.ICommand;
import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.mine.MineManager;
import org.bukkit.command.CommandSender;


@Command(aliases = { "juicymines", "jmines", "jm" }, permissions = "juicymines.*")
public class MineCreateCommand implements ICommand {

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    @CommandArgument(aliases = "create")
    public void onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 1)
            commandSender.sendMessage(plugin.replace("%prefix% &cНеверные аргументы, используйте: /jmines create [name]"));
        else {

            MineManager mineManager = JuicyMinesPlugin.getPlugin().getMineManager();

            String name = args[0];

            if (mineManager.isCreated(name))
                commandSender.sendMessage(plugin.replace("%prefix% &cШахта с именем " + name + " уже создана, используйте другое имя!"));
            else {

                JuicyMinesPlugin.getPlugin().getMineManager().create(name);
                commandSender.sendMessage(plugin.replace("%prefix% &fШахта с именем &e" + name + " &fуспешно создана!"));

            }
        }
    }
}