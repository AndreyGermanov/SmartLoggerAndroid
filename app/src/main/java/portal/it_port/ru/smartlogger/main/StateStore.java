package portal.it_port.ru.smartlogger.main;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import portal.it_port.ru.smartlogger.config.ConfigManager;
import portal.it_port.ru.smartlogger.utils.DataMap;

/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class StateStore {

    private Context context;
    private String currentCronjobId;
    private String cronjobsListFilter = "";
    private int currentTab = 0;
    private Gson gson = new Gson();
    private Screens currentScreen = Screens.DASHBOARD;
    private String settingsHost = "";
    private String settingsPort = "";
    private String settingsPollPeriod = "";
    private String settingsProtocol = "";

    private final String TAG = "StateStore";
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
        HashMap<String,Object> state = DataMap.create(
            "currentTab", getCurrentTab(),
            "currentCronjobId", getCurrentCronjobId(),
            "cronjobsListFilter", getCronjobsListFilter(),
            "currentScreen", getCurrentScreen().toString(),
            "settingsHost", getSettingsHost(),
            "settingsPort", getSettingsPort(),
            "settingsPollPeriod", getSettingsPollPeriod(),
            "settingsProtocol", getSettingsProtocol()
        );
        try {
            FileOutputStream os = context.openFileOutput("state.json", Context.MODE_PRIVATE);
            os.write(gson.toJson(state).getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadState() {
        ConfigManager configManager = ConfigManager.getInstance(context);
        configManager.setContext(context);
        try {
            HashMap<String,Object> state = (HashMap<String,Object>)gson.fromJson(
                new BufferedReader(new InputStreamReader(context.openFileInput("state.json"))).readLine(),HashMap.class
            );
            currentTab = Double.valueOf(DataMap.getOrDefault(state,"currentTab",currentTab).toString()).intValue();
            currentCronjobId = DataMap.getOrDefault(state,"currentCronjobId",currentCronjobId).toString();
            cronjobsListFilter = DataMap.getOrDefault(state,"cronjobsListFilter",cronjobsListFilter).toString();
            currentScreen = Screens.valueOf(DataMap.getOrDefault(state,"currentScreen",cronjobsListFilter).toString());
            settingsHost = DataMap.getOrDefault(state,"settingsHost",configManager.getHost()).toString();
            settingsPort = DataMap.getOrDefault(state,"settingsPort",configManager.getPort()).toString();
            settingsPollPeriod = DataMap.getOrDefault(state,"settingsPollPeriod",configManager.getPollPeriod()).toString();
            settingsProtocol = DataMap.getOrDefault(state,"settingsProtocol",configManager.getScheme()).toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG,"Application state file not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Screens getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screens currentScreen) {
        this.currentScreen = currentScreen;
        saveState();
    }

    public String getSettingsHost() {
        return settingsHost;
    }

    public void setSettingsHost(String settingsHost) {
        this.settingsHost = settingsHost;
        saveState();
    }

    public String getSettingsPort() {
        return settingsPort;
    }

    public void setSettingsPort(String settingsPort) {
        this.settingsPort = settingsPort;
        saveState();
    }

    public String getSettingsPollPeriod() {
        return settingsPollPeriod;
    }

    public void setSettingsPollPeriod(String settingsPollPeriod) {
        this.settingsPollPeriod = settingsPollPeriod;
        saveState();
    }

    public String getSettingsProtocol() {
        return settingsProtocol;
    }

    public void setSettingsProtocol(String settingsProtocol) {
        this.settingsProtocol = settingsProtocol;
        saveState();
    }

    public enum Screens {
        DASHBOARD,SETTINGS,CRONJOBS_LIST,CRONJOBS_ITEM
    }

}
