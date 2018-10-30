package portal.it_port.ru.smartlogger.main;

import android.app.Application;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class SmartLoggerApplication extends Application {

    private StateStore stateStore;
    private static SmartLoggerApplication instance;

    public SmartLoggerApplication() {}

    public static SmartLoggerApplication get() {
        if (instance == null) instance = new SmartLoggerApplication();
        return instance;
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        stateStore = StateStore.getInstance(getApplicationContext());
    }

    public StateStore getStateStore() {
        return stateStore;
    }

}
