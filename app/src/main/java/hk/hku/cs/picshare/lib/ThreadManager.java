package hk.hku.cs.picshare.lib;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static class InstanceHolder {
        private static final ThreadManager instance = new ThreadManager();
    }
    private final ExecutorService mExecutorService;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    public static ThreadManager getInstance() {
        return InstanceHolder.instance;
    }

    private ThreadManager() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void submit(Runnable runnable) {
        mExecutorService.submit(runnable);
    }

    public void runOnUiThread(Runnable runnable) {
        mMainHandler.post(runnable);
    }
}
