package net.juicy.mines.mine;

import lombok.Value;
import net.juicy.api.utils.load.IUnLoadable;
import net.juicy.api.utils.log.JuicyLogger;
import net.juicy.api.utils.log.JuicyLoggerElement;
import net.juicy.mines.JuicyMinesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Value
public class MineManager implements IUnLoadable {

    JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();
    Map<String, Mine> mines = new HashMap<>();

    JuicyLogger mineLogger = new JuicyLogger(plugin, "mines");
    JuicyLoggerElement mineElement = new JuicyLoggerElement("mines", mineLogger);

    public void load() {

        File[] mineFiles = new File(plugin.getDataFolder(), "Mines").listFiles();

        if (mineFiles != null)
            for (File file : mineFiles) {

                Mine mine = new Mine(file);
                mine.generatePatterns();

                mines.put(mine.getName(), mine);

                mineElement.addMessage("Mine loaded " + mine.getName());

            }

        mineElement.addMessage("Loaded " + mines.size() + " mines");

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

        mineElement.addMessage("Created mine " + mine.getName());

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

            // Если шахта с именем {name} найдена - удаляем её из списка и отключаем

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

        mineElement.save();
        mineLogger.save();

    }
}