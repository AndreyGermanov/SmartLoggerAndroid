package portal.it_port.ru.smartlogger.fragments;

import android.support.v4.app.Fragment;

import portal.it_port.ru.smartlogger.main.StateStore;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class BaseFragment extends Fragment {

    protected StateStore stateStore;

    public BaseFragment() {
        super();
        this.stateStore = StateStore.getInstance(getContext());
    }
}
