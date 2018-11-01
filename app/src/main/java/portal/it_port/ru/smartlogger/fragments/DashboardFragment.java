package portal.it_port.ru.smartlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.main.StateStore;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class DashboardFragment extends BaseFragment {

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if (inflater == null) return getView();
        return inflater.inflate(R.layout.fragment_dashboard,parent,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        stateStore.setCurrentScreen(StateStore.Screens.DASHBOARD);
    }
}
