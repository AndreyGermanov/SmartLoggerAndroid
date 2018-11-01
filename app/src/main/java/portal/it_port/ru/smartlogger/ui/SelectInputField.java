package portal.it_port.ru.smartlogger.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import portal.it_port.ru.smartlogger.R;

/**
 * Created by Andrey Germanov on 10/31/18.
 */
public class SelectInputField extends InputField {

    private Spinner spinner;
    List items;

    public SelectInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected TypedArray getAttributes() {
        return getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.SelectInputField, 0, 0);
    }

    @Override
    public View setupValueField() {
        TypedArray attributes = getAttributes();
        items = Arrays.asList(attributes.getTextArray(R.styleable.SelectInputField_list));
        System.out.println(items);
        spinner = new Spinner(getContext());
        spinner.setAdapter(new ArrayAdapter(context,android.R.layout.simple_spinner_item,items));
        attributes.recycle();
        return spinner;
    }

    @Override
    public void setValueField() {
        setupValueField();
        addView(spinner);
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        spinner.setSelection(items.indexOf(value)); }

    @Override
    protected String getLabelTextAttr() {
        TypedArray attrs = getAttributes();
        return attrs.getString(R.styleable.SelectInputField_label);
    }

    @Override
    protected String getValueAttr() {
        TypedArray attrs = getAttributes();
        return attrs.getString(R.styleable.SelectInputField_value);
    }

    @Override
    public void setChangeListener(InputValueChangeListener listener) {
        spinner.setOnItemSelectedListener(listener);
    }

    public List getItems() {
        return items;
    }
}
