package portal.it_port.ru.smartlogger.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.main.StateStore;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class DashboardFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard,container,false);
        SetupUI(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        stateStore.setCurrentScreen(StateStore.Screens.DASHBOARD);
    }

    private void SetupUI(View v) {};
}
