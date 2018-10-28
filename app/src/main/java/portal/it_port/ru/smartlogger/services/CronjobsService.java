package portal.it_port.ru.smartlogger.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import java.util.Timer;
import portal.it_port.ru.smartlogger.config.ConfigManager;

/**
 * Created by Andrey Germanov on 10/28/18.
 */
public class CronjobsService extends IntentService {

    public final static String CHANGE_SERVICE_STATUS = "portal.it_port.ru.smartlogger.CronjobsService.actions.changeServiceStatus";
    public final static String START_CRONJOBS_POLLING = "portal.it_port.ru.smartlogger.CronjobsService.actions.startCronjobsPolling";
    public final static String CRONJOBS_LIST_CHANGED = "portal.it_port.ru.smartlogger.CronjobsService.actions.cronjobsListChanged";

    private Timer getCronjobsTimer;

    public CronjobsService() {
        super("CronjobsService");
    }

    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        switch (intent.getAction()) {
            case CHANGE_SERVICE_STATUS:
                break;
            case START_CRONJOBS_POLLING:
                ConfigManager config = ConfigManager.getInstance();
                if (getCronjobsTimer == null) {
                    getCronjobsTimer = new Timer();
                    getCronjobsTimer.schedule(new GetCronjobsTask(getApplicationContext()),0,config.getPollPeriod());
                }
        }
    }
}
