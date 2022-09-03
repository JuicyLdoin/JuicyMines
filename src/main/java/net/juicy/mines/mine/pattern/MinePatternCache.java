package net.juicy.mines.mine.pattern;

import lombok.Value;
import net.juicy.mines.mine.Mine;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

@Value
public class MinePatternCache {

    Mine mine;
    Queue<MinePattern> patternQueue;

    public MinePattern getNext() {

        return getPattern(patternQueue.poll());

    }

    public MinePattern getPattern(MinePattern pattern) {

        // Если паттерн не найден, генерируем новый

        if (pattern == null)
            pattern = mine.generatePattern();

        // Если паттерн не сгенерирован выбрасывает UnsupportedOperationException

        if (!pattern.getState().equals(MinePatternState.GENERATED))
            throw new UnsupportedOperationException("MinePattern isn't generated!");

        /*
            Если настройка паттернов "cycle" включена, помещаем паттерн в конец очереди
            Если настройка паттернов "cycle" выключена, генерируем новый паттерн
         */

        if (mine.getMineOptions().getPatternOptions().isCycle())
            patternQueue.offer(pattern);
        else {

            pattern.setState(MinePatternState.USED);
            mine.generatePattern();

        }

        patternQueue.remove(pattern);

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

        List<MinePattern> patterns = new ArrayList<>();

        patternQueue.stream()
                .filter(pattern -> pattern.getState().equals(state))
                .forEach(patterns::add);

        return patterns;

    }
}