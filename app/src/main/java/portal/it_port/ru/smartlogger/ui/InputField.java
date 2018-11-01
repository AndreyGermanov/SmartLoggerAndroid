package portal.it_port.ru.smartlogger.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import portal.it_port.ru.smartlogger.R;

/**
 * Created by Andrey Germanov on 10/31/18.
 */
public abstract class InputField extends LinearLayout {

    protected TextView labelField,errorField;
    protected Object value;
    protected AttributeSet attrs;
    protected Context context;


    public static InputField inflate(View v,String fieldType,int resourceId) {
        switch (fieldType) {
            case "text": return (TextInputField)v.findViewById(resourceId);
            case "select": return (SelectInputField)v.findViewById(resourceId);
            default: return null;
        }
    }

    public InputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        setupView();
    }

    protected TypedArray getAttributes() {
        return getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.InputField, 0, 0);
    }

    protected void setupView() {
        setOrientation(VERTICAL);
        labelField = setupLabelField();
        if (labelField != null) addView(labelField);
        errorField = setupErrorField();
        if (errorField != null && !errorField.getText().toString().isEmpty()) addView(errorField);
        setValueField();
    }

    protected TextView setupLabelField() {
        TextView result = new TextView(getContext());
        result.setText(getLabelTextAttr());
        result.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        return result;
    }

    protected TextView setupErrorField() {
        TextView result = setupLabelField();
        result.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        result.setText("");
        result.setVisibility(GONE);
        addView(result);
        return result;
    }

    public String getLabel() { return labelField.getText().toString();}

    public void setLabel(String text) { labelField.setText(text);}

    public void setError(String text) {
        errorField.setError(text);
        errorField.setText(text);
        if (!text.isEmpty())
            errorField.setVisibility(VISIBLE);
        else
            errorField.setVisibility(GONE);
    }

    public Object getValue() { return value; }

    public void setValue(Object value) { this.value=value; }


    protected String getLabelTextAttr() {
        TypedArray attrs = getAttributes();
        return attrs.getString(R.styleable.InputField_label);
    }

    protected Object getValueAttr() {
        TypedArray attrs = getAttributes();
        return attrs.getString(R.styleable.InputField_value);
    }

    public abstract void setChangeListener(InputValueChangeListener listener);
    protected abstract View setupValueField();
    protected abstract void setValueField();
}
