package portal.it_port.ru.smartlogger.services;

import android.os.AsyncTask;
import com.google.gson.internal.LinkedTreeMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.TreeMap;

import portal.it_port.ru.smartlogger.config.ConfigManager;

/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class EnableDisableCronjobTask extends AsyncTask<HashMap,Void,Void> {

    // Link to current system configuration
    private ConfigManager config = ConfigManager.getInstance();

    @Override
    protected Void doInBackground(HashMap... hashmaps) {
        HashMap params = hashmaps[0];
        String cronjob_id = params.get("cronjob_id").toString();
        String enable = params.get("enable").toString();
        try {
            URL url = new URL(config.getScheme() + "://" + config.getHost() + ":" + config.getPort() + "/cronjobs/enable/"+cronjob_id+"/"+enable);
            URLConnection connection = url.openConnection();
            connection.connect();
            connection.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
