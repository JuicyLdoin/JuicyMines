package net.juicy.mines.mine.pattern;

import lombok.Data;
import net.juicy.mines.mine.Mine;
import net.juicy.mines.utils.IPlaceable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MinePattern implements IPlaceable {

    final UUID uuid = UUID.randomUUID();

    final Mine mine;
    final List<MinePatternBlock> blocks;

    MinePatternState state;

    final LocalDateTime generatedOn;
    long generatedPer = 0;

    public void addBlock(MinePatternBlock minePatternBlock) {

        if (!blocks.contains(minePatternBlock)) blocks.add(minePatternBlock);

    }

    public void place() {

        // Размещает все блоки

        blocks.forEach(MinePatternBlock::place);

    }
}