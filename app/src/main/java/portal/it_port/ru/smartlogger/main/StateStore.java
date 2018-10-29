package portal.it_port.ru.smartlogger.main;

/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class StateStore {

    private Screens currentScreen = Screens.CRONJOBS;
    private String currentCronjobId;

    private static StateStore instance;

    public static StateStore getInstance() {
        if (instance == null) instance = new StateStore();
        return instance;
    }

    private StateStore() {
        loadState();
    }

    private void saveState() {

    }

    private void loadState() {

    }

    public Screens getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screens currentScreen) {
        this.currentScreen = currentScreen;
        saveState();
    }

    public String getCurrentCronjobId() {
        return currentCronjobId;
    }

    public void setCurrentCronjobId(String currentCronjobId) {
        this.currentCronjobId = currentCronjobId;
        saveState();
    }

    public enum Screens {
        CRONJOBS,SETTINGS,DASHBOARD
    }
}
