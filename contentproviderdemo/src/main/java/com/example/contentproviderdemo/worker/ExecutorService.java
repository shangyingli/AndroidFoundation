package com.example.contentproviderdemo.worker;

import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorService {
    private static ExecutorService instance;
    private Executor executor;

    private ExecutorService() {
        init();
    }

    private void init() {
        executor = new ThreadPoolExecutor(
                5, 5, 60l,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
    }

    public static ExecutorService getDefaultExecutorService() {
        synchronized (ExecutorService.class) {
            if (instance == null) {
                instance = new ExecutorService();
            }
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public void execute(FutureTask task) {
        executor.execute(task);
    }
}
