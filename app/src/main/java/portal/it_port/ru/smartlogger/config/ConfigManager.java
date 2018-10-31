package portal.it_port.ru.smartlogger.config;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.utils.DataMap;

/**
 * Created by Andrey Germanov on 10/28/18.
 */
public class ConfigManager {

    private final String TAG = "ConfigManager";

    private String host = "portal.it-port.ru";
    private int port = 8080;
    private String scheme = "http";
    private int pollPeriod = 5000;
    private StateStore stateStore;
    private Context context;

    private Gson gson = new Gson();

    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance==null) instance = new ConfigManager();
        return instance;
    }

    public static ConfigManager getInstance(Context context) {
        if (instance==null) instance = new ConfigManager(context);
        instance.setContext(context);
        return instance;
    }

    private ConfigManager() {
        super();
        loadConfig();
    }

    private ConfigManager(Context context) {
        this.context = context;
        loadConfig();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getPollPeriod() {
        return pollPeriod;
    }

    public void setPollPeriod(int pollPeriod) {
        this.pollPeriod = pollPeriod;
    }

    private void loadConfig() {
        try {
            HashMap<String,Object> data = (HashMap<String,Object>)gson.fromJson(
                    new BufferedReader(new InputStreamReader(context.openFileInput("config.json"))).readLine(),HashMap.class
            );
            host = DataMap.getOrDefault(data,"host",host).toString();
            port = Double.valueOf(DataMap.getOrDefault(data,"port",port).toString()).intValue();
            scheme = DataMap.getOrDefault(data,"scheme",scheme).toString();
            pollPeriod = Double.valueOf(DataMap.getOrDefault(data,"port",pollPeriod).toString()).intValue();
        } catch (FileNotFoundException e) {
            Log.e(TAG,"Application config file not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean saveConfig() {
        HashMap<String,Object> data = DataMap.create(
                "host", host, "port", port,"scheme",scheme,"pollPeriod",pollPeriod
        );
        try {
            FileOutputStream os = context.openFileOutput("config.json", Context.MODE_PRIVATE);
            os.write(gson.toJson(data).getBytes());
            os.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setConfig() {
        stateStore = StateStore.getInstance(context);
        if (validateConfig().size() == 0) {
            host = stateStore.getSettingsHost();
            port = Integer.parseInt(stateStore.getSettingsPort());
            scheme = stateStore.getSettingsProtocol();
            pollPeriod = Integer.parseInt(stateStore.getSettingsPollPeriod());
        }
    }

    public HashMap<String,String> validateConfig() {
        HashMap<String,String> result = new HashMap<>();
        stateStore = StateStore.getInstance(context);
        String error = validateHost();
        if (!error.isEmpty()) result.put("host",error);
        error = validatePort();
        if (!error.isEmpty()) result.put("port",error);
        error = validateProtocol();
        if (!error.isEmpty()) result.put("protocol",error);
        error = validatePollPeriod();
        if (!error.isEmpty()) result.put("pollPeriod",error);
        return result;
    }

    private String validateHost() {
        if (stateStore.getSettingsHost().isEmpty()) return context.getResources().getString(R.string.requiredFieldError);
        return "";
    }

    private String validatePort() {
        if (stateStore.getSettingsPort().isEmpty()) return context.getResources().getString(R.string.requiredFieldError);
        try {
            int port = Integer.parseInt(stateStore.getSettingsPort());
            if (port <= 0 || port>65535) return context.getResources().getString(R.string.incorrectFieldValueError);
            return "";
        } catch (Exception e) {
            return context.getResources().getString(R.string.incorrectFieldValueError);
        }
    }

    private String validateProtocol() {
        if (stateStore.getSettingsProtocol().isEmpty()) return context.getResources().getString(R.string.requiredFieldError);
        if (!stateStore.getSettingsProtocol().equals("http") && !stateStore.getSettingsProtocol().equals("https"))
            return context.getResources().getString(R.string.incorrectFieldValueError);
        return "";
    }

    private String validatePollPeriod() {
        if (stateStore.getSettingsPollPeriod().isEmpty()) return context.getResources().getString(R.string.requiredFieldError);
        try {
            int pollPeriod = Integer.parseInt(stateStore.getSettingsPollPeriod());
            if (pollPeriod <= 0) return context.getResources().getString(R.string.incorrectFieldValueError);
            return "";
        } catch (Exception e) {
            return context.getResources().getString(R.string.incorrectFieldValueError);
        }
    }

    public void setContext(Context context) { this.context = context; }
}