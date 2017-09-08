package com.amit.a20170803.whereismyfood.Listener;

import android.util.Log;

import com.amit.a20170803.whereismyfood.Interface.LimitExceededListener;

/**
 * Created by to5y on 13/08/2017.
 */

public class DefaultLimitExceededListener implements LimitExceededListener {

    public void limitExceeded(int limit, int exceededValue) {

        String message = String.format("NumberPicker cannot set to %d because the limit is %d.", exceededValue, limit);
        Log.v(this.getClass().getSimpleName(), message);
    }
}