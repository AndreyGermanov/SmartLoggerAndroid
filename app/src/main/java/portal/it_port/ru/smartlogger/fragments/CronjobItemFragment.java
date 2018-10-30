package portal.it_port.ru.smartlogger.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.models.Cronjob;
import portal.it_port.ru.smartlogger.models.CronjobCollection;
import portal.it_port.ru.smartlogger.services.CronjobsService;

/**
 * Created by Andrey Germanov on 10/29/18.
 */
public class CronjobItemFragment extends Fragment implements View.OnClickListener {

    private Cronjob cronjob;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        cronjob = CronjobCollection.getInstance().getCronjob(getArguments().getString("cronjobId"));
        IntentFilter filter = new IntentFilter(CronjobsService.CRONJOBS_LIST_CHANGED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(new CronjobItemBroadcastReceiver(),filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cronjob_item,container,false);
        updateUI(view);
        return view;
    }

    private void updateUI(View view) {
        if (view == null) return;
        renderInfoTable(view);
        renderButton(view);
    }

    private void renderInfoTable(View view) {
        if (view == null) return;
        TextView cronjobName = view.findViewById(R.id.item_cronjob_name);
        TextView cronjobStatus = view.findViewById(R.id.item_cronjob_status);
        TextView cronjobLastRunTime = view.findViewById(R.id.item_cronjob_lastRunTime);
        cronjobName.setText(cronjob.getName());
        cronjobStatus.setText(cronjob.getStatus());
        Date lastRunTime = new Date(cronjob.getLastRunTimestamp()*1000);
        cronjobLastRunTime.setText(lastRunTime.toString());
    }

    private void renderButton(View view) {
        if (view == null) return;
        Button enabledButton = view.findViewById(R.id.item_cronjob_enabledDisabled);
        String buttonText = getResources().getString(R.string.enable);
        if (cronjob.isEnabled()) buttonText = getResources().getString(R.string.disable);
        enabledButton.setText(buttonText);
        enabledButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.item_cronjob_enabledDisabled) {
            Intent i = new Intent(getContext(),CronjobsService.class);
            i.setAction(CronjobsService.CHANGE_SERVICE_STATUS);
            i.putExtra("cronjob_id",cronjob.getId());
            i.putExtra("status",!cronjob.isEnabled());
            if (getActivity() != null) getActivity().startService(i);
        }
    }

    private class CronjobItemBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle args = intent.getExtras();
            if (args == null || !args.containsKey("changedRecordIDs")) return;
            ArrayList<String> changedRecordIDs = (ArrayList<String>) args.getSerializable("changedRecordIDs");
            if (changedRecordIDs.contains(cronjob.getId())) updateUI(getView());
        }
    }
}