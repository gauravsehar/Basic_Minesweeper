package com.example.gauravsehar.minesweepergs;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

/**
 * Created by Gaurav Sehar on 06-Feb-18.
 */

class checkAge implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {

            if (Integer.parseInt(s.toString()) > 100)
                s.replace(0, s.length(), "100");
        } catch (NumberFormatException nfe) {
            Log.e("GS", "StartActivity afterTextChanged :" + nfe);
        }
    }
}
