package portal.it_port.ru.smartlogger.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.List;

import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.fragments.CronjobItemFragment;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.models.Cronjob;
import portal.it_port.ru.smartlogger.models.CronjobCollection;


/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class CronjobItemActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Cronjob> cronjobs;
    private String currentCronjobId;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
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
    }

    private void setCronjobsList() {
        StateStore stateStore = StateStore.getInstance(getApplicationContext());
        cronjobs = CronjobCollection.getInstance().getCronjobsList(stateStore.getCronjobsListFilter());
        currentCronjobId = stateStore.getCurrentCronjobId();
    }

    private void setInitialItem() {
        if (currentCronjobId == null) return;
        Cronjob cronjob = CronjobCollection.getInstance().getCronjob(currentCronjobId);
        if (cronjob == null) return;
        int cronjobIndex = cronjobs.indexOf(cronjob);
        if (cronjobIndex != -1) viewPager.setCurrentItem(cronjobIndex);
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
            System.out.println(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            System.out.println(cronjobs.size());
            return cronjobs.size();
        }
    }
}
