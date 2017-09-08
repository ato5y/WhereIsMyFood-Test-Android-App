package com.amit.a20170803.whereismyfood.Listener;

import android.view.View;
import android.widget.EditText;

import com.amit.a20170803.whereismyfood.Enums.ActionEnum;
import com.amit.a20170803.whereismyfood.adapter.NumberPicker;

/**
 * Created by to5y on 13/08/2017.
 */

public class ActionListener implements View.OnClickListener {

    NumberPicker layout;
    ActionEnum action;
    EditText display;

    public ActionListener(NumberPicker layout, EditText display, ActionEnum action) {
        this.layout = layout;
        this.action = action;
        this.display = display;
    }

    @Override
    public void onClick(View v) {
        try {
            int newValue = Integer.parseInt(this.display.getText().toString());

            if (!this.layout.valueIsAllowed(newValue)) {
                return;
            }

            this.layout.setValue(newValue);
        } catch (NumberFormatException e) {
            this.layout.refresh();
        }

        switch (this.action) {
            case INCREMENT:
                this.layout.increment();
                break;
            case DECREMENT:
                this.layout.decrement();
                break;
        }
    }
}