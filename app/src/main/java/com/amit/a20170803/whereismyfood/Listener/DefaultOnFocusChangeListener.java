package com.amit.a20170803.whereismyfood.Listener;

import android.view.View;
import android.widget.EditText;

import com.amit.a20170803.whereismyfood.Enums.ActionEnum;
import com.amit.a20170803.whereismyfood.adapter.NumberPicker;

/**
 * Created by to5y on 13/08/2017.
 */

public class DefaultOnFocusChangeListener implements View.OnFocusChangeListener {

    NumberPicker layout;

    public DefaultOnFocusChangeListener(NumberPicker layout) {
        this.layout = layout;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText) v;

        if (!hasFocus) {
            try {
                int value = Integer.parseInt(editText.getText().toString());
                layout.setValue(value);

                if (layout.getValue() == value) {
                    layout.getValueChangedListener().valueChanged(value, ActionEnum.MANUAL);
                } else {
                    layout.refresh();
                }
            } catch (NumberFormatException e) {
                layout.refresh();
            }
        }
    }
}
