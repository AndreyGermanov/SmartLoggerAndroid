package portal.it_port.ru.smartlogger.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import portal.it_port.ru.smartlogger.services.CronjobsService;

/**
 * Created by Andrey Germanov on 10/28/18.
 */
public class CronjobsListFragment extends ListFragment {

    private final String TAG = this.getClass().getName();
    ArrayList<String> arr = new ArrayList<>();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,arr);
        setListAdapter(adapter);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(new CronjobsListBroadcaseReceiver(),new IntentFilter(CronjobsService.CRONJOBS_LIST_CHANGED));
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.i(TAG,getListAdapter().getItem(position).toString());
    }

    public class CronjobsListBroadcaseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
        }
    }
}
