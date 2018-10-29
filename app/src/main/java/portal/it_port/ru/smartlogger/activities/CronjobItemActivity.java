package portal.it_port.ru.smartlogger.activities;

import android.support.v4.app.Fragment;
import portal.it_port.ru.smartlogger.fragments.CronjobItemFragment;

/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class CronjobItemActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CronjobItemFragment();
    }
}
