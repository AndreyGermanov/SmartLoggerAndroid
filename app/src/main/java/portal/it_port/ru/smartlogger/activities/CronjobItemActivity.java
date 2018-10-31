package portal.it_port.ru.smartlogger.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import java.util.List;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.fragments.CronjobItemFragment;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.models.Cronjob;
import portal.it_port.ru.smartlogger.models.CronjobCollection;
import portal.it_port.ru.smartlogger.services.CronjobsService;


/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class CronjobItemActivity extends FragmentActivity {

    private ViewPager viewPager;
    private List<Cronjob> cronjobs;
    private String currentCronjobId;
    private StateStore stateStore;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        stateStore = StateStore.getInstance(getApplicationContext());
        setCronjobsList();
        viewPager = new ViewPager(this);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int i, float v, int i1) { }
            @Override public void onPageSelected(int i) {
                StateStore.getInstance(getApplicationContext()).setCurrentCronjobId(cronjobs.get(i).getId());
            }
            @Override public void onPageScrollStateChanged(int i) { }
        });
        setInitialItem();
        viewPager.setId(R.id.viewPager_cronjobs);
        setContentView(viewPager);
        setBroadcastReceiver();
    }

    private void setCronjobsList() {
        cronjobs = CronjobCollection.getInstance().getCronjobsList(stateStore.getCronjobsListFilter());
        currentCronjobId = stateStore.getCurrentCronjobId();
    }

    private void setBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(CronjobsService.CRONJOBS_LIST_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(new CronjobItemActivityBroadcastReceiver(),filter);
    }

    private void setInitialItem() {
        if (currentCronjobId == null) return;
        Cronjob cronjob = CronjobCollection.getInstance().getCronjob(currentCronjobId);
        if (cronjob == null) return;
        int cronjobIndex = cronjobs.indexOf(cronjob);
        if (cronjobIndex != -1) viewPager.setCurrentItem(cronjobIndex,false);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Cronjob cronjob = cronjobs.get(i);
            Fragment fragment = new CronjobItemFragment();
            Bundle args = new Bundle();
            args.putString("cronjobId",cronjob.getId());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return cronjobs.size();
        }
    }

    private class CronjobItemActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (cronjobs == null || cronjobs.size() == 0) {
               setCronjobsList();
               viewPager.getAdapter().notifyDataSetChanged();
               setInitialItem();
            }
        }
    }
}
