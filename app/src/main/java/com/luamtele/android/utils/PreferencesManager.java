package com.luamtele.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HP on 30/10/2015.
 */
public class PreferencesManager {
    public static String getValue(Context ctx, String valueKey) {
        String value = null;
        SharedPreferences sharedPref = ctx.getSharedPreferences("Velkeno.com", Context.MODE_PRIVATE);
        value = sharedPref.getString(valueKey, "");
        return value;
    }

    public static void setValue(Context ctx, String valueKey, String value) {
        SharedPreferences sharedPref = ctx.getSharedPreferences("Velkeno.com", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(valueKey, value);
        editor.commit();
    }
    public static int getValueInt(Context ctx, String valueKey) {
        int value = 0;
        SharedPreferences sharedPref = ctx.getSharedPreferences("Velkeno.com", Context.MODE_PRIVATE);
        value = sharedPref.getInt(valueKey, 0);
        return value;
    }

    public static void setValueInt(Context ctx, String valueKey, int value) {
        SharedPreferences sharedPref = ctx.getSharedPreferences("Velkeno.com", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(valueKey, value);
        editor.commit();
    }

}
