package net.juicy.mines.commands;

import net.juicy.api.utils.command.Command;
import net.juicy.api.utils.command.ICommand;
import net.juicy.mines.JuicyMines;
import net.juicy.mines.mine.MineManager;
import org.bukkit.command.CommandSender;

public class MineCreateCommand implements ICommand {

    private final JuicyMines plugin = JuicyMines.getPlugin();

    @Command(aliases = "create", permissions = "juicymines.*")
    public void onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 1) {

            commandSender.sendMessage(plugin.replace("%prefix% &cНеверные аргументы, используйте: /jmines create [name]"));
            return;

        }

        MineManager mineManager = JuicyMines.getPlugin().getMineManager();

        String name = args[0];

        if (mineManager.isCreated(name))
            commandSender.sendMessage(plugin.replace("%prefix% &cШахта с именем " + name + " уже создана, используйте другое имя!"));
        else {

            JuicyMines.getPlugin().getMineManager().create(name);
            commandSender.sendMessage(plugin.replace("%prefix% &fШахта с именем &e" + name + " &fуспешно создана!"));

        }
    }
}