package net.juicy.mines.listener.listeners.mine.edit;

import net.juicy.mines.mine.Mine;
import net.juicy.mines.utils.load.ILoadableListener;
import org.bukkit.entity.Player;

public interface IMineEditor extends ILoadableListener {

    void createForPlayer(Player player, Mine mine);

}