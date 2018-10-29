/**
 * Created by Andrey Germanov on 10/29/18.
 */
package portal.it_port.ru.smartlogger.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.TreeMap;
import portal.it_port.ru.smartlogger.config.ConfigManager;
import portal.it_port.ru.smartlogger.providers.CronjobsContentProvider;

/**
 * Class used to update list of current running tasks from server. CronjobsService runs
 * it in background to update CronjobsContentProvider if something changed.
 */
class GetCronjobsTask extends TimerTask {

    // Link to current system configuration
    private ConfigManager config = ConfigManager.getInstance();
    // Link to context of service which started this task
    private Context context;

    /**
     * Class constructor
     * @param context Link to servicer context
     */
    GetCronjobsTask(Context context) {
        this.context = context;
    }

    @Override
    /**
     * Matin method, which service uses to start this task
     */
    public void run() {
        try {
            TreeMap<String,LinkedTreeMap<String,Object>> received = getCurrentCronjobs();
            TreeMap<String,LinkedTreeMap<String,Object>> existing = getExistingCronjobs();
            if (!received.equals(existing)) {
                ArrayList<String> changedRecordIDs = new ArrayList<>();
                for (String key: received.keySet()) {
                    if (existing.containsKey(key) && existing.get(key).equals(received.get(key))) continue;
                    changedRecordIDs.add(key);
                    updateCronjobInDB(key,received.get(key));
                }
                Intent i = new Intent(CronjobsService.CRONJOBS_LIST_CHANGED);
                i.putExtra("changedRecordIDs",changedRecordIDs);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to download, parse and return list of currently running cronjobs on server
     * @return Map in which keys are cronjob IDs and values are information hash maps about
     * currently running tasks
     */
    private TreeMap<String,LinkedTreeMap<String,Object>> getCurrentCronjobs() {
        URLConnection connection;
        TreeMap<String,LinkedTreeMap<String,Object>> result = new TreeMap<>();
        try {
            URL url = new URL(config.getScheme() + "://" + config.getHost() + ":" + config.getPort() + "/cronjobs");
            connection = url.openConnection();
            if (config.getScheme().equals("https"))
                connection = url.openConnection();
            connection.connect();
            Gson gson = new Gson();
            String json = new BufferedReader(new InputStreamReader((InputStream) connection.getContent())).readLine();
            if (json == null || json.isEmpty()) return result;
            return parseCronjobs(gson.fromJson(json,TreeMap.class));
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * Method used to transform generated cronjobs JSON to list acce[ted by application
     * @param cronjobs Input JSON
     * @return Output JSON
     */
    private TreeMap<String,LinkedTreeMap<String,Object>> parseCronjobs(TreeMap<String,LinkedTreeMap<String,Object>> cronjobs) {
        TreeMap<String,LinkedTreeMap<String,Object>> result = new TreeMap<>();
        for (String key: cronjobs.keySet()) {
            LinkedTreeMap<String,Object> row = (LinkedTreeMap<String,Object>)cronjobs.get(key).get("value");
            row.put("id",key);
            result.put(key,row);
        }
        return result;
    }

    /**
     * Method used to get list of existing cronjobs inside local Content Provider
     * @return List of cronjobs in the same format as cronjobs returned from server
     */
    private TreeMap<String,LinkedTreeMap<String,Object>> getExistingCronjobs() {
        TreeMap<String,LinkedTreeMap<String,Object>> result = new TreeMap<>();
        String[] fields = new String[]{"enabled","id","lastRunTimestamp","name","status","type"};
        Cursor cursor = context.getContentResolver().query(CronjobsContentProvider.CRONJOBS_LIST_URI,fields,null,null,null);
        if (cursor == null || cursor.getCount() == 0) return result;
        while (cursor.moveToNext()) {
            result.put(cursor.getString(cursor.getColumnIndex("id")),getExistingCronjob(cursor,fields));
        }
        cursor.close();
        return result;
    }

    /**
     * Method used to create data structure for individual cronjob from Content Provider
     * @param cursor Cursor of Content provider pointed to needed record
     * @param fields Which fields to retrive from cursor record
     * @return Map with cronjob data of current cursor row
     */
    private LinkedTreeMap<String,Object> getExistingCronjob(Cursor cursor,String[] fields) {
        LinkedTreeMap<String,Object> row = new LinkedTreeMap<>();
        for (String field:fields) {
            String value = cursor.getString(cursor.getColumnIndex(field));
            if (field.equals("enabled"))
                row.put(field,Boolean.parseBoolean(value));
            else
                row.put(field,value);
        }
        return row;
    }

    /**
     * Method used to update cronjob record in Content Provider
     * @param id ID of cronjob to update
     * @param row Data to set in this record
     */
    private void updateCronjobInDB(String id,LinkedTreeMap<String,Object> row) {
        ContentValues values = new ContentValues();
        values.put("id",id);
        for (String field: row.keySet()) {
            if (field.equals("enabled"))
                values.put(field,Boolean.parseBoolean(row.get(field).toString()));
            else
                values.put(field,row.get(field).toString());
        }
        context.getContentResolver().insert(CronjobsContentProvider.CRONJOBS_LIST_URI,values);
    }
}
