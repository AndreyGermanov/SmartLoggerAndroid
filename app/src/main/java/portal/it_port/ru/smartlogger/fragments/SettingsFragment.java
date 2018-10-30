package portal.it_port.ru.smartlogger.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.ui.TextChangeListener;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener  {

    private StateStore stateStore;
    private EditText hostField,portField,pollPeriodField;
    private Button httpProtocolButton,httpsProtocolButton,saveButton;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        stateStore = StateStore.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings,container,false);
        setupUI(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        stateStore.setCurrentScreen(StateStore.Screens.DASHBOARD);
    }

    private void setupUI(View v) {
        setupButtons(v);
        setupInputFields(v);
        applyState();
    }

    private void setupInputFields(View v) {
        hostField = setupInputField(v,R.id.settings_host,stateStore.getSettingsHost());
        portField = setupInputField(v,R.id.settings_port,stateStore.getSettingsPort());
        pollPeriodField = setupInputField(v,R.id.settings_pollperiod,stateStore.getSettingsPollPeriod());
    }

    private EditText setupInputField(View rootView,int resourceId,String defaultValue) {
        EditText result = rootView.findViewById(resourceId);
        result.setText(defaultValue);
        result.addTextChangedListener(new TextChangeListener(result,this));
        return result;
    }

    private void setupButtons(View v) {
        httpProtocolButton = setupButton(v,R.id.settings_protocol_http);
        httpsProtocolButton = setupButton(v,R.id.settings_protocol_https);
        saveButton = setupButton(v,R.id.settings_save);
    }

    private Button setupButton(View rootView, int resourceId) {
        Button result = rootView.findViewById(resourceId);
        result.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
        result.setTextColor(getResources().getColor(R.color.buttonText));
        result.setOnClickListener(this);
        return result;
    }

    private void applyState() {
        System.out.println("APPLYING STATE");
        System.out.println(stateStore.getSettingsProtocol());
        if (stateStore.getSettingsProtocol().equals("http")) {
            httpProtocolButton.setBackgroundColor(getResources().getColor(R.color.selectedButtonBackground));
            httpProtocolButton.setTextColor(getResources().getColor(R.color.selectedButtonText));
        } else if (stateStore.getSettingsProtocol().equals("https")) {
            httpsProtocolButton.setBackgroundColor(getResources().getColor(R.color.selectedButtonBackground));
            httpsProtocolButton.setTextColor(getResources().getColor(R.color.selectedButtonText));
        }
    }

    public void onEditTextChanged(EditText editText, String value) {
        switch (editText.getId()) {
            case R.id.settings_host: stateStore.setSettingsHost(value);break;
            case R.id.settings_port: stateStore.setSettingsPort(value);break;
            case R.id.settings_pollperiod: stateStore.setSettingsPollPeriod(value);break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_protocol_http: stateStore.setSettingsProtocol("http");break;
            case R.id.settings_protocol_https: stateStore.setSettingsProtocol("https");break;
        }
        setupButtons(getView());
        applyState();
    }
}