/**
 * Created by Andrey Germanov on 10/22/18.
 */
package portal.it_port.ru.smartlogger.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import portal.it_port.ru.smartlogger.R;

/**
 * Base class which constructs activity with single fragment, which fills whole screen of
 * activity
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    // Content fragment of activity
    protected abstract Fragment createFragment();

    // Returns layout for this activity
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    /**
     * Method starts after activity constructed. Used to create fragment and put it to activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }

    }
}
