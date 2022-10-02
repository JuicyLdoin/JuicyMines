package net.juicy.mines;

import lombok.Getter;
import net.juicy.api.JuicyAPIPlugin;
import net.juicy.api.JuicyPlugin;
import net.juicy.api.utils.command.CommandManager;
import net.juicy.api.utils.command.ICommand;
import net.juicy.api.utils.load.Loader;
import net.juicy.mines.commands.*;
import net.juicy.mines.listener.ListenersManager;
import net.juicy.mines.mine.MineManager;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class JuicyMinesPlugin extends JuicyPlugin {

    public static JuicyMinesPlugin getPlugin() {

        return getPlugin(JuicyMinesPlugin.class);

    }

    private MineManager mineManager;
    private Loader loader;

    public void onEnable() {

        mineManager = new MineManager();

        loader = JuicyAPIPlugin.getPlugin().getLoader();

        loader.load(mineManager);
        loader.load(new ListenersManager());

        try {

            List<Class<? extends ICommand>> classes = new ArrayList<>();

            classes.add(MineCreateCommand.class);
            classes.add(MineDeleteCommand.class);
            classes.add(MineEditCommand.class);
            classes.add(MineFillCommand.class);
            classes.add(MineHelpCommand.class);

            loader.load(new CommandManager(this, "juicymines", getCommand("juicymines").getAliases(), classes));

        } catch (Exception exception) {

            exception.printStackTrace();

        }
    }

    public void onDisable() {

        loader.unload(mineManager);

    }

    public String getPrefix() {

        return "§e§lJuicyMines §f>> ";

    }

    public void reload() {

        mineManager = new MineManager();
        loader.load(mineManager);

    }
}