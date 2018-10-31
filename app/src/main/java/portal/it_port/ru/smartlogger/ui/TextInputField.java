package portal.it_port.ru.smartlogger.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import portal.it_port.ru.smartlogger.R;

/**
 * Created by Andrey Germanov on 10/31/18.
 */
public class TextInputField extends InputField {

    private EditText textField;

    public TextInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected TypedArray getAttributes() {
        return getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.TextInputField, 0, 0);
    }

    @Override
    protected View setupValueField() {
        TypedArray attrs = getAttributes();
        EditText result = new EditText(getContext());
        result.setText(getValueAttr());
        result.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        result.setInputType(attrs.getInt(R.styleable.TextInputField_inputType,1));
        return result;
    }

    @Override
    protected void setValueField() {
        textField = (EditText)setupValueField();
        addView(textField);
    }

    @Override
    public String getValue() { return value.toString();}

    @Override
    public void setValue(Object value) { super.setValue(value); textField.setText(value.toString()); }

    @Override
    protected String getLabelTextAttr() {
        TypedArray attrs = getAttributes();
        return attrs.getString(R.styleable.TextInputField_label);
    }

    @Override
    protected String getValueAttr() {
        TypedArray attrs = getAttributes();
        return attrs.getString(R.styleable.TextInputField_value);
    }

    @Override
    public void setChangeListener(InputValueChangeListener listener) {
        textField.addTextChangedListener(listener);
    }
}
