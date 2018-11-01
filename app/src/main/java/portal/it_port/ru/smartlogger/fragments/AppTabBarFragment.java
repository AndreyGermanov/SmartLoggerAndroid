package portal.it_port.ru.smartlogger.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.main.StateStore;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class AppTabBarFragment extends Fragment implements View.OnClickListener {

    public static final String CURRENT_TAB_CHANGED =
            "portal.it_port.ru.smartlogger.AppTabBarFragment.current_tab_changed";

    private StateStore stateStore;
    private Context context;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if (inflater == null) return getView();
        context = getContext();
        stateStore = StateStore.getInstance(getContext());
        View v = inflater.inflate(R.layout.fragment_tabbar,parent,false);
        setupUI(v);
        setBroadcastReceiver();
        return v;
    }

    private void setupUI(View v) {
        ImageButton[] tabs = new ImageButton[]{
                v.findViewById(R.id.tab_dashboard),
                v.findViewById(R.id.tab_cronjobs),
                v.findViewById(R.id.tab_settings)};
        for (ImageButton tab: tabs) {
            tab.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            tab.setOnClickListener(this);
        }
        if (tabs[stateStore.getCurrentTab()]!=null)
            tabs[stateStore.getCurrentTab()]
                    .setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
    }

    private void setBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(CURRENT_TAB_CHANGED);
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(new TabBarBroadcastReceiver(),filter);
    }

    @Override
    public void onClick(View view) {
        Integer selectedTab = null;
        switch (view.getId()) {
            case R.id.tab_dashboard: selectedTab = 0;break;
            case R.id.tab_cronjobs: selectedTab = 1;break;
            case R.id.tab_settings: selectedTab = 2;
        }
        if (selectedTab != null) {
            stateStore.setCurrentTab(selectedTab);
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(new Intent(CURRENT_TAB_CHANGED));
        }
    }

    private class TabBarBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) return;
            if (intent.getAction().equals(AppTabBarFragment.CURRENT_TAB_CHANGED)) {
                if (getView() != null) setupUI(getView());
            }
        }
    }
}
