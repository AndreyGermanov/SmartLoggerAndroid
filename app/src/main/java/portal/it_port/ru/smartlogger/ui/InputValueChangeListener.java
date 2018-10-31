package portal.it_port.ru.smartlogger.ui;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import portal.it_port.ru.smartlogger.fragments.SettingsFragment;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class InputValueChangeListener implements TextWatcher, AdapterView.OnItemSelectedListener {

    private SettingsFragment host;
    private View source;

    public InputValueChangeListener(View source, SettingsFragment host) {
        this.source = source;
        this.host = host;
    }

    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        host.onEditTextChanged(source,charSequence.toString());
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override public void afterTextChanged(Editable editable) {}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!(source instanceof SelectInputField)) return;
        SelectInputField field = (SelectInputField)source;
        Object value = field.items.get(position);
        host.onEditTextChanged(source,value);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
