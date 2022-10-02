package net.juicy.mines.mine.pattern;

import lombok.Value;
import net.juicy.mines.mine.Mine;

import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
public class MinePatternCache {

    Mine mine;
    Queue<MinePattern> patternQueue;

    public MinePattern getNext() {

        return getPattern(patternQueue.peek());

    }

    public MinePattern getPattern(MinePattern pattern) {

        // Если паттерн не найден, генерируем новый

        if (pattern == null)
            pattern = mine.generatePattern();

        // Если паттерн не сгенерирован выбрасывает UnsupportedOperationException

        if (!pattern.getState().equals(MinePatternState.GENERATED))
            throw new UnsupportedOperationException("MinePattern isn't generated!");

        // Если настройка паттернов "cycle" выключена, генерируем новый паттерн

        if (!mine.getMineOptions().getPatternOptions().isCycle()) {

            pattern.setState(MinePatternState.USED);
            mine.generatePattern();

            patternQueue.remove(pattern);

        }

        return pattern;

    }

    public MinePattern getByUUID(UUID uuid) {

        return getPattern(patternQueue.stream()
                .filter(pattern -> pattern.getUuid().equals(uuid))
                .findFirst()
                .get());

    }

    public List<MinePattern> getPatternsWithState(MinePatternState state) {

        // Возвращаем все паттерны со статусом {state}

        return patternQueue.stream()
                .filter(pattern -> pattern.getState().equals(state))
                .collect(Collectors.toList());

    }
}