package portal.it_port.ru.smartlogger.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import portal.it_port.ru.smartlogger.fragments.SettingsFragment;

/**
 * Created by Andrey Germanov on 10/30/18.
 */
public class TextChangeListener implements TextWatcher {

    private SettingsFragment host;
    private EditText source;

    public TextChangeListener(EditText source,SettingsFragment host) {
        this.source = source;
        this.host = host;
    }

    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        host.onEditTextChanged(source,charSequence.toString());
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override public void afterTextChanged(Editable editable) {}
}
