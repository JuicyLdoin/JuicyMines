package net.juicy.mines;

import lombok.Getter;
import net.juicy.api.JuicyPlugin;
import net.juicy.api.utils.command.CommandManager;
import net.juicy.api.utils.load.Loader;
import net.juicy.mines.commands.*;
import net.juicy.mines.listener.ListenersManager;
import net.juicy.mines.mine.MineManager;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class JuicyMines extends JuicyPlugin {

    public static JuicyMines getPlugin() {

        return (JuicyMines) getJuicyPlugin(JuicyMines.class);

    }

    private String prefix;
    private boolean debug;

    private MineManager mineManager;

    public void onEnable() {

        prefix = replace(getConfig().getString("prefix"));
        debug = getConfig().getBoolean("debug");

        mineManager = new MineManager();

        Loader.loader.load(mineManager);
        Loader.loader.load(new ListenersManager());

        try {

            List<Class<?>> classes = new ArrayList<>();

            classes.add(MineCreateCommand.class);
            classes.add(MineDeleteCommand.class);
            classes.add(MineEditCommand.class);
            classes.add(MineFillCommand.class);
            classes.add(MineHelpCommand.class);

            Loader.loader.load(new CommandManager(this, getCommand("juicymines").getAliases(), classes));

        } catch (Exception exception) {

            exception.printStackTrace();

        }
    }

    public void onDisable() {

        Loader.loader.unload(mineManager);

    }
}