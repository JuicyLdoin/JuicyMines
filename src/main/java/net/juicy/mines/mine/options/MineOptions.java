package net.juicy.mines.mine.options;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.juicy.api.bukkit.JuicyPlayer;
import net.juicy.mines.mine.pattern.MinePatternCache;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MineOptions {

    Location minLocation;
    Location maxLocation;

    int resetTime;
    int toReset;

    MinePatternCache patternCache;
    Map<Material, Double> blocks;

    int taskID;

    PatternOptions patternOptions;

    int totalBlocks;
    int minedBlocks;

    int resetOn;

    public boolean isAllowToBreakBlock(Block block) {

        if (block == null) return false;

        Location location = block.getLocation();
        boolean contains = !block.getType().equals(Material.AIR);

        if (!contains)
            for (Material material : blocks.keySet())
                if (block.getType().equals(material)) {

                    contains = true;
                    break;

                }

        return contains && isInside(location);

    }

    public boolean isAllowToBreakBlock(Location block) {

        return isAllowToBreakBlock(block.getBlock());

    }

    public boolean isInside(Location location) {

        return location.getWorld().equals(minLocation.getWorld()) &&
                location.getX() >= minLocation.getBlockX() && location.getX() <= maxLocation.getBlockX() &&
                location.getY() >= minLocation.getBlockY() && location.getY() <= maxLocation.getBlockY() &&
                location.getZ() >= minLocation.getBlockZ() && location.getZ() <= maxLocation.getBlockZ();

    }

    public boolean isInside(Player player) {

        return isInside(player.getLocation());

    }

    public boolean isInside(JuicyPlayer player) {

        return isInside(player.getLocation());

    }

    public void addMinedBlock() {

        minedBlocks++;

    }

    public float getMinedPercentage() {

        return minedBlocks == 0 ? 0 : ((float) minedBlocks / totalBlocks) * 100;

    }
}