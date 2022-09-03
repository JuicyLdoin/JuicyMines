package net.juicy.mines.utils;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class PluginsManager {

    public static PluginsManager pluginsManager = new PluginsManager();

    boolean WorldEdit;

    private PluginsManager() {

        WorldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null;

    }
}