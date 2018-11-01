package portal.it_port.ru.smartlogger.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.models.CronjobCollection;
import portal.it_port.ru.smartlogger.services.CronjobsService;

/**
 * Created by Andrey Germanov on 11/1/18.
 */
public class CronjobsManagerFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final String TAG = "CronjobsManagerFragment";
    private List<String> list = new ArrayList<>();
    private StateStore stateStore;
    private Spinner filterSpinner;
    private Context context;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if (inflater==null) return getView();
        stateStore = StateStore.getInstance(getContext());
        context = getContext();
        View v = inflater.inflate(R.layout.fragment_cronjobs_manager,parent,false);
        setupUI(v);
        setupBroadcastReceiver();
        return v;
    }

    public void setupUI(View v) {
        filterSpinner = v.findViewById(R.id.cronjobs_filter_dropdown);
        filterSpinner.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,list));
        filterSpinner.setOnItemSelectedListener(this);
    }

    public void setupBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(CronjobsService.CRONJOBS_LIST_CHANGED);
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(new CronjobsManagerBroadcastReceiver(),filter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        stateStore.setCronjobsListFilter(list.get(pos));
        Log.i(TAG,"Selected item number "+list.get(pos));
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(new Intent(CronjobsService.CRONJOBS_LIST_CHANGED));
    }

    @Override public void onNothingSelected(AdapterView<?> adapterView) { }

    private class CronjobsManagerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"Received broadcast about changes in cronjobs list");
            if (intent == null || intent.getAction() == null) return;
            if (intent.getAction().equals(CronjobsService.CRONJOBS_LIST_CHANGED)) {
                Set<String> newList = CronjobCollection.getInstance().getCronjobTypes();
                list.clear();
                list.add("");
                list.addAll(newList);
                filterSpinner.setSelection(list.indexOf(stateStore.getCronjobsListFilter()));
                ((ArrayAdapter)filterSpinner.getAdapter()).notifyDataSetChanged();
            }
        }
    }
}
