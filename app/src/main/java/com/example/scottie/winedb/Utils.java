package com.example.scottie.winedb;

import android.widget.EditText;

/**
 * Utils
 * <p>
 * A collection of utilities for the rest of the app
 *
 * @author Scott Garton
 * @version 1.0
 */
public class Utils {
    /**
     * isEmpty
     *
     * @param text EditText element used to check if empty
     * @return if empty (true) or not(false)
     */
    public static boolean isEmpty(EditText text) {
        String s = text.getText().toString().trim();
        return s.matches("");
    }
}
