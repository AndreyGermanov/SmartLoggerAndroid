package portal.it_port.ru.smartlogger.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import java.util.HashMap;
import portal.it_port.ru.smartlogger.R;
import portal.it_port.ru.smartlogger.config.ConfigManager;
import portal.it_port.ru.smartlogger.main.StateStore;
import portal.it_port.ru.smartlogger.ui.InputField;
import portal.it_port.ru.smartlogger.ui.InputValueChangeListener;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener  {

    private StateStore stateStore;
    private HashMap<String,InputField> fields = new HashMap<>();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        stateStore = StateStore.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings,parent,false);
        setupUI(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        stateStore.setCurrentScreen(StateStore.Screens.SETTINGS);
    }

    private void setupUI(View v) {
        setupInputFields(v);
        setupButtons(v);
    }

    private void setupInputFields(View v) {
        fields.put("protocol",setupInputField(v,"select",R.id.settings_protocol,stateStore
                .getSettingsProtocol()));
        fields.put("host",setupInputField(v,"text",R.id.settings_host,stateStore.getSettingsHost()));
        fields.put("port",setupInputField(v,"text",R.id.settings_port,stateStore.getSettingsPort()));
        fields.put("pollPeriod",setupInputField(v,"text",R.id.settings_pollperiod,stateStore
                .getSettingsPollPeriod()));
    }

    private InputField setupInputField(View rootView,String fieldType,int resourceId,String defaultValue) {
        InputField result = InputField.inflate(rootView,fieldType,resourceId);
        result.setValue(defaultValue);
        result.setChangeListener(new InputValueChangeListener(result,this));
        return result;
    }

    private void setupButtons(View v) {
        Button saveButton = v.findViewById(R.id.settings_save);
        saveButton.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
        saveButton.setTextColor(getResources().getColor(R.color.buttonText));
        saveButton.setOnClickListener(this);
    }

    public void onEditTextChanged(View editText, Object value) {
        if (value == null) return;
        switch (editText.getId()) {
            case R.id.settings_host: stateStore.setSettingsHost(value.toString());break;
            case R.id.settings_port: stateStore.setSettingsPort(value.toString());break;
            case R.id.settings_pollperiod: stateStore.setSettingsPollPeriod(value.toString());break;
            case R.id.settings_protocol: stateStore.setSettingsProtocol(value.toString());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.settings_save) saveConfig();
    }

    private void saveConfig() {
        clearErrorMessages();
        ConfigManager configManger = ConfigManager.getInstance();
        configManger.setContext(getContext());
        HashMap<String,String> errors = configManger.validateConfig();
        if (errors.size()>0) {
            showErrorMessages(errors);
            return;
        }
        configManger.setConfig();
        if (configManger.saveConfig())
            showSuccessToast();
        else
            showFailureToast();
    }

    private void clearErrorMessages() {
        for (String key: fields.keySet()) { fields.get(key).setError(""); }
    }

    private void showErrorMessages(HashMap<String,String> errors) {
        for (String fieldName: errors.keySet()) {
            InputField field = fields.get(fieldName);
            if (field != null) field.setError(errors.get(fieldName));
        }
    }

    private void showSuccessToast() {
        showToast(getResources().getString(R.string.configSavedSuccessfully));
    }

    private void showFailureToast() {
        showToast(getResources().getString(R.string.configSaveFailure));
    }

    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }
}