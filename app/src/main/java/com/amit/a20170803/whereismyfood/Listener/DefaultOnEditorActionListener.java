package com.amit.a20170803.whereismyfood.Listener;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.amit.a20170803.whereismyfood.Enums.ActionEnum;
import com.amit.a20170803.whereismyfood.adapter.NumberPicker;

/**
 * Created by to5y on 13/08/2017.
 */

public class DefaultOnEditorActionListener implements TextView.OnEditorActionListener {

    NumberPicker layout;

    public DefaultOnEditorActionListener(NumberPicker layout) {
        this.layout = layout;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            try {
                int value = Integer.parseInt(v.getText().toString());

                layout.setValue(value);

                if (layout.getValue() == value) {
                    layout.getValueChangedListener().valueChanged(value, ActionEnum.MANUAL);
                    return false;
                }
            } catch (NumberFormatException e) {
                layout.refresh();
            }
        }
        return true;
    }
}
