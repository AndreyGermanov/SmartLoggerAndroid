package portal.it_port.ru.smartlogger.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.activities.CronjobItemActivity;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.models.Cronjob;
import portal.it_port.ru.smartlogger.models.CronjobCollection;
import portal.it_port.ru.smartlogger.providers.CronjobsContentProvider;
import portal.it_port.ru.smartlogger.services.CronjobsService;

/**
 * Fragment which used to show and manage list of Cronjobs
 */
public class CronjobsListFragment extends ListFragment {

    private final String TAG = this.getClass().getName();

    // Filter used for list. Only cronjobs with this type displayed in the list
    private String cronjobType;
    private StateStore stateStore;
    // List of cronjobs displayed
    List<Cronjob> list;

    /**
     * Method starts automatically after fragment constructed
     * @param state Saved fragment state
     */
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        stateStore = StateStore.getInstance(getContext());
        cronjobType = StateStore.getInstance(getActivity().getApplicationContext()).getCronjobsListFilter();
        list = CronjobCollection.getInstance().getCronjobsList(cronjobType);
        if (stateStore.getCurrentScreen() == StateStore.Screens.CRONJOBS_ITEM)
            startActivity(new Intent(getContext(),CronjobItemActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        stateStore.setCurrentScreen(StateStore.Screens.CRONJOBS_LIST);
    }

    /**
     * Method starts automatically after all views constructed and displayed on fragment
     * @param view Link to root view
     * @param state Saved fragment state
     */
    @Override
    public void onViewCreated(View view,Bundle state) {
        super.onViewCreated(view,state);
        CronjobsListAdapter adapter = new CronjobsListAdapter(getActivity(),
                android.R.layout.simple_list_item_1,list);
        setListAdapter(adapter);
        LocalBroadcastManager
                .getInstance(getContext())
                .registerReceiver(new CronjobsListBroadcastReceiver(),
                        new IntentFilter(CronjobsService.CRONJOBS_LIST_CHANGED));
        ListView list = getListView();
        list.setDividerHeight(0);
    }

    @Override
    public void onListItemClick(ListView listView, View parent, int position, long id) {
        super.onListItemClick(listView, parent, position, id);
        Cronjob cronjob = (Cronjob)getListAdapter().getItem(position);
        StateStore.getInstance(getActivity().getApplicationContext()).setCurrentCronjobId(cronjob.getId());
        Intent i = new Intent(getActivity(), CronjobItemActivity.class);
        startActivity(i);
    }

    public class CronjobsListBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateCronjobs(context);
            list.clear();
            list.addAll(CronjobCollection.getInstance().getCronjobsList(cronjobType));
            ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
        }

        private void updateCronjobs(Context context) {
            Cursor cursor = context.getContentResolver().query(CronjobsContentProvider.CRONJOBS_LIST_URI,null,null,null,null);
            if (cursor == null) return;
            while (cursor.moveToNext()) CronjobCollection.getInstance().putCronjob(cursor);
            cursor.close();
        }
    }

    private class CronjobsListAdapter extends ArrayAdapter<Cronjob> {

        CronjobsListAdapter(@NonNull Context context, int resource, @NonNull List<Cronjob> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.cronjob_list_item,parent,false);
            TextView cronjobId = convertView.findViewById(R.id.list_cronjob_id);
            TextView cronjobName = convertView.findViewById(R.id.list_cronjob_name);
            TextView cronjobStatus = convertView.findViewById(R.id.list_cronjob_status);
            TextView cronjobEnabled = convertView.findViewById(R.id.list_cronjob_enabled);
            TextView cronjobLastRunTimestamp = convertView.findViewById(R.id.list_cronjob_lastRunTimestamp);
            Cronjob item = (Cronjob)getListAdapter().getItem(position);
            cronjobId.setText(item.getId());
            cronjobName.setText(item.getName());
            cronjobStatus.setText(item.getStatus());
            cronjobEnabled.setText(Boolean.toString(item.isEnabled()));
            if (item.getLastRunTimestamp()>0)
                cronjobLastRunTimestamp.setText(Long.toString(item.getLastRunTimestamp()));
            return convertView;
        }
    }
}
