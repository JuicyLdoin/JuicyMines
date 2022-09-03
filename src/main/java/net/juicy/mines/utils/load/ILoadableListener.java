package net.juicy.mines.utils.load;

import net.juicy.api.utils.load.ILoadable;
import net.juicy.mines.JuicyMines;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public interface ILoadableListener extends Listener, ILoadable {

    default void load() {

        Bukkit.getPluginManager().registerEvents(this, JuicyMines.getPlugin());

    }
}