package portal.it_port.ru.smartlogger.main;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import portal.it_port.ru.smartlogger.fragments.CronjobsListFragment;
import portal.it_port.ru.smartlogger.services.CronjobsService;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CronjobsListFragment();
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Intent i = new Intent(this,CronjobsService.class);
        i.setAction(CronjobsService.START_CRONJOBS_POLLING);
        startService(i);
    }
}
