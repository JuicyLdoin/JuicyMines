package net.juicy.mines.listener.listeners;

import net.juicy.api.utils.load.Loader;
import net.juicy.mines.JuicyMines;
import net.juicy.mines.mine.Mine;
import net.juicy.mines.utils.load.ILoadableListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements ILoadableListener {

    public BlockBreakListener() {

        Loader.loader.load(this);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        for (Mine mine : JuicyMines.getPlugin().getMineManager().getMines().values())
            if (mine.getMineOptions().isAllowToBreakBlock(event.getBlock()))
                mine.getMineOptions().addMinedBlock();

    }
}