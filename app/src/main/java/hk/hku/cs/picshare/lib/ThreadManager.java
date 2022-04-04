package hk.hku.cs.picshare.lib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static class InstanceHolder {
        private static final ThreadManager instance = new ThreadManager();
    }
    private final ExecutorService mExecutorService;
    public static ThreadManager getInstance() {
        return InstanceHolder.instance;
    }

    private ThreadManager() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void submit(Runnable runnable) {
        mExecutorService.submit(runnable);
    }
}
