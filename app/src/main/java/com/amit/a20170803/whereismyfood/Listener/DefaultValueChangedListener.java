package com.amit.a20170803.whereismyfood.Listener;

import android.util.Log;

import com.amit.a20170803.whereismyfood.Enums.ActionEnum;
import com.amit.a20170803.whereismyfood.Interface.ValueChangedListener;

/**
 * Created by to5y on 13/08/2017.
 */

public class DefaultValueChangedListener implements ValueChangedListener {

    public void valueChanged(int value, ActionEnum action) {

        String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
        String message = String.format("NumberPicker is %s to %d", actionText, value);
        Log.v(this.getClass().getSimpleName(), message);
    }
}
