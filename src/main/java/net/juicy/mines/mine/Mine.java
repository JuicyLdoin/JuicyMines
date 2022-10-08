package net.juicy.mines.mine;

import lombok.Getter;
import lombok.Setter;
import net.juicy.api.utils.log.JuicyLoggerElement;
import net.juicy.api.utils.util.LocationUtil;
import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.mine.options.MineOptions;
import net.juicy.mines.mine.options.PatternOptions;
import net.juicy.mines.mine.pattern.*;
import net.juicy.mines.utils.block.ChanceCalculator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Getter
@Setter
public class Mine {

    final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    String name;
    JuicyLoggerElement logger;

    MineOptions mineOptions;

    public Mine(String name, Location minLocation, Location maxLocation, int resetTime, Map<Material, Float> blocks) {

        this.name = name;
        logger = new JuicyLoggerElement(name, JuicyMinesPlugin.getPlugin().getMineManager().getMineLogger());

        mineOptions = new MineOptions(minLocation, maxLocation, resetTime, resetTime, new MinePatternCache(this, new LinkedList<>()), blocks, runTask(), new PatternOptions(3, false), 0, 0, 50);

    }

    public Mine(File file) {

        ConfigurationSection mineSection = YamlConfiguration.loadConfiguration(file).getConfigurationSection("Mine");

        name = mineSection.getString("name");
        logger = new JuicyLoggerElement(name, JuicyMinesPlugin.getPlugin().getMineManager().getMineLogger());

        String stringBlocks = mineSection.getString("blocks");
        Map<Material, Float> blocks = new HashMap<>();

        if (!stringBlocks.isEmpty())
            for (String block : stringBlocks.split("___"))
                if (!block.equals(""))
                    blocks.put(Material.getMaterial(block.split("-")[0]), Float.parseFloat(block.split("-")[1]));

        mineOptions = new MineOptions(LocationUtil.getLocation(mineSection.getString("minLocation")), LocationUtil.getLocation(mineSection.getString("maxLocation")),
                mineSection.getInt("resetTime"), mineSection.getInt("toReset"), new MinePatternCache(this, new LinkedList<>()), blocks, runTask(),
                new PatternOptions(mineSection.getInt("patternAmount"), mineSection.getBoolean("patternCycle")),
                mineSection.getInt("totalBlocks"), mineSection.getInt("minedBlocks"), mineSection.getInt("resetOn"));

    }

    public void fill() {

        // Заполняет шахту следующим паттерном

        fill(mineOptions.getPatternCache().getNext());

    }

    public void fill(MinePattern minePattern) {

        // Заполняет шахту паттерном {minePattern}

        mineOptions.setToReset(mineOptions.getResetTime());
        mineOptions.setMinedBlocks(0);

        minePattern.place();

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getWorld().equals(mineOptions.getMinLocation().getWorld()))
                .filter(player -> mineOptions.isInside(player.getLocation()))
                .forEach(player -> {

                    Location location = player.getLocation().clone();
                    location.setY(mineOptions.getMaxLocation().getBlockY() + 1);

                    player.teleport(location);

                });

        logger.addMessage("Mine " + name + " filled and generate new pattern");

    }

    public List<MinePattern> generatePatterns() {

        return generatePatterns(mineOptions.getPatternOptions().getAmount());

    }

    public List<MinePattern> generatePatterns(int amount) {

        // Генерирует {amount} паттернов

        logger.addMessage("Generating " + amount + " patterns for mine " + name);

        List<MinePattern> patterns = new ArrayList<>();

        IntStream.range(0, amount).forEach(i -> patterns.add(generatePattern()));

        return patterns;

    }

    public MinePattern generatePattern() {

        // Создаёт новый паттерн

        MinePattern minePattern = new MinePattern(this, new ArrayList<>(), LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        minePattern.setState(MinePatternState.GENERATION);

        mineOptions.getPatternCache().getPatternQueue().offer(minePattern);

        logger.addMessage("MinePattern (" + minePattern.getUuid() + ") created for mine " + name);

        // Запускает новый поток для генерации

        MinePatternFactory.getInstance().newThread(() -> {

            logger.addMessage("Start to generate pattern (" + minePattern.getUuid() + ") for " + name);

            long start = System.currentTimeMillis();

            // Процесс генерации блоков в паттерне

            Location minLocation = mineOptions.getMinLocation();
            Location maxLocation = mineOptions.getMaxLocation();

            List<ChanceCalculator> chances = ChanceCalculator.calculate(mineOptions.getBlocks());

            if (!chances.isEmpty())
                for (int x = minLocation.getBlockX(); x <= maxLocation.getBlockX(); x++)
                    for (int y = minLocation.getBlockY(); y <= maxLocation.getBlockY(); y++)
                        for (int z = minLocation.getBlockZ(); z <= maxLocation.getBlockZ(); z++) {

                            float random = ThreadLocalRandom.current().nextFloat();

                            for (ChanceCalculator chance : chances)
                                if (random <= chance.getChance()) {

                                    minePattern.addBlock(new MinePatternBlock(new Location(minLocation.getWorld(), x, y, z), chance.getBlock()));
                                    break;

                                }
                        }

            // Устанавливает статус паттерна как сгенерированный

            mineOptions.setTotalBlocks(minePattern.getBlocks().size());

            minePattern.setState(MinePatternState.GENERATED);
            minePattern.setGeneratedPer(System.currentTimeMillis() - start);

            logger.addMessage("The pattern (" + minePattern.getUuid() + ") for mine " + name + " was generated for " + minePattern.getGeneratedPer() + "ms");

        });

        return minePattern;

    }

    public void save(File file) {

        YamlConfiguration mineConfig = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection mineSection = mineConfig.createSection("Mine");

        mineSection.set("name", name);

        mineSection.set("minLocation", LocationUtil.getLocation(mineOptions.getMinLocation()));
        mineSection.set("maxLocation", LocationUtil.getLocation(mineOptions.getMaxLocation()));

        mineSection.set("resetTime", mineOptions.getResetTime());
        mineSection.set("toReset", mineOptions.getToReset());

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<Material, Float> entry : mineOptions.getBlocks().entrySet())
            stringBuilder.append(entry.getKey().name()).append("-").append(entry.getValue()).append("___");

        mineSection.set("blocks", stringBuilder.toString());

        PatternOptions patternOptions = mineOptions.getPatternOptions();

        mineSection.set("patternAmount", patternOptions.getAmount());
        mineSection.set("patternCycle", patternOptions.isCycle());

        mineSection.set("totalBlocks", mineOptions.getToReset());
        mineSection.set("minedBlocks", mineOptions.getMinedBlocks());

        mineSection.set("resetOn", mineOptions.getResetOn());

        try {

            mineConfig.save(file);

        } catch (IOException exception) {

            exception.printStackTrace();

        }

        logger.addMessage("SAVED");

        logger.save();

    }

    public void delete() {

        Bukkit.getScheduler().cancelTask(mineOptions.getTaskID());

    }

    public int runTask() {

        return new BukkitRunnable() {

            public void run() {

                try {

                    if (mineOptions.getResetOn() >= 0)
                        if (100 - mineOptions.getMinedPercentage() <= mineOptions.getResetOn())
                            fill();

                    if (mineOptions.getResetTime() > 0) {

                        mineOptions.setToReset(mineOptions.getToReset() - 1);

                        if (mineOptions.getToReset() == 0)
                            fill();

                    }
                } catch (Exception ignored) {

                    logger.addMessage("Try to fill, but pattern isn't found");

                }
            }
        }.runTaskTimer(plugin, 0, 20).getTaskId();
    }
}