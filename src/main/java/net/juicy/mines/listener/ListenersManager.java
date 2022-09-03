package net.juicy.mines.listener;

import net.juicy.api.utils.load.ILoadable;
import net.juicy.api.utils.load.list.LoadableList;
import net.juicy.mines.listener.listeners.*;
import net.juicy.mines.listener.listeners.mine.edit.*;

import java.util.ArrayList;
import java.util.List;

public class ListenersManager extends LoadableList {

    public ListenersManager() {

        List<ILoadable> list = new ArrayList<>();

        list.add(MineBlocksListener.mineBlocks);
        list.add(MineEditListener.mineEditor);
        list.add(MineLocationListener.mineLocations);
        list.add(MineRenameListener.mineRename);

        list.add(new BlockBreakListener());

        addAllLoadable(list);

    }
}