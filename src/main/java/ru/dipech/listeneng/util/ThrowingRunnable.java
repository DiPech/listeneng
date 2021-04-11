package ru.dipech.listeneng.util;

import ru.dipech.listeneng.exception.ApplicationException;

@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> extends Runnable {

    @Override
    default void run() {
        try {
            runThrows();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    void runThrows() throws E;

    static void throwingRunnable(ThrowingRunnable<Exception> runnable) {
        runnable.run();
    }
}
