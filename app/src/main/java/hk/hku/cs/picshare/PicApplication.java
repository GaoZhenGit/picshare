package hk.hku.cs.picshare;

import android.app.Application;

public class PicApplication extends Application {
    private static PicApplication instance;

    public static PicApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
