package net.juicy.mines.mine.pattern;

import lombok.NonNull;
import net.juicy.mines.JuicyMinesPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public class MinePatternFactory implements ThreadFactory, Runnable {

    private static final MinePatternFactory instance = new MinePatternFactory();

    public static MinePatternFactory getInstance() {

        return instance;

    }

    private final List<Thread> threads;

    private MinePatternFactory() {

        threads = new ArrayList<>();

        new Thread(this).start();

    }

    public synchronized Thread newThread(@NonNull Runnable runnable) {

        Thread thread = new Thread(runnable, "PatternGenerator-" + (threads.size() + 1));
        thread.start();

        threads.add(thread);

        return thread;

    }

    public void run() {

        while (JuicyMinesPlugin.getPlugin().isEnabled())
            synchronized (threads) {

                threads.removeIf(thread -> !thread.isAlive());

            }
    }
}