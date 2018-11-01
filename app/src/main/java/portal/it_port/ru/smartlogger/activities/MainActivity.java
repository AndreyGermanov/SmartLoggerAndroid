package portal.it_port.ru.smartlogger.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.fragments.AppTabBarFragment;
import portal.it_port.ru.smartlogger.fragments.CronjobsManagerFragment;
import portal.it_port.ru.smartlogger.fragments.DashboardFragment;
import portal.it_port.ru.smartlogger.fragments.SettingsFragment;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.services.CronjobsService;

/**
 * Main activity of application
 */
public class MainActivity extends FragmentActivity {

    ViewPager viewPager;
    private StateStore stateStore;

    /**
     * Method starts after activity constructed
     * @param state State to persist
     */
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        stateStore = StateStore.getInstance(this);
        setContentView(R.layout.activity_main);
        startCronjobsService();
        setupUI();
        setupBroadCastReceiver();
    }

    private void startCronjobsService() {
        Intent i = new Intent(this,CronjobsService.class);
        i.setAction(CronjobsService.START_CRONJOBS_POLLING);
        startService(i);
    }

    private void setupUI() {
        viewPager = findViewById(R.id.viewPager_main);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int i, float v, int i1) { }

            @Override public void onPageSelected(int i) {
                stateStore.setCurrentTab(i);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(new Intent(AppTabBarFragment.CURRENT_TAB_CHANGED));
            }

            @Override public void onPageScrollStateChanged(int i) { }
        });
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(stateStore.getCurrentTab());
    }

    private void setupBroadCastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppTabBarFragment.CURRENT_TAB_CHANGED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(new MainActivityBroadcastReceiver(),filter);
    }

    private class MainActivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction()==null) return;
            if (intent.getAction().equals(AppTabBarFragment.CURRENT_TAB_CHANGED)) {
                viewPager.setCurrentItem(stateStore.getCurrentTab());
            }
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new DashboardFragment();
                case 1: return new CronjobsManagerFragment();
                case 2: return new SettingsFragment();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
