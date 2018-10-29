/**
 * Created by Andrey Germanov on 10/28/18.
 */
package portal.it_port.ru.smartlogger.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.Timer;
import portal.it_port.ru.smartlogger.config.ConfigManager;
import portal.it_port.ru.smartlogger.utils.DataMap;

/**
 * Background service used to communicate with portal server and do various tasks related
 * to Cronjobs which is running on it (get list of cronjobs, enable/disable cronjob etc).
 * Activities and other parts of system can send tasks to this service in form of Intents.
 */
public class CronjobsService extends IntentService {

    // Intent actions, which this service handles
    public final static String CHANGE_SERVICE_STATUS = "portal.it_port.ru.smartlogger.CronjobsService.actions.changeServiceStatus";
    public final static String START_CRONJOBS_POLLING = "portal.it_port.ru.smartlogger.CronjobsService.actions.startCronjobsPolling";

    // Broadcast message codes, which this service can send to notify all interested listeners
    // about process or results of work
    public final static String CRONJOBS_LIST_CHANGED = "portal.it_port.ru.smartlogger.CronjobsService.actions.cronjobsListChanged";

    // Timer, used to run background Cronjobs sync task
    private Timer getCronjobsTimer;

    /**
     * Class constructor
     */
    public CronjobsService() {
        super("CronjobsService");
    }

    /**
     * Method starts after service constructed first time
     */
    public void onCreate() {
        super.onCreate();
    }

    @Override
    /**
     * Method automatically starts when service receives intent to handle it
     */
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        switch (intent.getAction()) {
            case CHANGE_SERVICE_STATUS:
                changeServiceStatus(intent);
                break;
            case START_CRONJOBS_POLLING:
                startCronjobsSyncTask();
        }
    }

    /**
     * Method used to start Cronjobs sync process. This is repeating process started
     * in regular time intervals. Task fetches data about currently running cronjobs from
     * server and updates data in Cronjobs Content provider based on received updates
     */
    private void startCronjobsSyncTask() {
        ConfigManager config = ConfigManager.getInstance();
        if (getCronjobsTimer == null) {
            getCronjobsTimer = new Timer();
            getCronjobsTimer.schedule(new GetCronjobsTask(getApplicationContext()),0,config.getPollPeriod());
        }
    }

    private void changeServiceStatus(Intent intent) {
        Bundle args = intent.getExtras();
        if (args == null) return;
        if (!args.containsKey("cronjob_id") || !args.containsKey("status")) return;
        String statusStr = args.getBoolean("status") ? "1" : "0";
        new EnableDisableCronjobTask().execute(DataMap.create("cronjob_id",args.get("cronjob_id"),"enable",statusStr));
    }
}
