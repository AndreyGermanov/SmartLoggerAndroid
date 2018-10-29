package portal.it_port.ru.smartlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import portal.it_port.ru.smartlogger.fragments.CronjobsListFragment;
import portal.it_port.ru.smartlogger.services.CronjobsService;

/**
 * Main activity of application
 */
public class MainActivity extends SingleFragmentActivity {

    /**
     * Method used to construct fragment, which will be used as content of activity
     * @return
     */
    @Override
    protected Fragment createFragment() {
        CronjobsListFragment fragment = new CronjobsListFragment();
        Bundle arguments = new Bundle();
        arguments.putString("type","loggers");
        fragment.setArguments(arguments);
        return fragment;
    }

    /**
     * Method starts after activity constructed
     * @param state
     */
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Intent i = new Intent(this,CronjobsService.class);
        i.setAction(CronjobsService.START_CRONJOBS_POLLING);
        startService(i);
    }
}
