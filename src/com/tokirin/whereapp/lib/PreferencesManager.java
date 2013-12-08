package com.tokirin.whereapp.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesManager {

    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PreferencesManager(Context context)
    {
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = pref.edit();
    }

    public SharedPreferences getPref()
    {
        return pref;
    }

    public void put(String key, String value)
    {
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value)
    {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, int value)
    {
        editor.putInt(key, value);
        editor.commit();
    }

    public String get(String key, String dftValue)
    {
        try
        {
            return pref.getString(key, dftValue);
        }
        catch (Exception e)
        {
            return dftValue;
        }
    }

    public int get(String key, int dftValue)
    {
        try
        {
            return pref.getInt(key, dftValue);
        }
        catch (Exception e)
        {
            return dftValue;
        }
    }

    public boolean get(String key, boolean dftValue)
    {
        try
        {
            return pref.getBoolean(key, dftValue);
        }
        catch (Exception e)
        {
            return dftValue;
        }
    }
}
