package net.juicy.mines.utils.block;

import lombok.Value;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class ChanceCalculator {

    public static List<ChanceCalculator> calculate(Map<Material, Float> chances) {

        List<ChanceCalculator> chanceList = new ArrayList<>();
        Map<Material, Float> blocks = new HashMap<>(chances);

        float max = 0f;

        for (double value : chances.values())
            max += value;

        if (max < 100f) {

            blocks.put(Material.AIR, 100f - max);
            max = 100f;

        }

        float chance = 0f;

        for (Map.Entry<Material, Float> entry : blocks.entrySet()) {

            chance += entry.getValue() / max;
            chanceList.add(new ChanceCalculator(entry.getKey(), chance));

        }

        return chanceList;

    }

    Material block;
    float chance;

}