package net.juicy.mines.mine.pattern;

import lombok.EqualsAndHashCode;
import lombok.Value;
import net.juicy.mines.utils.IPlaceable;
import org.bukkit.Location;
import org.bukkit.Material;

@Value
@EqualsAndHashCode
public class MinePatternBlock implements IPlaceable {

    Location location;
    Material block;

    public void place() {

        // Установка блока на локацию

        location.getBlock().setType(block, false);

    }
}