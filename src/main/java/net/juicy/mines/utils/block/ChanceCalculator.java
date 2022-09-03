package net.juicy.mines.utils.block;

import lombok.Value;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class ChanceCalculator {

    public static List<ChanceCalculator> calculate(Map<Material, Double> chances) {

        List<ChanceCalculator> chanceList = new ArrayList<>();
        Map<Material, Double> blocks = new HashMap<>(chances);

        double max = 0.0D;

        for (Map.Entry<Material, Double> entry : blocks.entrySet())
            max += entry.getValue();

        if (max < 100.0D) {

            blocks.put(Material.AIR, 100.0D - max);
            max = 100.0D;

        }

        double chance = 0.0D;

        for (Map.Entry<Material, Double> entry : blocks.entrySet()) {

            chance += entry.getValue() / max;
            chanceList.add(new ChanceCalculator(entry.getKey(), chance));

        }

        return chanceList;

    }

    Material block;
    double chance;

}