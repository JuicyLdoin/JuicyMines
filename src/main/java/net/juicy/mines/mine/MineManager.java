package net.juicy.mines.mine;

import lombok.Getter;
import net.juicy.api.utils.load.IUnLoadable;
import net.juicy.mines.JuicyMines;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MineManager implements IUnLoadable {

    private final JuicyMines plugin = JuicyMines.getPlugin();
    private final Map<String, Mine> mines = new HashMap<>();

    public void load() {

        File[] mineFiles = new File(plugin.getDataFolder(), "Mines").listFiles();

        if (mineFiles != null)
            for (File file : mineFiles) {

                Mine mine = new Mine(file);
                mine.generatePatterns();

                mines.put(mine.getName(), mine);

            }

        Bukkit.getConsoleSender().sendMessage("Loaded " + mines.size() + " mines");

    }

    public Mine getMine(String name) {

        return mines.get(name);

    }

    public boolean isCreated(String name) {

        return mines.containsKey(name);

    }

    public boolean isCreated(Mine mine) {

        return isCreated(mine.getName());

    }

    public Mine create(String name) {

        Mine mine = new Mine(name,
                new Location(Bukkit.getWorld("world"), 0, 0, 0), new Location(Bukkit.getWorld("world"), 0, 0, 0),
                60, new HashMap<>());

        mine.generatePatterns(mine.getMineOptions().getPatternOptions().getAmount());
        mine.save(getMineFile(mine));

        mines.put(name, mine);

        return mine;

    }

    public void rename(Mine mine, String newName) {

        mines.remove(mine.getName());

        getMineFile(mine).delete();

        mine.setName(newName);
        mine.save(getMineFile(mine));

        mines.put(newName, mine);

    }

    public void delete(String name) {

        Mine mine = getMine(name);

        if (mine != null) {

            // Если шахта с именем {name} удаляем её из списка и отключаем

            getMineFile(mine).delete();
            mine.delete();

            mines.remove(name);

        }
    }

    private File getMineFile(Mine mine) {

        return new File(new File(plugin.getDataFolder(), "Mines"), mine.getName().replace(" ", "") + "Mine.yml");

    }

    public void unload() {

        for (Mine mine : mines.values())
            mine.save(getMineFile(mine));

    }
}