package portal.it_port.ru.smartlogger.main;

import android.content.Context;

import portal.it_port.ru.smartlogger.R;

/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class StateStore {

    private Context context;
    private Screens currentScreen = Screens.CRONJOBS;
    private String currentCronjobId;
    private String cronjobsListFilter = "";
    private int currentTab = 0;

    private static StateStore instance;

    public static StateStore getInstance(Context context) {
        if (instance == null) instance = new StateStore(context);
        return instance;
    }

    private StateStore(Context context) {
        this.context = context;
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

    public String getCronjobsListFilter() {
        return cronjobsListFilter;
    }

    public void setCronjobsListFilter(String cronjobsListFilter) {
        this.cronjobsListFilter = cronjobsListFilter;
        saveState();
    }

    public static void setInstance(StateStore instance) {
        StateStore.instance = instance;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        saveState();
    }

    public enum Screens {
        CRONJOBS,SETTINGS,DASHBOARD
    }
}
